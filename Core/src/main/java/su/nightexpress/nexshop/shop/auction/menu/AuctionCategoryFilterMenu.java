package su.nightexpress.nexshop.shop.auction.menu;

import org.bukkit.entity.Player;
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
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.shop.auction.AuctionCategory;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.Placeholders;
import su.nightexpress.nexshop.shop.auction.config.AuctionConfig;

import java.util.*;

public class AuctionCategoryFilterMenu extends ConfigMenu<ExcellentShop> implements AutoPaged<AuctionCategory> {

    private final AuctionManager auctionManager;
    private final int[] objectSlots;
    private final String itemName;
    private final List<String> itemLore;
    private final ItemStack selectedIcon;

    public AuctionCategoryFilterMenu(@NotNull AuctionManager auctionManager, @NotNull JYML cfg) {
        super(auctionManager.plugin(), cfg);
        this.auctionManager = auctionManager;
        this.itemName = cfg.getString("Items.Name", Placeholders.CATEGORY_NAME); // Mewcraft - no legacy color
        this.itemLore = cfg.getStringList("Items.Lore"); // Mewcraft - no legacy color
        this.objectSlots = cfg.getIntArray("Items.Slots");
        this.selectedIcon = cfg.getItem("Selected");

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this))
            .addClick(MenuItemType.RETURN, (viewer, event) -> this.auctionManager.getMainMenu().openNextTick(viewer, 1))
            .addClick(MenuItemType.CONFIRMATION_DECLINE, (viewer, event) -> this.auctionManager.getMainMenu().openNextTick(viewer, 1))
            .addClick(MenuItemType.CONFIRMATION_ACCEPT, (viewer, event) -> this.auctionManager.getMainMenu().openNextTick(viewer, 1));

        this.load();
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        return this.objectSlots;
    }

    @Override
    public @NotNull List<AuctionCategory> getObjects(@NotNull Player player) {
        return new ArrayList<>(AuctionConfig.getCategories());
    }

    @Override
    public @NotNull ItemStack getObjectStack(@NotNull Player player, @NotNull AuctionCategory category) {
        Set<AuctionCategory> categories = AuctionMainMenu.getCategories(player);
        boolean isSelected = categories.contains(category);

        ItemStack icon = category.getIcon();
        ItemStack item = isSelected ? this.selectedIcon.clone() : category.getIcon();
        item.editMeta(meta -> {
            if (!isSelected) {
                meta.displayName(ComponentUtil.asComponent(this.itemName));
                meta.lore(ComponentUtil.asComponent(this.itemLore));
            }
            ItemUtil.replaceNameAndLore(meta, category.replacePlaceholders());
            ItemUtil.replacePlaceholderListComponent(meta, Placeholders.CATEGORY_ICON_LORE, ItemUtil.getLore(icon), true);
        });

        return item;
    }

    @Override
    public @NotNull ItemClick getObjectClick(@NotNull AuctionCategory category) {
        return (viewer, event) -> {
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType().isAir()) return;

            Player player = viewer.getPlayer();
            Set<AuctionCategory> categories = AuctionMainMenu.getCategories(player);
            if (categories.add(category) || categories.remove(category)) {
                this.openNextTick(viewer, viewer.getPage());
            }
        };
    }
}
