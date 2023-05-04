package me.prouge.bounty.commands;

import me.prouge.bounty.inventories.BountyInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class BountyCMD implements CommandExecutor {

    @Inject
    private BountyInv bountyInv;

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        bountyInv.openInventoryToPlayer((Player) sender, 1);
        return false;
    }
}
