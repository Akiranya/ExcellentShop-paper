package su.nightexpress.nexshop.shop.virtual.config;

import org.bukkit.Sound;
import su.nexmedia.engine.api.lang.LangKey;
import su.nightexpress.nexshop.Placeholders;

public class VirtualLang {

    public static final LangKey COMMAND_OPEN_DESC = LangKey.of("VirtualShop.Command.Open.Desc", "Opens specified shop.");
    public static final LangKey COMMAND_OPEN_USAGE = LangKey.of("VirtualShop.Command.Open.Usage", "[player]");

    public static final LangKey COMMAND_MENU_DESC = LangKey.of("VirtualShop.Command.Menu.Desc", "Opens Main Menu.");
    public static final LangKey COMMAND_MENU_USAGE = LangKey.of("VirtualShop.Command.Menu.Usage", "[player]");

    public static final LangKey COMMAND_SHOP_DESC = LangKey.of("VirtualShop.Command.Shop.Desc", "Open specified shop or main menu.");
    public static final LangKey COMMAND_SHOP_USAGE = LangKey.of("VirtualShop.Command.Shop.Usage", "[shopId]");

    public static final LangKey COMMAND_SELL_MENU_DESC = LangKey.of("VirtualShop.Command.SellMenu.Desc", "Open Sell GUI.");
    public static final LangKey COMMAND_SELL_MENU_USAGE = LangKey.of("VirtualShop.Command.SellMenu.Usage", "<shop> [player]");

    public static final LangKey SHOP_ERROR_BAD_WORLD = LangKey.of("VirtualShop.Shop.Error.BadWorld", "<red>Shop is disabled in this world!");
    public static final LangKey SHOP_ERROR_BAD_GAMEMODE = LangKey.of("VirtualShop.Shop.Error.BadGamemode", "<red>You can't use shop while in <yellow>" + Placeholders.GENERIC_TYPE + "</yellow> gamemode!");
    public static final LangKey SHOP_ERROR_INVALID = LangKey.of("VirtualShop.Shop.Error.Invalid", "<red>No such shop!");

    public static final LangKey PRODUCT_PURCHASE_SELL = LangKey.of("VirtualShop.Product.Purchase.Sell",
        "<! type:\"titles:15:60:15\" sound:\"" + Sound.ENTITY_EXPERIENCE_ORB_PICKUP.name() + "\" !>" +
        "\n<green><b>Successful Sale! " +
        "\n<gray>You sold <green>x" + Placeholders.GENERIC_AMOUNT + " " + Placeholders.GENERIC_ITEM + "</green> for <green>" + Placeholders.GENERIC_PRICE + "</green>!");

    public static final LangKey PRODUCT_PURCHASE_BUY = LangKey.of("VirtualShop.Product.Purchase.Buy",
        "<! type:\"titles:15:60:15\" sound:\"" + Sound.ENTITY_EXPERIENCE_ORB_PICKUP.name() + "\" !>" +
        "\n<green><b>Successful Purchase!" +
        "\n<gray>You bought <green>x" + Placeholders.GENERIC_AMOUNT + " " + Placeholders.GENERIC_ITEM + "</green> for <green>" + Placeholders.GENERIC_PRICE + "</green>!");

    public static final LangKey SELL_MENU_SOLD = LangKey.of("VirtualShop.SellMenu.SaleResult",
        "<! prefix:\"false\" sound:\"" + Sound.ENTITY_EXPERIENCE_ORB_PICKUP.name() + "\" !>" +
        "\n<green><b>Items Sold:" +
        "\n<gray>x" + Placeholders.GENERIC_AMOUNT + " " + Placeholders.GENERIC_ITEM + ":</green> " + Placeholders.GENERIC_PRICE);

    public static final LangKey EDITOR_SHOP_CREATE_ERROR_EXIST = LangKey.of("VirtualShop.Editor.Create.Error.Exist", "<red>Shop with such ID already exist!");
    public static final LangKey EDITOR_ENTER_SHOP_ID = LangKey.of("VirtualShop.Editor.Enter.Id", "<gray>Enter <green>[Shop Identifier]</green>");
    public static final LangKey EDITOR_ENTER_DESCRIPTION = LangKey.of("VirtualShop.Editor.Enter.Description", "<gray>Enter <green>[Description]</green>");
    public static final LangKey EDITOR_ENTER_NPC_ID = LangKey.of("VirtualShop.Editor.Enter.NpcId", "<gray>Enter <green>[NPC ID]</green>");
    public static final LangKey EDITOR_ENTER_TITLE = LangKey.of("VirtualShop.Editor.Enter.Title", "<gray>Enter <green>[Title]</green>");
    public static final LangKey EDITOR_ENTER_COMMAND = LangKey.of("VirtualShop.Editor.Enter.Command", "<gray>Enter <green>[Command]</green>");
}
