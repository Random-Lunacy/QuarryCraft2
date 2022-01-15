package com.randomlunacy.quarrycraft2.listeners;

import com.randomlunacy.quarrycraft2.objects.Messages;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev)
    {
        ev.getPlayer().sendMessage(Messages.getWelcome());
    }
}
