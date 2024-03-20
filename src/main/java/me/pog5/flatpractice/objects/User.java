package me.pog5.flatpractice.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;
import java.util.WeakHashMap;

public class User {
    // Internal
    private final Player player;
    public boolean isFighting = false;
    public boolean isDead = false;
    // Statistics
    public char kills, deaths = 0;
    public float killDeathRatio = 0;
    public char killstreak = 0;
    public char bestKillstreak = 0;
    // Cosmetic
    public String preferredDeathTitle = Variables.getRandomDeathTitle();
    public String preferredKillMessage = Variables.getRandomDeathMessage();
    // Kits
    public WeakHashMap<String, PlayerInventory> kits;
    public String defaultKit;
    // Options
    public boolean deathMessagesEnabled = true;
    public boolean deathTitlesEnabled = true;
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
    public void addKill(User killed) {
        this.kills++;
        this.killstreak++;
        recalcStats();
    }
    public void addDeath(User killer) {
        this.deaths++;
        this.killstreak = 0;
        recalcStats();
    }
    public void recalcStats() {
        if (deaths == 0)
            killDeathRatio = kills;
        else
            killDeathRatio = (float) kills / deaths;

        if (bestKillstreak < killstreak)
            bestKillstreak = killstreak;
    }
}
