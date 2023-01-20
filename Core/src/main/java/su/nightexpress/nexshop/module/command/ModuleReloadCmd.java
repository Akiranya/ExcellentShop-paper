package su.nightexpress.nexshop.module.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nexshop.Perms;
import su.nightexpress.nexshop.config.Lang;
import su.nightexpress.nexshop.module.ShopModule;

import java.util.Map;

public class ModuleReloadCmd extends ShopModuleCommand<ShopModule> {

    public ModuleReloadCmd(@NotNull ShopModule m) {
        super(m, new String[]{"reload"}, Perms.COMMAND_RELOAD);
    }

    @Override
    @NotNull
    public String getUsage() {
        return "";
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.getLangManager().getMessage(Lang.MODULE_COMMAND_RELOAD_DESC).getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, @NotNull Map<String, String> flags) {
        this.module.reload();
        this.plugin.getMessage(Lang.MODULE_COMMAND_RELOAD_DONE).replace("%module%", module.getName()).send(sender);
    }
}
