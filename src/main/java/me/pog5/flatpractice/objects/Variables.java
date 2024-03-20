package me.pog5.flatpractice.objects;

import me.pog5.flatpractice.FlatPractice;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class Variables {
    private final FlatPractice plugin;
    FileConfiguration config;

    /// World
    public static Location lobbySpawn;
    /// Combat
    public static List<String> deathMessages;
    public static List<String> deathTitles;

    public Variables(FlatPractice plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        loadConfigYML();
        saveConfigYML();
    }

    private void loadConfigYML() {
        // World
        lobbySpawn = config.getLocation("lobbyspawn",
                new Location(plugin.getServer().getWorlds().get(0), 0, 70, 0, 0, 0));
        // Combat
        deathMessages = config.getStringList("deathMessages");
        if (deathMessages.isEmpty())
            deathMessages = List.of("<green><attacker> <gray>was killed by <red><attacker> <dark_gray>(<red><attackerhealth> ♥<dark_gray>)");
        deathTitles = config.getStringList("deathTitles");
        if (deathTitles.isEmpty())
            deathTitles = List.of("<red><bold>EZ", "<red><bold>GG", "<red><bold>L");
    }
    private void saveConfigYML() {
        // World
        config.set("lobbyspawn", lobbySpawn);
        // Combat
        config.set("deathMessages", deathMessages);
        config.set("deathTitles", deathTitles);

        plugin.saveConfig();
        plugin.reloadConfig();
        this.config = this.plugin.getConfig();
    }

    public static String getRandomDeathMessage() {
        return deathMessages.get(new Random().nextInt(deathMessages.size()));
    }
    public static String getRandomDeathTitle() {
        return deathTitles.get(new Random().nextInt(deathTitles.size()));
    }
}
