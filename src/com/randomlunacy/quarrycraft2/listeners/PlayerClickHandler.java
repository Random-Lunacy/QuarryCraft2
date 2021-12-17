package com.randomlunacy.quarrycraft2.listeners;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.objects.Messages;
import com.randomlunacy.quarrycraft2.objects.Quarry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickHandler implements Listener {
    private boolean hasInteractPermission(PlayerInteractEvent e) {
        boolean permission =
                QuarryCraft2.getInstance().getQuarryList().canInteract(e.getClickedBlock().getLocation(), e.getPlayer());
        if (!permission) {
            e.getPlayer().sendMessage(Messages.getNoInteractPermission());
            e.setCancelled(true);
        }
        return permission;
    }

    private boolean hasBreakPermission(PlayerInteractEvent e) {
        boolean permission =
                QuarryCraft2.getInstance().getQuarryList().canBreak(e.getClickedBlock().getLocation(), e.getPlayer());
        if (!permission) {
            e.getPlayer().sendMessage(Messages.getBlockCannotBeBroken());
            e.setCancelled(true);
        }
        return permission;
    }

    // Handler for toggling Quarry pause state
    @EventHandler
    public void onPlayerLeftClickDiamondBlock(PlayerInteractEvent e) {
        // Test if player is left clicking a diamond block empty handed while not sneaking
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && hasInteractPermission(e) && hasBreakPermission(e)
                && !e.getPlayer().isSneaking() && e.getClickedBlock().getType().equals(Material.DIAMOND_BLOCK)
                && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            for (Quarry q : QuarryCraft2.getInstance().getQuarryList().getQuarries()) {
                if (q.isIn3x3(e.getClickedBlock())) {
                    // Toggle pause state and save
                    q.togglePause(e.getPlayer());
                    QuarryCraft2.getInstance().getQuarryList().saveQuarries();
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    // Handler for resetting mining cursor
    @EventHandler
    public void onPlayerSneakLeftClickDiamondBlock(PlayerInteractEvent e) {
        // Test if player is left clicking a diamond block while sneaking
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && hasInteractPermission(e) && hasBreakPermission(e)
                && e.getPlayer().isSneaking() && e.getClickedBlock().getType().equals(Material.DIAMOND_BLOCK)) {
            for (Quarry q : QuarryCraft2.getInstance().getQuarryList().getQuarries()) {
                if (q.isIn3x3(e.getClickedBlock())) {
                    // Reset cursor and save
                    q.resetMiningCursor();
                    e.getPlayer().sendMessage(Messages.getMiningCursorReset(q.getNextY()));
                    QuarryCraft2.getInstance().getQuarryList().saveQuarries();
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    // Handler for left click on chest
    @EventHandler
    public void onPlayerLeftClickChest(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                Chest centreChest = (Chest) e.getClickedBlock().getState();
                if (Quarry.isQuarryLayout(centreChest)) {
                    if (e.getPlayer().hasPermission(QuarryCraft2.BUILD_QUARRIES_PERMISSION)
                            && QuarryCraft2.getInstance().getQuarryList().countQuarries(e.getPlayer()) < QuarryCraft2
                                    .getInstance().getMainConfig().getQuarryLimit()
                            && QuarryCraft2.getInstance().getQuarryList().addQuarry(centreChest, e.getPlayer().getName())) {
                        // Create Quarry
                        if (!QuarryCraft2.getInstance().getQuarryList().getQuarry(centreChest).isMarkedForDeletion())
                            e.getPlayer().sendMessage(Messages.getQuarryCreated());
                        e.setCancelled(true);
                    } else if (QuarryCraft2.getInstance().getQuarryList().getQuarry(centreChest) != null) {
                        // Toggle Ender mining mode
                        if (!QuarryCraft2.getInstance().getQuarryList().getQuarry(centreChest).isMarkedForDeletion())
                            e.getPlayer().sendMessage(
                                    QuarryCraft2.getInstance().getQuarryList().getQuarry(centreChest).toggleEnderMining());
                        e.setCancelled(true);
                    } else if (!e.getPlayer().hasPermission(QuarryCraft2.BUILD_QUARRIES_PERMISSION)) {
                        // No build permissions
                        e.getPlayer().sendMessage(Messages.getNoBuildPermission());
                        e.setCancelled(true);
                    } else if (QuarryCraft2.getInstance().getQuarryList().countQuarries(e.getPlayer()) >= QuarryCraft2
                            .getInstance().getMainConfig().getQuarryLimit()) {
                        // Quarry limit reached
                        e.getPlayer().sendMessage(Messages
                                .getQuarryLimitReached(QuarryCraft2.getInstance().getMainConfig().getQuarryLimit()));
                        e.setCancelled(true);
                    } else {
                        // Quarries intersect
                        e.getPlayer().sendMessage(Messages.getQuarryIntersectError());
                        e.setCancelled(true);
                    }
            QuarryCraft2.getInstance().getQuarryList().saveQuarries();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSneakRightClickChest(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!e.getPlayer().isSneaking())
                return;
            Block clicked = e.getClickedBlock();
            if (clicked.getType().equals(Material.CHEST)) {
                Chest centreChest = (Chest) clicked.getState();
                if (Quarry.isQuarryLayout(centreChest)) {
                    Quarry q = QuarryCraft2.getInstance().getQuarryList().getQuarry(centreChest);
                    if (q != null && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        q.sendProgress(e.getPlayer());
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
