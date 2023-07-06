package su.nightexpress.nexshop.shop.virtual.config;

import org.bukkit.GameMode;
import su.nexmedia.engine.api.config.JOption;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.type.StockType;
import su.nightexpress.nexshop.api.type.TradeType;

import java.util.*;

public class VirtualConfig {

    /*public static final JOption<String>      DEFAULT_CURRENCY       = JOption.create("General.Default_Currency", CurrencyManager.VAULT,
        "Sets default currency for the Virtual Shop module.",
        "This currency will be used when you create new products or in case, where other currencies are not available.",
        "Compatible plugins: https://github.com/nulli0n/ExcellentShop-spigot/wiki/Shop-Currency");*/

    public static final JOption<Boolean> MAIN_MENU_ENABLED = JOption.create("General.Main_Menu_Enabled", true,
        "When 'true', enables the Main Menu, where you can list all of your Virtual Shops.");

    public static final JOption<String> SHOP_SHORTCUTS = JOption.create("General.Shop_Shortcuts", "shop",
        "A list of command aliases for quick access to main menu and shops.", "Split them with a comma.");

    public static final JOption<Boolean> SELL_MENU_ENABLED = JOption.create("General.Sell_Menu.Enabled", true,
        "When 'true' enables the Sell Menu, where you can quickly sell all your items.");

    public static final JOption<String> SELL_MENU_COMMANDS = JOption.create("General.Sell_Menu.Commands", "sellgui",
        "Custom command aliases to open the Sell Menu. Split them with a comma.");

    public static final JOption<Set<String>> DISABLED_GAMEMODES = JOption.create("General.Disabled_In_Gamemodes",
        Set.of(GameMode.CREATIVE.name()),
        "A list of Game Modes, where players can not access shops.",
        "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/GameMode.html");

    public static final JOption<Set<String>> DISABLED_WORLDS = JOption.create("General.Disabled_In_Worlds",
        Set.of("world_name", "example_world123"),
        "A list of worlds, where players can not access shops.");

    public static final JOption<String> SHOP_FORMAT_NAME = JOption.create("GUI.Shop_Format.Name", Placeholders.SHOP_NAME,
        "Sets display name for the shop item in the Main Menu.",
        "You can use 'Shop' placeholders here:" + Placeholders.URL_WIKI_PLACEHOLDERS
    ); // Mewcraft

    public static final JOption<List<String>> SHOP_FORMAT_LORE = JOption.create("GUI.Shop_Format.Lore",
        Arrays.asList(
            Placeholders.SHOP_VIRTUAL_DESCRIPTION,
            "",
            "<#ff9a9a>[!] <#d4d9d8>Need Permission:</#d4d9d8> " + Placeholders.SHOP_VIRTUAL_PERMISSION_REQUIRED
        ),
        "Sets lore for the shop item in the Main Menu.",
        "You can use 'Shop' placeholders here: " + Placeholders.URL_WIKI_PLACEHOLDERS
    ); // Mewcraft

    public static final JOption<List<String>> PRODUCT_FORMAT_LORE_GENERAL_ALL = JOption.create("GUI.Product_Format.Lore.General.All",
        Arrays.asList(
            Placeholders.GENERIC_LORE,
            "",
            Placeholders.GENERIC_DISCOUNT,
            "",
            "<yellow>Buy: <gold>" + Placeholders.PRODUCT_PRICE_BUY_FORMATTED + "</gold> <dark_gray>|</dark_gray> Sell: <gold>" + Placeholders.PRODUCT_PRICE_SELL_FORMATTED,
            "",
            "%stock_global_buy%",
            "%stock_global_sell%",
            "%stock_player_buy%",
            "%stock_player_sell%",
            "",
            "<dark_gray><red>Left-Click</red> → <white>Select Quantity</white> ← <red>Right-Click</red>",
            "<dark_gray><red>Shift-Left</red> → <white>Buy</white> <gray>(Quick)</gray> <white>Sell</white> ← <red>Shift-Right</red>",
            "<dark_gray><red>[F] Key</red> → <white>Sell All</white> <gray>(" + Placeholders.PRODUCT_PRICE_SELL_ALL_FORMATTED + ")</gray>"),
        "Sets lore for the product preview item in Virtual Shop GUI.",
        "This lore will be used when both Buy and Sell prices are available.",
        "Local Placeholders:",
        "- %lore% - Original lore of the product preview item.",
        "- %discount% - Discount info (if present)",
        "- %stock_global_buy% - Global stock info for purchase (if present)",
        "- %stock_global_sell% - Global stock info for sale (if present)",
        "- %stock_player_buy% - Player limit info for purchase (if present)",
        "- %stock_player_sell% - Player limit info for sale (if present).",
        "You can use 'Product' placeholders here: " + Placeholders.URL_WIKI_PLACEHOLDERS
    ); // Mewcraft

