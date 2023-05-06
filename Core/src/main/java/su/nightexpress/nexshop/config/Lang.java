package su.nightexpress.nexshop.config;

import org.bukkit.Sound;
import su.nexmedia.engine.api.lang.LangKey;
import su.nexmedia.engine.lang.EngineLang;
import su.nightexpress.nexshop.Placeholders;


public class Lang extends EngineLang {

    public static final LangKey COMMAND_CURRENCY_DESC = LangKey.of("Command.Currency.Desc", "Manage plugin currencies.");
    public static final LangKey COMMAND_CURRENCY_USAGE = LangKey.of("Command.Currency.Usage", "[help]");

    public static final LangKey COMMAND_CURRENCY_GIVE_DESC = LangKey.of("Command.Currency.Give.Desc", "Give specified currency to a player.");
    public static final LangKey COMMAND_CURRENCY_GIVE_USAGE = LangKey.of("Command.Currency.Give.Usage", "<currency> <player> <amount>");
    public static final LangKey COMMAND_CURRENCY_GIVE_DONE = LangKey.of("Command.Currency.Give.Done", "Given <green>x%amount% %currency_name%</green> to <green>%player_name%</green>.");

    public static final LangKey COMMAND_CURRENCY_TAKE_DESC = LangKey.of("Command.Currency.Take.Desc", "Take specified currency from a player.");
    public static final LangKey COMMAND_CURRENCY_TAKE_USAGE = LangKey.of("Command.Currency.Take.Usage", "<currency> <player> <amount>");
    public static final LangKey COMMAND_CURRENCY_TAKE_DONE = LangKey.of("Command.Currency.Take.Done", "Took <green>x%amount% %currency_name%</green> from <green>%player_name%</green>.");

    public static final LangKey COMMAND_CURRENCY_CREATE_DESC = LangKey.of("Command.Currency.Create.Desc", "Create/replace a currency from/with the item in hand.");
    public static final LangKey COMMAND_CURRENCY_CREATE_USAGE = LangKey.of("Command.Currency.Create.Usage", "<name>");
    public static final LangKey COMMAND_CURRENCY_CREATE_DONE_NEW = LangKey.of("Command.Currency.Create.Done.New", "Created a new currency <green>%currency_id%</green> as <green>%item%<green>.");
    public static final LangKey COMMAND_CURRENCY_CREATE_DONE_REPLACE = LangKey.of("Command.Currency.Create.Done.Replace", "Replaced item in the currency <green>%currency_id%</green> with <green>%item%</green>.");
    public static final LangKey COMMAND_CURRENCY_CREATE_ERROR_EXIST = LangKey.of("Command.Currency.Create.Error.Exist", "Currency <red>%currency_id%</red> is already exist and is not an Item Currency.");

    public static final LangKey MODULE_COMMAND_RELOAD_DESC = LangKey.of("Module.Command.Reload.Desc", "Reload the module.");
    public static final LangKey MODULE_COMMAND_RELOAD_DONE = LangKey.of("Module.Command.Reload.Done", "Module <green>%module%</green> reloaded!");

    public static final LangKey ERROR_CURRENCY_INVALID = LangKey.of("Error.Currency.Invalid", "<red>Invalid currency!");

    public static final LangKey EDITOR_GENERIC_ENTER_NAME = LangKey.of("Editor.Generic.Enter.Name", "<gray>Enter <green>[Name]");
    public static final LangKey EDITOR_GENERIC_ENTER_AMOUNT = LangKey.of("Editor.Generic.Enter.Amount", "<gray>Enter <green>[Amount]");
    public static final LangKey EDITOR_GENERIC_ENTER_DAY = LangKey.of("Editor.Generic.Enter.Day", "<gray>Enter <green>Day</green> in English");
    public static final LangKey EDITOR_GENERIC_ENTER_TIME = LangKey.of("Editor.Generic.Enter.Time", "<gray>Enter <green>Time</green> like 18:00:00");
    public static final LangKey EDITOR_GENERIC_ENTER_SECONDS = LangKey.of("Editor.Generic.Enter.Seconds", "<gray>Enter <green>seconds</green> amount");
    public static final LangKey EDITOR_GENERIC_ERROR_CURRENCY = LangKey.of("Editor.Generic.Error.Currency", "<red>Invalid currency!");
    public static final LangKey EDITOR_PRODUCT_ENTER_PRICE = LangKey.of("Editor.Product.Enter.Price", "<gray>Enter new <green>price");
    public static final LangKey EDITOR_PRODUCT_ENTER_CURRENCY = LangKey.of("Editor.Product.Enter.Currency", "<gray>Enter <green>currency id");

    public static final LangKey SHOP_PRODUCT_ERROR_UNBUYABLE = LangKey.of("Shop.Product.Error.Unbuyable", "<red>You can not buy this item!");
    public static final LangKey SHOP_PRODUCT_ERROR_UNSELLABLE = LangKey.of("Shop.Product.Error.Unsellable", "<red>You can not sell this item!");
    public static final LangKey SHOP_PRODUCT_ERROR_OUT_OF_STOCK = LangKey.of("Shop.Product.Error.OutOfStock", "<red>This product is out of stock!");
    public static final LangKey SHOP_PRODUCT_ERROR_OUT_OF_SPACE = LangKey.of("Shop.Product.Error.OutOfSpace", "<red>This shop is out of space!");
    public static final LangKey SHOP_PRODUCT_ERROR_OUT_OF_FUNDS = LangKey.of("Shop.Product.Error.OutOfFunds", "<red>This shop is out of money!");
    public static final LangKey SHOP_PRODUCT_ERROR_FULL_STOCK = LangKey.of("Shop.Product.Error.FullStock", "<red>This product is full of stock!");
    public static final LangKey SHOP_PRODUCT_ERROR_FULL_INVENTORY = LangKey.of("Shop.Product.Error.FullInventory", "<red>You can't buy items while your inventory is full!");

    public static final LangKey SHOP_PRODUCT_ERROR_TOO_EXPENSIVE = LangKey.of("Shop.Product.Error.TooExpensive",
        "<! type:\"titles:15:60:15\" sound:\"" + Sound.BLOCK_ANVIL_PLACE.name() + "\" !>" +
        "\n<red><b>Too Expensive!" +
        "\n<gray>You need: <red>" + Placeholders.GENERIC_PRICE + "</red>!");

    public static final LangKey SHOP_PRODUCT_ERROR_NOT_ENOUGH_ITEMS = LangKey.of("Shop.Product.Error.NotEnoughItems",
        "<! type:\"titles:15:60:15\" sound:\"" + Sound.BLOCK_ANVIL_PLACE.name() + "\" !>" +
        "\n<red><b>Not Enough Items!" +
        "\n<gray>You need: <red>x" + Placeholders.GENERIC_AMOUNT + " " + Placeholders.GENERIC_ITEM + "</red>!");
}
