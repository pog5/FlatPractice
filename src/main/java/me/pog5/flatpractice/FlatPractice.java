package me.pog5.flatpractice;

import me.pog5.flatpractice.commands.SuicideCommand;
import me.pog5.flatpractice.managers.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlatPractice extends JavaPlugin {
    private FlatPractice plugin;
    public FlatPractice getPlugin() {
        return plugin;
    }
    private UserManager userManager;
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public void onEnable() {
        this.plugin = this;
        userManager = new UserManager(getPlugin());
        getServer().getOnlinePlayers().forEach(p -> userManager.addPlayerToUsers(p));
        userManager.refreshUsers();

        getServer().getCommandMap().register("flatpractice", new SuicideCommand(getPlugin()));
    }

    @Override
    public void onDisable() {
        this.plugin = null;
    }

}
