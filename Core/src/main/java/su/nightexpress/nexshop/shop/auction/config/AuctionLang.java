package su.nightexpress.nexshop.shop.auction.config;

import su.nexmedia.engine.api.lang.LangKey;

public class AuctionLang {

    public static final LangKey COMMAND_OPEN_DESC       = LangKey.of("Auction.Command.Open.Desc", "Open auction.");
    public static final LangKey COMMAND_SELL_DESC       = LangKey.of("Auction.Command.Sell.Desc", "Add item on auction.");
    public static final LangKey COMMAND_SELL_USAGE      = LangKey.of("Auction.Command.Sell.Usage", "<price>");
    public static final LangKey COMMAND_EXPIRED_DESC    = LangKey.of("Auction.Command.Expired.Desc", "List of expired listings.");
    public static final LangKey COMMAND_EXPIRED_USAGE   = LangKey.of("Auction.Command.Expired.Usage", "[player]");
    public static final LangKey COMMAND_HISTORY_DESC    = LangKey.of("Auction.Command.History.Desc", "Your sales history.");
    public static final LangKey COMMAND_HISTORY_USAGE   = LangKey.of("Auction.Command.History.Usage", "[player]");
    public static final LangKey COMMAND_SELLING_DESC    = LangKey.of("Auction.Command.Selling.Desc", "List of your current listings.");
    public static final LangKey COMMAND_SELLING_USAGE   = LangKey.of("Auction.Command.Selling.Usage", "[player]");
    public static final LangKey COMMAND_UNCLAIMED_DESC  = LangKey.of("Auction.Command.Unclaimed.Desc", "List of unclaimed rewards for your listings.");
    public static final LangKey COMMAND_UNCLAIMED_USAGE = LangKey.of("Auction.Command.Unclaimed.Usage", "[player]");

    public static final LangKey LISTING_ADD_SUCCESS_INFO             = LangKey.of("Auction.Listing.Add.Success.Info", "<gray>You added <green>x%listing_item_amount% %listing_item_name%</green> on auction for <green>%listing_price%</green>. Tax amount: <red>%tax%</red>.");
    public static final LangKey LISTING_ADD_SUCCESS_ANNOUNCE         = LangKey.of("Auction.Listing.Add.Success.Announce", "<gray><green>%player_display_name%</green> just put <green>x%listing_item_amount% %listing_item_name%</green> on auction for <yellow>%listing_price%</yellow>!");
    public static final LangKey LISTING_ADD_ERROR_BAD_ITEM           = LangKey.of("Auction.Listing.Add.Error.BadItem", "{message: ~sound: ENTITY_VILLAGER_NO;}<red><yellow>%item%</yellow> can not be added on auction!");
    public static final LangKey LISTING_ADD_ERROR_LIMIT              = LangKey.of("Auction.Listing.Add.Error.Limit", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>You can not add more than <yellow>%amount%</yellow> active listings on auction!");
    public static final LangKey LISTING_ADD_ERROR_PRICE_TAX          = LangKey.of("Auction.Listing.Add.Error.Price.Tax", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>You can't afford the <yellow>%tax%%</yellow> price tax: <yellow>%amount%</yellow>!");
    public static final LangKey LISTING_ADD_ERROR_PRICE_CURRENCY_MIN = LangKey.of("Auction.Listing.Add.Error.Price.Currency.Min", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>Listing price for <yellow>%currency_name% currency</yellow> can not be smaller than <yellow>%amount%</yellow>!");
    public static final LangKey LISTING_ADD_ERROR_PRICE_CURRENCY_MAX = LangKey.of("Auction.Listing.Add.Error.Price.Currency.Max", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>Listing price for <yellow>%currency_name% currency</yellow> can not be greater than <yellow>%amount%</yellow>!");
    public static final LangKey LISTING_ADD_ERROR_PRICE_NEGATIVE     = LangKey.of("Auction.Listing.Add.Error.Price.Negative", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>Listing price can not be negative!");
    public static final LangKey LISTING_ADD_ERROR_PRICE_MATERIAL_MIN = LangKey.of("Auction.Listing.Add.Error.Price.Material.Min", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>Listing price for <yellow>x1 %item%</yellow> can not be smaller than <yellow>%amount%</yellow>!");
    public static final LangKey LISTING_ADD_ERROR_PRICE_MATERIAL_MAX = LangKey.of("Auction.Listing.Add.Error.Price.Material.Max", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>Listing price for <yellow>x1 %item%</yellow> can not be greater than <yellow>%amount%</yellow>!");
    public static final LangKey LISTING_ADD_ERROR_DISABLED_GAMEMODE  = LangKey.of("Auction.Listing.Add.Error.DisabledGamemode", "{message: ~sound: ENTITY_VILLAGER_NO;}<red>You can't add items in this game mode!");
    public static final LangKey LISTING_BUY_SUCCESS_INFO             = LangKey.of("Auction.Listing.Buy.Success.Info", "{message: ~prefix: false; ~type: TITLES; ~fadeIn: 20; ~stay: 50; ~fadeOut: 20; ~sound: ENTITY_PLAYER_LEVELUP;}<green><b>Successful Purchase!\n<gray>You bought <green>x%listing_item_amount% %listing_item_name%</green> from <green>%listing_seller%</green> for <green>%listing_price%</green>!");
    public static final LangKey LISTING_BUY_ERROR_NOT_ENOUGH_FUNDS   = LangKey.of("Auction.Listing.Buy.Error.NotEnoughFunds", "{message: ~prefix: false; ~type: TITLES; ~fadeIn: 20; ~stay: 50; ~fadeOut: 20; ~sound: BLOCK_ANVIL_PLACE;}<red><b>Not Enough Funds!\n<gray>Balance: <red>%balance%</red> <dark_gray>|</dark_gray> Needed: <red>%listing_price%</red>");
    public static final LangKey NOTIFY_LISTING_UNCLAIMED             = LangKey.of("Auction.Notify.Listing.Unclaimed", """
        {message: ~prefix: false;}
        <dark_gray><st>-------------</st><b>[ <yellow>Auction Notification</yellow> ]</b><st>-------------</st>
        <gray>     You have <yellow>%amount% unclaimed rewards</yellow> for your listings!
        <gray>                 <click:run_command:'/ah unclaimed'><hover:show_text:'<gray>Click to claim rewards!'><green><b>Click to Claim Now!</b></green></hover></click>
        <dark_gray><st>-----------------------------------------</st>""");
    public static final LangKey NOTIFY_LISTING_EXPIRED               = LangKey.of("Auction.Notify.Listing.Expired", """
        {message: ~prefix: false;}
        <dark_gray><st>-------------</st><b>[ <yellow>Auction Notification</yellow> ]</b><st>-------------</st>
        <gray>     You have <yellow>%amount% expired</yellow> listings!
        <gray>           <click:run_command:'/ah expired'><hover:show_text:'<gray>Click to claim rewards!'><green><b>Click to Take Now!</b></green></hover></click>
        <dark_gray><st>-----------------------------------------""");
    public static final LangKey NOTIFY_LISTING_CLAIM                 = LangKey.of("Auction.Notify.Listing.Claim", "<gray>You claimed <green>%listing_price%</green> for <green>%listing_item_name%</green>!");
    public static final LangKey ERROR_DISABLED_WORLD                 = LangKey.of("Auction.Error.DisabledWorld", "<red>Auction is disabled in this world!");
}
