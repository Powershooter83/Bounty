package me.prouge.bounty.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.prouge.bounty.Bounty;
import me.prouge.bounty.utils.BountyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Singleton
public class BountyManager {

    public HashMap<Player, List<ItemStack>> items = new HashMap<>();
    public HashMap<Player, UUID> playerBountyTemporary = new HashMap<>();
    @Inject
    private Bounty plugin;

    public List<BountyPlayer> getAllBounties() {
        List<BountyPlayer> players = new ArrayList<>();


        if (plugin.getConfig().getConfigurationSection("bounty") == null) {
            return players;
        }

        for (String key : plugin.getConfig().getConfigurationSection("bounty").getKeys(false)) {
            UUID victim = UUID.fromString(plugin.getConfig().get("bounty." + key + ".victim").toString());
            UUID killer = UUID.fromString(plugin.getConfig().get("bounty." + key + ".client").toString());
            players.add(new BountyPlayer(victim, killer, null, null, key));
        }
        return players;
    }

    public void removeBounty(BountyPlayer bounty) {
        System.out.println(bounty.getHash());
        plugin.getConfig().set("bounty." + bounty.getHash(), null);
        plugin.saveConfig();
    }


    public List<BountyPlayer> getArchiveBounties() {
        List<BountyPlayer> players = new ArrayList<>();

        if (plugin.getConfig().getConfigurationSection("archive") == null) {
            return players;
        }

        for (String key : plugin.getConfig().getConfigurationSection("archive").getKeys(false)) {
            UUID uuid = UUID.fromString(plugin.getConfig().get("archive." + key + ".victim").toString());

            List<String> clients = new ArrayList<>();
            for (Object o : plugin.getConfig().getList("archive." + key + ".clients")) {
                clients.add(String.valueOf(o));
            }

            players.add(new BountyPlayer(uuid,
                    UUID.fromString(plugin.getConfig().get("archive." + key + ".killer").toString()),
                    LocalDateTime.parse(plugin.getConfig().get("archive." + key + ".killDate").toString(),
                            DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")), clients, null));
        }
        return players;
    }


    public void createBounty(Player player) {
        UUID bountyPlayer = playerBountyTemporary.get(player);
        ConfigurationSection bountySection = plugin.getConfig().getConfigurationSection("bounty");
        if (bountySection == null) {
            bountySection = plugin.getConfig().createSection("bounty");
        }

        String hash = generateRandomHash();
        while(bountySection.getKeys(false).contains(hash)) {
            hash = generateRandomHash();
        }



        ConfigurationSection newBountySection = bountySection.createSection(hash);

        newBountySection.set("victim", bountyPlayer.toString());
        newBountySection.set("client", player.getUniqueId().toString());
        newBountySection.set("items", items.get(player));

        plugin.saveConfig();
    }

    public String generateRandomHash() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public List<ItemStack> getRewards(UUID victim) {
        List<ItemStack> itemStacks = new ArrayList<>();

        for (String key : plugin.getConfig().getConfigurationSection("bounty").getKeys(false)) {
            if (plugin.getConfig().get("bounty." + key + ".victim").equals(victim.toString())) {
                plugin.getConfig().getList("bounty." + key + ".items").forEach(itemStack -> itemStacks.add((ItemStack) itemStack));
            }
        }
        return itemStacks;
    }

    public void addTemporaryBounty(Player player, UUID victim) {
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
