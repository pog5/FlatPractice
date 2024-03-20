package me.pog5.flatpractice.listeners;

import me.pog5.flatpractice.FlatPractice;
import me.pog5.flatpractice.objects.User;
import me.pog5.flatpractice.objects.Variables;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        event.deathMessage(MiniMessage.miniMessage().deserialize(Variables.getRandomDeathMessage(),
                Placeholder.unparsed("attacker", victim.getKiller().getName()),
                Placeholder.unparsed("victim", event.getEntity().getName()),
                Placeholder.unparsed("attackerhealth", String.valueOf(attacker.getHealth()))
        ));

        final User victimUser = plugin.getUserManager().getUserFromUUID(victim.getUniqueId());
        final User attackerUser = plugin.getUserManager().getUserFromUUID(attacker.getUniqueId());
        attackerUser.addKill(victimUser);
        victimUser.addDeath(attackerUser);
    }
}
