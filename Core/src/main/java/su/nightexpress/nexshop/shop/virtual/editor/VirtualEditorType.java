package su.nightexpress.nexshop.shop.virtual.editor;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nightexpress.nexshop.Placeholders;

import java.util.ArrayList;
import java.util.List;

public enum VirtualEditorType implements EditorButtonType {

    SHOP_OBJECT(Material.EMERALD, Placeholders.SHOP_NAME,
        EditorButtonType.info("Pages: <white>" + Placeholders.SHOP_VIRTUAL_PAGES + "</white>"),
        EditorButtonType.click("Left-Click to <white>Edit</white>\nShift-Right to <white>Delete</white> <gray>(No Undo)</gray>")),
    SHOP_CREATE(Material.ANVIL, "Create Shop",
        EditorButtonType.info("Create a new virtual shop."),
        EditorButtonType.click("Left-Click to <white>Create</white>")),
    SHOP_CHANGE_NAME(Material.NAME_TAG, "Shop Name",
        EditorButtonType.current(Placeholders.SHOP_NAME),
        EditorButtonType.info("Sets the shop display name. This name is used in GUIs, messages, etc."),
        EditorButtonType.click("Left-Click to <white>Edit</white>")),
    SHOP_CHANGE_DESCRIPTION(Material.MAP, "Shop Description",
        EditorButtonType.current(Placeholders.SHOP_VIRTUAL_DESCRIPTION),
        EditorButtonType.info("Sets the shop description. It's used mainly in GUIs."),
        EditorButtonType.click("Left-Click to <white>Add Line</white>\nRight-Click to <white>Clear</white>")),
    SHOP_CHANGE_TITLE,
    SHOP_CHANGE_PAGES(Material.ARROW, "Pages Amount",
        EditorButtonType.current(Placeholders.SHOP_VIRTUAL_PAGES),
        EditorButtonType.info("Sets how many pages the shop will have."),
        EditorButtonType.note("Don't forget to edit product's pages when decreasing this value."),
        EditorButtonType.click("Left-Click to <white>Plus Page</white>\nRight-Click to <white>Minus Page</white>")),
    SHOP_CHANGE_ICON(Material.ITEM_FRAME, "Shop Icon",
        EditorButtonType.current(Placeholders.SHOP_VIRTUAL_ICON_TYPE),
        EditorButtonType.info("Sets an icon for the shop. This icon is displayed in GUIs."),
        EditorButtonType.note("You don't have to set name and lore for it. Instead, use shop Name and Description options."),
        EditorButtonType.click("Drag & Drop to <white>Replace</white>")),
    SHOP_CHANGE_DISCOUNTS(Material.GOLD_NUGGET, "Discounts",
        EditorButtonType.info("Create and manage shop discounts here!"),
        EditorButtonType.click("Left-Click to <white>Navigate</white>")),
    SHOP_CHANGE_PRODUCTS(Material.CHEST, "Products",
        EditorButtonType.info("Create and manage shop products here!"),
        EditorButtonType.click("Left-Click to <white>Navigate</white>")),
    SHOP_CHANGE_PERMISSION(Material.REDSTONE, "Permission Requirement",
        EditorButtonType.current("Enabled: <white>" + Placeholders.SHOP_VIRTUAL_PERMISSION_REQUIRED + "</white>\nNode: <white>" + Placeholders.SHOP_VIRTUAL_PERMISSION_NODE + "</white>"),
        EditorButtonType.info("Sets if players must have permission to be able to use this shop."),
        EditorButtonType.click("Left-Click to <white>Toggle</white>")),
    SHOP_CHANGE_TRANSACTIONS(Material.REPEATER, "Enabled Transactions",
        EditorButtonType.current("Buying Enabled: <white>" + Placeholders.SHOP_BUY_ALLOWED + "</white>\nSelling Enabled: <white>" + Placeholders.SHOP_SELL_ALLOWED + "</white>"),
        EditorButtonType.info("Enables specified transactions in the shop."),
        EditorButtonType.click("Left-Click to <white>Toggle Buying</white>\nRight-Click to <white>Toggle Selling</white>")),
    SHOP_CHANGE_CITIZENS_ID(Material.PLAYER_HEAD, "Attached NPCs",
        EditorButtonType.current(Placeholders.SHOP_VIRTUAL_NPC_IDS),
        EditorButtonType.info("A list of NPC Ids attached to this shop. These NPCs will open the shop GUI when you interact with them."),
        EditorButtonType.warn("You must have Citizens installed!"),
        EditorButtonType.click("Left-Click to <white>Add NPC</white>\nRight-Click to <white>Clear List</white>")),
    SHOP_CHANGE_VIEW_DESIGN(Material.PAINTING, "Shop GUI Design",
        EditorButtonType.current("Title: <white>" + Placeholders.SHOP_VIRTUAL_VIEW_TITLE + "</white>\nSize: <white>" + Placeholders.SHOP_VIRTUAL_VIEW_SIZE + "</white>"),
        EditorButtonType.info("Here you can set the GUI layout for this shop.\nSimply add/remove items as you want and you're done!"),
        EditorButtonType.note("Press [Q] key on item to change it's Type. It's useful to create Page and Back buttons."),
        EditorButtonType.click("Left-Click to <white>Navigate</white>\nShift-Left to <white>Change Title</white>\nShift-Right to <white>Change Size</white>")),

