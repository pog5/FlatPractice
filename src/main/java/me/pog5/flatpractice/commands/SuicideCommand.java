package me.pog5.flatpractice.commands;

import me.pog5.flatpractice.FlatPractice;
import me.pog5.flatpractice.objects.User;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SuicideCommand extends BukkitCommand {
    private final FlatPractice plugin;
    public SuicideCommand(FlatPractice plugin) {
        super("suicide");
        this.plugin = plugin;
    }
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
         if (!(sender instanceof Player) || !sender.hasPermission("flatpractice.command.suicide")) return false;
         final User senderUser = plugin.getUserManager().getUser(((Player) sender).getUniqueId());
         ((Player) sender).setHealth(0.0D);
        return false;
    }
}
