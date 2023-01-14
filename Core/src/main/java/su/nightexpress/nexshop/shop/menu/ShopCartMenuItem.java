package su.nightexpress.nexshop.shop.menu;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemImpl;

public class ShopCartMenuItem extends MenuItemImpl {

    private int productAmount;

    public ShopCartMenuItem(@NotNull MenuItem menuItem) {
        super(menuItem);
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }
}
