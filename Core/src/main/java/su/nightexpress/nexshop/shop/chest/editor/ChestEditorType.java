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
