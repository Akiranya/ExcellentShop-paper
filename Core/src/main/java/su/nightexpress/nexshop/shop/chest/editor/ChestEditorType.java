package su.nightexpress.nexshop.shop.chest.editor;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nightexpress.nexshop.Placeholders;

import java.util.List;

public enum ChestEditorType implements EditorButtonType {

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

    private final Material material;
    private String name;
    private List<String> lore;

    ChestEditorType() {
        this(Material.AIR, "", "");
    }

    ChestEditorType(@NotNull Material material, @NotNull String name, @NotNull String... lore) {
        this.material = material;
        this.setName(name);
        this.setLore(EditorButtonType.fineLore(lore));
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return material;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public List<String> getLore() {
        return lore;
    }

    public void setLore(@NotNull List<String> lore) {
        this.lore = lore;
    }
}