    public static final JOption<List<String>> PRODUCT_FORMAT_LORE_GENERAL_BUY_ONLY = JOption.create("GUI.Product_Format.Lore.General.Buy_Only",
        Arrays.asList(
            Placeholders.GENERIC_LORE,
            "",
            Placeholders.GENERIC_DISCOUNT,
            "",
            "<yellow>Buy: <gold>" + Placeholders.PRODUCT_PRICE_BUY_FORMATTED,
            "",
            "%stock_global_buy%",
            "%stock_player_buy%",
            "",
            "<dark_gray><red>Left-Click</red> → <white>Select Quantity</white>",
            "<dark_gray><red>Shift-Left</red> → <white>Quick Buy</white>"
        ),
        "Sets lore for the product preview item in Virtual Shop GUI.",
        "This lore will be used when only Buy price is available.",
        "Local Placeholders:",
        "- %lore% - Original lore of the product preview item.",
        "- %discount% - Discount info (if present)",
        "- %stock_global_buy% - Global stock info for purchase (if present)",
        "- %stock_player_buy% - Player limit info for purchase (if present).",
        "You can use 'Product' placeholders here: " + Placeholders.URL_WIKI_PLACEHOLDERS
    ); // Mewcraft

    public static final JOption<List<String>> PRODUCT_FORMAT_LORE_GENERAL_SELL_ONLY = JOption.create("GUI.Product_Format.Lore.General.Sell_Only",
        Arrays.asList(
            Placeholders.GENERIC_LORE,
            "",
            "%discount%",
            "",
            "<yellow>Sell: <gold>" + Placeholders.PRODUCT_PRICE_SELL_FORMATTED,
            "",
            "%stock_global_sell%",
            "%stock_player_sell%",
            "",
            "<dark_gray><red>Left-Click</red> → <white>Select Quantity</white>",
            "<dark_gray><red>Shift-Left → <white>Quick Sell</white>",
            "<dark_gray><red>[F] Key → <white>Sell All</white> <gray>(" + Placeholders.PRODUCT_PRICE_SELL_ALL_FORMATTED + ")</gray>"
        ),
        "Sets lore for the product preview item in Virtual Shop GUI.",
        "This lore will be used when only Sell price is available.",
        "Local Placeholders:",
        "- %lore% - Original lore of the product preview item.",
        "- %stock_global_sell% - Global stock info for sale (if present)",
        "- %stock_player_sell% - Player limit info for sale (if present).",
        "You can use 'Product' placeholders here: " + Placeholders.URL_WIKI_PLACEHOLDERS
    ); // Mewcraft

    public static final JOption<List<String>> PRODUCT_FORMAT_LORE_DISCOUNT = JOption.create("GUI.Product_Format.Lore.Discount",
        Collections.singletonList("<red><b>[!] <#C70039>SALE <yellow>" + Placeholders.PRODUCT_DISCOUNT_AMOUNT + "</yellow>% OFF</#C70039> [!]"),
        "Sets the discount display format when there is active discounts in the shop applicable to a product.",
        "You can use 'Product' placeholders here: " + Placeholders.URL_WIKI_PLACEHOLDERS
    ); // Mewcraft

