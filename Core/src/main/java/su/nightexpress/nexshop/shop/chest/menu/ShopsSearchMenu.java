package su.nightexpress.nexshop.shop.chest.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.shop.chest.ChestShopModule;
import su.nightexpress.nexshop.shop.chest.impl.ChestProduct;

import java.util.*;

public class ShopsSearchMenu extends ConfigMenu<ExcellentShop> implements AutoPaged<ChestProduct> {

    private final ChestShopModule module;
    private final Map<Player, List<ChestProduct>> searchCache;

    private final int[] productSlots;
    private final Component productName;
    private final List<Component> productLore;

    public ShopsSearchMenu(@NotNull ChestShopModule module) {
        super(module.plugin(), JYML.loadOrExtract(module.plugin(), module.getLocalPath() + "/menu/", "shops_search.yml"));
        this.module = module;
        this.searchCache = new WeakHashMap<>();

        this.productSlots = cfg.getIntArray("Product.Slots");
        // Mewcraft start
        this.productName = cfg.getComponent("Product.Name", Component.empty());
        this.productLore = cfg.getComponentList("Product.Lore");
        // Mewcraft end

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.CLOSE, (viewer, event) -> plugin.runTask(task -> viewer.getPlayer().closeInventory()))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this))
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this));

        this.load();
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    public void open(@NotNull Player player, @NotNull Material material) {
        List<ChestProduct> products = new ArrayList<>();
        this.module.getShops().forEach(shop -> {
            products.addAll(shop.getProducts().stream().filter(product -> product.getItem().getType() == material).toList());
        });
        this.searchCache.put(player, products);
        this.open(player, 1);
    }

    @NotNull
    private Collection<ChestProduct> getSearchResult(@NotNull Player player) {
        return this.searchCache.getOrDefault(player, Collections.emptyList());
    }

    @Override
    public int[] getObjectSlots() {
        return this.productSlots;
    }

    @Override
    @NotNull
    public List<ChestProduct> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.getSearchResult(player).stream()
            .sorted((p1, p2) -> (int) (p1.getPricer().getPriceBuy() - p2.getPricer().getPriceBuy())).toList());
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull ChestProduct product) {
        ItemStack item = new ItemStack(product.getItem());
        // Mewcraft start
        item.editMeta(meta -> {
            meta.displayName(this.productName);
            meta.lore(this.productLore);
            ItemUtil.replaceNameAndLore(item, product.replacePlaceholders(), product.getShop().replacePlaceholders());
            ItemUtil.replacePlaceholderListComponent(meta, Placeholders.PRODUCT_PREVIEW_LORE, ItemUtil.getLore(item), true); // Last to compress empty
        });
        // Mewcraft end
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull ChestProduct product) {
        return (viewer, event) -> {
            product.getShop().teleport(viewer.getPlayer());
        };
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);
        this.searchCache.remove(viewer.getPlayer());
    }
}
