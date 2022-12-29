package su.nightexpress.nexshop.shop.chest.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.ShopAPI;
import su.nightexpress.nexshop.api.shop.ShopView;
import su.nightexpress.nexshop.api.type.ShopClickType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChestShopView extends ShopView<ChestShop> {

    private static int[]        PRODUCT_SLOTS;
    private static List<String> PRODUCT_FORMAT_LORE;

    public ChestShopView(@NotNull ChestShop shop) {
        super(shop, JYML.loadOrExtract(shop.plugin(), ShopAPI.getChestShop().getPath() + "view.yml"));

        PRODUCT_SLOTS = cfg.getIntArray("Product_Slots");
        PRODUCT_FORMAT_LORE = cfg.getStringList("Product_Format.Lore.Text");

        IMenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                this.onItemClickDefault(player, type2);
            }
        };

        for (String id : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + id, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(click);
            }
            this.addItem(menuItem);
        }

        this.setTitle(StringUtil.asComponent(shop.getName()));
    }

    @Override
    public void displayProducts(@NotNull Player player, @NotNull Inventory inventory, int page) {
        int len = PRODUCT_SLOTS.length;

        List<ChestProduct> list = this.getShop().getProducts().stream().toList();
        List<List<ChestProduct>> split = CollectionsUtil.split(list, len);

        int pages = split.size();
        if (pages < 1) list = Collections.emptyList();
        else list = split.get(page - 1);

        int count = 0;
        for (ChestProduct product : list) {
            ItemStack preview = product.getPreview();
            ItemMeta meta = preview.getItemMeta();

            if (meta != null) {
                List<Component> lore = new ArrayList<>();

                for (String lineFormat : PRODUCT_FORMAT_LORE) {
                    if (lineFormat.contains(Placeholders.GENERIC_LORE)) {
                        List<Component> list2 = meta.lore();
                        if (list2 != null) lore.addAll(list2);
                        continue;
                    }
                    lore.add(StringUtil.asComponent(lineFormat));
                }

                lore = StringUtil.applyStringReplacer(product.replacePlaceholders(player), lore);
                lore = StringUtil.applyStringReplacer(product.getCurrency().replacePlaceholders(), lore);
                meta.lore(lore);
                preview.setItemMeta(meta);
            }

            IMenuItem menuItem = new MenuItem(preview);
            menuItem.setSlots(PRODUCT_SLOTS[count++]);
            menuItem.setClick((p2, type, e) -> {
                ShopClickType clickType = ShopClickType.getByDefault(e.getClick());
                if (clickType == null) return;

                product.prepareTrade(p2, clickType);
            });
            this.addItem(player, menuItem);
        }
        this.setPage(player, page, pages);
    }
}
