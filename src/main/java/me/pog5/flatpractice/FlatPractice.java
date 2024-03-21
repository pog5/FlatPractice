package me.pog5.flatpractice;

import me.pog5.flatpractice.commands.SuicideCommand;
import me.pog5.flatpractice.listeners.DamageListener;
import me.pog5.flatpractice.listeners.DeathListener;
import me.pog5.flatpractice.listeners.JoinQuitListener;
import me.pog5.flatpractice.managers.UserManager;
import org.bukkit.plugin.PluginManager;
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

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        this.plugin = null;
    }

    private void registerListeners() {
        final PluginManager pluginManager = getServer().getPluginManager();

        if (pluginManager.isPluginEnabled("PlaceholderAPI"))
            getServer().getWorlds();
            // todo Add PAPI support for Kills/Deaths/Stats/etc...

        pluginManager.registerEvents(new JoinQuitListener(plugin), plugin);
        pluginManager.registerEvents(new DamageListener(plugin), plugin);
        pluginManager.registerEvents(new DeathListener(plugin), plugin);
    }
    private void registerCommands() {
        getServer().getCommandMap().register("flatpractice", new SuicideCommand(getPlugin()));
    }

}