    DISCOUNT_OBJECT(Material.GOLD_NUGGET, "Shop Discount",
        EditorButtonType.info("Discount: <white>" + Placeholders.DISCOUNT_CONFIG_AMOUNT + "<white>%\nDays: \n" + Placeholders.DISCOUNT_CONFIG_DAYS + "\nTimes: \n" + Placeholders.DISCOUNT_CONFIG_TIMES),
        EditorButtonType.click("Left-Click to <white>Edit</white>\nShift-Right to <white>Delete</white> <gray>(No Undo)")),
    DISCOUNT_CREATE(Material.ANVIL, "Create Discount",
        EditorButtonType.info("Create a new discount config for the shop."),
        EditorButtonType.click("Left-Click to <white>Create</white>")),
    DISCOUNT_CHANGE_DISCOUNT(Material.GOLD_NUGGET, "Discount Amount",
        EditorButtonType.current(Placeholders.DISCOUNT_CONFIG_AMOUNT + "%"),
        EditorButtonType.info("Sets the discount amount (in percent)."),
        EditorButtonType.click("Left-Click to <white>Edit</white>")),
    DISCOUNT_CHANGE_DURATION(Material.REPEATER, "Duration",
        EditorButtonType.current(Placeholders.DISCOUNT_CONFIG_DURATION),
        EditorButtonType.info("For how long this discount will have effect?"),
        EditorButtonType.click("Left-Click to <white>Edit</white>")),
    DISCOUNT_CHANGE_DAY(Material.DAYLIGHT_DETECTOR, "Activation Days",
        EditorButtonType.current(Placeholders.DISCOUNT_CONFIG_DAYS),
        EditorButtonType.info("A list of days, when this discount will be activated."),
        EditorButtonType.warn("You have to set at least ONE day and time for the discount to work!"),
        EditorButtonType.click("Left-Click to <white>Add Day</white>\nRight-Click to <white>Clear</white>")),
    DISCOUNT_CHANGE_TIME(Material.CLOCK, "Activation Times",
        EditorButtonType.current(Placeholders.DISCOUNT_CONFIG_TIMES),
        EditorButtonType.info("A list of times, when this discount will be activated."),
        EditorButtonType.warn("You have to set at least ONE day and time for the discount to work!"),
        EditorButtonType.click("Left-Click to <white>Add Time</white>\nRight-Click to <white>Clear</white>")),

