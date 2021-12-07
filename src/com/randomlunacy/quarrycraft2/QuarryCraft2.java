package com.randomlunacy.quarrycraft2;

import java.util.ArrayList;
import java.util.List;

import com.randomlunacy.quarrycraft2.commands.ExampleCommand;
import com.randomlunacy.quarrycraft2.config.MainConfiguration;
import com.randomlunacy.quarrycraft2.listeners.PlayerJoinListener;
import com.randomlunacy.quarrycraft2.objects.GuideBook;
import com.randomlunacy.quarrycraft2.objects.Messages;
import com.randomlunacy.quarrycraft2.objects.Quarries;
import com.randomlunacy.quarrycraft2.objects.Quarry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class QuarryCraft2 extends JavaPlugin {

	public static final String PLUGIN_NAME = "QuarryCraft2";

	public static final String BUILD_QUARRIES_PERMISSION = "quarrycraft2.buildquarries";

	private static QuarryCraft2 pluginInstance;

	private Quarries quarries;
	private GuideBook gbGiver;

	private MainConfiguration configuration;

	public static synchronized QuarryCraft2 getInstance() {
		return pluginInstance;
	}

	public MainConfiguration getMainConfig() {
		return configuration;
	}

	private static synchronized void setInstance(QuarryCraft2 instance) {
		pluginInstance = instance;
	}

	/**
	 * Called when Bukkit server enables the plugin
	 * For improved reload behavior, use this as if it was the class constructor
	 */
	@Override
	public void onEnable() {
		QuarryCraft2.setInstance(this);

		// Save default configs if they not exist. Then load the configurations into
		// memory
		configuration = new MainConfiguration();

		gbGiver = new GuideBook();

		this.setupCommands();
		this.setupEventListeners();
	}

	/***
	 * Called whenever Bukkit server disables the plugin.
	 * For improved reload behavior, try to reset the plugin to it's initial state
	 * here.
	 */
	@Override
	public void onDisable() {
		quarries.saveQuarries();
		this.getServer().getScheduler().cancelTasks(this);
	}

	private void setupCommands() {
		// TODO: Add remaining commands
		this.getCommand("example").setExecutor(new ExampleCommand());
	}

	private void setupEventListeners() {
		// TODO: Add remaining events
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
	}

	public boolean hasPermission(Player p, String perm) {
		if (p.isOp())
			return true;

		return p.hasPermission(perm);
	}

	class guideCommand implements CommandExecutor {

		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
			if (args.length == 1 && sender.isOp() && args[0].equals("reload")) {
				sender.sendMessage(Messages.getReloadingConfig());
				configuration = new MainConfiguration();
				return true;
			}

			if (!(sender instanceof Player))
				return true;

			Player p = (Player) sender;
			if (args.length == 1 && args[0].equals("guide")) {
				gbGiver.giveGuideBook(p);
			}

			return true;
		}
	}

	class QuarryCraftTabCompleter implements TabCompleter {
		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			ArrayList<String> ls = new ArrayList<String>();

			ls.add("guide");
			if (sender.isOp())
				ls.add("reload");

			return ls;
		}
	}

	//// Event Handlers ////

	/**
	 * @param e
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (configuration.isWelcomeMessageEnabled()) {
			e.getPlayer().sendMessage(Messages.getWelcome());
		}
	}

	/**
	 * @param e
	 */
	@EventHandler
	public void pistonRetractEvent(BlockPistonRetractEvent e) {
		Location pLoc = e.getBlock().getLocation();
		if (!pistonAllowed(pLoc.getWorld(), pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ())) {
			e.setCancelled(true);
		}
	}

	/**
	 * @param e
	 */
	@EventHandler
	public void pistonExtendEvent(BlockPistonExtendEvent e) {
		Location pLoc = e.getBlock().getLocation();
		if (!pistonAllowed(pLoc.getWorld(), pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ())) {
			e.setCancelled(true);
		}
	}

	/**
	 * @param e
	 */
	@EventHandler
	public void onPlayerLeftClick(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (!canInteract(e.getClickedBlock().getLocation(), e.getPlayer())) {
				e.getPlayer().sendMessage(Messages.getNoInteractPermission());
				e.setCancelled(true);
				return;
			}
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)
					&& !canBreak(e.getClickedBlock().getLocation(), e.getPlayer())) {
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
					if (hasPermission(e.getPlayer(), BUILD_QUARRIES_PERMISSION)
							&& quarries.countQuarries(e.getPlayer()) < configuration.getQuarryLimit()
							&& quarries.addQuarry(centreChest, e.getPlayer().getName())) {
						if (!quarries.getQuarry(centreChest).isMarkedForDeletion())
							e.getPlayer().sendMessage(Messages.getQuarryCreated());
						e.setCancelled(true);
					} else if (quarries.getQuarry(centreChest) != null) {
						if (!quarries.getQuarry(centreChest).isMarkedForDeletion())
							e.getPlayer().sendMessage(quarries.getQuarry(centreChest).toggleEnderMining());
						e.setCancelled(true);
					} else if (!hasPermission(e.getPlayer(), BUILD_QUARRIES_PERMISSION)) {
						e.getPlayer().sendMessage(Messages.getNoBuildPermission());
						e.setCancelled(true);
					} else if (quarries.countQuarries(e.getPlayer()) >= configuration.getQuarryLimit()) {
						e.getPlayer()
								.sendMessage(
										Messages.getQuarryLimitReached(configuration.getQuarryLimit()));
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

	public boolean canInteract(Location l, Player p) {
		for (Quarry q : quarries.getQuarries())
			if (!q.canInteractAt(l, p))
				return false;
		return true;
	}

	public boolean canBreak(Location l, Player p) {
		for (Quarry q : quarries.getQuarries())
			if (!q.canBreak(l, p))
				return false;
		return true;
	}

	public boolean pistonAllowed(World w, int x, int y, int z) {
		for (Quarry q : quarries.getQuarries()) {
			if (!q.pistonAllowed(w, x, y, z))
				return false;
		}
		return true;
	}

	class QuarryCleaner extends BukkitRunnable {
		@Override
		public void run() {
			for (Quarry q : quarries.getQuarries())
				if ((q.isMarkedForDeletion() || !Quarry.isQuarryLayout(q.getCentreChest())) && q.isClearedPlatform()) {

					q.tellOwner(Messages.getQuarryDestroyed(q.getCentreChestLocation()));
					quarries.removeQuarry(q);
					return;
				}
		}
	}

}