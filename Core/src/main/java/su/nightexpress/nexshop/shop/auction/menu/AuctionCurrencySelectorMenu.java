package su.nightexpress.nexshop.shop.auction.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.menu.click.ClickHandler;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.ConfigMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.Pair;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.api.currency.Currency;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.AuctionUtils;
import su.nightexpress.nexshop.shop.auction.Placeholders;
import su.nightexpress.nexshop.shop.auction.config.AuctionConfig;

import java.util.*;
import java.util.function.UnaryOperator;

public class AuctionCurrencySelectorMenu extends ConfigMenu<ExcellentShop> implements AutoPaged<Currency> {

    private final AuctionManager auctionManager;
    private final int[] objectSlots;
    private final String itemName;
    private final List<String> itemLore;

    private static final Map<Player, Pair<ItemStack, Double>> PREPARED_ITEM = new WeakHashMap<>();

    public AuctionCurrencySelectorMenu(@NotNull AuctionManager auctionManager, @NotNull JYML cfg) {
        super(auctionManager.plugin(), cfg);
        this.auctionManager = auctionManager;
        this.itemName = cfg.getString("Items.Name", Placeholders.LISTING_ITEM_NAME); // Mewcraft - no legacy color
        this.itemLore = cfg.getStringList("Items.Lore"); // Mewcraft - no legacy color
        this.objectSlots = cfg.getIntArray("Items.Slots");

        this.registerHandler(MenuItemType.class)
            .addClick(MenuItemType.PAGE_NEXT, ClickHandler.forNextPage(this))
            .addClick(MenuItemType.PAGE_PREVIOUS, ClickHandler.forPreviousPage(this))
            .addClick(MenuItemType.CLOSE, (viewer, event) -> plugin.runTask(task -> viewer.getPlayer().closeInventory()));

        this.load();
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);
        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    public void open(@NotNull Player player, @NotNull ItemStack item, double price) {
        PREPARED_ITEM.put(player, Pair.of(item, price));
        this.open(player, 1);
    }

    private @Nullable Pair<ItemStack, Double> getPrepared(@NotNull Player player) {
        return PREPARED_ITEM.get(player);
    }

    @Override
    public int[] getObjectSlots() {
        return this.objectSlots;
    }

    @Override
    public @NotNull List<Currency> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.auctionManager.getCurrencies(player));
    }

    @Override
    public @NotNull ItemStack getObjectStack(@NotNull Player player, @NotNull Currency currency) {
        ItemStack item = currency.getIcon();

        Pair<ItemStack, Double> prepared = this.getPrepared(player);
        if (prepared == null) return item;

        double price = prepared.getSecond();
        double tax = AuctionUtils.calculateTax(price, AuctionConfig.LISTINGS_TAX_ON_LISTING_ADD);

        // Prepare displayName and lore
        UnaryOperator<String> replacer = str -> str
            .transform(currency.replacePlaceholders())
            .replace(Placeholders.GENERIC_PRICE, currency.format(price))
            .replace(Placeholders.GENERIC_TAX, currency.format(tax));
        final String name = StringUtil.replace(this.itemName, replacer);
        final List<String> lore = StringUtil.replace(this.itemLore, replacer);

        // Apply displayName and lore
        item.editMeta(meta -> {
            meta.displayName(ComponentUtil.asComponent(name));
            meta.lore(ComponentUtil.asComponent(lore));
        });
        return item;
    }

    @Override
    public @NotNull ItemClick getObjectClick(@NotNull Currency currency) {
        return (viewer, event) -> {
            Player player = viewer.getPlayer();
            Pair<ItemStack, Double> prepared = this.getPrepared(player);
            if (prepared == null) {
                player.closeInventory();
                return;
            }

            if (this.auctionManager.add(player, prepared.getFirst(), currency, prepared.getSecond())) {
                PREPARED_ITEM.remove(player);
            }
            player.closeInventory();
        };
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        super.onClose(viewer, event);
        Pair<ItemStack, Double> prepared = PREPARED_ITEM.remove(viewer.getPlayer());
        if (prepared != null) {
            PlayerUtil.addItem(viewer.getPlayer(), prepared.getFirst());
        }
    }
}
