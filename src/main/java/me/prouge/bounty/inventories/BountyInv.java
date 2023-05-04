package me.prouge.bounty.inventories;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.prouge.bounty.managers.BountyManager;
import me.prouge.bounty.utils.BountyPlayer;
import me.prouge.bounty.utils.Heads;
import me.prouge.bounty.utils.ItemBuilder;
import me.prouge.bounty.utils.OrderBy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BountyInv {

    @Inject
    private BountyManager bountyManager;


    public void openArchiveInventory(Player player, int page, OrderBy order) {
        int pageSize = 21;
        int start = (page - 1) * pageSize;
        int end = start + pageSize;

        List<BountyPlayer> archiveBounties = bountyManager.getArchiveBounties();

        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder > Archive " + page);


        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        if (archiveBounties.size() == 0) {
            inventory.setItem(22, new ItemBuilder(createCustomSkull(Heads.ZERO.texture))
                    .setName("§7Es gibt noch keine ausgeführten Aufträge...").toItemStack());
        } else {
            switch (order) {
                case DEATH_DATE_ASC -> {
                    inventory.setItem(40, new ItemBuilder(createCustomSkull(Heads.ARROW_UP.texture)).setName("§7Aufsteigend nach Datum").toItemStack());
                    Collections.sort(archiveBounties, Comparator.comparing(BountyPlayer::getKillDate));
                }
                case DEATH_DATE_DESC -> {
                    inventory.setItem(40,
                            new ItemBuilder(createCustomSkull(Heads.ARROW_DOWN.texture))
                                    .setName("§7Absteigend nach Datum").toItemStack());
                    Collections.sort(archiveBounties, Comparator.comparing(BountyPlayer::getKillDate).reversed());
                }
                default -> {
                }
            }
        }


        int slot = 10;
        for (int i = start; i < end && i < archiveBounties.size(); i++) {
            OfflinePlayer bountyPlayer = Bukkit.getOfflinePlayer(archiveBounties.get(i).getUuid());
            List<String> auftrag = new ArrayList<>();
            auftrag.add("§8» §7Getötet von §c" + Bukkit.getOfflinePlayer(archiveBounties.get(i).getKiller()).getName());
            int index = 0;
            for (String client : archiveBounties.get(i).getClients()) {
                if (index != 0) {
                    auftrag.add("                    §8→ §c" + Bukkit.getOfflinePlayer(UUID.fromString(client)).getName());
                }
                if (index == 0) {
                    auftrag.add("§8» §7Im Auftrag von §c" + Bukkit.getOfflinePlayer(UUID.fromString(client)).getName());
                }
                index++;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
            String formattedDate = archiveBounties.get(i).getKillDate().format(formatter);
            auftrag.add("§8» §7Getötet am §c" + formattedDate);

            inventory.setItem(slot,
                    new ItemBuilder(getPlayerSkull(bountyPlayer))
                            .setName("§b" + bountyPlayer.getName())
                            .setLore(auftrag)
                            .toItemStack()


            );
            if (slot == 16 || slot == 25 || slot == 34) {
                slot += 3;
            } else {
                slot++;
            }
        }

        if (page > 1) {
            inventory.setItem(37, new ItemBuilder(createCustomSkull(Heads.ARROW_LEFT.texture))
                    .setName("§cVorherige Seite").toItemStack());
        }

        if (end < archiveBounties.size()) {
            inventory.setItem(43, new ItemBuilder(createCustomSkull(Heads.ARROW_RIGHT.texture))
                    .setName("§cNächste Seite").toItemStack());
        }


        inventory.setItem(49, new ItemBuilder(createCustomSkull(Heads.BACK.texture))
                .setName("§cZurück §7{Rechtsklick}").toItemStack());
        player.openInventory(inventory);
    }


    public void openInventoryToPlayer(final Player player, final int page) {
        int pageSize = 21;
        int start = (page - 1) * pageSize;
        int end = start + pageSize;

        List<BountyPlayer> archiveBounties = bountyManager.getAllBounties();


        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder > " + page);
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        if (bountyManager.getAllBounties().size() == 0) {

            inventory.setItem(22, new ItemBuilder(createCustomSkull(Heads.ZERO.texture))
                    .setName("§7Zurzeit hat es keine Kopfgeld aufträge...").toItemStack());
        }

        int slot = 10;
        for (int i = start; i < end && i < archiveBounties.size(); i++) {
            if (archiveBounties.get(i).getKiller().toString().equals(player.getUniqueId().toString())) {
                inventory.setItem(slot, new ItemBuilder(getPlayerSkull(Bukkit.getOfflinePlayer(archiveBounties.get(i).getUuid())))
                        .setName("§b" + Bukkit.getOfflinePlayer(archiveBounties.get(i).getUuid()).getName())
                        .setLore("§8» §7Auftrag von: §c" + Bukkit.getOfflinePlayer(UUID.fromString(archiveBounties.get(i).getKiller().toString())).getName(),
                                "", "§7{Rechtsklick} um das Kopfgeld anzusehen!",
                                "§7{Shift + Rechtsklick} um das Kopfgeld zu §c§lLÖSCHEN!", "§8§o" + archiveBounties.get(i).getHash())

                        .toItemStack());
            } else {
                inventory.setItem(slot, new ItemBuilder(getPlayerSkull(Bukkit.getOfflinePlayer(archiveBounties.get(i).getUuid())))
                        .setName("§b" + Bukkit.getOfflinePlayer(archiveBounties.get(i).getUuid()).getName())
                        .setLore("§8» §7Auftrag von: §c" + Bukkit.getOfflinePlayer(UUID.fromString(archiveBounties.get(i).getKiller().toString())).getName(),
                                "", "§7{Rechtsklick} um das Kopfgeld anzusehen!", "§8§o" + archiveBounties.get(i).getHash())
                        .toItemStack());
            }

            if (slot == 16 || slot == 25 || slot == 34) {
                slot += 3;
            } else {
                slot++;
            }

        }

        if (page > 1) {
            inventory.setItem(37, new ItemBuilder(createCustomSkull(Heads.ARROW_LEFT.texture))
                    .setName("§cVorherige Seite").toItemStack());
        }

        if (end < archiveBounties.size()) {
            inventory.setItem(43, new ItemBuilder(createCustomSkull(Heads.ARROW_RIGHT.texture))
                    .setName("§cNächste Seite").toItemStack());
        }


        long actuelBounties = bountyManager
                .getAllBounties()
                .stream()
                .filter(bountyPlayer -> bountyPlayer.getUuid().toString().equals(player.getUniqueId().toString())).count();

        List<BountyPlayer> bountyPlayers = bountyManager.getArchiveBounties();

        long bountyDeaths = bountyPlayers.stream().
                filter(bountyPlayer -> bountyPlayer.getUuid().toString().equals(player.getUniqueId().toString())).count();

        long bountyKills = bountyPlayers.stream().
                filter(bountyPlayer -> bountyPlayer.getKiller().toString().equals(player.getUniqueId().toString())).count();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

        LocalDateTime newestDate = bountyPlayers.stream()
                .map(BountyPlayer::getKillDate)
                .max(Comparator.naturalOrder())
                .orElse(null);

        String formattedDate = newestDate != null ? newestDate.format(formatter) : "Kein Datum gefunden.";

        inventory.setItem(47, new ItemBuilder(createCustomSkull(Heads.PLAYER.texture))
                .setName("§cStats §7{Hover}")
                .setLore("§8» §7Kopfgelder auf dich: §c" + actuelBounties,
                        "§8» §7Anzahl Kopfgeldmorde: §c" + bountyKills,
                        "§8» §7Anzahl Kopfgeldtode: §c" + bountyDeaths,
                        "§8» §7Kopfgelder (all-time): §c" + (bountyDeaths + actuelBounties),
                        "§8» §7Letzer Kopfgeld Tod: §c" + formattedDate
                ).toItemStack());
        inventory.setItem(49, new ItemBuilder(createCustomSkull(Heads.CREATE.texture))
                .setName("§cKopfgeld platzieren §7{Rechtsklick}")
                .setLore("§8➥ §7Setze ein Kopfgeld, sowie eine Belohnung",
                        "   §7auf einen Spieler aus, um andere Spieler",
                        "   §7anzulocken, die ihn versuchen zu töten!",
                        "",
                        "§c§lACHTUNG", "§cEin platziertes Kopfgeld kann entfernt werden,", "§cdie gesetzten Items gehen jedoch verloren!").toItemStack());
        inventory.setItem(51, new ItemBuilder(createCustomSkull(Heads.ARCHIVE.texture))
                .setName("§cArchiv §7{Rechtsklick}")
                .setLore("§8➥ §7Siehe alle vergangenen",
                        "   §7Kopfgeldaufträge im Detail",
                        "   §7nochmals an!").toItemStack());
        player.openInventory(inventory);
    }

    public void openSetBountyInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder > §7auswahl");

        int index = 10;
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            inventory.setItem(index, new ItemBuilder(getPlayerSkull(p)).setName("§b" + p.getName()).toItemStack());
            index++;
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        inventory.setItem(49, new ItemBuilder(createCustomSkull(Heads.BACK.texture
        )).setName("§7Zurück").toItemStack());
        player.openInventory(inventory);

    }

    public void openSetRewardInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder > §6Reward");

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        inventory.setItem(49, new ItemBuilder(createCustomSkull(Heads.SAVE.texture)).setName("§6Speichern").toItemStack());
        player.openInventory(inventory);
    }

    public void openConfirmBountyInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§c§lKopfgelder > §6Bestätigen");

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }


        inventory.setItem(12, new ItemBuilder(createCustomSkull(Heads.CONFIRM.texture)).setName("§eBestätigen").toItemStack());
        inventory.setItem(14, new ItemBuilder(createCustomSkull(Heads.CANCEL.texture)).setName("§cAbbrechen").toItemStack());
        player.openInventory(inventory);

    }

    public void openRewardInventory(Player player, String hash) {
        BountyPlayer bountyPlayerFound = null;
        for (BountyPlayer bountyPlayer : bountyManager.getAllBounties()) {
            if (bountyPlayer.getHash().equals(hash)) {
                bountyPlayerFound = bountyPlayer;
                break;
            }
        }
        if (bountyPlayerFound == null) {
            return;
        }


        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgeld > §b" + Bukkit.getOfflinePlayer(bountyPlayerFound.getUuid()).getName());
        bountyManager.getRewards(hash).forEach(inventory::addItem);

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        inventory.setItem(49, new ItemBuilder(createCustomSkull(Heads.BACK.texture))
                .setName("§cZurück §7{Rechtsklick}").toItemStack());

        player.openInventory(inventory);
    }


    private ItemStack getPlayerSkull(Player player) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        assert skullMeta != null;
        skullMeta.setOwningPlayer(player);
        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }

    private ItemStack getPlayerSkull(OfflinePlayer player) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        assert skullMeta != null;
        skullMeta.setOwningPlayer(player);
        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }

    private ItemStack createCustomSkull(String texture) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skullItem.setItemMeta(skullMeta);
        return skullItem;
    }


}
