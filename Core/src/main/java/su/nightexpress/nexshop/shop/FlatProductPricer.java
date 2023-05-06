package su.nightexpress.nexshop.shop;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nightexpress.nexshop.api.shop.ProductPricer;
import su.nightexpress.nexshop.api.type.PriceType;
import su.nightexpress.nexshop.api.type.TradeType;

public class FlatProductPricer extends ProductPricer {

    public FlatProductPricer() {

    }

    public static @NotNull FlatProductPricer read(@NotNull JYML cfg, @NotNull String path) {
        FlatProductPricer pricer = new FlatProductPricer();
        for (TradeType tradeType : TradeType.values()) {
            pricer.setPrice(tradeType, cfg.getDouble(path + "." + tradeType.name()));
        }
        return pricer;
    }

    @Override
    public void write(@NotNull JYML cfg, @NotNull String path) {
        this.priceCurrent.forEach(((tradeType, amount) -> cfg.set(path + "." + tradeType.name(), amount)));
    }

    @Override
    public void update() {

    }

    @Override
    public @NotNull PriceType getType() {
        return PriceType.FLAT;
    }
}
