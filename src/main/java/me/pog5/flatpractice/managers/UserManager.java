package me.pog5.flatpractice.managers;

import me.pog5.flatpractice.FlatPractice;
import me.pog5.flatpractice.objects.User;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {
    private final FlatPractice plugin;
    private WeakHashMap<Player, Boolean> userQueue; // Player, then if it should remove
    public HashMap<UUID, User> users;
    public WeakHashMap<String, User> usersByName;
    public UserManager(FlatPractice plugin) {
        this.plugin = plugin;
    }

    public void addPlayerToUsers(Player player) {
        if (!userQueue.containsKey(player)) userQueue.put(player, false);
    }
    public void removeUser(User user) {
        UUID uuid = user.getUUID();
        String name = user.getName();
        if (users.containsValue(user))
            users.remove(uuid);
        if (usersByName.containsValue(user))
            usersByName.remove(name);
    }
    public User getUser(UUID uuid) {
        return users.get(uuid);
    }
    public User getUser(String name) {
        return usersByName.get(name);
    }
    public void refreshUsers() {
        // Process userQueue to add the new pending users to the users list
        for (Iterator<Map.Entry<Player, Boolean>> iterator = userQueue.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Player, Boolean> entry = iterator.next();
            Player p = entry.getKey();
            boolean shouldRemove = entry.getValue();

            if (!p.isOnline()) {
                if (shouldRemove) {
                    iterator.remove(); // Remove from userQueue
                }
                continue;
            }

            UUID uuid = p.getUniqueId();
            String name = p.getName();

            if (!users.containsKey(uuid))
                users.put(uuid, new User(p)); // Assuming User constructor takes Player
            if (!usersByName.containsKey(name))
                usersByName.put(name, new User(p));

            if (shouldRemove) {
                iterator.remove(); // Remove from userQueue
            }
        }

        // Remove invalid players from users and usersByName list
        List<UUID> invalidUUIDsToRemove = new ArrayList<>();
        for (Iterator<Map.Entry<UUID, User>> iterator = users.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<UUID, User> entry = iterator.next();
            UUID uuid = entry.getKey();
            User user = entry.getValue();

            if (user.getPlayer() == null) {
                iterator.remove(); // Remove from users
                invalidUUIDsToRemove.add(uuid);
            }
        }
        invalidUUIDsToRemove.forEach(users::remove);

        List<String> invalidNamesToRemove = new ArrayList<>();
        for (Iterator<Map.Entry<String, User>> iterator = usersByName.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, User> entry = iterator.next();
            User user = entry.getValue();

            if (user.getPlayer() == null) {
                iterator.remove(); // Remove from usersByName
                invalidNamesToRemove.add(user.getName());
            }
        }
        invalidNamesToRemove.forEach(usersByName::remove);
    }

}
