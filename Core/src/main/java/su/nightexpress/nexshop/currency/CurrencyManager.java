package su.nightexpress.nexshop.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.hooks.misc.VaultHook;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.api.currency.Currency;
import su.nightexpress.nexshop.api.currency.CurrencyHandler;
import su.nightexpress.nexshop.currency.handler.ExpPointsHandler;
import su.nightexpress.nexshop.currency.handler.GemsEconomyHandler;
import su.nightexpress.nexshop.currency.handler.VaultEconomyHandler;
import su.nightexpress.nexshop.currency.impl.ConfigCurrency;
import su.nightexpress.nexshop.currency.impl.ItemCurrency;
import su.nightexpress.nexshop.hook.HookId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CurrencyManager extends AbstractManager<ExcellentShop> {

    public static final String DIR_DEFAULT = "/currency/default/";
    public static final String DIR_CUSTOM = "/currency/custom_item/";

    public static final String EXP = "exp";
    public static final String VAULT = "vault";

    private final Map<String, Currency> currencyMap;

    public CurrencyManager(@NotNull ExcellentShop plugin) {
        super(plugin);
        this.currencyMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.plugin.getConfigManager().extractResources(DIR_DEFAULT);
        this.plugin.getConfigManager().extractResources(DIR_CUSTOM);

        this.registerCurrency(EXP, ExpPointsHandler::new);

        if (Hooks.hasVault() && VaultHook.hasEconomy()) {
            this.registerCurrency(VAULT, VaultEconomyHandler::new);
        }
        if (Hooks.hasPlugin(HookId.GEMS_ECONOMY)) {
            GemsEconomyHandler.getCurrencies().forEach(it -> registerCurrency("gemseconomy_" + it, () -> new GemsEconomyHandler(it)));
        }

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + DIR_CUSTOM, true)) {
            ItemCurrency currency = new su.nightexpress.nexshop.currency.impl.ItemCurrency(plugin, cfg);
            if (currency.load()) {
                this.registerCurrency(currency);
            }
        }
    }

    private void deprecatedCurrency(@NotNull String plugin) {
        this.plugin.warn("=".repeat(15));
        this.plugin.warn("Support for the '" + plugin + "' plugin is deprecated!");
        this.plugin.warn("=".repeat(15));
    }

    @Override
    protected void onShutdown() {
        this.currencyMap.clear();
    }

    public boolean registerCurrency(@NotNull String id, @NotNull Supplier<CurrencyHandler> supplier) {
        JYML cfg = JYML.loadOrExtract(plugin, DIR_DEFAULT + id + ".yml");
        ConfigCurrency currency = new ConfigCurrency(plugin, cfg, supplier.get());
        if (!currency.load()) return false;

        return this.registerCurrency(currency);
    }

    public boolean registerCurrency(@NotNull Currency currency) {
        this.currencyMap.put(currency.getId(), currency);
        this.plugin.info("Registered currency: " + currency.getId());
        return true;
    }

    public boolean hasCurrency() {
        return !this.currencyMap.isEmpty();
    }

    public @NotNull Collection<Currency> getCurrencies() {
        return currencyMap.values();
    }

    public @NotNull Set<String> getCurrencyIds() {
        return this.currencyMap.keySet();
    }

    public @Nullable Currency getCurrency(@NotNull String id) {
        return this.currencyMap.get(id.toLowerCase());
    }

    public @NotNull Currency getAny() {
        return this.getCurrencies().stream().findFirst().orElseThrow();
    }
}
