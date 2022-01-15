package com.randomlunacy.quarrycraft2.commands;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.objects.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {

    /**
     * @param sender Entity which used the command.
     * @param Command Object with details about the executed command
     * @param String label String with the command run, without the passed arguments.
     * @param String args The arguments passed by the player. Each space in the command counts as a new argument.
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Messages.getReloadingConfig());
        QuarryCraft2.getInstance().reloadConfig();
        return true;
    }

}
