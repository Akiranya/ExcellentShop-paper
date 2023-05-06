package su.nightexpress.nexshop.shop.auction.command;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nexshop.Perms;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.config.AuctionLang;
import su.nightexpress.nexshop.shop.auction.menu.AuctionUnclaimedMenu;

public class AuctionUnclaimedCommand extends AbstractOpenCommand {

    public AuctionUnclaimedCommand(@NotNull AuctionManager module) {
        super(module, new String[]{"unclaimed"}, Perms.AUCTION_COMMAND_UNCLAIMED);
    }

    @Override
    public @NotNull String getDescription() {
        return plugin.getMessage(AuctionLang.COMMAND_UNCLAIMED_DESC).getLocalized();
    }

    @Override
    public @NotNull String getUsage() {
        return plugin.getMessage(AuctionLang.COMMAND_UNCLAIMED_USAGE).getLocalized();
    }

    @Override
    protected @NotNull AuctionUnclaimedMenu getMenu() {
        return this.module.getUnclaimedMenu();
    }

    @Override
    protected @Nullable Permission getPermissionsOthers() {
        return Perms.AUCTION_COMMAND_UNCLAIMED_OTHERS;
    }
}
