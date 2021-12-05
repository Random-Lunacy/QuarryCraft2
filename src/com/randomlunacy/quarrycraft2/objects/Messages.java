package com.randomlunacy.quarrycraft2.objects;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Messages {
	public static final String MESSAGE_PREFIX = ChatColor.GREEN + "[" + ChatColor.BLUE + QuarryCraft2.PLUGIN_NAME
			+ ChatColor.GREEN + "]" + ChatColor.RESET;
	private static final String RELOADING_CONFIG = MESSAGE_PREFIX + " Reloading config...";
	private static final String PLEASE_WAIT = MESSAGE_PREFIX + ChatColor.RED
			+ "Please wait %d seconds before using that command again." + ChatColor.RESET;
	private static final String NO_INTERACT_PERMISSION = MESSAGE_PREFIX + ChatColor.DARK_RED
			+ "You do not have permission to interact with this quarry." + ChatColor.RESET;
	private static final String BLOCK_CANNOT_BE_BROKEN = MESSAGE_PREFIX + ChatColor.DARK_RED
			+ "Sorry, this block may not be broken." + ChatColor.RESET;
	private static final String QUARRY_CREATED = MESSAGE_PREFIX + ChatColor.GREEN + "You have created a new quarry."
			+ ChatColor.RESET;
	private static final String NO_BUILD_PERMISSION = MESSAGE_PREFIX + ChatColor.RED
			+ "You do not have permission to build quarries." + ChatColor.RESET;
	private static final String QUARRY_LIMIT_REACHED = MESSAGE_PREFIX + ChatColor.DARK_RED
			+ "You have reached your quarry limit (%d)." + ChatColor.RESET;
	private static final String INTERSECT_ERROR = MESSAGE_PREFIX + ChatColor.DARK_RED + "Quarries may not intersect!"
			+ ChatColor.RESET;
	private static final String MINING_CURSOR_RESET = MESSAGE_PREFIX + "Mining cursor reset to  y=" + ChatColor.DARK_GREEN
			+ "%d" + ChatColor.RESET + ".";
	private static final String QUARRY_DESTROYED = MESSAGE_PREFIX + ChatColor.DARK_RED + "Quarry at %s destroyed." + ChatColor.RESET;


	private static final String QUARRY_OVERSIZED = MESSAGE_PREFIX + ChatColor.DARK_RED
			+ "Your quarry is over the quarry area limit and is being restricted to a %dx%d area." + ChatColor.RESET;
	private static final String quarryPausedBeforeCoords = ChatColor.YELLOW + "Your quarry at ";
	private static final String quarryPausedAfterCoords = " has been paused.";
	private static final String quarryUnpausedBeforeCoords = ChatColor.GREEN + "Your quarry at ";
	private static final String quarryUnpausedAfterCoords = " is no longer paused.";
	private static final String quarryModified = "Your quarry has been modified:";
	private static final String miningDelay = "Mining Delay";
	private static final String emeraldBlocksToUpgrade = "Emerald blocks to next upgrade";
	private static final String blocksMinedAtATime = "Blocks mined at a time";
	private static final String diamondBlocksToUpgrade = "Diamond blocks to next upgrade";
	private static final String efficiency = "Efficiency";
	private static final String enderReplaceDirt = "Ender mining replaces blocks with dirt";
	private static final String quarryFinishedStatus = "The quarry is finished at";
	private static final String quarryMiningStatus = "The quarry is mining at";
	private static final String quarryPausedStatus = "The quarry is paused at";
	private static final String miningModeToggled = "Mining mode toggled";
	private static final String classic = "Classic";
	private static final String ender = "Ender";
	private static final String resumedBeforeCoords = ChatColor.GREEN + "Your quarry at ";
	private static final String resumedAfterCoords = " has resumed working";
	private static final String noSpaceBeforeCoords = ChatColor.RED + "Your quarry at ";
	private static final String noSpaceAfterCoords = " has no space for new items and is now paused.";
	private static final String noFuelBeforeCoords = ChatColor.RED + "Your quarry at ";
	private static final String noFuelAfterCoords = " has run out of fuel and is now paused.";
	private static final String finishedBeforeCoords = ChatColor.BLUE + "Your quarry at ";
	private static final String finishedAfterCoords = " is now finished";
	private static final String playerJoin1 = ChatColor.GREEN + "[" + QuarryCraft2.PLUGIN_NAME + "]" + ChatColor.WHITE
			+ " Welcome ";
	private static final String playerJoin2 = ChatColor.GREEN + "[" + QuarryCraft2.PLUGIN_NAME + "]" + ChatColor.WHITE
			+ " This server has "
			+ QuarryCraft2.PLUGIN_NAME + " installed.";
	private static final String playerJoin3 = ChatColor.GREEN + "[" + QuarryCraft2.PLUGIN_NAME + "]" + ChatColor.WHITE
			+ " Type "
			+ ChatColor.BLUE + "/quarrycraft guide" + ChatColor.WHITE + " to get started!";
	private static final String mayNotBuildHere = ChatColor.RED + "You may not build your quarry here!";

	public static String getMessagePrefix() {
		return MESSAGE_PREFIX;
	}

	public static String reloadingConfig() {
		return RELOADING_CONFIG;
	}

	public static String pleaseWaitBeforeNumSeconds(long seconds) {
		return String.format(PLEASE_WAIT, seconds);
	}

	public static String noInteractPermission() {
		return NO_INTERACT_PERMISSION;
	}

	public static String blockCannotBeBroken() {
		return BLOCK_CANNOT_BE_BROKEN;
	}

	public static String quarryCreated() {
		return QUARRY_CREATED;
	}



	public Messages() {
	}

	public static String noBuildPermission() {
		return NO_BUILD_PERMISSION;
	}

	public static String quarryLimitReached(int limit) {
		return String.format(QUARRY_LIMIT_REACHED, limit);
	}

	public String quarryIntersectError() {
		return INTERSECT_ERROR;
	}

	public String miningCursorReset(int newY) {
		return String.format(MINING_CURSOR_RESET, newY);
	}

	public String quarryDestroyed(Location location) {
		return String.format(QUARRY_DESTROYED, location.toVector().toString());
	}

	public String quarryOversized(int width, int length) {
		return String.format(QUARRY_OVERSIZED, width, length);
	}

	public String quarryPausedBeforeCoords() {
		return quarryPausedBeforeCoords;
	}

	public String quarryPausedAfterCoords() {
		return quarryPausedAfterCoords;
	}

	public String quarryUnpausedBeforeCoords() {
		return quarryUnpausedBeforeCoords;
	}

	public String quarryUnpausedAfterCoords() {
		return quarryUnpausedAfterCoords;
	}

	public String quarryModified() {
		return quarryModified;
	}

	public String miningDelay() {
		return miningDelay;
	}

	public String emeraldBlocksToUpgrade() {
		return emeraldBlocksToUpgrade;
	}

	public String blocksMinedAtATime() {
		return blocksMinedAtATime;
	}

	public String diamondBlocksToUpgrade() {
		return diamondBlocksToUpgrade;
	}

	public String efficiency() {
		return efficiency;
	}

	public String enderReplaceDirt() {
		return enderReplaceDirt;
	}

	public String quarryFinishedStatus() {
		return quarryFinishedStatus;
	}

	public String quarryMiningStatus() {
		return quarryMiningStatus;
	}

	public String quarryPausedStatus() {
		return quarryPausedStatus;
	}

	public String miningModeToggled() {
		return miningModeToggled;
	}

	public String classic() {
		return classic;
	}

	public String ender() {
		return ender;
	}

	public String resumedBeforeCoords() {
		return resumedBeforeCoords;
	}

	public String resumedAfterCoords() {
		return resumedAfterCoords;
	}

	public String noSpaceBeforeCoords() {
		return noSpaceBeforeCoords;
	}

	public String noSpaceAfterCoords() {
		return noSpaceAfterCoords;
	}

	public String noFuelBeforeCoords() {
		return noFuelBeforeCoords;
	}

	public String noFuelAfterCoords() {
		return noFuelAfterCoords;
	}

	public String finishedBeforeCoords() {
		return finishedBeforeCoords;
	}

	public String finishedAfterCoords() {
		return finishedAfterCoords;
	}

	public String playerJoin1() {
		return playerJoin1;
	}

	public String playerJoin2() {
		return playerJoin2;
	}

	public String playerJoin3() {
		return playerJoin3;
	}

	public String mayNotBuildHere() {
		return mayNotBuildHere;
	}

}
