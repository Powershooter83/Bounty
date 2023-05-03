package me.prouge.bounty.inventories;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.prouge.bounty.managers.BountyManager;
import me.prouge.bounty.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.UUID;

public class BountyInv {

    @Inject
    private BountyManager bountyManager;


    public void openInventoryToPlayer(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder");
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        if (bountyManager.getAllBounties().size() == 0) {
            inventory.setItem(22, new ItemBuilder(createCustomSkull(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3Jh" +
                            "ZnQubmV0L3RleHR1cmUvMGViZTdlNTIxN" +
                            "TE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIxMDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ"
            ))
                    .setName("§7Zurzeit hat es keine Kopfgeld aufträge...").toItemStack());
        }

        int slot = 0;
        for (Player bountyPlayer : bountyManager.getAllBounties()) {
            inventory.setItem(slot, getPlayerSkull(bountyPlayer));
            slot++;
        }


        inventory.setItem(49, new ItemBuilder(createCustomSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90Z" +
                        "Xh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyM" +
                        "Dk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZ" +
                        "TAyNzgwMTQyZjcxNiJ9fX0"))
                .setName("§cKopfgeld platzieren §7{Rechtsklick}")
                .setLore("§8➥ §7Setze ein Kopfgeld, sowie eine Belohnung",
                        "   §7auf einen Spieler aus, um andere Spieler",
                        "   §7anzulocken, die ihn versuchen zu töten!",
                        "",
                        "§c§lACHTUNG", "§cEin platziertes Kopfgeld kann entfernt werden,", "§cdie gesetzten Items gehen jedoch verloren!").toItemStack());
        player.openInventory(inventory);
    }

    public void openSetBountyInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder > §7auswahl");

        int index = 10;
        for (Player p : Bukkit.getOnlinePlayers()) {
            inventory.setItem(index, getPlayerSkull(p));
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        inventory.setItem(49, new ItemBuilder(createCustomSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90Z" +
                        "Xh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY1MmUyY" +
                        "jkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3M" +
                        "zRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0="
        )).setName("§7Zurück").toItemStack());
        player.openInventory(inventory);

    }

    public void openSetRewardInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§c§lKopfgelder > §6Reward");

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }

        inventory.setItem(49, new ItemBuilder(createCustomSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJ" +
                        "lcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMwZjQ1MzdkMjE0ZDM4NjY2ZTYzM" +
                        "DRlOWM4NTFjZDZmN2U0MWEwZWI3YzI1MDQ5YzlkMjJjOGM1ZjY1NDV" +
                        "kZiJ9fX0=")).setName("§6Speichern").toItemStack());
        player.openInventory(inventory);
    }

    public void openConfirmBountyInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§c§lKopfgelder > §6Bestätigen");

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§b").toItemStack());
        }


        inventory.setItem(12, new ItemBuilder(createCustomSkull(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh" +
                        "0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk5ODBjMWQyMTE4MDlhO" +
                        "WI2NTY1MDg4ZjU2YTM4ZjJlZjQ5MTE1YzEwN" +
                        "TRmYTY2MjQ1MTIyZTllZWVkZWNjMiJ9fX0")).setName("§eBestätigen").toItemStack());
        inventory.setItem(14, new ItemBuilder(createCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6" +
                "Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTljZGI5YWYzOGNm" +
                "NDFkYWE1M2JjOGNkYTc2NjVjNTA5NjMyZDE0ZTY3OGYwZjE5ZjI2M2Y0NmU1NDFkOGEzMCJ9fX0")).setName("§cAbbrechen").toItemStack());
        player.openInventory(inventory);

    }

    public void openRewardInventory(Player player, Player victim) {

    }


    private ItemStack getPlayerSkull(Player player) {
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
