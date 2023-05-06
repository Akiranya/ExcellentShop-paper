package su.nightexpress.nexshop.shop.auction.command;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nexshop.Perms;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.config.AuctionLang;
import su.nightexpress.nexshop.shop.auction.menu.AuctionSellingMenu;

public class AuctionSellingCommand extends AbstractOpenCommand {

    public AuctionSellingCommand(@NotNull AuctionManager module) {
        super(module, new String[]{"selling", "listings"}, Perms.AUCTION_COMMAND_SELLING);
    }

    @Override
    public @NotNull String getUsage() {
        return plugin.getMessage(AuctionLang.COMMAND_SELLING_USAGE).getLocalized();
    }

    @Override
    public @NotNull String getDescription() {
        return plugin.getMessage(AuctionLang.COMMAND_SELLING_DESC).getLocalized();
    }

    @Override
    protected @NotNull AuctionSellingMenu getMenu() {
        return this.module.getSellingMenu();
    }

    @Override
    protected @Nullable Permission getPermissionsOthers() {
        return Perms.AUCTION_COMMAND_SELLING_OTHERS;
    }
}
