package su.nightexpress.nexshop.shop.virtual.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.*;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.shop.virtual.VirtualShopModule;
import su.nightexpress.nexshop.shop.virtual.config.VirtualLang;
import su.nightexpress.nexshop.shop.virtual.impl.VirtualPreparedProduct;
import su.nightexpress.nexshop.shop.virtual.impl.VirtualProduct;
import su.nightexpress.nexshop.shop.virtual.impl.VirtualShop;

import java.util.*;
import java.util.List;

public class ShopSellMenu extends AbstractMenu<ExcellentShop> {

    private final VirtualShopModule module;
    private final Component itemName;
    private final List<Component> itemLore;
    private final int[] itemSlots;

    private static final Map<Player, List<Pair<ItemStack, VirtualProduct>>> USER_ITEMS = new WeakHashMap<>();
    private static final Map<Player, VirtualShop> USER_SHOP = new WeakHashMap<>();

    public ShopSellMenu(@NotNull VirtualShopModule module, @NotNull JYML cfg) {
        super(module.plugin(), cfg, "");
        this.module = module;
        this.itemName = cfg.getComponent("Item.Name", Component.text("%item_name%"));
        this.itemLore = cfg.getComponentList("Item.Lore");
        this.itemSlots = cfg.getIntArray("Item.Slots");

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type1) {
                this.onItemClickDefault(player, type1);
            } else if (type instanceof ItemType type1) {
                if (type1 == ItemType.SELL) {
                    List<Pair<ItemStack, VirtualProduct>> userItems = USER_ITEMS.remove(player);
                    if (userItems != null && !userItems.isEmpty()) {
                        userItems.forEach(pair -> {
                            PlayerUtil.addItem(player, pair.getFirst());
                            VirtualPreparedProduct preparedProduct = pair.getSecond().getPrepared(TradeType.SELL);
                            preparedProduct.setAmount(pair.getFirst().getAmount());
                            preparedProduct.sell(player, false);
                        });
                        player.updateInventory();
                        plugin.getMessage(VirtualLang.SELL_MENU_SOLD).send(player);
                    }
                    player.closeInventory();
                }
            }
        };

        for (String id : cfg.getSection("Content")) {
            MenuItem menuItem = cfg.getMenuItem("Content." + id, ItemType.class);
            if (menuItem.getType() != null) {
                menuItem.setClickHandler(click);
            }
            this.addItem(menuItem);
        }
    }

    enum ItemType {
        SELL
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return true;

        if (slotType == SlotType.PLAYER) {

            VirtualShop shop = USER_SHOP.get(player);
            VirtualProduct product = this.module.searchForBestProduct(player, item, TradeType.SELL, shop);
            if (product == null) return true;

            int firstSlot = -1;
            for (int slot : this.itemSlots) {
                int firstEmpty = inventory.firstEmpty();
                if (firstEmpty == -1) break;
                if (firstEmpty == slot) {
                    firstSlot = firstEmpty;
                    break;
                }
            }
            if (firstSlot < 0) return true;

            // int toSell = product.getStock().getPossibleAmount(TradeType.SELL, player);
            // if (toSell == 0) return true;
            // if (toSell > 0) toSell = item.getAmount();

            ItemStack icon = item.clone();
            ItemMeta meta = icon.getItemMeta();
            if (meta == null) return true;

            double price = product.getPricer().getPriceSell() * item.getAmount(); // toSell;

            List<Component> lore = new ArrayList<>(this.itemLore);
            lore.replaceAll(line -> {
                line = ComponentUtil.replace(line, product.getShop().replacePlaceholders(), product.replacePlaceholders());
                return ComponentUtil.replace(line, Placeholders.GENERIC_PRICE, product.getCurrency().format(price));
            });
            lore = ComponentUtil.replacePlaceholderList("%item_lore%", lore, ItemUtil.getLore(item));
            lore = ComponentUtil.compressEmptyLines(lore);

            meta.displayName(ComponentUtil.replace(this.itemName, "%item_name%", ItemUtil.getName(item)));
            meta.lore(lore);

            icon.setItemMeta(meta);
            inventory.setItem(firstSlot, icon);

            USER_ITEMS.computeIfAbsent(player, k -> new ArrayList<>()).add(Pair.of(item.clone(), product));
            item.setAmount(0);
        }
        return true;
    }

    public void open(Player player, int page, VirtualShop shop) {
        USER_SHOP.put(player, shop); // Add backend shop for the player
        super.open(player, page);
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
        super.onClose(player, e);

        // Return remaining items to the player
        List<Pair<ItemStack, VirtualProduct>> userItems = USER_ITEMS.remove(player);
        if (userItems != null) {
            userItems.forEach(pair -> PlayerUtil.addItem(player, pair.getFirst()));
        }

        // Clear the backend shop for the player
        USER_SHOP.remove(player);
    }
}
