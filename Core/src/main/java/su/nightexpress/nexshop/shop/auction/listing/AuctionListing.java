package su.nightexpress.nexshop.shop.auction.listing;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.ComponentUtil;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.nexshop.api.currency.ICurrency;
import su.nightexpress.nexshop.shop.auction.config.AuctionConfig;
import su.nightexpress.nexshop.shop.auction.Placeholders;

import java.util.UUID;
import java.util.function.UnaryOperator;

public class AuctionListing extends AbstractAuctionItem {

    private final long expireDate;

    public AuctionListing(
        @NotNull Player player,
        @NotNull ItemStack itemStack,
        @NotNull ICurrency currency,
        double price
    ) {
        this(
            UUID.randomUUID(),
            player.getUniqueId(),
            ComponentUtil.asMiniMessage(player.displayName()),
            itemStack,
            currency,
            price,
            System.currentTimeMillis(),
            System.currentTimeMillis() + AuctionConfig.LISTINGS_EXPIRE_IN
        );
    }

    public AuctionListing(
        @NotNull UUID id,
        @NotNull UUID owner,
        @NotNull String ownerName,
        @NotNull ItemStack itemStack,
        @NotNull ICurrency currency,
        double price,
        long dateCreation,
        long expireDate
    ) {
        super(id, owner, ownerName, itemStack, currency, price, dateCreation);
        this.expireDate = expireDate;
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return str -> super.replacePlaceholders().apply(str
            .replace(Placeholders.LISTING_EXPIRES_IN, TimeUtil.formatTimeLeft(this.getExpireDate()))
        );
    }

    public long getExpireDate() {
        return expireDate;
    }

    public long getDeleteDate() {
        return this.getExpireDate() + AuctionConfig.LISTINGS_PURGE_IN;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.getExpireDate();
    }

    /*public boolean isValid() {
        return System.currentTimeMillis() <= this.getDeleteDate();
    }*/
}
