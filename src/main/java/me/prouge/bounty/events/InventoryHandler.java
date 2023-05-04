package me.prouge.bounty.events;

import me.prouge.bounty.inventories.BountyInv;
import me.prouge.bounty.managers.BountyManager;
import me.prouge.bounty.utils.BountyPlayer;
import me.prouge.bounty.utils.OrderBy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();


        if (event.getView().getTitle().startsWith("§c§lKopfgelder > Archive") && event.getRawSlot() < 54) {
            event.setCancelled(true);
            int currentPage = Integer.parseInt(event.getView().getTitle().split("§c§lKopfgelder > Archive ")[1]);

            if (event.getRawSlot() == 37) {
                if (event.getClickedInventory().getItem(40).getItemMeta().getDisplayName().equals("§7Aufsteigend nach Datum")) {
                    bountyInv.openArchiveInventory(player, currentPage - 1, OrderBy.DEATH_DATE_ASC);
                } else {
                    bountyInv.openArchiveInventory(player, currentPage - 1, OrderBy.DEATH_DATE_DESC);
                }

            }
            if (event.getRawSlot() == 43) {
                if (event.getClickedInventory().getItem(40).getItemMeta().getDisplayName().equals("§7Aufsteigend nach Datum")) {
                    bountyInv.openArchiveInventory(player, currentPage + 1, OrderBy.DEATH_DATE_ASC);
                } else {
                    bountyInv.openArchiveInventory(player, currentPage + 1, OrderBy.DEATH_DATE_DESC);
                }
            }

            if (event.getRawSlot() == 49) {
                bountyInv.openInventoryToPlayer(player, 1);
            }

            if (event.getRawSlot() == 40) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§7Aufsteigend nach Datum")) {
                    bountyInv.openArchiveInventory(player, currentPage, OrderBy.DEATH_DATE_DESC);
                } else {
                    bountyInv.openArchiveInventory(player, currentPage, OrderBy.DEATH_DATE_ASC);
                }


            }
            return;

        }


        if (event.getView().getTitle().equals("§c§lKopfgelder > §7auswahl")) {
            event.setCancelled(true);

            if (event.getRawSlot() == 49) {
                bountyInv.openInventoryToPlayer(player, 1);
                return;
            }
            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();


                bountyManager.addTemporaryBounty(player, skullMeta.getOwningPlayer().getUniqueId());

                bountyInv.openSetRewardInventory(player);
                return;
            }
            return;

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

                bountyManager.addTemporaryInventory(player,
                        itemsToAdd);
                bountyInv.openConfirmBountyInventory(player);
            }
            return;

        }

        if (event.getView().getTitle().equals("§c§lKopfgelder > §6Bestätigen")) {
            event.setCancelled(true);
            if (event.getRawSlot() == 12) {
                bountyManager.createBounty(player);
                player.closeInventory();
            }

            if (event.getRawSlot() == 14) {
                bountyManager.dropTemporaryInventory(player);
                player.closeInventory();
            }
            return;

        }
        if (event.getView().getTitle().startsWith("§c§lKopfgelder >") && event.getRawSlot() < 54) {
            event.setCancelled(true);
            int currentPage = Integer.parseInt(event.getView().getTitle().split("§c§lKopfgelder > ")[1]);

            if (event.getRawSlot() == 43) {
                bountyInv.openInventoryToPlayer(player, currentPage + 1);

            }


            if (event.getRawSlot() == 37) {
                bountyInv.openInventoryToPlayer(player, currentPage - 1);


            }

            if (event.getRawSlot() == 49) {
                bountyInv.openSetBountyInventory(player);
                return;
            }

            if (event.getRawSlot() == 51) {
                bountyInv.openArchiveInventory(player, 1, OrderBy.DEATH_DATE_ASC);
                return;
            }

            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD && event.isShiftClick()) {
                ItemStack itemstack = event.getCurrentItem();
                List<String> lore = itemstack.getItemMeta().getLore();
                String key = lore.get(lore.size() - 1).substring(4);
                for (BountyPlayer bountyPlayer : bountyManager.getAllBounties()) {
                    if (bountyPlayer.getHash().equals(key) && bountyPlayer.getKiller().toString().equals(player.getUniqueId().toString())) {
                        bountyManager.removeBounty(bountyPlayer);
                        bountyInv.openInventoryToPlayer(player, currentPage);
                        Bukkit.getOnlinePlayers().forEach(p ->
                                p.sendMessage("§8[§cKopfgeld§8] §7» Der Spieler §b" + player.getName() + " hat ein Kopfgeld §czurückgezogen§7!"));
                        return;
                    }

                }


            }

            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();
                if (skullMeta.getOwningPlayer() == null) {
                    return;
                }
                List<String> lore = event.getCurrentItem().getItemMeta().getLore();
                String key = lore.get(lore.size() - 1).substring(4);


                bountyInv.openRewardInventory(player, key);
            }
        }
        if (event.getView().getTitle().startsWith("§c§lKopfgeld > ") && event.getRawSlot() < 54) {
            event.setCancelled(true);
            if (event.getRawSlot() == 49) {
                bountyInv.openInventoryToPlayer(player, 1);
            }
        }

    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {

        if (event.getView().getTitle().equals("§c§lKopfgelder > §6Reward") && !bountyManager.hasTemporaryInventory((Player) event.getPlayer())) {
            bountyManager.removeTemporaryBounty((Player) event.getPlayer());
            for (int i = 0; i < 45; i++) {
                if (event.getInventory().getItem(i) != null && !event.getInventory().getItem(i).getType().equals(Material.AIR)) {
                    event.getPlayer().getInventory().addItem(event.getInventory().getItem(i));
                }
            }

        }
    }


}
