package su.nightexpress.nexshop.shop.virtual.impl;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.item.PluginItem;
import su.nexmedia.engine.api.item.PluginItemRegistry;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.ShopAPI;
import su.nightexpress.nexshop.api.IScheduled;
import su.nightexpress.nexshop.api.currency.ICurrency;
import su.nightexpress.nexshop.api.shop.Product;
import su.nightexpress.nexshop.api.shop.ProductPricer;
import su.nightexpress.nexshop.api.type.PriceType;
import su.nightexpress.nexshop.api.type.StockType;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.currency.CurrencyId;
import su.nightexpress.nexshop.shop.FlatProductPricer;
import su.nightexpress.nexshop.shop.FloatProductPricer;
import su.nightexpress.nexshop.shop.virtual.editor.menu.EditorShopProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class VirtualProduct extends Product<VirtualProduct, VirtualShop, VirtualProductStock> {

    private       int                   shopSlot;
    private       int                   shopPage;
    private       List<String>          commands;

    private EditorShopProduct editor;

    public VirtualProduct(@NotNull ICurrency currency, @NotNull ItemStack item) {
        this(UUID.randomUUID().toString(), item, currency);
    }

    public VirtualProduct(@NotNull String id, @NotNull ItemStack preview, @NotNull ICurrency currency) {
        super(id, preview, currency);
        this.commands = new ArrayList<>();
    }

    @NotNull
    public static VirtualProduct read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        if (cfg.contains(path + ".Purchase")) {
            cfg.addMissing(path + ".Currency", cfg.getString(path + ".Purchase.Currency"));
            cfg.addMissing(path + ".Item_Meta_Enabled", cfg.getBoolean(path + ".Purchase.Item_Meta_Enabled"));
            double buyMin = cfg.getDouble(path + ".Purchase.BUY.Price_Min");
            double buyMax = cfg.getDouble(path + ".Purchase.BUY.Price_Max");
            double sellMin = cfg.getDouble(path + ".Purchase.SELL.Price_Min");
            double sellMax = cfg.getDouble(path + ".Purchase.SELL.Price_Max");

            if (cfg.getBoolean(path + ".Purchase.Randomizer.Enabled")) {
                FloatProductPricer pricer = new FloatProductPricer();
                pricer.setPriceMin(TradeType.BUY, buyMin);
                pricer.setPriceMax(TradeType.BUY, buyMax);
                pricer.setPriceMin(TradeType.SELL, sellMin);
                pricer.setPriceMin(TradeType.SELL, sellMax);
                pricer.setDays(IScheduled.parseDays(cfg.getString(path + ".Purchase.Randomizer.Times.Days", "")));
                pricer.setTimes(IScheduled.parseTimesOld(cfg.getStringList(path + ".Purchase.Randomizer.Times.Times")));
                cfg.addMissing(path + ".Price.Type", pricer.getType().name());
                pricer.write(cfg, path + ".Price");
            } else {
                FlatProductPricer pricer = new FlatProductPricer();
                pricer.setPrice(TradeType.BUY, buyMin);
                pricer.setPrice(TradeType.SELL, sellMin);
                cfg.addMissing(path + ".Price.Type", pricer.getType().name());
                pricer.write(cfg, path + ".Price");
            }

            cfg.remove(path + ".Purchase");
            cfg.saveChanges();
        }
        if (cfg.contains(path + ".Limit")) {
            VirtualProductStock stock = new VirtualProductStock();
            for (TradeType tradeType : TradeType.values()) {
                int init = cfg.getInt(path + ".Limit." + tradeType.name() + ".Amount");
                int cooldown = cfg.getInt(path + ".Limit." + tradeType.name() + ".Cooldown");

                stock.setInitialAmount(StockType.PLAYER, tradeType, init);
                stock.setRestockCooldown(StockType.PLAYER, tradeType, cooldown);
            }
            cfg.addMissing(path + ".Stock", stock);
            cfg.remove(path + ".Limit");
            cfg.saveChanges();
        }

        // Start - Integrations with custom items from external plugins
        PluginItem<?> pluginItemPreview = null;
        PluginItem<?> pluginItemReal = null;
        // End

        ItemStack preview = cfg.getItemEncoded(path + ".Shop_View.Preview");
        // Start - Integrations with custom items from external plugins
        String reference1 = cfg.getString(path + ".Shop_View.PluginItem");
        if (PluginItemRegistry.isPluginItemId(reference1)) {
            pluginItemPreview = PluginItemRegistry.fromConfig(reference1);
            ItemStack pluginItemStack = pluginItemPreview.createItemStack();
            if (pluginItemStack != null) preview = pluginItemStack;
        }
        // End
        if (preview == null || preview.getType().isAir()) {
            throw new IllegalStateException("Invalid preview item!");
        }
        String currencyId = cfg.getString(path + ".Currency", CurrencyId.VAULT);
        ICurrency currency = ShopAPI.getCurrencyManager().getCurrency(currencyId);
        if (currency == null) {
            throw new IllegalStateException("Invalid currency!");
        }
        VirtualProduct product = new VirtualProduct(id, preview, currency);

        product.setSlot(cfg.getInt(path + ".Shop_View.Slot", -1));
        product.setPage(cfg.getInt(path + ".Shop_View.Page", -1));
        product.setDiscountAllowed(cfg.getBoolean(path + ".Discount.Allowed"));
        product.setItemMetaEnabled(cfg.getBoolean(path + ".Item_Meta_Enabled"));

        ItemStack real = cfg.getItemEncoded(path + ".Reward.Item");
        // Start - integrations with custom items from external plugins
        String reference2 = cfg.getString(path + ".Reward.PluginItem");
        if (PluginItemRegistry.isPluginItemId(reference2)) {
            pluginItemReal = PluginItemRegistry.fromConfig(reference2);
            ItemStack pluginItemStack = pluginItemReal.createItemStack();
            if (pluginItemStack != null) real = pluginItemStack;
        }
        // End
        product.setItem(real);
        product.setCommands(cfg.getStringList(path + ".Reward.Commands"));

        PriceType priceType = cfg.getEnum(path + ".Price.Type", PriceType.class, PriceType.FLAT);
        product.setPricer(ProductPricer.read(priceType, cfg, path + ".Price"));
        product.setStock(VirtualProductStock.read(cfg, path + ".Stock"));
        // Start - integrations with custom items from external plugins
        product.setPreviewPluginItem(pluginItemPreview);
        product.setPluginItem(pluginItemReal);
        // End
        return product;
    }

    @Override
    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.setItemEncoded(path + ".Shop_View.Preview", this.getPreview());
        cfg.set(path + ".Shop_View.Slot", this.getSlot());
        cfg.set(path + ".Shop_View.Page", this.getPage());
        cfg.set(path + ".Discount.Allowed", this.isDiscountAllowed());
        cfg.set(path + ".Item_Meta_Enabled", this.isItemMetaEnabled());
        cfg.set(path + ".Stock", this.getStock());
        ProductPricer pricer = this.getPricer();
        cfg.set(path + ".Currency", this.getCurrency().getId());
        cfg.set(path + ".Price.Type", pricer.getType().name());
        cfg.set(path + ".Price", pricer);
        cfg.setItemEncoded(path + ".Reward.Item", this.getItem());
        cfg.set(path + ".Reward.Commands", this.getCommands());
        // Start - integrations with custom items from external plugins
        cfg.set(path + ".Shop_View.PluginItem", this.getPreviewPluginItemOrNull() != null ? this.getPreviewPluginItemOrNull().asReference() : null);
        cfg.set(path + ".Reward.PluginItem", this.getPluginItemOrNull() != null ? this.getPluginItemOrNull().asReference() : null);
        // End
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return str -> super.replacePlaceholders().apply(str
            .replace(Placeholders.PRODUCT_VIRTUAL_COMMANDS, String.join(DELIMITER_DEFAULT, this.getCommands()))
        );
    }

    @Override
    public void clear() {
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
    }

    @Override
    @NotNull
    public EditorShopProduct getEditor() {
        if (this.editor == null) {
            this.editor = new EditorShopProduct(this.getShop().plugin(), this);
        }
        return this.editor;
    }

    @NotNull
    protected VirtualProduct get() {
        return this;
    }

    @Override
    @NotNull
    public VirtualPreparedProduct getPrepared(@NotNull TradeType buyType) {
        return new VirtualPreparedProduct(this, buyType);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && !this.hasCommands();
    }

    @Override
    public boolean isItemMatches(@NotNull ItemStack item) {
        if (this.getPluginItemOrNull() != null) {
            // If PluginItem is set for the real item,
            // we match items using PluginItem#matches()
            return this.getPluginItemOrNull().matches(item);
        }
        return super.isItemMatches(item);
    }

    public int getSlot() {
        return this.shopSlot;
    }

    public void setSlot(int slot) {
        this.shopSlot = slot;
    }

    public int getPage() {
        return this.shopPage;
    }

    public void setPage(int page) {
        this.shopPage = page;
    }

    @NotNull
    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(@NotNull List<String> commands) {
        this.commands = commands;
    }

    public boolean hasCommands() {
        return !this.getCommands().isEmpty();
    }
}
