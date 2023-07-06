package su.nightexpress.nexshop.shop.auction.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.Perms;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.Placeholders;
import su.nightexpress.nexshop.shop.auction.listing.AbstractAuctionItem;

import java.util.*;
import java.util.function.UnaryOperator;

public abstract class AbstractAuctionMenu<A extends AbstractAuctionItem> extends ConfigMenu<ExcellentShop> implements AutoPaged<A> {

    protected AuctionManager auctionManager;

    protected int[] objectSlots;

    // Mewcraft - display name and lore are stored as MiniMessage strings
    protected String itemName;
    protected List<String> itemLore;

    protected Map<FormatType, List<String>> loreFormat;
    protected Map<Player, UUID> seeOthers;

    private static final String PLACEHOLDER_LORE_FORMAT = "%lore_format%";

    public AbstractAuctionMenu(@NotNull AuctionManager auctionManager, @NotNull JYML cfg) {
        super(auctionManager.plugin(), cfg);
        this.auctionManager = auctionManager;
        this.seeOthers = new WeakHashMap<>();
        this.loreFormat = new HashMap<>();

        this.itemName = cfg.getString("Items.Name", Placeholders.LISTING_ITEM_NAME); // Mewcraft
        this.itemLore = cfg.getStringList("Items.Lore"); // Mewcraft
        this.objectSlots = cfg.getIntArray("Items.Slots");
        for (FormatType formatType : FormatType.values()) {
            this.loreFormat.put(formatType, cfg.getStringList("Lore_Format." + formatType.name())); // Mewcraft
        }

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this))
            .addClick(MenuItemType.RETURN, (viewer, event) -> this.auctionManager.getMainMenu().openNextTick(viewer, 1))
        ;
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    enum FormatType {
        OWNER, PLAYER, ADMIN
    }

    public void open(@NotNull Player player, int page, @NotNull UUID id) {
        if (!id.equals(player.getUniqueId())) {
            this.seeOthers.put(player, id);
        }
        this.open(player, page);
    }

    @Override
    public int[] getObjectSlots() {
        return objectSlots;
    }

    @Override
    public @NotNull ItemStack getObjectStack(@NotNull Player player, @NotNull A aucItem) {
        ItemStack aucItemStack = aucItem.getItemStack().clone();
        UnaryOperator<String> replacer = aucItem.replacePlaceholders();
        aucItemStack.editMeta(meta -> {
            // Modify displayName
            meta.displayName(ComponentUtil.asComponent(replacer.apply(itemName)));
            // Modify lore
            List<String> rawLore;
            rawLore = StringUtil.replacePlaceholderList(PLACEHOLDER_LORE_FORMAT, this.itemLore, this.getLoreFormat(player, aucItem));
            rawLore = StringUtil.replace(rawLore, replacer);
            List<Component> lore;
            lore = ComponentUtil.asComponent(rawLore);
            lore = ComponentUtil.replacePlaceholderList(Placeholders.LISTING_ITEM_LORE, lore, ItemUtil.getLore(aucItemStack));
            meta.lore(lore);
        });
        return aucItemStack;
    }

    protected @NotNull List<String> getLoreFormat(@NotNull Player player, @NotNull A aucItem) {
        FormatType formatType = FormatType.PLAYER;
        if (player.hasPermission(Perms.AUCTION_LISTING_REMOVE_OTHERS)) formatType = FormatType.ADMIN;
        else if (aucItem.isOwner(player)) formatType = FormatType.OWNER;

        return this.loreFormat.getOrDefault(formatType, Collections.emptyList());
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);
        this.seeOthers.remove(viewer.getPlayer());
    }
}
