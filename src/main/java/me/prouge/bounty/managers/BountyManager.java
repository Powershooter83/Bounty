package me.prouge.bounty.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.prouge.bounty.Bounty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Singleton
public class BountyManager {

    public HashMap<Player, List<ItemStack>> items = new HashMap<>();
    public HashMap<Player, Player> playerBountyTemporary = new HashMap<>();
    @Inject
    private Bounty plugin;

    public List<Player> getAllBounties() {
        List<Player> players = new ArrayList<>();

        for (String key : plugin.getConfig().getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            players.add(Bukkit.getPlayer(uuid));
        }
        return players;
    }

    public void createBounty(Player player) {
        Player bountyPlayer = playerBountyTemporary.get(player);

        plugin.getConfig().set(bountyPlayer.getUniqueId() + ".client",
                player.getUniqueId().toString());
        plugin.getConfig().set(bountyPlayer.getUniqueId() + ".items",
                items.get(player));
        plugin.saveConfig();
    }

    public List<ItemStack> getRewards(Player victim) {
        List<ItemStack> itemStacks = new ArrayList<>();

        plugin.getConfig().getList(victim.getUniqueId() + ".items").forEach(itemStack -> itemStacks.add((ItemStack) itemStack));

        return itemStacks;
    }

    public void addTemporaryBounty(Player player, Player victim) {
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
