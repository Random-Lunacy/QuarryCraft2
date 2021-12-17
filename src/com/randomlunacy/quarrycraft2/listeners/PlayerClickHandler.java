package com.randomlunacy.quarrycraft2.listeners;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.objects.Messages;
import com.randomlunacy.quarrycraft2.objects.Quarries;
import com.randomlunacy.quarrycraft2.objects.Quarry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
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

    // Handler for sneak left click on chest
    @EventHandler
    public void onPlayerSneakLeftClickChest(PlayerInteractEvent e) {
        // Ignore non-sneak clicks
        Player player = e.getPlayer();
        if (!player.isSneaking())
            return;

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.CHEST)) {

            Chest centreChest = (Chest) e.getClickedBlock().getState();
            if (Quarry.isQuarryLayout(centreChest)) {
                Quarries quarryList = QuarryCraft2.getInstance().getQuarryList();
                if (quarryList.getQuarry(centreChest) != null) {
                    // Toggle Ender mining mode if quarry exists
                    if (!quarryList.getQuarry(centreChest).isMarkedForDeletion())
                        player.sendMessage(quarryList.getQuarry(centreChest).toggleEnderMining());
                    e.setCancelled(true);
                } else {
                    // Attempt to create the quarry
                    createQuarry(player, centreChest, quarryList);
                    e.setCancelled(true);
                }
                quarryList.saveQuarries();
            }
        }
    }

    private void createQuarry(Player player, Chest centreChest, Quarries quarryList) {
        // Check for build permissions
        if (!player.hasPermission(QuarryCraft2.BUILD_QUARRIES_PERMISSION)) {

            player.sendMessage(Messages.getNoBuildPermission());
            return;
        }

        // Check for quarry limit reached
        int quarryLimit = QuarryCraft2.getInstance().getMainConfig().getQuarryLimit();
        if (quarryList.countQuarries(player) >= quarryLimit) {
            // Quarry limit reached
            player.sendMessage(Messages.getQuarryLimitReached(quarryLimit));
            return;
        }

        // Try to Create Quarry
        if (quarryList.addQuarry(centreChest, player.getName())) {
            if (!quarryList.getQuarry(centreChest).isMarkedForDeletion())
                // Create successful
                player.sendMessage(Messages.getQuarryCreated());
        } else {
            // Quarries intersect
            player.sendMessage(Messages.getQuarryIntersectError());
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
