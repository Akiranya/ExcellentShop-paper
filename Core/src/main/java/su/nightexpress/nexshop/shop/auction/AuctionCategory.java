package su.nightexpress.nexshop.shop.auction;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;

import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class AuctionCategory implements IPlaceholder {

    private final String id;
    private final String name;
    private final ItemStack icon;
    private final Set<String> materials;

    public AuctionCategory(
        @NotNull String id,
        @NotNull String name,
        @NotNull ItemStack icon,
        @NotNull Set<String> materials) {
        this.id = id.toLowerCase().replace(" ", "_");
        this.name = name;
        this.icon = icon;
        this.materials = materials.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return str -> str
            .replace(Placeholders.CATEGORY_ID, this.getId())
            .replace(Placeholders.CATEGORY_NAME, this.getName())
            .replace(Placeholders.CATEGORY_ICON_NAME, ComponentUtil.asMiniMessage(ItemUtil.getName(this.getIcon())))
            // .replace(Placeholders.CATEGORY_ICON_LORE, String.join("\n", ComponentUtil.asMiniMessage(ItemUtil.getLore(this.getIcon())))
            ;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public ItemStack getIcon() {
        return this.icon.clone();
    }

    @NotNull
    public Set<String> getMaterials() {
        return this.materials;
    }

    public boolean isItemOfThis(@NotNull ItemStack item) {
        return this.isItemOfThis(item.getType());
    }

    public boolean isItemOfThis(@NotNull Material material) {
        return this.isItemOfThis(material.name());
    }

    public boolean isItemOfThis(@NotNull String name) {
        return this.materials.contains(name.toLowerCase()) || this.materials.contains(Placeholders.MASK_ANY);
    }
}
