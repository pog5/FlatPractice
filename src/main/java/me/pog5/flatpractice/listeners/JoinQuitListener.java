package me.pog5.flatpractice.listeners;

import me.pog5.flatpractice.FlatPractice;
import me.pog5.flatpractice.objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    private final FlatPractice plugin;
    public JoinQuitListener(FlatPractice plugin) {
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        plugin.getUserManager().addPlayerToUsers(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        final User targetUser = plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (targetUser == null)
            return;
        if (targetUser.isFighting)
            targetUser.addDeath(null);
        plugin.getUserManager().removeUser(targetUser);
    }
}
