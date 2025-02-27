package su.nightexpress.nexshop.api.shop;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.shop.util.TransactionResult;

public abstract class PreparedProduct<P extends Product<P, ?, ?>> implements Placeholder {

    private final P product;
    private final TradeType buyType;
    private final boolean all;
    private final PlaceholderMap placeholderMap;

    private int units;

    public PreparedProduct(@NotNull P product, @NotNull TradeType buyType, boolean all) {
        this.product = product;
        this.buyType = buyType;
        this.all = all;

        this.setUnits(1);

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.GENERIC_ITEM, () -> ComponentUtil.asMiniMessage(ItemUtil.getName(this.getProduct().getPreview())))
            .add(Placeholders.GENERIC_AMOUNT, () -> String.valueOf(this.getAmount()))
            .add(Placeholders.GENERIC_UNITS, () -> String.valueOf(this.getUnits()))
            .add(Placeholders.GENERIC_TYPE, () -> this.getShop().plugin().getLangManager().getEnum(this.getTradeType()))
            .add(Placeholders.GENERIC_PRICE, () -> this.getProduct().getCurrency().format(this.getPrice()))
        ;
    }

    @Override
    public @NotNull PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    public @NotNull Shop<?, P> getShop() {
        return this.getProduct().getShop();
    }

    public @NotNull P getProduct() {
        return this.product;
    }

    public @NotNull TradeType getTradeType() {
        return this.buyType;
    }

    public boolean isAll() {
        return all;
    }

    public int getUnits() {
        return this.units;
    }

    public void setUnits(int units) {
        this.units = Math.max(units, 1);
    }

    public int getAmount() {
        return this.getProduct().getUnitAmount() * this.getUnits();
    }

    public double getPrice() {
        Product<P, ?, ?> product = this.getProduct();
        double price = product.getPricer().getPrice(this.getTradeType());
        return price * this.getUnits();
    }

    public @NotNull TransactionResult trade(@NotNull Player player) {
        return this.getTradeType() == TradeType.BUY ? this.buy(player) : this.sell(player);
    }

    protected abstract @NotNull TransactionResult buy(@NotNull Player player);

    protected abstract @NotNull TransactionResult sell(@NotNull Player player);
}
