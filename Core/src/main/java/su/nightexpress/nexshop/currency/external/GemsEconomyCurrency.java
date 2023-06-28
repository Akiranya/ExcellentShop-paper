package su.nightexpress.nexshop.currency.external;

import me.xanium.gemseconomy.api.Account;
import me.xanium.gemseconomy.api.Currency;
import me.xanium.gemseconomy.api.GemsEconomy;
import me.xanium.gemseconomy.api.GemsEconomyProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nightexpress.nexshop.ShopAPI;
import su.nightexpress.nexshop.api.currency.AbstractCurrency;
import su.nightexpress.nexshop.api.currency.MultiCurrency;
import su.nightexpress.nexshop.currency.CurrencyManager;
import su.nightexpress.nexshop.currency.config.CurrencyConfig;

import java.util.Locale;

/**
 * This class should be instantiated with a currency identifier in GemsEconomy.
 */
public class GemsEconomyCurrency extends AbstractCurrency implements MultiCurrency {
    private final String identifier;

    public GemsEconomyCurrency(@NotNull String identifier) {
        super(loadOrCreateConfig(identifier));
        this.identifier = identifier;
    }

    public static void registerCurrencies() {
        // GemsEconomy plugin itself has multi currency support, which means that
        // we need to dynamically register an ICurrency for each currency in GemsEconomy database.
        // This also includes the dynamic creation of currency config files in ExcellentShop.

        for (Currency currency : GemsEconomyProvider.get().getLoadedCurrencies()) {
            ShopAPI.getCurrencyManager().registerCurrency(new GemsEconomyCurrency(currency.getName()));
        }
    }

    private static CurrencyConfig loadOrCreateConfig(String identifier) {
        JYML jyml = JYML.loadOrExtract(
            ShopAPI.PLUGIN,
            CurrencyManager.DIR_DEFAULT
            + "gemseconomy~"
            + identifier.toLowerCase(Locale.ROOT)
            + ".yml"
        );
        if (!jyml.isSet("Name")) {
            jyml.set("Name", "GemsEconomy : " + identifier.toUpperCase(Locale.ROOT));
        }
        CurrencyConfig config = new CurrencyConfig(ShopAPI.PLUGIN, jyml);
        config.save();
        return config;
    }

    @Override
    public double getBalance(@NotNull Player player) {
        GemsEconomy economy = GemsEconomyProvider.get();
        Currency currency = economy.getCurrency(identifier);
        Account account = economy.getAccount(player.getUniqueId());
        validateCurrency(currency);
        validateAccount(account, player);
        return account.getBalance(currency);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        GemsEconomy economy = GemsEconomyProvider.get();
        Currency currency = economy.getCurrency(identifier);
        Account account = economy.getAccount(player.getUniqueId());
        validateCurrency(currency);
        validateAccount(account, player);
        account.deposit(currency, amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        GemsEconomy economy = GemsEconomyProvider.get();
        Currency currency = economy.getCurrency(identifier);
        Account account = economy.getAccount(player.getUniqueId());
        validateCurrency(currency);
        validateAccount(account, player);
        account.withdraw(currency, amount);
    }

    private <T> void validateAccount(T account, Player player) {
        if (account == null) {
            throw new IllegalStateException("Cannot find GemsEconomy account for player: " + player.getName());
        }
    }

    private <T> void validateCurrency(T currency) {
        if (currency == null) {
            throw new IllegalStateException(
                "Cannot find GemsEconomy currency: " + identifier + ". "
                + "You may have deleted a currency from the GemsEconomy database? "
                + "Try to run command `/excellentshop reload` to load the "
                + "currencies from GemsEconomy again."
            );
        }
    }
}
