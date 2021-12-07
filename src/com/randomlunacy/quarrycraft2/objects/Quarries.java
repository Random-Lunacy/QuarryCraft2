package com.randomlunacy.quarrycraft2.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.randomlunacy.quarrycraft2.utils.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

public class Quarries {
	private List<Quarry> myQuarries;

	public Quarries() {
		myQuarries = new ArrayList<>();
		loadQuarries();
	}

	public List<Quarry> getQuarries() {
		return myQuarries;
	}

	public Quarry getQuarry(Chest centreChest) {
		for (Quarry quarry : myQuarries) {
			if (quarry.isSameCentreChest(centreChest))
				return quarry;
		}
		return null;
	}

	public void removeQuarry(Quarry q) {
		int i = findQuarryIndex(q);
		if (i != -1) {
			myQuarries.get(i).cancel();
			myQuarries.remove(i);
			saveQuarries();
		}
	}

	private int findQuarryIndex(Quarry q) {
		for (int i = 0; i < myQuarries.size(); i++) {
			if (q.getLocation().equals(myQuarries.get(i).getLocation())) {
				return i;
			}
		}
		// Quarry not found
		return -1;
	}

	// Check if the file exists and is not a directory
	private static boolean isFileExists(File file) {
		return file.exists() && !file.isDirectory();
	}

	// TODO: Update to store in plugin configuration folder rather than world
	// folders
	public void saveQuarries() {
		for (World w : Bukkit.getWorlds()) {
			String wName = w.getName();
			String fileSeparator = System.getProperty("file.separator");
			File folder = new File(wName + fileSeparator + QuarryCraft2.PLUGIN_NAME);
			folder.mkdir();
			String path = wName + fileSeparator + QuarryCraft2.PLUGIN_NAME + fileSeparator + "qc2-quarries.txt";
			File quarryFile = new File(path);
			quarryFile.delete();
			String fileString = "";
			for (Quarry q : myQuarries) {
				Location quarryLoc = q.getLocation();
				if (!quarryLoc.getWorld().getName().equals(wName))
					continue;
				int minX = q.getMinX();
				int minZ = q.getMinZ();
				int maxX = q.getMaxX();
				int maxZ = q.getMaxZ();
				fileString += quarryLoc.getWorld().getName() + ";" + quarryLoc.getBlockX() + ";" + quarryLoc.getBlockY()
						+ ";" + quarryLoc.getBlockZ() + ";" + minX + ";" + minZ + ";" + maxX + ";" + maxZ + ";"
						+ q.isClassicMode() + ";" + q.getOwner() + ";" + q.isPaused() + "\n";
			}

			try {
				FileOutputStream fos = new FileOutputStream(path);
				fos.write(fileString.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				Logger.logSevere("Failure writing to '" + path + "'': " + e);
			}
		}

	}

	// TODO: Update to store in plugin configuration folder rather than world
	// folders
	private void loadQuarries() {
		for (World w : Bukkit.getWorlds()) {
			String wName = w.getName();
			String fileSeparator = System.getProperty("file.separator");
			String path = wName + fileSeparator + QuarryCraft2.PLUGIN_NAME + fileSeparator + "qc2-quarries.txt";

			if (!isFileExists(new File(path))) {
				// No qc2-quarries.txt exists, create one.
				saveQuarries();
			}
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(path));
				String currentCoords;
				String[] locString;
				int minX, minZ, maxX, maxZ;
				boolean classicMode;
				int x, y, z;
				String ownerName;
				do {
					currentCoords = inFile.readLine();
					if (currentCoords == null)
						break;
					locString = currentCoords.split(";");
					x = Integer.parseInt(locString[1]);
					y = Integer.parseInt(locString[2]);
					z = Integer.parseInt(locString[3]);
					minX = Integer.parseInt(locString[4]);
					minZ = Integer.parseInt(locString[5]);
					maxX = Integer.parseInt(locString[6]);
					maxZ = Integer.parseInt(locString[7]);
					ownerName = locString[9].trim();
					boolean isPaused = false;
					if (locString.length == 11)
						isPaused = locString[10].trim().equals("true");
					classicMode = locString[8].trim().contentEquals("true");
					Location quarryLoc = new Location(Bukkit.getWorld(locString[0]), x, y, z);
					addQuarry(quarryLoc, minX, maxX, minZ, maxZ, classicMode, ownerName, isPaused);

				} while (currentCoords != null);
				inFile.close();
			} catch (IOException e) {
				Logger.logSevere("Failure reading from '" + path + "'': " + e);
			}
		}
	}

	public boolean addQuarry(Chest centreChest, String name) {
		if (getQuarry(centreChest) == null) {
			Quarry quarry = new Quarry(centreChest, name);
			if (quarryIntersects(quarry))
				return false;
			myQuarries.add(quarry);
			quarry.runTaskTimer(QuarryCraft2.getInstance(), 0, 0);
			return true;
		}
		return false;
	}

	public boolean addQuarry(Location centreChestLocation, int minX, int maxX, int minZ, int maxZ, boolean mode,
			String name, boolean paused) {
		if (centreChestLocation.getWorld().getBlockAt(centreChestLocation).getType().equals(Material.CHEST)) {
			Quarry quarry = new Quarry(
					(Chest) centreChestLocation.getWorld().getBlockAt(centreChestLocation).getState(), minX, maxX, minZ,
					maxZ, mode, name);
			if (quarryIntersects(quarry)) {
				return false;
			}
			quarry.setPaused(paused);
			myQuarries.add(quarry);
			quarry.runTaskTimer(QuarryCraft2.getInstance(), 0, 0);
			return true;
		}
		return false;
	}

	public int countQuarries(Player p) {
		int count = 0;
		for (Quarry q : myQuarries)
			if (q.isOwner(p))
				count++;
		return count;
	}

	private boolean quarryIntersects(Quarry qc) {
		for (Quarry q : myQuarries) {
			if (qc.getCentreChestLocation().equals(q.getCentreChestLocation()))
				continue;
			if (q.ptIntersects(qc.getWorld(), qc.getMinX(), qc.getMinZ())
				|| q.ptIntersects(qc.getWorld(), qc.getMinX(), qc.getMaxZ())
				|| q.ptIntersects(qc.getWorld(), qc.getMaxX(), qc.getMinZ())
				|| q.ptIntersects(qc.getWorld(), qc.getMaxX(), qc.getMaxZ())
				|| qc.ptIntersects(q.getWorld(), q.getMinX(), q.getMinZ())
				|| qc.ptIntersects(q.getWorld(), q.getMinX(), q.getMaxZ())
				|| qc.ptIntersects(q.getWorld(), q.getMaxX(), q.getMinZ())
				|| qc.ptIntersects(q.getWorld(), q.getMaxX(), q.getMaxZ()))
				return true;
		}
		return false;
	}
}
