package su.nightexpress.nexshop.currency.config;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.currency.CurrencyManager;

public class CurrencyItemConfig extends CurrencyConfig {

    protected ItemStack item;

    public CurrencyItemConfig(@NotNull ExcellentShop plugin, @NotNull String id) {
        this(plugin, JYML.loadOrExtract(plugin, CurrencyManager.DIR_CUSTOM + id + ".yml"));
    }

    public CurrencyItemConfig(@NotNull ExcellentShop plugin, @NotNull JYML cfg) {
        super(plugin, cfg);

        this.item = cfg.getItem("Item");
    }

    @Override
    public void onSave() {
        super.onSave();
        this.cfg.setItem("Item", this.getItem());
        this.cfg.remove("Icon");
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = item.clone();
    }

    @NotNull
    public ItemStack getItem() {
        return this.item.clone();
    }

    @Override
    @NotNull
    public ItemStack getIcon() {
        return this.getItem();
    }
}
