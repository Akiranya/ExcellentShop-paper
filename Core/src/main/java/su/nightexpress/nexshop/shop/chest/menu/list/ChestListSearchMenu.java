package su.nightexpress.nexshop.shop.chest.menu.list;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenuAuto;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.shop.chest.ChestShopModule;
import su.nightexpress.nexshop.shop.chest.impl.ChestProduct;

import java.util.*;

public class ChestListSearchMenu extends AbstractMenuAuto<ExcellentShop, ChestProduct> {

    private final ChestShopModule                chestShop;
    private final Map<String, Set<ChestProduct>> searchCache;

    private final int[]           productSlots;
    private final Component       productName;
    private final List<Component> productLore;

    public ChestListSearchMenu(@NotNull ChestShopModule chestShop) {
        super(chestShop.plugin(), JYML.loadOrExtract(chestShop.plugin(), chestShop.getPath() + "menu/search.yml"), "");
        this.chestShop = chestShop;
        this.searchCache = new HashMap<>();

        this.productSlots = cfg.getIntArray("Product.Slots");
        this.productName = cfg.getComponent("Product.Name", Component.empty());
        this.productLore = cfg.getComponentList("Product.Lore");

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                this.onItemClickDefault(player, type2);
            }
        };

        for (String sId : cfg.getSection("Content")) {
            MenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClickHandler(click);
            }
            this.addItem(menuItem);
        }
    }

    public void searchProduct(@NotNull Player player, @NotNull Material material) {
        Set<ChestProduct> products = new HashSet<>();
        this.chestShop.getShops().forEach(shop -> {
            products.addAll(shop.getProducts().stream().filter(product -> product.getItem().getType() == material).toList());
        });
        this.searchCache.put(player.getName(), products);
    }

    @NotNull
    public Collection<ChestProduct> getSearchResult(@NotNull Player player) {
        return this.searchCache.getOrDefault(player.getName(), Collections.emptySet());
    }

    @Override
    protected int[] getObjectSlots() {
        return this.productSlots;
    }

    @Override
    @NotNull
    protected List<ChestProduct> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.getSearchResult(player).stream()
                                   .sorted((p1, p2) -> (int) (p1.getPricer().getPriceBuy() - p2.getPricer().getPriceBuy())).toList());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull ChestProduct product) {
        ItemStack item = new ItemStack(product.getItem());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(this.productName);
        meta.lore(this.productLore);
        item.setItemMeta(meta);

        ItemUtil.replace(item, product.replacePlaceholders());
        ItemUtil.replace(item, product.getShop().replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected MenuClick getObjectClick(@NotNull Player player, @NotNull ChestProduct product) {
        return (player1, type, e) -> {
            product.getShop().teleport(player1);
        };
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
        super.onClose(player, e);
        this.searchCache.remove(player.getName());
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
