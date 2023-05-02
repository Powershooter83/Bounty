package me.prouge.bounty.managers;

import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@Singleton
public class BountyManager {

    public HashMap<Player, List<ItemStack>> items = new HashMap<>();


    public void getAllBounties() {

    }

    public void createBounty() {

    }

    public void getRewards() {

    }

    public void addTemporaryInventory(Player player, final List<ItemStack> contents) {
        items.put(player, contents);
    }

    public void dropTemporaryInventory(Player player) {
        items.get(player).forEach(itemStack -> player.getInventory().addItem(itemStack));
        items.remove(player);
    }




}
