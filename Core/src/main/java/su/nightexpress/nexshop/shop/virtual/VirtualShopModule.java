package su.nightexpress.nexshop.shop.virtual;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.utils.FileUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.currency.ICurrency;
import su.nightexpress.nexshop.api.shop.ItemProduct;
import su.nightexpress.nexshop.api.shop.Product;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.data.price.ProductPriceManager;
import su.nightexpress.nexshop.data.stock.ProductStockManager;
import su.nightexpress.nexshop.module.ModuleId;
import su.nightexpress.nexshop.module.ShopModule;
import su.nightexpress.nexshop.shop.virtual.command.SellMenuCommand;
import su.nightexpress.nexshop.shop.virtual.command.ShopCommand;
import su.nightexpress.nexshop.shop.virtual.command.child.EditorCommand;
import su.nightexpress.nexshop.shop.virtual.command.child.MenuCommand;
import su.nightexpress.nexshop.shop.virtual.command.child.OpenCommand;
import su.nightexpress.nexshop.shop.virtual.config.VirtualConfig;
import su.nightexpress.nexshop.shop.virtual.config.VirtualLang;
import su.nightexpress.nexshop.shop.virtual.config.VirtualPerms;
import su.nightexpress.nexshop.shop.virtual.editor.VirtualLocales;
import su.nightexpress.nexshop.shop.virtual.editor.menu.ShopListEditor;
import su.nightexpress.nexshop.shop.virtual.impl.product.VirtualProduct;
import su.nightexpress.nexshop.shop.virtual.impl.shop.VirtualShop;
import su.nightexpress.nexshop.shop.virtual.impl.shop.VirtualShopBank;
import su.nightexpress.nexshop.shop.virtual.menu.ShopMainMenu;
import su.nightexpress.nexshop.shop.virtual.menu.ShopSellMenu;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class VirtualShopModule extends ShopModule {

    public static final String DIR_SHOPS = "/shops/";

    public static ICurrency defaultCurrency;

    private final Map<String, VirtualShop> shops;

    private ShopMainMenu mainMenu;
    private ShopSellMenu sellMenu;
    private ShopListEditor editor;

    public VirtualShopModule(@NotNull ExcellentShop plugin) {
        super(plugin, ModuleId.VIRTUAL_SHOP);
        this.shops = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        this.plugin.getLangManager().loadMissing(VirtualLang.class);
        this.plugin.getLangManager().loadEditor(VirtualLocales.class);
        this.plugin.getLang().saveChanges();
        this.plugin.registerPermissions(VirtualPerms.class);

        File dir = new File(this.getFullPath() + "shops");
        if (!dir.exists()) {
            for (String id : new String[]{"blocks", "brewing", "farmers_market", "fish_market", "food", "hostile_loot", "peaceful_loot", "tools", "weapons", "wool"}) {
                this.plugin.getConfigManager().extractResources(this.getPath() + "shops/" + id);
            }
        }

        this.cfg.initializeOptions(VirtualConfig.class);
        if ((defaultCurrency = plugin.getCurrencyManager().getCurrency(VirtualConfig.DEFAULT_CURRENCY.get())) == null) {
            this.interruptLoad("Invalid default currency!");
            return;
        }

        this.loadShops();
        this.loadMainMenu();

        this.moduleCommand.addChildren(new OpenCommand(this));
        this.moduleCommand.addChildren(new EditorCommand(this));

        if (VirtualConfig.MAIN_MENU_ENABLED.get()) {
            this.moduleCommand.addChildren(new MenuCommand(this));
        }
        if (VirtualConfig.SELL_MENU_ENABLED.get()) {
            this.sellMenu = new ShopSellMenu(this, JYML.loadOrExtract(plugin, this.getPath() + "sell.menu.yml"));
            this.plugin.getCommandManager().registerCommand(new SellMenuCommand(this, VirtualConfig.SELL_MENU_COMMANDS.get().split(",")));
        }
        if (!VirtualConfig.SHOP_SHORTCUTS.get().isEmpty()) {
            this.plugin.getCommandManager().registerCommand(new ShopCommand(this));
        }
    }

    @Override
    protected void onShutdown() {
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
        if (this.mainMenu != null) {
            this.mainMenu.clear();
            this.mainMenu = null;
        }
        if (this.sellMenu != null) {
            this.sellMenu.clear();
            this.sellMenu = null;
        }
        this.getShops().forEach(VirtualShop::clear);
        this.getShopsMap().clear();
        super.onShutdown();
    }

    private void loadShops() {
        for (File folder : FileUtil.getFolders(this.getFullPath() + "shops")) {
            String id = folder.getName();

            //<editor-fold desc="OLD DATA">
            File fileOld = new File(folder.getAbsolutePath(), folder.getName() + ".yml");
            File fileNew = new File(folder.getAbsolutePath(), "config.yml");
            if (fileOld.exists() && !fileNew.exists()) {
                if (!fileOld.renameTo(fileNew)) {
                    this.error("Unable to rename shop config file: " + fileOld.getName());
                    continue;
                }
            }
            //</editor-fold>

            VirtualShop shop = new VirtualShop(this, new JYML(fileNew), id);
            if (shop.load()) {
                this.getShopsMap().put(shop.getId(), shop);
                ProductStockManager.loadData(shop).thenRun(() -> shop.getProducts().forEach(product -> product.getStock().unlock()));
                ProductPriceManager.loadData(shop).thenRun(() -> shop.getProducts().forEach(product -> product.getPricer().update()));
            } else {
                this.error("Shop not loaded: " + shop.getFile().getName());
            }
        }
        this.info("Shops Loaded: " + this.getShopsMap().size());
    }

    private void loadMainMenu() {
        if (this.mainMenu != null) {
            this.mainMenu.clear();
            this.mainMenu = null;
        }

        if (!VirtualConfig.MAIN_MENU_ENABLED.get()) return;
        this.mainMenu = new ShopMainMenu(this);
    }

    public @NotNull ShopListEditor getEditor() {
        if (this.editor == null) {
            this.editor = new ShopListEditor(this);
        }
        return editor;
    }

    public boolean createShop(@NotNull String id) {
        if (this.getShopById(id) != null) {
            return false;
        }
        JYML cfg = new JYML(this.getFullPath() + DIR_SHOPS + id, "config.yml");
        VirtualShop shop = new VirtualShop(this, cfg, id);
        shop.setName("<yellow><b>" + StringUtil.capitalizeFully(id.replace("_", " ")));
        shop.setDescription(List.of("<gray>Edit me in <green>/shop editor"));
        shop.setIcon(new ItemStack(Material.CHEST_MINECART));
        shop.setBank(new VirtualShopBank(shop));
        shop.save();
        shop.load();
        this.getShopsMap().put(shop.getId(), shop);
        return true;
    }

    public boolean isAvailable(@NotNull Player player, boolean notify) {
        if (!player.hasPermission(VirtualPerms.BYPASS_WORLDS)) {
            if (VirtualConfig.DISABLED_WORLDS.get().contains(player.getWorld().getName())) {
                if (notify) plugin.getMessage(VirtualLang.SHOP_ERROR_BAD_WORLD).send(player);
                return false;
            }
        }

        if (!player.hasPermission(VirtualPerms.BYPASS_GAMEMODE)) {
            if (VirtualConfig.DISABLED_GAMEMODES.get().contains(player.getGameMode().name())) {
                if (notify) plugin.getMessage(VirtualLang.SHOP_ERROR_BAD_GAMEMODE)
                    .replace(Placeholders.GENERIC_TYPE, plugin.getLangManager().getEnum(player.getGameMode()))
                    .send(player);
                return false;
            }
        }

        return true;
    }

    public @NotNull ShopSellMenu getSellMenu() {
        return sellMenu;
    }

    public @Nullable ShopMainMenu getMainMenu() {
        return mainMenu;
    }

    public boolean delete(@NotNull VirtualShop shop) {
        if (FileUtil.deleteRecursive(this.getFullPath() + DIR_SHOPS + shop.getId())) {
            shop.clear();
            this.getShopsMap().remove(shop.getId());
            this.loadMainMenu();
            return true;
        }
        return false;
    }

    public @NotNull Map<String, VirtualShop> getShopsMap() {
        return this.shops;
    }

    public @NotNull Collection<VirtualShop> getShops() {
        return this.getShopsMap().values();
    }

    public @NotNull List<VirtualShop> getShops(@NotNull Player player) {
        return this.getShops().stream().filter(shop -> shop.canAccess(player, false)).toList();
    }

    public @Nullable VirtualShop getShopById(@NotNull String id) {
        return this.getShopsMap().get(id.toLowerCase());
    }

    /**
     * Gets the best product by given item from given shop.
     * <p>
     * <p>If given trade type is SELL, then it will search for the highest price.
     * <p>If given trade type is BUY, then it will search for the lowest price.
     *
     * @param player    the player who is trading the product
     * @param item      the item to match with
     * @param tradeType the trade type
     * @param shop      the shop from which the product is searched
     *
     * @return a product if there is a match, otherwise null
     */
    public @Nullable VirtualProduct getProductByItem(@NotNull Player player, @NotNull ItemStack item, @NotNull TradeType tradeType, @NotNull VirtualShop shop) {
        if (!shop.canAccess(player, false) || !shop.isTransactionEnabled(tradeType))
            return null;

        Set<VirtualProduct> products = shop.getProducts().stream()
            .filter(product -> {
                if (product instanceof ItemProduct itemProduct && !itemProduct.isItemMatches(item)) return false;
                if (tradeType == TradeType.BUY && !product.isBuyable()) return false;
                if (tradeType == TradeType.SELL && !product.isSellable()) return false;
                return product.getStock().getPossibleAmount(tradeType, player) != 0;
            })
            .collect(Collectors.toSet());

        Comparator<Product<?, ?, ?>> comp = (p1, p2) -> (int) (p1.getPricer().getPrice(tradeType) - p2.getPricer().getPrice(tradeType));

        // If the trade type is BUY, we find the product with the LOWEST price
        // If the trade type is SELL, we find the product with the HIGHEST price
        return (tradeType == TradeType.BUY ? products.stream().min(comp) : products.stream().max(comp)).orElse(null);
    }
}
