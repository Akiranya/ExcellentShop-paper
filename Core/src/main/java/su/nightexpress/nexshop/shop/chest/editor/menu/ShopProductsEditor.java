package su.nightexpress.nexshop.shop.chest.editor.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.nexshop.api.shop.Product;
import su.nightexpress.nexshop.api.type.TradeType;
import su.nightexpress.nexshop.editor.GenericEditorType;
import su.nightexpress.nexshop.editor.menu.EditorProductList;
import su.nightexpress.nexshop.shop.chest.config.ChestConfig;
import su.nightexpress.nexshop.shop.chest.config.ChestLang;
import su.nightexpress.nexshop.shop.chest.editor.ChestEditorType;
import su.nightexpress.nexshop.shop.chest.impl.ChestShop;

public class ShopProductsEditor extends EditorProductList<ChestShop> {

    public ShopProductsEditor(@NotNull ChestShop shop) {
        super(shop);
    }

    @Override
    public boolean onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
        int page = this.getPage(player);
        int productCount = 0;
        int maxProducts = ChestConfig.getMaxShopProducts(player);
        if (maxProducts < 0) maxProducts = inventory.getSize();

        for (Product<?, ?, ?> shopProduct : this.shop.getProducts()) {
            if (productCount >= maxProducts || productCount >= inventory.getSize()) break;

            ItemStack productIcon = new ItemStack(shopProduct.getPreview());
            ItemMeta productMeta = productIcon.getItemMeta();
            if (productMeta == null) continue;

            ItemStack editorIcon = ChestEditorType.PRODUCT_OBJECT.getItem();
            productMeta.displayName(ItemUtil.getItemName(editorIcon));
            productMeta.lore(ItemUtil.getLore(editorIcon));
            productIcon.setItemMeta(productMeta);
            ItemUtil.replace(productIcon, shopProduct.replacePlaceholders());

            MenuItem item = new MenuItem(productIcon);
            item.setSlots(productCount++);
            item.setClickHandler((p, type, e) -> {
                if (e.isLeftClick()) {
                    shopProduct.getEditor().open(p, 1);
                    return;
                }
                if (e.isShiftClick()) {
                    if (e.isRightClick()) {
                        if (shopProduct.getStock().getLeftAmount(TradeType.BUY) > 0) {
                            plugin.getMessage(ChestLang.EDITOR_ERROR_PRODUCT_LEFT).send(p);
                            return;
                        }
                        this.shop.removeProduct(shopProduct.getId());
                        this.shop.save();
                        this.open(p, page);
                    }
                }
            });
            this.addItem(player, item);
        }

        MenuItem free = new MenuItem(GenericEditorType.PRODUCT_FREE_SLOT.getItem());
        int[] freeSlots = new int[maxProducts - productCount];
        int count2 = 0;
        for (int slot = productCount; slot < maxProducts; slot++) {
            freeSlots[count2++] = slot;
        }

        free.setSlots(freeSlots);
        free.setClickHandler((player2, type, e) -> {
            ItemStack cursor = e.getCursor();
            if (cursor == null || !this.shop.createProduct(player2, cursor)) return;

            e.getView().setCursor(null);
            PlayerUtil.addItem(player2, cursor);
            this.shop.save();
            this.open(player2, page);
        });
        this.addItem(player, free);
        return true;
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        Player player = (Player) e.getWhoClicked();
        if (slotType != SlotType.PLAYER || !PlayerUtil.isBedrockPlayer(player)) {
            return super.cancelClick(e, slotType);
        }

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType().isAir()) return true;

        int maxProducts = ChestConfig.getMaxShopProducts(player);
        int hasProducts = this.shop.getProducts().size();
        if (maxProducts >= 0 && hasProducts >= maxProducts) return true;

        this.shop.createProduct(player, item);

        return true;
    }
}
