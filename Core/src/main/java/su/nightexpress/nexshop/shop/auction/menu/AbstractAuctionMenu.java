package su.nightexpress.nexshop.shop.auction.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenuAuto;
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

public abstract class AbstractAuctionMenu<A extends AbstractAuctionItem> extends AbstractMenuAuto<ExcellentShop, A> {

    protected AuctionManager auctionManager;

    protected int[] objectSlots;

    // display name and lore are stored as MiniMessage representation
    protected String itemName;
    protected List<String> itemLore;

    protected Map<FormatType, List<String>> loreFormat;
    protected Map<Player, UUID> seeOthers;

    private static final String PLACEHOLDER_LORE_FORMAT = "%lore_format%";

    public AbstractAuctionMenu(@NotNull AuctionManager auctionManager, @NotNull JYML cfg) {
        super(auctionManager.plugin(), cfg, "");
        this.auctionManager = auctionManager;
        this.seeOthers = new WeakHashMap<>();
        this.loreFormat = new HashMap<>();

        this.itemName = cfg.getString("Items.Name", Placeholders.LISTING_ITEM_NAME);
        this.itemLore = cfg.getStringList("Items.Lore");
        this.objectSlots = cfg.getIntArray("Items.Slots");
        for (FormatType formatType : FormatType.values()) {
            this.loreFormat.put(formatType, cfg.getStringList("Lore_Format." + formatType.name()));
        }
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
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull A aucItem) {
        ItemStack aucItemStack = aucItem.getItemStack().clone();
        UnaryOperator<String> replacer = aucItem.replacePlaceholders();
        aucItemStack.editMeta(meta -> {
            // Prepare name
            String rawName = replacer.apply(this.itemName);
            Component name = ComponentUtil.asComponent(rawName);
            meta.displayName(name);
            // Prepare lore
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

    @NotNull
    protected List<String> getLoreFormat(@NotNull Player player, @NotNull A aucItem) {
        FormatType formatType = FormatType.PLAYER;
        if (player.hasPermission(Perms.ADMIN)) formatType = FormatType.ADMIN;
        else if (aucItem.isOwner(player)) formatType = FormatType.OWNER;

        return this.loreFormat.getOrDefault(formatType, Collections.emptyList());
    }

    @Override
    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
        super.onClose(player, e);
        this.seeOthers.remove(player);
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return true;
    }
}
