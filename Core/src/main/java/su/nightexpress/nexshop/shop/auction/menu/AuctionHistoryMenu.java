package su.nightexpress.nexshop.shop.auction.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nightexpress.nexshop.shop.auction.AuctionManager;
import su.nightexpress.nexshop.shop.auction.listing.AuctionCompletedListing;

import java.util.*;

public class AuctionHistoryMenu extends AbstractAuctionMenu<AuctionCompletedListing> {

    public AuctionHistoryMenu(@NotNull AuctionManager auctionManager, @NotNull JYML cfg) {
        super(auctionManager, cfg);
        this.load();
    }

    @Override
    public @NotNull List<AuctionCompletedListing> getObjects(@NotNull Player player) {
        UUID id = this.seeOthers.getOrDefault(player, player.getUniqueId());
        return this.auctionManager.getHistoryListings(id);
    }

    @Override
    public @NotNull ItemClick getObjectClick(@NotNull AuctionCompletedListing item) {
        return (viewer, event) -> {

        };
    }
}
