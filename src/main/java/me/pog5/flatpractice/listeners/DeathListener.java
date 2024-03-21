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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

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
        final User victimUser = plugin.getUserManager().getUser(victim.getUniqueId());
        final User attackerUser = plugin.getUserManager().getUser(attacker.getUniqueId());
        final Component deathMessage = MiniMessage.miniMessage().deserialize(attackerUser.preferredKillMessage,
                Placeholder.unparsed("attacker", attackerUser.getName()),
                Placeholder.unparsed("victim", victimUser.getName()),
                Placeholder.unparsed("attackerhealth", String.valueOf(attacker.getHealth()))
        );
        final Location deathPos = victim.getLocation().clone();
        final PlayerInventory victimInventory = victim.getInventory();

        event.setCancelled(true);
        attackerUser.addKill(victimUser);
        victimUser.addDeath(attackerUser);
        victimUser.isDead = true;

        for (Player npc : deathPos.getNearbyPlayers(100, 250))
            if (plugin.getUserManager().getUser(npc.getUniqueId()).deathMessagesEnabled)
                npc.sendMessage(deathMessage);
        for (ItemStack item : victimInventory.getContents())
            if (item != null && !item.containsEnchantment(Enchantment.VANISHING_CURSE))
                deathPos.getWorld().dropItemNaturally(deathPos, item.clone());
        victim.setGameMode(GameMode.SPECTATOR);
        victim.setLastDeathLocation(deathPos);
        try {
            victim.setHealth(Objects.requireNonNull(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        } catch (NullPointerException ignored) {
            victim.setHealth(20);
        }
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

