package me.prouge.bounty;

import me.prouge.bounty.inject.InjectionModule;
import org.bukkit.plugin.java.JavaPlugin;

public class Bounty extends JavaPlugin {

    @Override
    public void onEnable() {
        new InjectionModule(this);
        registerCommands();
    }

    private void registerCommands(){

    }

}
