package com.randomlunacy.quarrycraft2.handlers;

import com.randomlunacy.quarrycraft2.QuarryCraft2;
import com.sk89q.worldguard.bukkit.ProtectionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class WorldGuardHandler {
	private WorldGuardHandler() {}
	
	public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
	
	public static boolean doWGProtection(World world, int minX, int maxX, int minZ, int maxZ, String owner) {
		if(Bukkit.getPlayer(owner) == null) {
			return true;
		}
		WorldGuardPlugin wg = getWorldGuard();
		ProtectionQuery pq = wg.createProtectionQuery();
		
		for(int x=minX-1; x <= maxX+1; x++) {
			for(int y=QuarryCraft2.getInstance().getMainConfig().getMinWGY(); y<= QuarryCraft2.getInstance().getMainConfig().getMaxWGY(); y++) {
				for(int z=minZ-1; z<=maxZ+1; z++) {
					if(!pq.testBlockBreak(Bukkit.getPlayer(owner), world.getBlockAt(x,y,z)))
						return false;
				}
			}
		}
		return true;
	}
}