    PRODUCT_OBJECT(Material.EMERALD, "%product_preview_name%",
        EditorButtonType.click("Shift-Left to <white>Edit</white>\nShift-Right to <white>Remove</white> <gray>(No Undo)")),
    PRODUCT_CHANGE_PRICE(Material.NAME_TAG, "Price Manager",
        EditorButtonType.current("Buy: <white>" + Placeholders.PRODUCT_PRICE_BUY + "</white>\nSell: <white>" + Placeholders.PRODUCT_PRICE_SELL + "</white>"),
        EditorButtonType.info("Here you can change product price type and set prices."),
        EditorButtonType.click("Left-Click to <white>Navigate</white>\n[Q] Key to <white>Refresh</white>")),
    PRODUCT_CHANGE_CURRENCY(Material.EMERALD, "Product Currency",
        EditorButtonType.current(Placeholders.PRODUCT_CURRENCY),
        EditorButtonType.info("Sets the product currency."),
        EditorButtonType.click("Left-Click to <white>Edit</white>")),
    PRODUCT_CHANGE_PREVIEW(Material.ITEM_FRAME, "Product Preview",
        EditorButtonType.current(Placeholders.PRODUCT_PREVIEW_NAME),
        EditorButtonType.info("Sets the item that will describe this product."),
        EditorButtonType.note("If you want to add a custom name or description, please do it in this item name and lore."),
        EditorButtonType.click("Drag & Drop to <white>Replace</white>\nShift-Left to <white>Get Item</white>")),
    PRODUCT_CHANGE_ITEM(Material.ITEM_FRAME, "Actual Item",
        EditorButtonType.current(Placeholders.PRODUCT_ITEM_NAME),
        EditorButtonType.info("Sets the actual item that player can buy or sell."),
        EditorButtonType.note("Custom items are fully supported here."),
        EditorButtonType.warn("Products without 'Actual Item' can not be sold!"),
        EditorButtonType.click("Drag & Drop to <white>Replace</white>\nRight-Click to <white>Disable</white>\nShift-Left to <white>Get Item</white>")),
    PRODUCT_CHANGE_COMMANDS(Material.COMMAND_BLOCK, "Product Commands",
        EditorButtonType.current(Placeholders.PRODUCT_VIRTUAL_COMMANDS),
        EditorButtonType.info("A list of commands, that will be executed when player purchases this product."),
        EditorButtonType.click("Left-Click to <white>Add Command</white>\nRight-Click to <white>Clear</white>")),
    PRODUCT_CHANGE_DISCOUNT(Material.GOLD_NUGGET, "Discounts Allowed",
        EditorButtonType.current("Allowed: <white>" + Placeholders.PRODUCT_DISCOUNT_ALLOWED + "</white>"),
        EditorButtonType.info("Sets if the product price can be affected by the shop's discounts."),
        EditorButtonType.click("Left-Click to <white>Toggle</white>")),
    PRODUCT_CHANGE_ITEM_META(Material.REPEATER, "Item Meta Enabled",
        EditorButtonType.current("Enabled: <white>" + Placeholders.PRODUCT_ITEM_META_ENABLED + "</white>"),
        EditorButtonType.info("When enabled, the metadata (name, lore, etc) of the product item and item player wants to sell, must be the same."),
        EditorButtonType.warn("Enable this for custom items!"),
        EditorButtonType.click("Left-Click to <white>Toggle</white>")),
    PRODUCT_CHANGE_STOCK_GLOBAL(Material.COMPOSTER, "Product Global Stock",
        EditorButtonType.current(
            "Buy Stock Initial: <white>" + Placeholders.PRODUCT_STOCK_GLOBAL_BUY_AMOUNT_INITIAL + "</white>" +
            "\nBuy Restock Time: <white>" + Placeholders.PRODUCT_STOCK_GLOBAL_BUY_RESTOCK_TIME + "</white>" +
            "\nSell Stock Initial: <white>" + Placeholders.PRODUCT_STOCK_GLOBAL_SELL_AMOUNT_INITIAL + "</white>" +
            "\nSell Restock Time: <white>" + Placeholders.PRODUCT_STOCK_GLOBAL_SELL_RESTOCK_TIME) + "</white>",
        EditorButtonType.info("Sets how many of the product the shop can store at all.\nEvery time a player purchases/sells product, stock amount will be changed. When there is 0 items left, buying/selling will be unavailable until restock."),
        EditorButtonType.note("You can set -1 for initial amount to make it unlimited."),
        EditorButtonType.note("You can set -1 for restock time to make it never auto-restock."),
        EditorButtonType.click("Left-Click to <white>Edit Buy Initial</white>\nRight-Click to <white>Edit Buy Restock</white>\nShift-Left to <white>Edit Sell Initial</white>\nShift-Right to <white>Edit Sell Restock</white>")),
    PRODUCT_CHANGE_STOCK_GLOBAL_BUY_INITIAL_AMOUNT,
    PRODUCT_CHANGE_STOCK_GLOBAL_BUY_RESTOCK_TIME,
    PRODUCT_CHANGE_STOCK_GLOBAL_SELL_INITIAL_AMOUNT,
    PRODUCT_CHANGE_STOCK_GLOBAL_SELL_RESTOCK_TIME,
    PRODUCT_CHANGE_STOCK_PLAYER(Material.COMPOSTER, "Product Player Limits",
        EditorButtonType.current(
            "Buy Limit Amount: <white>" + Placeholders.PRODUCT_STOCK_PLAYER_BUY_AMOUNT_INITIAL + "</white>" +
            "\nBuy Cooldown: <white>" + Placeholders.PRODUCT_STOCK_PLAYER_BUY_RESTOCK_TIME + "</white>" +
            "\nSell Limit Amount: <white>" + Placeholders.PRODUCT_STOCK_PLAYER_SELL_AMOUNT_INITIAL + "</white>" +
            "\nSell Cooldown: <white>" + Placeholders.PRODUCT_STOCK_PLAYER_SELL_RESTOCK_TIME) + "</white>",
        EditorButtonType.info("Sets how many of the product a player can buy/sell until it's refreshed by the cooldown.\nIf the global stock amount is less than player current limit, the global stock amount will be used as max. possible."),
        EditorButtonType.note("You can set -1 for limit amount to make it unlimited."),
        EditorButtonType.note("You can set -1 for cooldown to make it never auto-refresh."),
        EditorButtonType.click("Left-Click to <white>Edit Buy Limit</white>\nRight-Click to <white>Edit Buy Cooldown</white>\nShift-Left to <white>Edit Sell Limit</white>\nShift-Right to <white>Edit Sell Cooldown</white>")),
    PRODUCT_CHANGE_STOCK_PLAYER_BUY_INITIAL_AMOUNT,
    PRODUCT_CHANGE_STOCK_PLAYER_BUY_RESTOCK_TIME,
    PRODUCT_CHANGE_STOCK_PLAYER_SELL_INITIAL_AMOUNT,
    PRODUCT_CHANGE_STOCK_PLAYER_SELL_RESTOCK_TIME,
    ;

    private final Material        material;
    private       Component       name;
    private       List<Component> lore;

    VirtualEditorType() {
        this(Material.AIR, "", "");
    }

    VirtualEditorType(@NotNull Material material, @NotNull String name, @NotNull String... lore) {
        this.material = material;
        this.setName(ComponentUtil.asComponent(name));
        this.setLore(ComponentUtil.asComponent(EditorButtonType.fineLore(lore)));
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return material;
    }

    @NotNull
    public Component getName() {
        return name;
    }

    public void setName(@NotNull Component name) {
        this.name = name;
    }

    @NotNull
    public List<Component> getLore() {
        return lore;
    }

    public void setLore(@NotNull List<Component> lore) {
        this.lore = new ArrayList<>(lore);
    }
}
