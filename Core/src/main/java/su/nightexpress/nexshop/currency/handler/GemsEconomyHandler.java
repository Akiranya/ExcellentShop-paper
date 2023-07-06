package su.nightexpress.nexshop.currency.handler;

import me.xanium.gemseconomy.api.GemsEconomy;
import me.xanium.gemseconomy.api.GemsEconomyProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nexshop.api.currency.CurrencyHandler;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class GemsEconomyHandler implements CurrencyHandler {
    private final String identifier;

    public GemsEconomyHandler(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public static @NotNull Set<String> getCurrencies() {
        Set<String> currencies = new HashSet<>();
        GemsEconomyProvider.get().getLoadedCurrencies().forEach(it -> {
            currencies.add(it.getName().toLowerCase(Locale.ROOT));
        });
        return currencies;
    }

    @Override
    public double getBalance(@NotNull Player player) {
        GemsEconomy economy = GemsEconomyProvider.get();
        me.xanium.gemseconomy.api.Currency currency = Objects.requireNonNull(economy.getCurrency(identifier), "currency");
        me.xanium.gemseconomy.api.Account account = Objects.requireNonNull(economy.getAccount(player.getUniqueId()), "account");
        return account.getBalance(currency);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        GemsEconomy economy = GemsEconomyProvider.get();
        me.xanium.gemseconomy.api.Currency currency = Objects.requireNonNull(economy.getCurrency(identifier), "currency");
        me.xanium.gemseconomy.api.Account account = Objects.requireNonNull(economy.getAccount(player.getUniqueId()), "account");
        account.deposit(currency, amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        GemsEconomy economy = GemsEconomyProvider.get();
        me.xanium.gemseconomy.api.Currency currency = Objects.requireNonNull(economy.getCurrency(identifier), "currency");
        me.xanium.gemseconomy.api.Account account = Objects.requireNonNull(economy.getAccount(player.getUniqueId()), "account");
        account.withdraw(currency, amount);
    }
}
