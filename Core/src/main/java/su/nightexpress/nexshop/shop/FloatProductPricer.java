package su.nightexpress.nexshop.shop;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.IScheduled;
import su.nightexpress.nexshop.api.shop.ProductPricer;
import su.nightexpress.nexshop.api.type.PriceType;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.data.price.ProductPriceData;
import su.nightexpress.nexshop.data.price.ProductPriceManager;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class FloatProductPricer extends ProductPricer implements IScheduled {

    private final Map<TradeType, double[]> priceMinMax;
    private Set<DayOfWeek>   days;
    private Set<LocalTime> times;

    private ScheduledFuture<?> updateTask;

    public FloatProductPricer() {
        this.priceMinMax = new HashMap<>();
        this.days = new HashSet<>();
        this.times = new HashSet<>();
    }

    @NotNull
    public static FloatProductPricer read(@NotNull JYML cfg, @NotNull String path) {
        FloatProductPricer pricer = new FloatProductPricer();
        Map<TradeType, double[]> priceMap = new HashMap<>();
        for (TradeType tradeType : TradeType.values()) {
            double min = cfg.getDouble(path + "." + tradeType.name() + ".Min", -1D);
            double max = cfg.getDouble(path + "." + tradeType.name() + ".Max", -1D);
            pricer.setPriceMin(tradeType, min);
            pricer.setPriceMax(tradeType, max);
        }
        pricer.setDays(IScheduled.parseDays(cfg.getString(path + ".Refresh.Days", "")));
        pricer.setTimes(IScheduled.parseTimes(cfg.getStringList(path + ".Refresh.Times")));

        return pricer;
    }

    @Override
    public void write(@NotNull JYML cfg, @NotNull String path) {
        this.priceMinMax.forEach(((tradeType, arr) -> {
            cfg.set(path + "." + tradeType.name() + ".Min", arr[0]);
            cfg.set(path + "." + tradeType.name() + ".Max", arr[1]);
        }));
        cfg.set(path + ".Refresh.Days", this.getDays().stream().map(DayOfWeek::name).collect(Collectors.joining(",")));
        cfg.set(path + ".Refresh.Times", this.getTimes().stream().map(TIME_FORMATTER::format).toList());
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return str -> str
            .replace(Placeholders.PRODUCT_PRICER_BUY_MIN, String.valueOf(this.getPriceMin(TradeType.BUY)))
            .replace(Placeholders.PRODUCT_PRICER_BUY_MAX, String.valueOf(this.getPriceMax(TradeType.BUY)))
            .replace(Placeholders.PRODUCT_PRICER_SELL_MIN, String.valueOf(this.getPriceMin(TradeType.SELL)))
            .replace(Placeholders.PRODUCT_PRICER_SELL_MAX, String.valueOf(this.getPriceMax(TradeType.SELL)))
            // .replace(Placeholders.PRODUCT_PRICER_FLOAT_REFRESH_DAYS, String.join(DELIMITER_DEFAULT, this.getDays().stream().map(DayOfWeek::name).toList()))
            // .replace(Placeholders.PRODUCT_PRICER_FLOAT_REFRESH_TIMES, String.join(DELIMITER_DEFAULT, this.getTimes().stream().map(TIME_FORMATTER::format).toList()))
            ;
    }

    @Override
    public void update() {
        ProductPriceData priceData = this.getData();
        boolean hasData = priceData != null;
        if (hasData) {
            this.setPrice(TradeType.BUY, priceData.getLastBuyPrice());
            this.setPrice(TradeType.SELL, priceData.getLastSellPrice());
        }
        else {
            this.randomize();
        }
        this.startScheduler();
    }

    @Override
    public void startScheduler() {
        this.updateTask = this.createScheduler();
    }

    @Override
    @NotNull
    public Runnable getCommand() {
        return this::randomize;
    }

    @Override
    public boolean canSchedule() {
        return this.updateTask == null || this.updateTask.isDone();
    }

    @Override
    public void stopScheduler() {
        if (this.updateTask != null) {
            this.updateTask.cancel(true);
        }
    }

    @Override
    @NotNull
    public PriceType getType() {
        return PriceType.FLOAT;
    }

    @NotNull
    @Override
    public Set<DayOfWeek> getDays() {
        return days;
    }

    @Override
    public void setDays(@NotNull Set<DayOfWeek> days) {
        this.days = days;
    }

    @NotNull
    @Override
    public Set<LocalTime> getTimes() {
        return times;
    }

    @Override
    public void setTimes(@NotNull Set<LocalTime> times) {
        this.times = times;
    }

    @Nullable
    public ProductPriceData getData() {
        return ProductPriceManager.getData(this.getProduct().getShop().getId(), this.getProduct().getId());
    }

    public double getPriceMin(@NotNull TradeType tradeType) {
        return this.priceMinMax.computeIfAbsent(tradeType, b -> new double[]{-1, -1})[0];
    }

    public double getPriceMax(@NotNull TradeType tradeType) {
        return this.priceMinMax.computeIfAbsent(tradeType, b -> new double[]{-1, -1})[1];
    }

    public void setPriceMin(@NotNull TradeType tradeType, double price) {
        this.priceMinMax.computeIfAbsent(tradeType, b -> new double[]{-1, -1})[0] = price;
    }

    public void setPriceMax(@NotNull TradeType tradeType, double price) {
        this.priceMinMax.computeIfAbsent(tradeType, b -> new double[]{-1, -1})[1] = price;
    }

    public void randomize() {
        double buyPrice = Rnd.getDouble(this.getPriceMin(TradeType.BUY), this.getPriceMax(TradeType.BUY));
        double sellPrice = Rnd.getDouble(this.getPriceMin(TradeType.SELL), this.getPriceMax(TradeType.SELL));
        if (sellPrice > buyPrice && buyPrice >= 0) {
            sellPrice = buyPrice;
        }

        //this.setPrice(TradeType.BUY, buyPrice);
        //this.setPrice(TradeType.SELL, sellPrice);

        ProductPriceData priceData = this.getData();
        boolean hasData = priceData != null;
        if (!hasData) {
            priceData = new ProductPriceData(this);
        }
        priceData.setLastBuyPrice(buyPrice);
        priceData.setLastSellPrice(sellPrice);
        priceData.setLastUpdated(System.currentTimeMillis());
        if (!hasData) {
            ProductPriceManager.createData(priceData);
        }
        else {
            ProductPriceManager.saveData(priceData);
        }

        this.update();
    }
}
