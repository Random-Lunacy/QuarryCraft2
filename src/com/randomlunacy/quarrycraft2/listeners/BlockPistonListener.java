package com.randomlunacy.quarrycraft2.listeners;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockPistonListener implements Listener
{
    @EventHandler
    public void pistonRetractEvent(BlockPistonRetractEvent e)
    {
        Location pLoc = e.getBlock().getLocation();
        if (!QuarryCraft2.getInstance().getQuarryList().pistonAllowed(pLoc.getWorld(), pLoc.getBlockX(), pLoc.getBlockY(),
                pLoc.getBlockZ()))
        {
            e.setCancelled(true);
        }
    }

    /**
     * @param e
     */
    @EventHandler
    public void pistonExtendEvent(BlockPistonExtendEvent e)
    {
        Location pLoc = e.getBlock().getLocation();
        if (!QuarryCraft2.getInstance().getQuarryList().pistonAllowed(pLoc.getWorld(), pLoc.getBlockX(), pLoc.getBlockY(),
                pLoc.getBlockZ()))
        {
            e.setCancelled(true);
        }
    }
}
