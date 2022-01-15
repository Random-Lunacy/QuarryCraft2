package com.randomlunacy.quarrycraft2.commands;

import com.randomlunacy.quarrycraft2.objects.GuideBook;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuideCommand implements CommandExecutor
{
    private GuideBook gbGiver = new GuideBook();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {
        if (sender instanceof Player p)
        {
            gbGiver.giveGuideBook(p);
        }
        return true;
    }
}
