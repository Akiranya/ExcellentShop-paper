package su.nightexpress.nexshop.shop.chest.command;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.nexshop.module.command.ShopModuleCommand;
import su.nightexpress.nexshop.shop.chest.ChestPerms;
import su.nightexpress.nexshop.shop.chest.ChestShopModule;
import su.nightexpress.nexshop.shop.chest.config.ChestLang;
import su.nightexpress.nexshop.shop.chest.type.ChestShopType;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CreateCommand extends ShopModuleCommand<ChestShopModule> {

    public CreateCommand(@NotNull ChestShopModule module) {
        super(module, new String[]{"create"}, ChestPerms.CREATE);
    }

    @Override
    public @NotNull String getDescription() {
        return plugin.getMessage(ChestLang.COMMAND_CREATE_DESC).getLocalized();
    }

    @Override
    public @NotNull String getUsage() {
        return plugin.getMessage(ChestLang.COMMAND_CREATE_USAGE).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return Stream.of(ChestShopType.values()).filter(type -> type.hasPermission(player)).map(Enum::name).toList();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 100);
        ChestShopType type = args.length >= 2 ? StringUtil.getEnum(args[1], ChestShopType.class).orElse(ChestShopType.PLAYER) : ChestShopType.PLAYER;
        this.module.createShop(player, block, type);
    }
}
