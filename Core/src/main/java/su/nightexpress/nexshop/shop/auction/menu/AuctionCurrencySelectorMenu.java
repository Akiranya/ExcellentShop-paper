package su.nightexpress.nexshop.shop.auction.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenuAuto;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.*;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.api.currency.ICurrency;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.AuctionUtils;
import su.nightexpress.nexshop.shop.auction.Placeholders;
import su.nightexpress.nexshop.shop.auction.config.AuctionConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class AuctionCurrencySelectorMenu extends AbstractMenuAuto<ExcellentShop, ICurrency> {

    private final AuctionManager  auctionManager;
    private final int[]           objectSlots;
    private final Component       itemName;
    private final List<Component> itemLore;

    private static final Map<Player, Pair<ItemStack, Double>> PREPARED_ITEM = new WeakHashMap<>();

    public AuctionCurrencySelectorMenu(@NotNull AuctionManager auctionManager, @NotNull JYML cfg) {
        super(auctionManager.plugin(), cfg, "");
        this.auctionManager = auctionManager;
        this.itemName = cfg.getComponent("Items.Name", ComponentUtil.asComponent(Placeholders.LISTING_ITEM_NAME));
        this.itemLore = cfg.getComponentList("Items.Lore");
        this.objectSlots = cfg.getIntArray("Items.Slots");

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

    public void open(@NotNull Player player, @NotNull ItemStack item, double price) {
        PREPARED_ITEM.put(player, Pair.of(item, price));
        this.open(player, 1);
    }

    @Nullable
    private Pair<ItemStack, Double> getPrepared(@NotNull Player player) {
        return PREPARED_ITEM.get(player);
    }

    @Override
    protected int[] getObjectSlots() {
        return this.objectSlots;
    }

    @Override
    @NotNull
    protected List<ICurrency> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.auctionManager.getCurrencies(player));
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull ICurrency currency) {
        ItemStack item = currency.getIcon();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        Pair<ItemStack, Double> prepared = this.getPrepared(player);
        if (prepared == null) return item;

        double price = prepared.getSecond();
        double tax = AuctionUtils.calculateTax(price, AuctionConfig.LISTINGS_TAX_ON_LISTING_ADD);

        meta.displayName(this.itemName);
        meta.lore(this.itemLore);
        item.setItemMeta(meta);

        ItemUtil.replace(item, currency.replacePlaceholders());
        ItemUtil.replace(item, str -> str
            .replace(Placeholders.GENERIC_PRICE, currency.format(price))
            .replace(Placeholders.GENERIC_TAX, currency.format(tax))
        );
        return item;
    }

    @Override
    @NotNull
    protected MenuClick getObjectClick(@NotNull Player player, @NotNull ICurrency currency) {
        return (player2, type, e) -> {
            Pair<ItemStack, Double> prepared = this.getPrepared(player2);
            if (prepared == null) {
                player2.closeInventory();
                return;
            }

            if (this.auctionManager.add(player2, prepared.getFirst(), currency, prepared.getSecond())) {
                PREPARED_ITEM.remove(player2);
            }
            player2.closeInventory();
        };
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
        super.onClose(player, e);
        Pair<ItemStack, Double> prepared = PREPARED_ITEM.remove(player);
        if (prepared != null) {
            PlayerUtil.addItem(player, prepared.getFirst());
        }
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent inventoryClickEvent, @NotNull SlotType slotType) {
        return true;
    }
}
