package su.nightexpress.nexshop;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.command.list.ReloadSubCommand;
import su.nightexpress.nexshop.api.type.PriceType;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.command.currency.CurrencyMainCommand;
import su.nightexpress.nexshop.config.Config;
import su.nightexpress.nexshop.config.Lang;
import su.nightexpress.nexshop.currency.CurrencyManager;
import su.nightexpress.nexshop.data.ShopDataHandler;
import su.nightexpress.nexshop.data.ShopUserManager;
import su.nightexpress.nexshop.data.user.ShopUser;
import su.nightexpress.nexshop.hooks.HookId;
import su.nightexpress.nexshop.module.ModuleManager;
import su.nightexpress.nexshop.shop.PriceUpdateTask;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.chest.ChestShopModule;
import su.nightexpress.nexshop.shop.chest.compatibility.WorldGuardFlags;
import su.nightexpress.nexshop.shop.menu.ShopCartMenu;
import su.nightexpress.nexshop.shop.virtual.VirtualShopModule;

public class ExcellentShop extends NexPlugin<ExcellentShop> implements UserDataHolder<ExcellentShop, ShopUser> {

    private ShopDataHandler dataHandler;
    private ShopUserManager userManager;

    private ShopCartMenu cartMenu;
    private CurrencyManager currencyManager;
    private ModuleManager moduleManager;

    private PriceUpdateTask priceUpdateTask;

    @Override
    protected @NotNull ExcellentShop getSelf() {
        return this;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.getServer().getPluginManager().getPlugin(HookId.WORLD_GUARD) != null) {
            WorldGuardFlags.setupFlag();
        }
    }

    @Override
    public void enable() {
        this.cartMenu = new ShopCartMenu(this);

        this.currencyManager = new CurrencyManager(this);
        this.currencyManager.setup();

        this.moduleManager = new ModuleManager(this);
        this.moduleManager.setup();
        this.moduleManager.loadModules();

        this.priceUpdateTask = new PriceUpdateTask(this);
        this.priceUpdateTask.start();
    }

    @Override
    public void disable() {
        if (this.priceUpdateTask != null) {
            this.priceUpdateTask.stop();
            this.priceUpdateTask = null;
        }
        this.cartMenu.clear();
        if (this.moduleManager != null) {
            this.moduleManager.shutdown();
            this.moduleManager = null;
        }
        if (this.currencyManager != null) {
            this.currencyManager.shutdown();
            this.currencyManager = null;
        }
    }

    @Override
    public boolean setupDataHandlers() {
        try {
            this.dataHandler = ShopDataHandler.getInstance(this);
            this.dataHandler.setup();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        this.userManager = new ShopUserManager(this);
        this.userManager.setup();

        return true;
    }

    @Override
    public void loadConfig() {
        this.getConfig().initializeOptions(Config.class);
    }

    @Override
    public void loadLang() {
        this.getLangManager().loadMissing(Lang.class);
        this.getLangManager().setupEnum(TradeType.class);
        this.getLangManager().setupEnum(PriceType.class);
        this.getLang().saveChanges();
    }

    @Override
    public void registerCommands(@NotNull GeneralCommand<ExcellentShop> mainCommand) {
        mainCommand.addChildren(new CurrencyMainCommand(this));
        mainCommand.addChildren(new ReloadSubCommand<>(this, Perms.COMMAND_RELOAD));
    }

    @Override
    public void registerPermissions() {
        this.registerPermissions(Perms.class);
    }

    @Override
    public void registerHooks() {

    }

    @Override
    public @NotNull ShopDataHandler getData() {
        return this.dataHandler;
    }

    @Override
    public @NotNull ShopUserManager getUserManager() {
        return userManager;
    }

    public @NotNull CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public @NotNull ShopCartMenu getCartMenu() {
        return cartMenu;
    }

    public @NotNull ModuleManager getModuleCache() {
        return this.moduleManager;
    }

    public @Nullable VirtualShopModule getVirtualShop() {
        return this.moduleManager.getVirtualShop();
    }

    public @Nullable ChestShopModule getChestShop() {
        return this.moduleManager.getChestShop();
    }

    public @Nullable AuctionManager getAuctionManager() {
        return this.moduleManager.getAuctionManager();
    }
}
