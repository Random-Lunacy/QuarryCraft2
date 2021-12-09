package com.randomlunacy.quarrycraft2.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.handlers.GriefPreventionHandler;
import com.randomlunacy.quarrycraft2.handlers.WorldGuardHandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Quarry extends BukkitRunnable {
    Location centreChestLocation;
    Chest centreChest;
    float storedEnergy;
    int minX;
    int minZ;
    int maxX;
    int maxZ;
    int nextX;
    int nextY;
    int nextZ;
    boolean classicMode = true;
    boolean enderReplaceDirt = false;
    boolean alerted = false;
    boolean markedForDeletion = false;
    World world;
    int blocksPerTick = 1;
    int miningDelay = 20;
    int counter = 0;
    String owner;
    boolean paused;
    boolean finished;
    int worldBottom = -64;

    int emeraldBlocks = 0;
    int diamondBlocks = 0;
    int goldBlocks = 0;
    boolean platformDone;
    boolean clearedPlatform;
    int platX;
    int platZ;

    float energyMod = 1.0f;

    // TODO: Make this configureable
    static Material[] ignored =
            {Material.BEDROCK, Material.AIR, Material.WATER, Material.LAVA, Material.GRASS, Material.GRASS_BLOCK,
                    Material.DIRT_PATH, Material.STONE, Material.COBBLESTONE, Material.DIRT, Material.COARSE_DIRT};

    // TODO: Add command to let other players use quarry

    public Quarry(Chest centreChest, String owner) {
        this.owner = owner;
        this.centreChest = centreChest;
        this.centreChestLocation = centreChest.getLocation();
        world = centreChest.getWorld();
        storedEnergy = 0;
        setBounds();
        if (getArea() > QuarryCraft2.getInstance().getMainConfig().getMaxQuarryWidth()
                * QuarryCraft2.getInstance().getMainConfig().getMaxQuarryLength()) {
            tellOwner(Messages.getQuarryOversized(QuarryCraft2.getInstance().getMainConfig().getMaxQuarryWidth(),
                    QuarryCraft2.getInstance().getMainConfig().getMaxQuarryLength()));
            int xRad = (int) ((Math.sqrt(QuarryCraft2.getInstance().getMainConfig().getMaxQuarryWidth()
                    * QuarryCraft2.getInstance().getMainConfig().getMaxQuarryLength()) - 1) / 2);
            int zRad = xRad;
            int cx = centreChestLocation.getBlockX();
            int cz = centreChestLocation.getBlockZ();

            minX = cx - xRad;
            maxX = cx + xRad;
            minZ = cz - zRad;
            maxZ = cz + zRad;

            nextX = minX;
            nextY = centreChestLocation.getBlockY() - 2;
            nextZ = minZ;
        }
        if (QuarryCraft2.getInstance().getMainConfig().isWorldGuardProtectionEnabled())
            doWGProtection();
        if (QuarryCraft2.getInstance().getMainConfig().isGriefPreventionProtectionEnabled())
            doGPProtection();
        clearedPlatform = false;
        platX = minX - 1;
        platZ = minZ - 1;
        paused = false;
        finished = false;
    }

    public Quarry(Chest centreChest, int minX, int maxX, int minZ, int maxZ, boolean mode, String owner) {
        this(centreChest, owner);
        classicMode = mode;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        nextX = minX;
        nextY = centreChestLocation.getBlockY() - 2;
        nextZ = minZ;
        platX = minX - 1;
        platZ = minZ - 1;
    }

    /**
     * Validate quarry layout
     * 
     * @param centreChest Chest object for the chest at the center of the quarry
     * @return true if layout is valid
     */
    public static boolean isQuarryLayout(Chest centreChest) {
        Location centreLoc = centreChest.getLocation();
        int cx = centreLoc.getBlockX();
        int cy = centreLoc.getBlockY();
        int cz = centreLoc.getBlockZ();
        World w = centreLoc.getWorld();

        return w.getBlockAt(cx - 1, cy, cz).getType().equals(Material.DIAMOND_BLOCK)
                && w.getBlockAt(cx + 1, cy, cz).getType().equals(Material.DIAMOND_BLOCK)
                && w.getBlockAt(cx, cy, cz - 1).getType().equals(Material.DIAMOND_BLOCK)
                && w.getBlockAt(cx, cy, cz + 1).getType().equals(Material.DIAMOND_BLOCK)
                && w.getBlockAt(cx - 1, cy, cz - 1).getType().equals(Material.REDSTONE_BLOCK)
                && w.getBlockAt(cx - 1, cy, cz + 1).getType().equals(Material.REDSTONE_BLOCK)
                && w.getBlockAt(cx + 1, cy, cz - 1).getType().equals(Material.REDSTONE_BLOCK)
                && w.getBlockAt(cx + 1, cy, cz + 1).getType().equals(Material.REDSTONE_BLOCK);
    }

    public void togglePause() {
        togglePause(Bukkit.getPlayer(owner));
    }

    public void togglePause(Player p) {
        paused = !paused;

        if (paused) {
            p.sendMessage(Messages.getQuarryPaused(centreChestLocation));
            if (!isOwner(p))
                tellOwner(Messages.getQuarryPaused(centreChestLocation));
        } else {
            p.sendMessage(Messages.getQuarryRestarted(centreChestLocation));
            if (!isOwner(p))
                tellOwner(Messages.getQuarryRestarted(centreChestLocation));
            alerted = false;
        }
    }

    public boolean isOwner(Player p) {
        return (owner != null) && p.getName().equals(owner);
    }

    public boolean pistonAllowed(World w, int x, int y, int z) {
        return (world.getName().equals(w.getName()) && x >= minX - 15 && x <= maxX + 15 && z >= minZ - 15 && z <= maxZ + 15
                && y >= centreChestLocation.getBlockY() - 16 && y <= centreChestLocation.getBlockY() + 16);
    }

    public void movePlatformCursor() {
        if (platformDone)
            return;
        platX++;
        if (platX > maxX + 1) {
            if (platZ < maxZ + 1) {
                platX = minX - 1;
                platZ++;
            } else {
                platX = maxX + 1;
                platformDone = true;
            }
        }
    }

    public void movePlatformBreaker() {
        if (clearedPlatform)
            return;
        platX++;
        if (platX > maxX + 1) {
            if (platZ < maxZ + 1) {
                platX = minX - 1;
                platZ++;
            } else {
                platX = maxX + 1;
                clearedPlatform = true;
            }
        }
    }

    public void resetPlatformCursor() {
        platX = minX - 1;
        platZ = minZ - 1;
    }

    public void doWGProtection() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null)
            return;
        if (Bukkit.getPlayer(owner) == null)
            return;

        if (!WorldGuardHandler.doWGProtection(world, minX, maxX, minZ, maxZ, owner)) {
            paused = true;
            markedForDeletion = true;
            tellOwner(Messages.mayNotBuildHere());
            return;
        }
    }

    public void doGPProtection() {
        if (Bukkit.getPluginManager().getPlugin("GriefPrevention") == null)
            return;
        if (Bukkit.getPlayer(owner) == null)
            return;
        if (markedForDeletion)
            return;

        Player p = Bukkit.getPlayer(owner);

        for (int x = minX - 1; x <= maxX + 1; x++) {
            for (int z = minZ - 1; z <= maxZ + 1; z++) {
                if (!GriefPreventionHandler.checkLocation(p, new Location(world, x, centreChestLocation.getBlockY() - 1, z))) {
                    paused = true;
                    markedForDeletion = true;
                    tellOwner(Messages.mayNotBuildHere());
                    return;
                }
            }
        }
    }

    public void buildPlatform() {
        if (platformDone)
            return;
        Block currentBlock;
        for (int i = 0; i < maxX - minX + 1; i++) {
            // Do borders and centres
            if (platZ == minZ - 1 || platZ == maxZ + 1 || platX == minX - 1 || platX == maxX + 1
                    || (platZ >= centreChestLocation.getBlockZ() - 3 && platZ <= centreChestLocation.getBlockZ() + 3)
                    || (platX >= centreChestLocation.getBlockX() - 3 && platX <= centreChestLocation.getBlockX() + 3)) {
                currentBlock = world.getBlockAt(platX, centreChestLocation.getBlockY() - 1, platZ);
                if (platZ == minZ - 1 || platZ == maxZ + 1 || platX == minX - 1 || platX == maxX + 1) {
                    if (classicMode)
                        currentBlock.setType(Material.BLACK_STAINED_GLASS);
                    else currentBlock.setType(Material.WHITE_STAINED_GLASS);
                }

                else {
                    if (classicMode)
                        currentBlock.setType(Material.GREEN_STAINED_GLASS);
                    else {
                        currentBlock.setType(Material.PURPLE_STAINED_GLASS);
                        world.spawnParticle(Particle.DRAGON_BREATH, currentBlock.getLocation(), 1);
                    }

                }

            } else {
                currentBlock = world.getBlockAt(platX, centreChestLocation.getBlockY() - 1, platZ);
                if (classicMode)
                    currentBlock.setType(Material.CYAN_STAINED_GLASS);
                else currentBlock.setType(Material.BLACK_STAINED_GLASS);
            }
            if (classicMode)
                world.playSound(currentBlock.getLocation(), Sound.BLOCK_GLASS_PLACE, 1f, 1f);
            else world.playSound(currentBlock.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            movePlatformCursor();
        }

    }

    public boolean ptIntersects(World w, int x, int z) {
        return w.getName().equals(world.getName()) && x >= minX - 1 && x <= maxX + 1 && z >= minZ - 1 && z <= maxZ + 1;
    }

    public void clearPlatform() {
        for (int i = 0; i < maxX - minX + 1; i++) {
            Block currentBlock = world.getBlockAt(platX, centreChestLocation.getBlockY() - 1, platZ);
            if (currentBlock.getType().equals(Material.BLACK_STAINED_GLASS)
                    || currentBlock.getType().equals(Material.GREEN_STAINED_GLASS)
                    || currentBlock.getType().equals(Material.CYAN_STAINED_GLASS)
                    || currentBlock.getType().equals(Material.PURPLE_STAINED_GLASS)
                    || currentBlock.getType().equals(Material.WHITE_STAINED_GLASS)) {
                world.playSound(currentBlock.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
                currentBlock.setType(Material.AIR);
            }
            movePlatformBreaker();
        }
    }

    public void calculateUpgrades() {
        int emeraldBlockCount = 0; // Emeralds blocks increase the mining rate. 4 emerald blocks
                                   // reduces mining
                                   // delay by 1 tick
        int diamondBlockCount = 0; // Diamond blocks increase the blocks mined at a time. 4 diamond
                                   // blocks give 1
                                   // more block at a time
        int goldBlockCount = 0;
        boolean hasNetherStar = false;

        int cx = centreChestLocation.getBlockX();
        int cy = centreChestLocation.getBlockY();
        int cz = centreChestLocation.getBlockZ();

        if (world.getBlockAt(cx - 1, cy + 1, cz - 1).getType().equals(Material.CHEST)
                || world.getBlockAt(cx - 1, cy + 1, cz - 1).getType().equals(Material.TRAPPED_CHEST)) {
            Inventory chestInv = ((Chest) world.getBlockAt(cx - 1, cy + 1, cz - 1).getState()).getInventory();
            for (int i = 0; i < chestInv.getSize(); i++) {
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))
                    emeraldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))
                    diamondBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))
                    goldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))
                    hasNetherStar = true;
            }
        }
        if (world.getBlockAt(cx - 1, cy + 1, cz + 1).getType().equals(Material.CHEST)
                || world.getBlockAt(cx - 1, cy + 1, cz + 1).getType().equals(Material.TRAPPED_CHEST)) {
            Inventory chestInv = ((Chest) world.getBlockAt(cx - 1, cy + 1, cz + 1).getState()).getInventory();
            for (int i = 0; i < chestInv.getSize(); i++) {
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))
                    emeraldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))
                    diamondBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))
                    goldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))
                    hasNetherStar = true;
            }
        }
        if (world.getBlockAt(cx + 1, cy + 1, cz - 1).getType().equals(Material.CHEST)
                || world.getBlockAt(cx + 1, cy + 1, cz - 1).getType().equals(Material.TRAPPED_CHEST)) {
            Inventory chestInv = ((Chest) world.getBlockAt(cx + 1, cy + 1, cz - 1).getState()).getInventory();
            for (int i = 0; i < chestInv.getSize(); i++) {
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))
                    emeraldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))
                    diamondBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))
                    goldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))
                    hasNetherStar = true;
            }
        }
        if (world.getBlockAt(cx + 1, cy + 1, cz + 1).getType().equals(Material.CHEST)
                || world.getBlockAt(cx + 1, cy + 1, cz + 1).getType().equals(Material.TRAPPED_CHEST)) {
            Inventory chestInv = ((Chest) world.getBlockAt(cx + 1, cy + 1, cz + 1).getState()).getInventory();
            for (int i = 0; i < chestInv.getSize(); i++) {
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.EMERALD_BLOCK))
                    emeraldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.DIAMOND_BLOCK))
                    diamondBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.GOLD_BLOCK))
                    goldBlockCount += chestInv.getItem(i).getAmount();
                if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(Material.NETHER_STAR))
                    hasNetherStar = true;
            }
        }

        if (emeraldBlockCount > 76)
            emeraldBlockCount = 76;
        if (diamondBlockCount > 36)
            diamondBlockCount = 36;
        if (goldBlockCount > 100)
            goldBlockCount = 100;

        if (emeraldBlockCount != emeraldBlocks || diamondBlockCount != diamondBlocks || (enderReplaceDirt != hasNetherStar)
                || goldBlockCount != goldBlocks) {
            emeraldBlocks = emeraldBlockCount;
            diamondBlocks = diamondBlockCount;
            goldBlocks = goldBlockCount;
            enderReplaceDirt = hasNetherStar;

            energyMod = (101.0f - (float) goldBlocks) / 101.0f;
            float efficiency = 100.0f * ((float) goldBlocks) / 101.0f;

            miningDelay = 20 - ((int) (Math.floor(((float) emeraldBlocks) / 4.0)));
            blocksPerTick = 1 + (int) Math.floor(((float) diamondBlocks) / 4.0);

            int emeraldsToNext = 4 - emeraldBlocks % 4;
            int diamondsToNext = 4 - diamondBlocks % 4;

            if (emeraldBlocks == 76)
                emeraldsToNext = 0;
            if (diamondBlocks == 36)
                diamondsToNext = 0;

            List<String> msgs = new ArrayList<>(Arrays.asList(Messages.getQuarryModified(centreChestLocation),
                    Messages.getStatusMiningDelay(miningDelay), Messages.getStatusEmeraldBlocksToUpgrade(emeraldsToNext),
                    Messages.getStatusMiningRate(blocksPerTick), Messages.getStatusDiamondBlocksToUpgrade(diamondsToNext),
                    Messages.getStatusEfficiency(efficiency), Messages.getStatusMode(classicMode)));
            if (!classicMode) {
                msgs.add(Messages.getStatusEnderReplace(enderReplaceDirt));
            }
            tellOwner(msgs.toArray(new String[msgs.size()]));

        }

    }

    public void sendProgress() {
        if (finished)
            tellOwner(Messages.getQuarryFinishedStatus(nextY));
        else if (!paused)
            tellOwner(Messages.getQuarryMiningStatus(nextY));
        else tellOwner(Messages.getQuarryPausedStatus(nextY));
    }

    public void sendProgress(Player p) {
        if (finished)
            p.sendMessage(Messages.getQuarryFinishedStatus(nextY));
        else if (!paused)
            p.sendMessage(Messages.getQuarryMiningStatus(nextY));
        else p.sendMessage(Messages.getQuarryPausedStatus(nextY));
    }

    public boolean isSameCentreChest(Chest someCentreChest) {
        return centreChest != null && centreChest.equals(someCentreChest);
    }

    public boolean checkCentreChest() {
        return world.getBlockAt(centreChestLocation) != null
                && world.getBlockAt(centreChestLocation).getType().equals(Material.CHEST) && Quarry.isQuarryLayout(centreChest);
    }

    public Location getLocation() {
        return centreChestLocation;
    }

    public String toggleEnderMining() {
        alerted = false;
        classicMode = !classicMode;
        resetMiningCursor();
        resetPlatformCursor();
        platformDone = false;
        return Messages.getMiningModeToggled(classicMode);
    }

    public boolean isIn3x3(Block b) {
        Location bLoc = b.getLocation();
        return bLoc.getBlockX() >= centreChestLocation.getBlockX() - 1
                && bLoc.getBlockX() <= centreChestLocation.getBlockX() + 1
                && bLoc.getBlockZ() >= centreChestLocation.getBlockZ() - 1
                && bLoc.getBlockZ() <= centreChestLocation.getBlockZ() + 1
                && bLoc.getBlockY() == centreChestLocation.getBlockY();

    }

    public void resetMiningCursor() {
        nextY = centreChestLocation.getBlockY() - 2;
        nextX = minX;
        nextZ = minZ;
        finished = false;
    }

    public boolean hasFuel(float amount) {
        centreChest = (Chest) world.getBlockAt(centreChestLocation).getState();
        Inventory centreInv = centreChest.getInventory();
        float energySoFar = storedEnergy;
        if (energySoFar >= amount) {
            return true;
        }
        QuarryFuel currentFuel;
        for (int i = 0; i < centreInv.getSize(); i++) {
            ItemStack currentItem = centreInv.getItem(i);
            if (currentItem == null)
                continue;
            currentFuel = QuarryFuel.getFuel(currentItem.getType());
            if (currentFuel != null)
                energySoFar += currentFuel.getEnergyValue() * centreInv.getItem(i).getAmount();
            if (energySoFar >= amount) {
                return true;
            }
        }
        return false;
    }

    public boolean consumeFuel(float amount) {
        float energyToConsume = amount;
        float tempStoredEnergy = storedEnergy;
        if (storedEnergy > 0) {
            if (storedEnergy >= energyToConsume) {
                storedEnergy -= energyToConsume;
                return true;
            } else {
                energyToConsume -= tempStoredEnergy;
                tempStoredEnergy = 0;
            }
        }
        if (hasFuel(energyToConsume)) {
            Inventory centreInv = centreChest.getInventory();
            QuarryFuel currentFuel;
            for (int i = 0; i < centreInv.getSize(); i++) {
                ItemStack currentItem = centreInv.getItem(i);
                if (currentItem != null)
                    currentFuel = QuarryFuel.getFuel(centreInv.getItem(i).getType());
                else currentFuel = null;

                if (currentFuel != null) {

                    float stackEnergy = currentFuel.getEnergyValue() * centreInv.getItem(i).getAmount();

                    if (stackEnergy <= energyToConsume) {
                        // Take the whole stack
                        centreInv.setItem(i, new ItemStack(Material.AIR));

                        energyToConsume -= stackEnergy;

                        if (energyToConsume == 0) {
                            storedEnergy = tempStoredEnergy;
                            return true;
                        }
                    } else {
                        // Take part of the stack
                        int numToTake = (int) Math.ceil(energyToConsume / currentFuel.getEnergyValue());
                        centreInv.getItem(i).setAmount(centreInv.getItem(i).getAmount() - numToTake);
                        tempStoredEnergy += ((float) numToTake) * currentFuel.getEnergyValue();

                        tempStoredEnergy -= energyToConsume;
                        storedEnergy = tempStoredEnergy;

                        return true;
                    }

                }
            }

            // This should never happen
            return false;
        } else {
            return false;
        }
    }

    public boolean isShulkerBox(Material mat) {
        return mat.equals(Material.SHULKER_BOX) || mat.equals(Material.BLACK_SHULKER_BOX)
                || mat.equals(Material.BLUE_SHULKER_BOX) || mat.equals(Material.BROWN_SHULKER_BOX)
                || mat.equals(Material.CYAN_SHULKER_BOX) || mat.equals(Material.GRAY_SHULKER_BOX)
                || mat.equals(Material.GREEN_SHULKER_BOX) || mat.equals(Material.LIGHT_BLUE_SHULKER_BOX)
                || mat.equals(Material.LIGHT_GRAY_SHULKER_BOX) || mat.equals(Material.LIME_SHULKER_BOX)
                || mat.equals(Material.MAGENTA_SHULKER_BOX) || mat.equals(Material.ORANGE_SHULKER_BOX)
                || mat.equals(Material.PINK_SHULKER_BOX) || mat.equals(Material.PURPLE_SHULKER_BOX)
                || mat.equals(Material.RED_SHULKER_BOX);
    }

    public boolean isFiltered(Material mat) {
        int cx = centreChestLocation.getBlockX();
        int cy = centreChestLocation.getBlockY();
        int cz = centreChestLocation.getBlockZ();

        if (isShulkerBox(world.getBlockAt(cx - 1, cy + 1, cz - 1).getType())) {
            Inventory chestInv = ((ShulkerBox) world.getBlockAt(cx - 1, cy + 1, cz - 1).getState()).getInventory();
            if (chestInv.contains(mat))
                return true;
        }

        if (isShulkerBox(world.getBlockAt(cx - 1, cy + 1, cz + 1).getType())) {
            Inventory chestInv = ((ShulkerBox) world.getBlockAt(cx - 1, cy + 1, cz + 1).getState()).getInventory();
            if (chestInv.contains(mat))
                return true;
        }

        if (isShulkerBox(world.getBlockAt(cx + 1, cy + 1, cz - 1).getType())) {
            Inventory chestInv = ((ShulkerBox) world.getBlockAt(cx + 1, cy + 1, cz - 1).getState()).getInventory();
            if (chestInv.contains(mat))
                return true;
        }

        if (isShulkerBox(world.getBlockAt(cx + 1, cy + 1, cz + 1).getType())) {
            Inventory chestInv = ((ShulkerBox) world.getBlockAt(cx + 1, cy + 1, cz + 1).getState()).getInventory();
            if (chestInv.contains(mat))
                return true;
        }

        return false;
    }

    public boolean addMined(Material mat) {
        if (isFiltered(mat)) {
            return true;
        }
        int cx = centreChestLocation.getBlockX();
        int cz = centreChestLocation.getBlockZ();
        int y = centreChestLocation.getBlockY();

        // Do x = minX to maxX for z=cz
        for (int x = minX; x <= maxX; x++) {
            // Check for an iron bar
            if (!world.getBlockAt(x, y, cz).getType().equals(Material.IRON_BARS))
                continue;

            // Check for chest at cz-1
            if (world.getBlockAt(x, y, cz - 1).getType().equals(Material.CHEST)
                    || world.getBlockAt(x, y, cz - 1).getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) world.getBlockAt(x, y, cz - 1).getState();
                Inventory chestInv = chest.getInventory();
                for (int i = 0; i < chestInv.getSize(); i++) {
                    if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat)
                            && chestInv.getItem(i).getAmount() < 64) {
                        chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount() + 1);
                        return true;
                    }
                    if (chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
                        chestInv.setItem(i, new ItemStack(mat));
                        return true;
                    }
                }
            }

            // Check for chest at cz+1
            if (world.getBlockAt(x, y, cz + 1).getType().equals(Material.CHEST)
                    || world.getBlockAt(x, y, cz + 1).getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) world.getBlockAt(x, y, cz + 1).getState();
                Inventory chestInv = chest.getInventory();
                for (int i = 0; i < chestInv.getSize(); i++) {
                    if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat)
                            && chestInv.getItem(i).getAmount() < 64) {
                        chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount() + 1);
                        return true;
                    }
                    if (chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
                        chestInv.setItem(i, new ItemStack(mat));
                        return true;
                    }
                }
            }

            // Check for chest at y+1
            if (world.getBlockAt(x, y + 1, cz).getType().equals(Material.CHEST)
                    || world.getBlockAt(x, y + 1, cz).getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) world.getBlockAt(x, y + 1, cz).getState();
                Inventory chestInv = chest.getInventory();
                for (int i = 0; i < chestInv.getSize(); i++) {
                    if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat)
                            && chestInv.getItem(i).getAmount() < 64) {
                        chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount() + 1);
                        return true;
                    }
                    if (chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
                        chestInv.setItem(i, new ItemStack(mat));
                        return true;
                    }
                }
            }

        }

        // Do z = minZ to maxZ for x=cx
        for (int z = minZ; z <= maxZ; z++) {
            // Check for an iron bar
            if (!world.getBlockAt(cx, y, z).getType().equals(Material.IRON_BARS))
                continue;

            // Check for chest at cx-1
            if (world.getBlockAt(cx - 1, y, z).getType().equals(Material.CHEST)
                    || world.getBlockAt(cx - 1, y, z).getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) world.getBlockAt(cx - 1, y, z).getState();
                Inventory chestInv = chest.getInventory();
                for (int i = 0; i < chestInv.getSize(); i++) {
                    if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat)
                            && chestInv.getItem(i).getAmount() < 64) {
                        chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount() + 1);
                        return true;
                    }
                    if (chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
                        chestInv.setItem(i, new ItemStack(mat));
                        return true;
                    }
                }
            }

            // Check for chest at cx+1
            if (world.getBlockAt(cx + 1, y, z).getType().equals(Material.CHEST)
                    || world.getBlockAt(cx + 1, y, z).getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) world.getBlockAt(cx + 1, y, z).getState();
                Inventory chestInv = chest.getInventory();
                for (int i = 0; i < chestInv.getSize(); i++) {
                    if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat)
                            && chestInv.getItem(i).getAmount() < 64) {
                        chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount() + 1);
                        return true;
                    }
                    if (chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
                        chestInv.setItem(i, new ItemStack(mat));
                        return true;
                    }
                }
            }

            // Check for chest at y+1
            if (world.getBlockAt(cx, y + 1, z).getType().equals(Material.CHEST)
                    || world.getBlockAt(cx, y + 1, z).getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) world.getBlockAt(cx, y + 1, z).getState();
                Inventory chestInv = chest.getInventory();
                for (int i = 0; i < chestInv.getSize(); i++) {
                    if (chestInv.getItem(i) != null && chestInv.getItem(i).getType().equals(mat)
                            && chestInv.getItem(i).getAmount() < 64) {
                        chestInv.getItem(i).setAmount(chestInv.getItem(i).getAmount() + 1);
                        return true;
                    }
                    if (chestInv.getItem(i) == null || chestInv.getItem(i).getAmount() == 0) {
                        chestInv.setItem(i, new ItemStack(mat));
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public int getXRad() {
        int soFar = 1;
        int current = 1;
        Location currentPos = centreChestLocation.clone().add(2, 0, 0);
        while (world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
            current++;
            currentPos = currentPos.add(1, 0, 0);
        }
        if (current > soFar)
            soFar = current;
        current = 1;
        currentPos = centreChestLocation.clone().add(-2, 0, 0);
        while (world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
            current++;
            currentPos = currentPos.add(-1, 0, 0);
        }
        if (current > soFar)
            soFar = current;
        return soFar;
    }

    public int getZRad() {
        int soFar = 1;
        int current = 1;
        Location currentPos = centreChestLocation.clone().add(0, 0, 2);
        while (world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
            current++;
            currentPos = currentPos.add(0, 0, 1);
        }
        if (current > soFar)
            soFar = current;
        current = 1;
        currentPos = centreChestLocation.clone().add(0, 0, -2);
        while (world.getBlockAt(currentPos).getType().equals(Material.IRON_BARS)) {
            current++;
            currentPos = currentPos.add(0, 0, -1);
        }
        if (current > soFar)
            soFar = current;
        return soFar;
    }

    public void tellOwner(String msg) {
        for (Player p : world.getPlayers()) {
            if (isOwner(p)) {
                p.sendMessage(msg);
            }
        }
    }

    public void tellOwner(String... msg) {
        for (Player p : world.getPlayers()) {
            if (isOwner(p)) {
                p.sendMessage(msg);
            }
        }
    }

    public boolean canInteractAt(Location l, Player p) {
        if (p.hasPermission("quarrycraft.useall"))
            return true;
        if (isOwner(p) && p.hasPermission("quarrycraft.use")) {
            return true;
        }
        if (l.getWorld().getName().equals(world.getName()) && l.getBlockX() >= minX - 1 && l.getBlockX() <= maxX + 1
                && l.getBlockZ() >= minZ - 1 && l.getBlockZ() <= maxZ + 1)
            return false;
        return true;
    }

    public boolean canBreak(Location l, Player p) {
        if ((isOwner(p) && p.hasPermission("quarrycraft.use")) || p.hasPermission("quarrycraft.useall")) {
            if (l.getWorld().getName().equals(world.getName()) && l.getBlockX() >= minX - 1 && l.getBlockX() <= maxX + 1
                    && l.getBlockZ() >= minZ - 1 && l.getBlockZ() <= maxZ + 1
                    && l.getBlockY() == centreChestLocation.getBlockY() - 1)
                return false;
            return true;
        }
        if (l.getWorld().getName().equals(world.getName()) && l.getBlockX() >= minX - 1 && l.getBlockX() <= maxX + 1
                && l.getBlockZ() >= minZ - 1 && l.getBlockZ() <= maxZ + 1)
            return false;
        return true;
    }

    public int getArea() {
        return Math.abs(minX - maxX + 1) * Math.abs(minZ - maxZ + 1);
    }

    public void setBounds() {
        int xRad = getXRad();
        int zRad = getZRad();
        int cx = centreChestLocation.getBlockX();
        int cz = centreChestLocation.getBlockZ();

        minX = cx - xRad;
        maxX = cx + xRad;
        minZ = cz - zRad;
        maxZ = cz + zRad;

        nextX = minX;
        nextY = centreChestLocation.getBlockY() - 2;
        nextZ = minZ;
    }

    public void moveMiningCursor() {
        if (nextX == maxX && nextY == worldBottom && nextZ == maxZ) {
            finished = true;
            return;
        }
        if (nextX == maxX) {
            // Move to next Z if possible
            if (nextZ < maxZ) {
                nextX = minX;
                nextZ++;
            } else {
                // Move to next layer
                nextX = minX;
                nextZ = minZ;
                nextY--;
            }
        } else {
            // Move one in x direction
            nextX++;
        }

    }

    public boolean isIgnored(Material mat) {
        for (int i = 0; i < ignored.length; i++) {
            if (ignored[i].equals(mat))
                return true;
        }
        return false;
    }

    public Block findNextBlock() {
        Block currentBlock = world.getBlockAt(nextX, nextY, nextZ);
        if (classicMode) {
            return currentBlock;
        }

        // Otherwise using ender mining
        while (isIgnored(currentBlock.getType())) {
            moveMiningCursor();
            currentBlock = world.getBlockAt(nextX, nextY, nextZ);
            if (nextX == maxX && nextY == worldBottom && nextZ == maxZ)
                break;
        }
        return currentBlock;
    }

    public void mineNextBlock() {
        if (markedForDeletion)
            return;
        Block blockToMine = findNextBlock();

        if (classicMode) {
            // Ignore air, water, lava, or bedrock
            Material thisMaterial = blockToMine.getType();
            while (thisMaterial.equals(Material.AIR) || thisMaterial.equals(Material.WATER)
                    || thisMaterial.equals(Material.LAVA) || thisMaterial.equals(Material.BEDROCK)) {
                moveMiningCursor();
                blockToMine = findNextBlock();
                thisMaterial = blockToMine.getType();
                if (nextX == maxX && nextY == worldBottom && nextZ == maxZ)
                    break;
            }
            if (thisMaterial.equals(Material.AIR) || thisMaterial.equals(Material.WATER) || thisMaterial.equals(Material.LAVA)
                    || thisMaterial.equals(Material.BEDROCK)) {
                if (nextX == maxX && nextY == worldBottom && nextZ == maxZ && !alerted) {
                    alerted = true;
                    finished = true;
                    tellOwner(Messages.getFinished(centreChestLocation));
                }

                return;
            }

            float energyToUse = thisMaterial.getHardness() * energyMod;
            if (hasFuel(energyToUse)) {
                if (addMined(thisMaterial)) {
                    consumeFuel(energyToUse);
                    if (thisMaterial.equals(Material.DIRT) || thisMaterial.equals(Material.GRASS)
                            || thisMaterial.equals(Material.GRASS_BLOCK))
                        world.playSound(blockToMine.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.0f);
                    else if (thisMaterial.equals(Material.GRAVEL))
                        world.playSound(blockToMine.getLocation(), Sound.BLOCK_GRAVEL_BREAK, 1.0f, 1.0f);
                    else world.playSound(blockToMine.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
                    blockToMine.setType(Material.AIR);
                    if (alerted) {
                        alerted = false;
                        tellOwner(Messages.getQuarryRestarted(centreChestLocation));
                    }
                } else {
                    paused = true;
                    if (!alerted) {
                        alerted = true;
                        tellOwner(Messages.getNoSpace(centreChestLocation));
                    }
                }
            } else {
                paused = true;
                if (!alerted) {
                    alerted = true;
                    tellOwner(Messages.getNoFuel(centreChestLocation));
                }
            }

        }

        else {
            // Ender mode
            Material thisMaterial = blockToMine.getType();
            if (thisMaterial.equals(Material.AIR) || thisMaterial.equals(Material.WATER) || thisMaterial.equals(Material.LAVA)
                    || thisMaterial.equals(Material.BEDROCK)) {
                if (nextX == maxX && nextY == worldBottom && nextZ == maxZ && !alerted) {
                    alerted = true;
                    finished = true;
                    tellOwner(Messages.getFinished(centreChestLocation));
                }

                return;
            }

            // Otherwise it is mineable and quarry isn't finished
            float energyToUse = thisMaterial.getHardness() * energyMod;
            energyToUse *= 50.0f;
            if (hasFuel(energyToUse)) {
                if (addMined(thisMaterial)) {
                    consumeFuel(energyToUse);
                    world.playSound(blockToMine.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    if (enderReplaceDirt && !blockToMine.isPassable())
                        blockToMine.setType(Material.DIRT);
                    else blockToMine.setType(Material.AIR);
                    if (alerted) {
                        alerted = false;
                        tellOwner(Messages.getQuarryRestarted(centreChestLocation));
                    }
                } else {
                    paused = true;
                    if (!alerted) {
                        alerted = true;
                        tellOwner(Messages.getNoSpace(centreChestLocation));
                    }
                }
            } else {
                paused = true;
                if (!alerted) {
                    alerted = true;
                    tellOwner(Messages.getNoFuel(centreChestLocation));
                }
            }
        }

    }

    @Override
    public void run() {
        if (checkCentreChest()) {
            if (!paused && counter % miningDelay == 0) {
                for (int i = 0; i < blocksPerTick; i++) {
                    mineNextBlock();
                }
            }
        } else {
            // This quarry should be removed from the world in some way
            if (!markedForDeletion)
                resetPlatformCursor();
            markedForDeletion = true;
        }

        if (!markedForDeletion && !platformDone /* && nextY < centreChestLocation.getBlockY()-1 */)
            buildPlatform();
        if (markedForDeletion && !clearedPlatform)
            clearPlatform();

        if (counter % 10 == 0)
            calculateUpgrades();

        counter++;
        if (counter == Integer.MAX_VALUE)
            counter = 1;
    }

    public Location getCentreChestLocation() {
        return centreChestLocation;
    }

    public void setCentreChestLocation(Location centreChestLocation) {
        this.centreChestLocation = centreChestLocation;
    }

    public Chest getCentreChest() {
        return centreChest;
    }

    public void setCentreChest(Chest centreChest) {
        this.centreChest = centreChest;
    }

    public float getStoredEnergy() {
        return storedEnergy;
    }

    public void setStoredEnergy(float storedEnergy) {
        this.storedEnergy = storedEnergy;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }

    public int getNextX() {
        return nextX;
    }

    public void setNextX(int nextX) {
        this.nextX = nextX;
    }

    public int getNextY() {
        return nextY;
    }

    public void setNextY(int nextY) {
        this.nextY = nextY;
    }

    public int getNextZ() {
        return nextZ;
    }

    public void setNextZ(int nextZ) {
        this.nextZ = nextZ;
    }

    public boolean isClassicMode() {
        return classicMode;
    }

    public void setClassicMode(boolean classicMode) {
        this.classicMode = classicMode;
    }

    public boolean isEnderReplaceDirt() {
        return enderReplaceDirt;
    }

    public void setEnderReplaceDirt(boolean enderReplaceDirt) {
        this.enderReplaceDirt = enderReplaceDirt;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getBlocksPerTick() {
        return blocksPerTick;
    }

    public void setBlocksPerTick(int blocksPerTick) {
        this.blocksPerTick = blocksPerTick;
    }

    public int getMiningDelay() {
        return miningDelay;
    }

    public void setMiningDelay(int miningDelay) {
        this.miningDelay = miningDelay;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getEmeraldBlocks() {
        return emeraldBlocks;
    }

    public void setEmeraldBlocks(int emeraldBlocks) {
        this.emeraldBlocks = emeraldBlocks;
    }

    public int getDiamondBlocks() {
        return diamondBlocks;
    }

    public void setDiamondBlocks(int diamondBlocks) {
        this.diamondBlocks = diamondBlocks;
    }

    public int getGoldBlocks() {
        return goldBlocks;
    }

    public void setGoldBlocks(int goldBlocks) {
        this.goldBlocks = goldBlocks;
    }

    public boolean isPlatformDone() {
        return platformDone;
    }

    public void setPlatformDone(boolean platformDone) {
        this.platformDone = platformDone;
    }

    public boolean isClearedPlatform() {
        return clearedPlatform;
    }

    public void setClearedPlatform(boolean clearedPlatform) {
        this.clearedPlatform = clearedPlatform;
    }

    public int getPlatX() {
        return platX;
    }

    public void setPlatX(int platX) {
        this.platX = platX;
    }

    public int getPlatZ() {
        return platZ;
    }

    public void setPlatZ(int platZ) {
        this.platZ = platZ;
    }

    public float getEnergyMod() {
        return energyMod;
    }

    public void setEnergyMod(float energyMod) {
        this.energyMod = energyMod;
    }

    public static Material[] getIgnored() {
        return ignored;
    }

    public static void setIgnored(Material[] ignored) {
        Quarry.ignored = ignored;
    }

}
