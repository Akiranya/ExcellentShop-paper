package su.nightexpress.nexshop.shop.auction;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderConstants;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.ItemUtil;

import java.util.*;
import java.util.stream.Collectors;

public class AuctionCategory implements Placeholder {

    private final String id;
    private final String name;
    private final ItemStack icon;
    private final Set<String> materials;

    private final PlaceholderMap placeholderMap;

    public AuctionCategory(
        @NotNull String id,
        @NotNull String name,
        @NotNull ItemStack icon,
        @NotNull Set<String> materials
    ) {
        this.id = id.toLowerCase().replace(" ", "_");
        this.name = name;
        this.icon = icon.clone();
        this.materials = materials.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.CATEGORY_ID, this::getId)
            .add(Placeholders.CATEGORY_NAME, this::getName)
            .add(Placeholders.CATEGORY_ICON_NAME, () -> ComponentUtil.asMiniMessage(ItemUtil.getName(this.getIcon())))
            /*.add(Placeholders.CATEGORY_ICON_LORE, () -> String.join("\n", ComponentUtil.asMiniMessage(ItemUtil.getLore(this.getIcon()))))*/; // Mewcraft - fix lore

        ItemUtil.replaceNameAndLore(this.icon, this.replacePlaceholders());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
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
        return new ItemStack(this.icon);
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
        return this.materials.contains(name.toLowerCase()) || this.materials.contains(PlaceholderConstants.WILDCARD);
    }
}
