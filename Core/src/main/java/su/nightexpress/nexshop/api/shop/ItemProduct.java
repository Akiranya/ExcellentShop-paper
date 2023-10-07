package su.nightexpress.nexshop.api.shop;

import cc.mewcraft.spatula.item.PluginItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemProduct {

    boolean isRespectItemMeta();

    void setRespectItemMeta(boolean respectItemMeta);

    // Akiranya starts - plugin item support
    void setRespectPluginItem(boolean respectPluginItem);

    boolean isRespectPluginItem();

    @Nullable PluginItem<?> getPluginItem();

    void setPluginItem(@Nullable PluginItem<?> pluginItem);

    int getPluginItemAmount();

    void setPluginItemAmount(int amount);
    // Akiranya ends

    /**
     * Gets the item representing this product.
     * <p>
     * Implementation notes: if this product has plugin item enabled, then this should always return the latest version
     * of the plugin item.
     *
     * @return the item representing this product
     */
    @NotNull ItemStack getItem();

    /**
     * Sets the item representing this product.
     *
     * @param item the item representing this product
     */
    void setItem(@NotNull ItemStack item);

    default boolean isItemMatches(@NotNull ItemStack item) {
        // Akiranya starts - plugin item support
        if (isRespectPluginItem() && getPluginItem() != null) {
            // NB: Plugin Item Match takes priority over Item Meta Match
            return getPluginItem().matches(item);
        }
        // Akiranya ends
        return this.isRespectItemMeta() ? this.getItem().isSimilar(item) : this.getItem().getType() == item.getType();
    }
}
