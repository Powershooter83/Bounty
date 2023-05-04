package me.prouge.bounty;

import me.prouge.bounty.commands.BountyCMD;
import me.prouge.bounty.events.InventoryHandler;
import me.prouge.bounty.events.PlayerDeathEvent;
import me.prouge.bounty.inject.InjectionModule;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

import static org.spigotmc.SpigotConfig.config;

@Singleton
public class Bounty extends JavaPlugin {

    @Inject
    private BountyCMD bountyCMD;

    @Inject
    private InventoryHandler inventoryHandler;

    @Inject
    private PlayerDeathEvent playerDeathEvent;


    @Override
    public void onEnable() {
        new InjectionModule(this);
        registerCommands();

        config.options().copyDefaults(true);
        saveConfig();

        getServer().getPluginManager().registerEvents(inventoryHandler, this);
        getServer().getPluginManager().registerEvents(playerDeathEvent, this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("bounty")).setExecutor(bountyCMD);
    }


}
