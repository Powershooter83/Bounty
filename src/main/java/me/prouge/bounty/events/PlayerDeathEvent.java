package me.prouge.bounty.events;

import com.google.inject.Inject;
import me.prouge.bounty.Bounty;
import me.prouge.bounty.managers.BountyManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlayerDeathEvent implements Listener {

    @Inject
    private Bounty plugin;

    @Inject
    private BountyManager bountyManager;

    @EventHandler
    public void onPlayerDeathEvent(org.bukkit.event.entity.PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        if (bountyManager.getAllBounties().stream().filter(bountyPlayer -> bountyPlayer.getUuid().toString().equals(event.getEntity().getUniqueId().toString())).count() == 0) {
            return;
        }


        Location deathLocation = event.getEntity().getLocation();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

        bountyManager.getRewards(event.getEntity().getPlayer().getUniqueId()).forEach(reward -> deathLocation.getWorld().dropItemNaturally(deathLocation, reward));


        List<String> players = new ArrayList<>();
        for (String key : plugin.getConfig().getConfigurationSection("bounty").getKeys(false)) {
            if (plugin.getConfig().get("bounty." + key + ".victim").equals(event.getEntity().getUniqueId().toString())) {
                players.add((String) plugin.getConfig().get("bounty." + key + ".client"));
                plugin.getConfig().set("bounty." + key, null);
            }
        }

        int entry = plugin.getConfig().getStringList("archive").size() + 1;
        plugin.getConfig().set("archive." + entry + ".killer", event.getEntity().getKiller().getUniqueId().toString());
        plugin.getConfig().set("archive." + entry + ".clients", players);
        plugin.getConfig().set("archive." + entry + ".victim", event.getEntity().getUniqueId().toString());
        plugin.getConfig().set("archive." + entry + ".killDate", now.format(formatter));
        plugin.getConfig().set("archive." + entry + ".killWeapon", event.getEntity().getKiller().getItemInUse());
        plugin.saveConfig();


        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, "§8[§cKopfgeld§8] §7» §b" + event.getEntity().getKiller().getName() + " §7hat sich das Kopfgeld von §c" + event.getEntity().getName() + " §7geschnappt!"));


    }

    public void sendMessage(Player player, String message) {
        ComponentBuilder builder = new ComponentBuilder(message).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(player.getLocation().toString()).append("\n").create()));

        player.spigot().sendMessage(builder.create());
    }


}
