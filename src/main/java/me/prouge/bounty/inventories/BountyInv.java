package me.prouge.bounty.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class BountyInv {

    public void openInventoryToPlayer(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Spielerkopf-Inventar");
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        assert skullMeta != null;
        skullMeta.setOwningPlayer(player);
        skullItem.setItemMeta(skullMeta);
        inventory.setItem(0, skullItem);
        player.openInventory(inventory);
    }


}
