package su.nightexpress.nexshop.shop.virtual.impl.product;

import cc.mewcraft.spatula.item.PluginItem;
import cc.mewcraft.spatula.item.PluginItemRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.currency.Currency;
import su.nightexpress.nexshop.api.shop.ItemProduct;

import java.util.Objects;
import java.util.UUID;

public class VirtualItemProduct extends VirtualProduct implements ItemProduct {

    private ItemStack item;
    private boolean respectItemMeta;
    // Akiranya starts - allow the product to match with custom items from external plugins
    private int pluginItemAmount;
    private PluginItem<?> pluginItem;
    private boolean respectPluginItem;
    // Akiranya ends

    public VirtualItemProduct(@NotNull ItemStack item, @NotNull Currency currency) {
        this(UUID.randomUUID().toString(), item, currency);
    }

    public VirtualItemProduct(@NotNull String id, @NotNull ItemStack item, @NotNull Currency currency) {
        super(id, currency);
        this.setItem(item);
        this.setRespectItemMeta(item.hasItemMeta());
        this.placeholderMap
            .add(Placeholders.PRODUCT_ITEM_META_ENABLED, () -> LangManager.getBoolean(this.isRespectItemMeta()))
            .add(Placeholders.PRODUCT_PLUGIN_ITEM_ENABLED, () -> LangManager.getBoolean(this.isRespectPluginItem()))
        ;
    }

    @Override
    public void delivery(@NotNull Player player, int count) {
        int amount = this.getUnitAmount() * count;
        PlayerUtil.addItem(player, this.getItem(), amount);
    }

    @Override
    public void take(@NotNull Player player, int count) {
        int amount = this.getUnitAmount() * count;
        PlayerUtil.takeItem(player, this::isItemMatches, amount);
    }

    @Override
    public int count(@NotNull Player player) {
        return PlayerUtil.countItem(player, this::isItemMatches);
    }

    @Override
    public @NotNull ItemStack getPreview() {
        return this.getItem();
    }

    @Override
    public boolean isRespectItemMeta() {
        return this.respectItemMeta;
    }

    @Override
    public void setRespectItemMeta(boolean respectItemMeta) {
        this.respectItemMeta = respectItemMeta;
    }

    // Akiranya starts - plugin item support
    @Override
    public boolean isRespectPluginItem() {
        return this.respectPluginItem;
    }

    @Override
    public void setRespectPluginItem(final boolean respectPluginItem) {
        this.respectPluginItem = respectPluginItem;
        this.setPluginItem(PluginItemRegistry.INSTANCE.byItemStackOrNull(this.item));
        this.setPluginItemAmount(this.item.getAmount());
    }

    @Override
    public @Nullable PluginItem<?> getPluginItem() {
        return this.pluginItem;
    }

    @Override
    public void setPluginItem(final @Nullable PluginItem<?> pluginItem) {
        this.pluginItem = pluginItem;
    }

    @Override
    public int getPluginItemAmount() {
        return this.pluginItemAmount;
    }

    @Override
    public void setPluginItemAmount(final int amount) {
        this.pluginItemAmount = amount;
    }
    // Akiranya ends

    @Override
    public int getUnitAmount() {
        return this.getItem().getAmount();
    }

    @Override
    public @NotNull ItemStack getItem() {
        // Akiranya starts - plugin item support
        if (isRespectPluginItem() && getPluginItem() != null) {
            ItemStack itemStack = Objects.requireNonNull(getPluginItem().createItemStack());
            itemStack.setAmount(this.getPluginItemAmount());
            return itemStack;
        }
        // Akiranya ends
        return this.item.clone();
    }

    @Override
    public void setItem(@NotNull ItemStack item) {
        this.item = item.clone();
    }

    @Override
    public boolean hasSpace(@NotNull Player player) {
        return PlayerUtil.countItemSpace(player, this.getItem()) > 0;
    }
}
