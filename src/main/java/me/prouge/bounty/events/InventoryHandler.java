package me.prouge.bounty.events;

import me.prouge.bounty.inventories.BountyInv;
import me.prouge.bounty.managers.BountyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryHandler implements Listener {


    @Inject
    private BountyInv bountyInv;

    @Inject
    private BountyManager bountyManager;

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§c§lKopfgelder") && event.getRawSlot() < 54) {
            event.setCancelled(true);

            if (event.getRawSlot() == 49) {
                bountyInv.openSetBountyInventory((Player) event.getWhoClicked());
                return;
            }

            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();
                bountyInv.openRewardInventory((Player) event.getWhoClicked(),

                        skullMeta.getOwningPlayer().getPlayer());
                bountyInv.openSetRewardInventory((Player) event.getWhoClicked());
                return;
            }
        }


        if (event.getView().getTitle().equals("§c§lKopfgelder > §7auswahl")) {
            event.setCancelled(true);

            if (event.getRawSlot() == 49) {
                bountyInv.openInventoryToPlayer((Player) event.getWhoClicked());
                return;
            }
            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();
                bountyManager.addTemporaryBounty((Player) event.getWhoClicked(),
                        Objects.requireNonNull(Objects.requireNonNull(skullMeta).getOwningPlayer()).getPlayer());

                bountyInv.openSetRewardInventory((Player) event.getWhoClicked());
                return;
            }


        }
        if (event.getView().getTitle().equals("§c§lKopfgelder > §6Reward")) {
            if (event.getRawSlot() >= 45 && event.getRawSlot() < 54) {
                event.setCancelled(true);
            }

            if (event.getRawSlot() == 49) {
                List<ItemStack> itemsToAdd = new ArrayList<>();

                for (int slot = 0; slot <= 44; slot++) {
                    ItemStack item = Objects.requireNonNull(event.getClickedInventory()).getItem(slot);
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        itemsToAdd.add(item);
                    }
                }

                bountyManager.addTemporaryInventory((Player) event.getWhoClicked(),
                        itemsToAdd);
                bountyInv.openConfirmBountyInventory((Player) event.getWhoClicked());
            }

        }

        if (event.getView().getTitle().equals("§c§lKopfgelder > §6Bestätigen")) {
            event.setCancelled(true);
            if (event.getRawSlot() == 12) {
                bountyManager.createBounty((Player) event.getWhoClicked());
                ((Player) event.getWhoClicked()).closeInventory();
            }

            if (event.getRawSlot() == 14) {
                bountyManager.dropTemporaryInventory((Player) event.getWhoClicked());
                ((Player) event.getWhoClicked()).closeInventory();
            }

        }

    }
}
