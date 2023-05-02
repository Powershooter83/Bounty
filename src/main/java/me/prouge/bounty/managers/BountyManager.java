package me.prouge.bounty.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.prouge.bounty.Bounty;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.spigotmc.SpigotConfig.config;

@Singleton
public class BountyManager {

    public HashMap<Player, List<ItemStack>> items = new HashMap<>();
    public HashMap<Player, Player> playerBountyTemporary = new HashMap<>();
    @Inject
    private Bounty plugin;

    public List<Player> getAllBounties() {


        List<Player> players = new ArrayList<>();


        System.out.println(config.getConfigurationSection("bounty").getKeys(false));
//        if (section != null) {
//            for (String key : section.getKeys(false)) {
//                System.out.println(key);
//                System.out.println(section.getString(key));
//                String uuidString = section.getString(key);
//             //   UUID uuid = UUID.fromString(uuidString);
//           //     players.add(Bukkit.getPlayer(uuid));
//            }
//        }

        return players;



    }

    public void createBounty(Player player) {
        Player bountyPlayer = playerBountyTemporary.get(player);

        plugin.getConfig().set("bounty." + bountyPlayer.getUniqueId() + ".client",
                player.getUniqueId().toString());
        plugin.getConfig().set("bounty." + bountyPlayer.getUniqueId() + ".items",
                items.get(player));
        plugin.saveConfig();
    }

    public void getRewards() {

    }

    public void addTemporaryBounty(Player player, Player victim){
        playerBountyTemporary.put(player, victim);
    }



    public void addTemporaryInventory(Player player, final List<ItemStack> contents) {
        items.put(player, contents);
    }

    public void dropTemporaryInventory(Player player) {
        items.get(player).forEach(itemStack -> player.getInventory().addItem(itemStack));
        items.remove(player);
    }


}
