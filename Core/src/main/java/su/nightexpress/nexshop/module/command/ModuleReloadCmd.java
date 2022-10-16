package su.nightexpress.nexshop.module.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import su.nightexpress.nexshop.Perms;
import su.nightexpress.nexshop.config.Lang;
import su.nightexpress.nexshop.module.ShopModule;

public class ModuleReloadCmd extends ShopModuleCommand<ShopModule> {

    public ModuleReloadCmd(@NotNull ShopModule m) {
        super(m, new String[]{"reload"}, Perms.ADMIN);
    }

    @Override
    @NotNull
    public String getUsage() {
        return "";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "Reload the module.";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        this.module.reload();
        this.plugin.getMessage(Lang.Module_Cmd_Reload).replace("%module%", module.getName()).send(sender);
    }
}
