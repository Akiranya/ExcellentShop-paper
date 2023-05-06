package su.nightexpress.nexshop.shop.virtual.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.GeneralCommand;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.nexshop.ExcellentShop;
import su.nightexpress.nexshop.shop.virtual.VirtualShopModule;
import su.nightexpress.nexshop.shop.virtual.config.VirtualLang;
import su.nightexpress.nexshop.shop.virtual.config.VirtualPerms;
import su.nightexpress.nexshop.shop.virtual.impl.shop.VirtualShop;

import java.util.List;
import java.util.Map;

public class SellMenuCommand extends GeneralCommand<ExcellentShop> {

    private final VirtualShopModule module;

    public SellMenuCommand(@NotNull VirtualShopModule module, @NotNull String[] aliases) {
        super(module.plugin(), aliases, VirtualPerms.COMMAND_SELL_MENU);
        this.module = module;
    }

    @Override
    public @NotNull String getUsage() {
        return plugin.getMessage(VirtualLang.COMMAND_SELL_MENU_USAGE).getLocalized();
    }

    @Override
    public @NotNull String getDescription() {
        return plugin.getMessage(VirtualLang.COMMAND_SELL_MENU_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1 && player.hasPermission(VirtualPerms.COMMAND_SELL_MENU)) {
            return module.getShops(player).stream().map(VirtualShop::getId).toList();
        } else if (arg == 2 && player.hasPermission(VirtualPerms.COMMAND_SELL_MENU_OTHERS)) {
            return CollectionsUtil.playerNames();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        if (args.length < 1) {
            this.printUsage(sender);
        } else if (args.length == 1) {
            // Command Label: /<command> <shop>

            if (sender instanceof Player player) {
                VirtualShop shop = this.module.getShopById(args[0]);
                if (shop == null) {
                    plugin.getMessage(VirtualLang.SHOP_ERROR_INVALID).send(sender);
                    return;
                }
                this.module.getSellMenu().open(player, 1, shop);
            } else {
                this.printUsage(sender);
            }

        } else if (args.length == 2) {
            // Command Label: /<command> <shop> [player]

            VirtualShop shop = this.module.getShopById(args[0]);
            if (shop == null) {
                plugin.getMessage(VirtualLang.SHOP_ERROR_INVALID).send(sender);
                return;
            }

            Player player = plugin.getServer().getPlayer(args[1]);
            if (player == null) {
                this.errorPlayer(sender);
                return;
            }
            if (!player.equals(sender) && !sender.hasPermission(VirtualPerms.COMMAND_SELL_MENU_OTHERS)) {
                this.errorPermission(sender);
                return;
            }
            if (!shop.hasPermission(player)) {
                this.errorPermission(player);
                return;
            }

            this.module.getSellMenu().open(player, 1, shop);
        }
    }
}
