package su.nightexpress.nexshop.shop.auction.command;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nexshop.Perms;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.config.AuctionLang;
import su.nightexpress.nexshop.shop.auction.menu.AuctionExpiredMenu;

public class AuctionExpiredCommand extends AbstractOpenCommand {

    public AuctionExpiredCommand(@NotNull AuctionManager module) {
        super(module, new String[]{"expired"}, Perms.AUCTION_COMMAND_EXPIRED);
    }

    @Override
    public @NotNull String getUsage() {
        return plugin.getMessage(AuctionLang.COMMAND_EXPIRED_USAGE).getLocalized();
    }

    @Override
    public @NotNull String getDescription() {
        return plugin.getMessage(AuctionLang.COMMAND_EXPIRED_DESC).getLocalized();
    }

    @Override
    protected @NotNull AuctionExpiredMenu getMenu() {
        return this.module.getExpiredMenu();
    }

    @Override
    protected @Nullable Permission getPermissionsOthers() {
        return Perms.AUCTION_COMMAND_EXPIRED_OTHERS;
    }
}
