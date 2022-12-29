package su.nightexpress.nexshop.shop.virtual.config;

import su.nexmedia.engine.api.lang.LangKey;
import su.nightexpress.nexshop.Placeholders;

public class VirtualLang {

    public static final LangKey COMMAND_OPEN_DESC  = LangKey.of("VirtualShop.Command.Open.Desc", "Opens specified shop.");
    public static final LangKey COMMAND_OPEN_USAGE = LangKey.of("VirtualShop.Command.Open.Usage", "[shop] [player]");

    public static final LangKey MAIN_MENU_ERROR_DISABLED = LangKey.of("VirtualShop.MainMenu.Error.Disabled", "<red>Main shop menu is disabled!");
    public static final LangKey OPEN_ERROR_BAD_WORLD     = LangKey.of("VirtualShop.Open.Error.BadWorld", "<red>Shop is disabled in this world!");
    public static final LangKey OPEN_ERROR_BAD_GAMEMODE  = LangKey.of("VirtualShop.Open.Error.BadGamemode", "<red>You can't use shop while in <yellow>" + Placeholders.GENERIC_TYPE + "</yellow> gamemode!");
    public static final LangKey OPEN_ERROR_INVALID_SHOP  = LangKey.of("VirtualShop.Open.Error.InvalidShop", "<red>No such shop!");
    public static final LangKey PRODUCT_PURCHASE_SELL    = LangKey.of("VirtualShop.Product.Purchase.Sell", "{message: ~type: TITLES; ~fadeIn: 10; ~stay: 80; ~fadeOut: 10;}<green><b>Successful! \n <gray>You sold <green>x%amount% %item%</green> for <green>%price%</green>!");
    public static final LangKey PRODUCT_PURCHASE_BUY     = LangKey.of("VirtualShop.Product.Purchase.Buy", "{message: ~type: TITLES; ~fadeIn: 10; ~stay: 80; ~fadeOut: 10;}<green><b>Successful Purchase! \n <gray>You bought <green>x%amount% %item%</green> for <green>%price%</green>!");

    public static final LangKey EDITOR_ENTER_ID                = LangKey.of("VirtualShop.Editor.Enter.Id", "<gray>Enter shop id...");
    public static final LangKey EDITOR_ENTER_NAME              = LangKey.of("VirtualShop.Editor.Enter.Name", "<gray>Enter shop <green>name</green>...");
    public static final LangKey EDITOR_ENTER_DESCRIPTION       = LangKey.of("VirtualShop.Editor.Enter.Description", "<gray>Enter <green>description</green>...");
    public static final LangKey EDITOR_ENTER_NPC_ID            = LangKey.of("VirtualShop.Editor.Enter.NpcId", "<gray>Enter Citizens id...");
    public static final LangKey EDITOR_ENTER_TITLE             = LangKey.of("VirtualShop.Editor.Enter.Title", "<gray>Enter shop title...");
    public static final LangKey EDITOR_ENTER_AMOUNT            = LangKey.of("VirtualShop.Editor.Enter.Amount", "<gray>Enter new amount...");
    public static final LangKey EDITOR_SHOP_CREATE_ERROR_EXIST = LangKey.of("VirtualShop.Editor.Create.Error.Exist", "<red>Shop with such ID already exist!");
    public static final LangKey EDITOR_PRODUCT_ENTER_COMMAND   = LangKey.of("VirtualShop.Editor.Product.Enter.Command", "<gray>Enter new command...");
}
