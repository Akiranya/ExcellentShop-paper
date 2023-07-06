package su.nightexpress.nexshop.shop.virtual.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.Pair;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.shop.util.TransactionResult;
import su.nightexpress.nexshop.shop.virtual.VirtualShopModule;
import su.nightexpress.nexshop.shop.virtual.config.VirtualLang;
import su.nightexpress.nexshop.shop.virtual.impl.product.VirtualPreparedProduct;
import su.nightexpress.nexshop.shop.virtual.impl.product.VirtualProduct;
import su.nightexpress.nexshop.shop.virtual.impl.shop.VirtualShop;

import java.util.*;

public class ShopSellMenu extends ConfigMenu<ExcellentShop> {

    private final VirtualShopModule module;
    private final Component itemName;
    private final List<Component> itemLore;
    private final int[] itemSlots;

    /**
     * The backend shop to which the player can sell items.
     */
    private static final Map<Player, VirtualShop> USER_SHOP = new WeakHashMap<>();
    /**
     * The items which are selected by the player to be sold.
     */
    private static final Map<Player, Pair<List<ItemStack>, Set<VirtualProduct>>> USER_ITEMS = new WeakHashMap<>();

    public ShopSellMenu(@NotNull VirtualShopModule module, @NotNull JYML cfg) {
        super(module.plugin(), cfg);
        this.module = module;
        this.itemName = cfg.getComponent("Item.Name", Component.text("%item_name%"));
        this.itemLore = cfg.getComponentList("Item.Lore");
        this.itemSlots = cfg.getIntArray("Item.Slots");

        this.registerHandler(ItemType.class)
            // Define the handler for the SELL button
            .addClick(ItemType.SELL, (viewer, e) -> {
                Player player = viewer.getPlayer();
                Pair<List<ItemStack>, Set<VirtualProduct>> userItems = USER_ITEMS.remove(player);
                if (userItems == null || userItems.getFirst().isEmpty() || userItems.getSecond().isEmpty())
                    // There is nothing to be sold, just return
                    return;

                ItemStack[] original = player.getInventory().getContents();
                player.getInventory().clear();

                List<TransactionResult> profits = new ArrayList<>();
                userItems.getFirst().forEach(item -> PlayerUtil.addItem(player, item));
                userItems.getSecond().forEach(product -> {
                    VirtualPreparedProduct preparedProduct = product.getPrepared(TradeType.SELL, true);
                    TransactionResult result = preparedProduct.trade(player);

                    if (result.getResult() == TransactionResult.Result.SUCCESS) {
                        profits.add(result);
                    }
                });

                ItemStack[] left = player.getInventory().getContents();
                player.getInventory().setContents(original);
                Arrays.asList(left).forEach(item -> {
                    if (item != null && !item.getType().isAir()) PlayerUtil.addItem(player, item);
                });

                player.updateInventory();
                player.closeInventory();
                if (profits.isEmpty()) return;

                plugin.getMessage(VirtualLang.SELL_MENU_SOLD)
                    .replace(
                        str -> str.contains(Placeholders.GENERIC_ITEM),
                        (line, list) -> profits.forEach(result -> list.add(result.replacePlaceholders().apply(line))))
                    .send(player);
            });

        this.load();
    }

    enum ItemType {
        SELL
    }

    public void open(Player player, int page, VirtualShop shop) {
        USER_SHOP.put(player, shop); // Add backend shop for the player
        super.open(player, page);
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @Nullable ItemStack item, @NotNull SlotType slotType, int slot, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, item, slotType, slot, event);

        // Define the handler when the player clicks item in THEIR OWN inventory

        Player player = viewer.getPlayer();
        Inventory inventory = event.getInventory();
        VirtualShop shop = USER_SHOP.get(player);
        if (item == null || item.getType().isAir() || shop == null) return;

        if (slotType == SlotType.PLAYER) {
            VirtualProduct product = this.module.getProductByItem(player, item, TradeType.SELL, shop);
            if (product == null) {
                return; // TODO send message for unsellable items
            }

            // Test if there's any empty slot to accept the item to be sold
            int firstSlot = Arrays.stream(this.itemSlots).filter(i -> {
                ItemStack testItem = inventory.getItem(i);
                return testItem == null || testItem.getType().isAir();
            }).findFirst().orElse(-1);
            if (firstSlot < 0) return;

            ItemStack icon = item.clone();
            ItemMeta meta = icon.getItemMeta();

            double price = product.getPricer().getPriceSell() * (item.getAmount() / (double) product.getUnitAmount());

            List<Component> lore = new ArrayList<>(this.itemLore);
            lore = ComponentUtil.replace(lore,
                str -> str.replace(Placeholders.GENERIC_PRICE, product.getCurrency().format(price)),
                product.getShop().replacePlaceholders(),
                product.replacePlaceholders()
            );
            lore = ComponentUtil.replacePlaceholderList("%item_lore", lore, ItemUtil.getLore(item));
            lore = ComponentUtil.compressEmptyLines(lore);

            meta.displayName(ComponentUtil.replace(this.itemName, "%item_name%", ItemUtil.getName(item)));
            meta.lore(lore);
            ItemUtil.removeItalic(meta);
            icon.setItemMeta(meta);
            inventory.setItem(firstSlot, icon);

            Pair<List<ItemStack>, Set<VirtualProduct>> pair = USER_ITEMS.computeIfAbsent(player, k -> Pair.of(new ArrayList<>(), new HashSet<>()));
            pair.getFirst().add(item.clone());
            pair.getSecond().add(product);
            item.setAmount(0);
        }
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);

        Player player = viewer.getPlayer();
        Pair<List<ItemStack>, Set<VirtualProduct>> userItems = USER_ITEMS.remove(player);
        if (userItems != null) {
            userItems.getFirst().forEach(item -> PlayerUtil.addItem(player, item));
        }
    }
}
