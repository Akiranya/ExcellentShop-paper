package su.nightexpress.nexshop.shop.chest.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.shop.ShopView;
import su.nightexpress.nexshop.api.type.ShopClickAction;
import su.nightexpress.nexshop.config.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChestShopView extends ShopView<ChestShop, ChestProduct> implements AutoPaged<ChestProduct> {

    private static int[] PRODUCT_SLOTS;
    private static List<String> PRODUCT_FORMAT_LORE;

    public ChestShopView(@NotNull ChestShop shop) {
        super(shop, JYML.loadOrExtract(shop.plugin(), shop.getModule().getPath() + "view.yml"));

        PRODUCT_SLOTS = cfg.getIntArray("Product_Slots");
        PRODUCT_FORMAT_LORE = cfg.getStringList("Product_Format.Lore.Text");

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this))
            .addClick(MenuItemType.CLOSE, (viewer, event) -> this.plugin.runTask(task -> viewer.getPlayer().closeInventory()));

        this.load();

        this.getItems().forEach(menuItem -> {
            if (menuItem.getOptions().getDisplayModifier() == null) {
                menuItem.getOptions().setDisplayModifier((viewer, item) -> ItemUtil.replaceNameAndLore(item, this.shop.replacePlaceholders()));
            }
        });
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        options.setTitle(this.getShop().getName());

        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return PRODUCT_SLOTS;
    }

    @Override
    public @NotNull List<ChestProduct> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.getShop().getProducts());
    }

    @Override
    public @NotNull ItemStack getObjectStack(@NotNull Player player, @NotNull ChestProduct product) {
        ItemStack preview = product.getPreview();
        preview.editMeta(meta -> {
            List<Component> lore = new ArrayList<>();

            for (String lineFormat : PRODUCT_FORMAT_LORE) {
                if (lineFormat.contains(Placeholders.GENERIC_LORE)) {
                    List<Component> list2 = meta.lore();
                    if (list2 != null) lore.addAll(list2);
                    continue;
                }
                lore.add(ComponentUtil.asComponent(lineFormat));
            }

            PlaceholderMap placeholderMap = new PlaceholderMap();
            placeholderMap.getKeys().addAll(product.getPlaceholders(player).getKeys());
            placeholderMap.getKeys().addAll(product.getCurrency().getPlaceholders().getKeys());
            placeholderMap.getKeys().addAll(shop.getPlaceholders().getKeys());
            lore = ComponentUtil.replace(lore, placeholderMap.replacer());
            meta.lore(lore);
        });
        return preview;
    }

    @Override
    public @NotNull ItemClick getObjectClick(@NotNull ChestProduct product) {
        return (viewer, event) -> {
            ShopClickAction clickType = Config.GUI_CLICK_ACTIONS.get().get(event.getClick());
            if (clickType == null) return;

            product.prepareTrade(viewer.getPlayer(), clickType);
        };
    }

    @Override
    public @NotNull Comparator<ChestProduct> getObjectSorter() {
        return ((o1, o2) -> 0);
    }
}
