package com.randomlunacy.quarrycraft2.objects;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Messages {
    private Messages() {}

    public static final String MESSAGE_PREFIX =
            ChatColor.GREEN + "[" + ChatColor.BLUE + QuarryCraft2.PLUGIN_NAME + ChatColor.GREEN + "] " + ChatColor.RESET;
    private static final String RELOADING_CONFIG = MESSAGE_PREFIX + " Reloading config...";
    private static final String PLEASE_WAIT =
            MESSAGE_PREFIX + ChatColor.RED + "Please wait %d seconds before using that command again." + ChatColor.RESET;
    private static final String NO_INTERACT_PERMISSION =
            MESSAGE_PREFIX + ChatColor.DARK_RED + "You do not have permission to interact with this quarry." + ChatColor.RESET;
    private static final String BLOCK_CANNOT_BE_BROKEN =
            MESSAGE_PREFIX + ChatColor.DARK_RED + "Sorry, this block may not be broken." + ChatColor.RESET;
    private static final String QUARRY_CREATED =
            MESSAGE_PREFIX + ChatColor.GREEN + "You have created a new quarry." + ChatColor.RESET;
    private static final String NO_BUILD_PERMISSION =
            MESSAGE_PREFIX + ChatColor.RED + "You do not have permission to build quarries." + ChatColor.RESET;
    private static final String QUARRY_LIMIT_REACHED =
            MESSAGE_PREFIX + ChatColor.DARK_RED + "You have reached your quarry limit (%d)." + ChatColor.RESET;
    private static final String INTERSECT_ERROR =
            MESSAGE_PREFIX + ChatColor.DARK_RED + "Quarries may not intersect!" + ChatColor.RESET;
    private static final String MINING_CURSOR_RESET =
            MESSAGE_PREFIX + "Mining cursor reset to  y=" + ChatColor.DARK_GREEN + "%d" + ChatColor.RESET + ".";
    private static final String QUARRY_DESTROYED =
            MESSAGE_PREFIX + ChatColor.DARK_RED + "The quarry at %s has been destroyed." + ChatColor.RESET;
    private static final String QUARRY_OVERSIZED = MESSAGE_PREFIX + ChatColor.DARK_RED
            + "Your quarry is over the area limit and is being restricted to a %dx%d area." + ChatColor.RESET;
    private static final String QUARRY_PAUSED =
            MESSAGE_PREFIX + ChatColor.YELLOW + "The quarry at %s has been paused." + ChatColor.RESET;
    private static final String QUARRY_RESTARTED =
            MESSAGE_PREFIX + ChatColor.GREEN + "The quarry at %s has resumed mining." + ChatColor.RESET;
    private static final String QUARRY_MODIFIED = MESSAGE_PREFIX + "Your quarry at %s has been modified:";
    private static final String QUARRY_FINISHED_STATUS =
            MESSAGE_PREFIX + "The quarry is finished at y=" + ChatColor.DARK_BLUE + "%d" + ChatColor.RESET + ".";
    private static final String QUARRY_MINING_STATUS =
            MESSAGE_PREFIX + "The quarry is mining at y=" + ChatColor.DARK_BLUE + "%d" + ChatColor.RESET + ".";
    private static final String QUARRY_PAUSED_STATUS =
            MESSAGE_PREFIX + "The quarry is paused at y=" + ChatColor.DARK_GREEN + "%d" + ChatColor.RESET + ".";
    private static final String MINING_MODE_TOGGLED = MESSAGE_PREFIX + "Mining mode toggled: %s" + ChatColor.RESET + ".";
    private static final String MODE_CLASSIC = "Classic";
    private static final String MODE_ENDER = "Ender";
    private static final String NO_SPACE = MESSAGE_PREFIX + ChatColor.RED
            + "Your quarry at %s has no space for new items and is now paused." + ChatColor.RESET;
    private static final String NO_FUEL =
            MESSAGE_PREFIX + ChatColor.RED + "Your quarry at %s has run out of fuel and is now paused." + ChatColor.RESET;
    private static final String FINISHED = ChatColor.BLUE + "Your quarry at %s is now finished" + ChatColor.RESET;
    private static final String WELCOME =
            MESSAGE_PREFIX + "Type " + ChatColor.BLUE + "/quarrycraft2 guide" + ChatColor.RESET + " to get started!";
    private static final String BUILD_NOT_ALLOWED_HERE =
            MESSAGE_PREFIX + ChatColor.RED + "You may not build your quarry here." + ChatColor.RESET;

    // Status messages
    private static final String STATUS_MINING_DELAY = "    Mining Delay: " + ChatColor.DARK_GREEN + "%d" + ChatColor.RESET;
    private static final String STATUS_EMERALD_TO_UPGRADE =
            "    Emerald blocks to next upgrade: " + ChatColor.GREEN + "%d" + ChatColor.RESET;
    private static final String STATUS_MINING_RATE =
            "    Blocks mined at a time: " + ChatColor.DARK_BLUE + "%d" + ChatColor.RESET;
    private static final String STATUS_DIAMOND_TO_UPGRADE =
            "    Diamond blocks to next upgrade: " + ChatColor.AQUA + "%d" + ChatColor.RESET;
    private static final String STATUS_EFFICIENCY = "    Efficiency:" + ChatColor.YELLOW + " %.2f%%" + ChatColor.RESET;
    private static final String STATUS_MODE = "    Mining mode: " + ChatColor.GOLD + "%s" + ChatColor.RESET;
    private static final String STATUS_ENDER_REPLACE =
            "    Replacing blocks with dirt: " + ChatColor.GOLD + "%b" + ChatColor.RESET;

    private static final String STATUS_SILK = "    Silk Touch: " + ChatColor.BLUE + "On" + ChatColor.RESET;
    private static final String STATUS_FORTUNE = "    Fortune Level: " + ChatColor.BLUE + "%d" + ChatColor.RESET;
    private static final String STATUS_CRAFTING = "    Crafting Storage Blocks: " + ChatColor.BLUE + "On" + ChatColor.RESET;

    public static String getMessagePrefix() {
        return MESSAGE_PREFIX;
    }

    public static String getReloadingConfig() {
        return RELOADING_CONFIG;
    }

    public static String getPleaseWait(long seconds) {
        return String.format(PLEASE_WAIT, seconds);
    }

    public static String getNoInteractPermission() {
        return NO_INTERACT_PERMISSION;
    }

    public static String getBlockCannotBeBroken() {
        return BLOCK_CANNOT_BE_BROKEN;
    }

    public static String getQuarryCreated() {
        return QUARRY_CREATED;
    }

    public static String getNoBuildPermission() {
        return NO_BUILD_PERMISSION;
    }

    public static String getQuarryLimitReached(int limit) {
        return String.format(QUARRY_LIMIT_REACHED, limit);
    }

    public static String getQuarryIntersectError() {
        return INTERSECT_ERROR;
    }

    public static String getMiningCursorReset(int newY) {
        return String.format(MINING_CURSOR_RESET, newY);
    }

    public static String getQuarryDestroyed(Location location) {
        return String.format(QUARRY_DESTROYED, location.toVector().toString());
    }

    public static String getQuarryOversized(int width, int length) {
        return String.format(QUARRY_OVERSIZED, width, length);
    }

    public static String getQuarryPaused(Location location) {
        return String.format(QUARRY_PAUSED, location.toVector().toString());
    }

    public static String getQuarryRestarted(Location location) {
        return String.format(QUARRY_RESTARTED, location.toVector().toString());
    }

    public static String getQuarryModified(Location location) {
        return String.format(QUARRY_MODIFIED, location.toVector().toString());
    }

    public static String getStatusMiningDelay(int delay) {
        return String.format(STATUS_MINING_DELAY, delay);
    }

    public static String getStatusEmeraldBlocksToUpgrade(int count) {
        return String.format(STATUS_EMERALD_TO_UPGRADE, count);
    }

    public static String getStatusMiningRate(int count) {
        return String.format(STATUS_MINING_RATE, count);
    }

    public static String getStatusDiamondBlocksToUpgrade(int count) {
        return String.format(STATUS_DIAMOND_TO_UPGRADE, count);
    }

    public static String getStatusEfficiency(float percentage) {
        return String.format(STATUS_EFFICIENCY, percentage);
    }

    public static String getStatusMode(boolean classicMode) {
        return String.format(STATUS_MODE, classicMode ? MODE_CLASSIC : MODE_ENDER);
    }

    public static String getStatusEnderReplace(boolean replacing) {
        return String.format(STATUS_ENDER_REPLACE, replacing);
    }

    public static String getStatusSilkTouch(boolean silking) {
        return String.format(STATUS_SILK, silking);
    }

    public static String getStatusCraftingStorageBlocks(boolean crafting)
    {
        return String.format(STATUS_CRAFTING, crafting);
    }

    public static String getStatusFortune(int fortuneLevel) {
        return String.format(STATUS_FORTUNE, fortuneLevel);
    }

    public static String getQuarryFinishedStatus(int y) {
        return String.format(QUARRY_FINISHED_STATUS, y);
    }

    public static String getQuarryMiningStatus(int y) {
        return String.format(QUARRY_MINING_STATUS, y);
    }

    public static String getQuarryPausedStatus(int y) {
        return String.format(QUARRY_PAUSED_STATUS, y);
    }

    public static String getMiningModeToggled(boolean classicMode) {
        return String.format(MINING_MODE_TOGGLED, classicMode ? ChatColor.GREEN + MODE_CLASSIC : ChatColor.BLUE + MODE_ENDER);
    }

    public static String getNoSpace(Location location) {
        return String.format(NO_SPACE, location.toVector().toString());
    }

    public static String getNoFuel(Location location) {
        return String.format(NO_FUEL, location.toVector().toString());
    }

    public static String getFinished(Location location) {
        return String.format(FINISHED, location.toVector().toString());
    }

    public static String getWelcome() {
        return WELCOME;
    }

    public static String mayNotBuildHere() {
        return BUILD_NOT_ALLOWED_HERE;
    }
}
