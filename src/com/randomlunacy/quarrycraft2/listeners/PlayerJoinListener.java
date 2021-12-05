package com.randomlunacy.quarrycraft2.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

public class PlayerJoinListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent ev) {
    ev.getPlayer().sendMessage(QuarryCraft2.messagingPrefix + "Welcome to the server! -QuarryCraft2");
  }
}