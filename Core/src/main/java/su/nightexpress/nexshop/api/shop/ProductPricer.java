package su.nightexpress.nexshop.api.shop;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nightexpress.nexshop.api.type.PriceType;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.currency.internal.ItemCurrency;
import su.nightexpress.nexshop.shop.DynamicProductPricer;
import su.nightexpress.nexshop.shop.FlatProductPricer;
import su.nightexpress.nexshop.shop.FloatProductPricer;

import java.util.HashMap;
import java.util.Map;

public abstract class ProductPricer implements Placeholder {

    protected final Map<TradeType, Double> priceCurrent;
    protected final PlaceholderMap placeholderMap;

    protected Product<?, ?, ?> product;

    public ProductPricer() {
        this.priceCurrent = new HashMap<>();
        this.setPrice(TradeType.BUY, -1D);
        this.setPrice(TradeType.SELL, -1D);
        this.placeholderMap = new PlaceholderMap();
    }

    public static @NotNull ProductPricer read(@NotNull JYML cfg, @NotNull String path) {
        PriceType priceType = cfg.getEnum(path + ".Type", PriceType.class, PriceType.FLAT);

        return switch (priceType) {
            case FLAT -> FlatProductPricer.read(cfg, path);
            case FLOAT -> FloatProductPricer.read(cfg, path);
            case DYNAMIC -> DynamicProductPricer.read(cfg, path);
        };
    }

    public static @NotNull ProductPricer from(@NotNull PriceType priceType) {
        return switch (priceType) {
            case FLAT -> new FlatProductPricer();
            case FLOAT -> new FloatProductPricer();
            case DYNAMIC -> new DynamicProductPricer();
        };
    }

    public abstract void write(@NotNull JYML cfg, @NotNull String path);

    @Override
    public @NotNull PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    public abstract void update();

    public abstract @NotNull PriceType getType();

    public @NotNull Product<?, ?, ?> getProduct() {
        if (this.product == null) {
            throw new IllegalStateException("Product is undefined!");
        }
        return this.product;
    }

    public void setProduct(@NotNull Product<?, ?, ?> product) {
        this.product = product;
    }

    public double getPrice(@NotNull TradeType tradeType) {
        double price = this.priceCurrent.computeIfAbsent(tradeType, b -> -1D);

        if (tradeType == TradeType.BUY && price > 0 && this.getProduct().isDiscountAllowed()) {
            price *= this.getProduct().getShop().getDiscountModifier();
        }
        return price;
    }

    public void setPrice(@NotNull TradeType tradeType, double price) {
        if (this.product != null && this.getProduct().getCurrency() instanceof ItemCurrency) {
            price = (int) Math.floor(price);
        }
        this.priceCurrent.put(tradeType, price);
    }

    public double getPriceBuy() {
        return this.getPrice(TradeType.BUY);
    }

    public double getPriceSell() {
        return this.getPrice(TradeType.SELL);
    }

    public double getPriceSellAll(@NotNull Player player) {
        int amountHas = this.getProduct().countUnits(player);
        int amountCan = this.getProduct().getStock().getPossibleAmount(TradeType.SELL, player);

        int balance = Math.min((amountCan < 0 ? amountHas : amountCan), amountHas);
        return balance * this.getPriceSell();
    }
}
