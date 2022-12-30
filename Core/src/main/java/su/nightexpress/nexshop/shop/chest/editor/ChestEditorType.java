package su.nightexpress.nexshop.shop.chest.editor;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nightexpress.nexshop.Placeholders;

import java.util.ArrayList;
import java.util.List;

public enum ChestEditorType implements EditorButtonType {

    SHOP_CHANGE_NAME(Material.NAME_TAG, "Shop Name",
        EditorButtonType.current(Placeholders.SHOP_NAME),
        EditorButtonType.info("Sets the shop display name. This name is displayed above the shop."),
        EditorButtonType.click("Left-Click to <white>Change</white>")),
    SHOP_CHANGE_TYPE(Material.REDSTONE, "Shop Type",
        EditorButtonType.current(Placeholders.SHOP_CHEST_TYPE),
        EditorButtonType.info("Sets the shop type."),
        EditorButtonType.note("Admin shops have unlimited funds and items."),
        EditorButtonType.note("Player shop have to be restocked with items and funds."),
        EditorButtonType.click("Left-Click to <white>Toggle</white>")),
    SHOP_CHANGE_TRANSACTIONS(Material.WRITABLE_BOOK, "Enabled Transactions",
        EditorButtonType.current("Buying Enabled: <white>" + Placeholders.SHOP_BUY_ALLOWED + "</white>\nSelling Enabled: <white>" + Placeholders.SHOP_SELL_ALLOWED) + "</white>",
        EditorButtonType.info("Enables or disables Buying and Selling in the shop."),
        EditorButtonType.click("Left-Click to <white>Toggle Buying</white>\nRight-Click to <white>Toggle Selling</white>")),
    SHOP_CHANGE_PRODUCTS(Material.CHEST, "Shop Products",
        EditorButtonType.info("Here you can manage your products and add new ones."),
        EditorButtonType.click("Left-Click to <white>Navigate</white>")),
    SHOP_BANK(Material.EMERALD, "Shop Bank",
        EditorButtonType.current("Balance:\n" + Placeholders.SHOP_BANK_BALANCE),
        EditorButtonType.info("Bank is where you deposit funds for the shop to be able to purchase items from players. And where all incoming funds from sales are stored for you to withdraw them later."),
        EditorButtonType.warn("If shop don't have enough funds, players won't be able to sell into it"),
        EditorButtonType.click("Left-Click to <white>Deposit</white>\nRight-Click to <white>Withdraw</white>")),
    SHOP_BANK_DEPOSIT,
    SHOP_BANK_WITHDRAW,
    SHOP_DELETE(Material.BARRIER, "Delete Shop",
        EditorButtonType.info("Permanently deletes this shop."),
        EditorButtonType.warn("You have to remove all product items from the container first."),
        EditorButtonType.click("Shift-Left to <white>Delete</white>")),

    PRODUCT_OBJECT(Material.EMERALD, "%product_preview_name%",
        EditorButtonType.click("Left to <white>Edit</white>\nShift-Right to <white>Remove</white> (No Undo)")),
    PRODUCT_CHANGE_CURRENCY(Material.EMERALD, "Product Currency",
        EditorButtonType.current(Placeholders.PRODUCT_CURRENCY),
        EditorButtonType.info("Sets the product currency."),
        EditorButtonType.click("Left-Click to <white>Edit</white>")),
    PRODUCT_CHANGE_PRICE(Material.NAME_TAG, "Price Manager",
        EditorButtonType.current("Buy: <white>" + Placeholders.PRODUCT_PRICE_BUY + "</white>\nSell: <white>" + Placeholders.PRODUCT_PRICE_SELL + "</white>"),
        EditorButtonType.info("Here you can change product price type and set prices."),
        EditorButtonType.click("Left-Click to <white>Navigate</white>\n[Q] Key to <white>Refresh</white>")),
    ;

    private final Material        material;
    private       Component       name;
    private       List<Component> lore;

    ChestEditorType() {
        this(Material.AIR, "", "");
    }

    ChestEditorType(@NotNull Material material, @NotNull String name, @NotNull String... lore) {
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
