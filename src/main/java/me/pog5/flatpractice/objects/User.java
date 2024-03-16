package me.pog5.flatpractice.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;
import java.util.WeakHashMap;

public class User {
    // Internal
    private final Player player;
    public boolean isFighting = false;
    // Statistics
    public char kills, deaths = 0;
    public float killDeathRatio = 0;
    // Kits
    public WeakHashMap<String, PlayerInventory> kits;
    public String defaultKit;
    // Options
    public boolean deathMessagesEnabled = true;
    public boolean scoreboardEnabled = true;
    public User(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
    public UUID getUUID() {
        return this.player.getUniqueId();
    }
    public String getName() {
        return this.player.getName();
    }

    public void killedPlayer(Player target) {

    }
}
