package com.randomlunacy.quarrycraft2.listeners;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.objects.Messages;
import com.randomlunacy.quarrycraft2.objects.Quarries;
import com.randomlunacy.quarrycraft2.objects.Quarry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickHandler implements Listener {
    @EventHandler
    public void onPlayerLeftClick(PlayerInteractEvent e) {
        Quarries quarries = QuarryCraft2.getInstance().getQuarryList();
        int quarryLimit = QuarryCraft2.getInstance().getMainConfig().getQuarryLimit();

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!quarries.canInteract(e.getClickedBlock().getLocation(), e.getPlayer())) {
                e.getPlayer().sendMessage(Messages.getNoInteractPermission());
                e.setCancelled(true);
                return;
            }
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)
                    && !quarries.canBreak(e.getClickedBlock().getLocation(), e.getPlayer())) {
                e.getPlayer().sendMessage(Messages.getBlockCannotBeBroken());
                e.setCancelled(true);
                return;
            }
        }

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block clicked = e.getClickedBlock();
            if (!e.getPlayer().isSneaking()) {
                if (clicked.getType().equals(Material.DIAMOND_BLOCK)
                        && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    for (Quarry q : quarries.getQuarries()) {
                        if (q.isIn3x3(clicked)) {
                            q.togglePause(e.getPlayer());
                            e.setCancelled(true);
                            quarries.saveQuarries();
                            return;
                        }
                    }
                }
                return;
            }

            if (clicked.getType().equals(Material.CHEST)) {
                Chest centreChest = (Chest) clicked.getState();
                if (Quarry.isQuarryLayout(centreChest)) {
                    if (e.getPlayer().hasPermission(QuarryCraft2.BUILD_QUARRIES_PERMISSION)
                            && quarries.countQuarries(e.getPlayer()) < quarryLimit
                            && quarries.addQuarry(centreChest, e.getPlayer().getName())) {
                        if (!quarries.getQuarry(centreChest).isMarkedForDeletion())
                            e.getPlayer().sendMessage(Messages.getQuarryCreated());
                        e.setCancelled(true);
                    } else if (quarries.getQuarry(centreChest) != null) {
                        if (!quarries.getQuarry(centreChest).isMarkedForDeletion())
                            e.getPlayer().sendMessage(quarries.getQuarry(centreChest).toggleEnderMining());
                        e.setCancelled(true);
                    } else if (!e.getPlayer().hasPermission(QuarryCraft2.BUILD_QUARRIES_PERMISSION)) {
                        e.getPlayer().sendMessage(Messages.getNoBuildPermission());
                        e.setCancelled(true);
                    } else if (quarries.countQuarries(e.getPlayer()) >= quarryLimit) {
                        e.getPlayer().sendMessage(Messages.getQuarryLimitReached(quarryLimit));
                        e.setCancelled(true);
                    } else {
                        e.getPlayer().sendMessage(Messages.getQuarryIntersectError());
                        e.setCancelled(true);
                    }
                    quarries.saveQuarries();
                }
            }
            if (clicked.getType().equals(Material.DIAMOND_BLOCK)) {
                for (Quarry q : quarries.getQuarries()) {
                    if (q.isIn3x3(clicked)) {
                        q.resetMiningCursor();
                        e.getPlayer().sendMessage(Messages.getMiningCursorReset(q.getNextY()));
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!e.getPlayer().isSneaking())
                return;
            Block clicked = e.getClickedBlock();
            if (clicked.getType().equals(Material.CHEST)) {
                Chest centreChest = (Chest) clicked.getState();
                if (Quarry.isQuarryLayout(centreChest)) {
                    Quarry q = quarries.getQuarry(centreChest);
                    if (q != null && e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        q.sendProgress(e.getPlayer());
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