    public static final JOption<Map<StockType, Map<TradeType, List<String>>>> PRODUCT_FORMAT_LORE_STOCK = new JOption<Map<StockType, Map<TradeType, List<String>>>>("GUI.Product_Format.Lore.Stock",
        (cfg, path, def) -> {
            Map<StockType, Map<TradeType, List<String>>> map = new HashMap<>();
            for (StockType stockType : StockType.values()) {
                for (TradeType tradeType : TradeType.values()) {
                    List<String> lore = cfg.getStringList(path + "." + stockType.name() + "." + tradeType.name());
                    map.computeIfAbsent(stockType, k -> new HashMap<>()).put(tradeType, lore);
                }
            }
            return map;
        },
        () -> {
            Map<StockType, Map<TradeType, List<String>>> map = new HashMap<>();
            map.computeIfAbsent(StockType.GLOBAL, k -> new HashMap<>()).put(TradeType.BUY, Collections.singletonList("<#95fafa>Buy Stock <dark_gray>→</dark_gray> <#84dbdb>" + Placeholders.PRODUCT_STOCK_GLOBAL_BUY_AMOUNT_LEFT + "<gray>/</gray>" + Placeholders.PRODUCT_STOCK_GLOBAL_BUY_AMOUNT_INITIAL + "</#84dbdb> <gray>(⟳ <white>" + Placeholders.PRODUCT_STOCK_GLOBAL_BUY_RESTOCK_DATE + "</white>)"));
            map.computeIfAbsent(StockType.GLOBAL, k -> new HashMap<>()).put(TradeType.SELL, Collections.singletonList("<#95fafa>Sell Stock <dark_gray>→</dark_gray> <#84dbdb>" + Placeholders.PRODUCT_STOCK_GLOBAL_SELL_AMOUNT_LEFT + "<gray>/</gray>" + Placeholders.PRODUCT_STOCK_GLOBAL_SELL_AMOUNT_INITIAL + "</#84dbdb> <gray>(⟳ <white>" + Placeholders.PRODUCT_STOCK_GLOBAL_SELL_RESTOCK_DATE + "</white>)"));
            map.computeIfAbsent(StockType.PLAYER, k -> new HashMap<>()).put(TradeType.BUY, Collections.singletonList("<#ff7777>Buy Limit <dark_gray>→</dark_gray> <#e16060>" + Placeholders.PRODUCT_STOCK_PLAYER_BUY_AMOUNT_LEFT + "<gray>/</gray>" + Placeholders.PRODUCT_STOCK_PLAYER_BUY_AMOUNT_INITIAL + "</#e16060> <gray>(⟳ <white>" + Placeholders.PRODUCT_STOCK_PLAYER_BUY_RESTOCK_DATE + "</white>)"));
            map.computeIfAbsent(StockType.PLAYER, k -> new HashMap<>()).put(TradeType.SELL, Collections.singletonList("<#ff7777>Sell Limit <dark_gray>→</dark_gray> <#e16060>" + Placeholders.PRODUCT_STOCK_PLAYER_SELL_AMOUNT_LEFT + "<gray>/</gray>" + Placeholders.PRODUCT_STOCK_PLAYER_SELL_AMOUNT_INITIAL + "</#e16060> <gray>(⟳ <white>" + Placeholders.PRODUCT_STOCK_PLAYER_SELL_RESTOCK_DATE + "</white>)"));
            return map;
        },
        "Sets the stock display format for each Stock and Trade types.",
        "If product stock settings is undefined, format will be skipped.",
        "You can use 'Product' placeholders here: " + Placeholders.URL_WIKI_PLACEHOLDERS
    ).setWriter((cfg, path, map) -> {
        map.forEach((stockType, map1) -> {
            map1.forEach(((tradeType, lore) -> {
                cfg.set(path + "." + stockType.name() + "." + tradeType.name(), lore);
            }));
        });
    });
}
