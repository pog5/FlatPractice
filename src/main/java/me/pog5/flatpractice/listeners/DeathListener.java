package me.pog5.flatpractice.listeners;

import me.pog5.flatpractice.FlatPractice;
import me.pog5.flatpractice.objects.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private final FlatPractice plugin;
    public DeathListener(FlatPractice plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (event.deathMessage() == null) return;
        final Player victim = event.getEntity();
        if (victim.getKiller() == null) return;
        final Player attacker = victim.getKiller();
        final User victimUser = plugin.getUserManager().getUserFromUUID(victim.getUniqueId());
        final User attackerUser = plugin.getUserManager().getUserFromUUID(attacker.getUniqueId());
        final Component deathMessage = MiniMessage.miniMessage().deserialize(attackerUser.preferredKillMessage,
                Placeholder.unparsed("attacker", attackerUser.getName()),
                Placeholder.unparsed("victim", victimUser.getName()),
                Placeholder.unparsed("attackerhealth", String.valueOf(attacker.getHealth()))
        );
        final Location deathPos = victim.getLocation();

        attackerUser.addKill(victimUser);
        victimUser.addDeath(attackerUser);
        for (Player npc : deathPos.getNearbyPlayers(100, 250)) {
            if (plugin.getUserManager().getUserFromUUID(npc.getUniqueId()).deathMessagesEnabled)
                npc.sendMessage(deathMessage);
        }
        event.setCancelled(true);
        victimUser.isDead = true;
        victim.setGameMode(GameMode.ADVENTURE);
        victim.setLastDeathLocation(deathPos);
        victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        victim.setFoodLevel(20);
        victim.getInventory().clear();
        victim.updateInventory();
        victim.clearActivePotionEffects();
        if (victimUser.deathTitlesEnabled) {
            final Title deathTitle = Title.title(MiniMessage.miniMessage().deserialize(attackerUser.preferredDeathTitle), deathMessage);
            victim.showTitle(deathTitle);
        }
    }
}

