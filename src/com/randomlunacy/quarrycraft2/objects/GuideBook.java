package com.randomlunacy.quarrycraft2.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuideBook{
	// TODO: restructure to Map
	private List<Date> lastUsed;
	private List<Player> playersUsed;
	
	public GuideBook() {
		lastUsed = new ArrayList<>();
		playersUsed = new ArrayList<>();
	}
	
	private long getTimeSinceLastUse(Player p) {
		for(int i=0; i < playersUsed.size(); i++) {
			Player pl = playersUsed.get(i);
			if(pl.getName().equals(p.getName()))
				return new Date().getTime() - lastUsed.get(i).getTime();
		}
		// Player has not used, return time longer than cooldown
		return QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() + 1;
	}
	
	private int getPlayerIndex(Player p) {
		for(int i=0; i < playersUsed.size(); i++) {
			if(playersUsed.get(i).getName().equals(p.getName()))
				return i;
		}
		return -1;
	}
	
	public void giveGuideBook(Player p) {
		if(getTimeSinceLastUse(p) > QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown()) {
			int playerIndex = getPlayerIndex(p);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"give @a[name="+p.getName()+"] written_book{pages:[\"[\\\"\\\",{\\\"text\\\":\\\"QuarryCraft Guide\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\nThis plugin was coded by Warren Hood.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\\n\\\"},{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020 \\\",\\\"bold\\\":true,\\\"italic\\\":true},{\\\"text\\\":\\\"Contents\\\",\\\"bold\\\":true,\\\"italic\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"3. Building the quarry\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":3}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"7. Fuel\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":7}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"8. Fuel Efficiency\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":8}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"9. Upgrades\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":9}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"12. Block Filters\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":12}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"13. Mining Modes\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":13}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"14. Pausing the quarry\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":14}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"14. Viewing progress\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":14}},{\\\"text\\\":\\\"\\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"16. Video Tutorial\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":16}}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Building the quarry\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 1.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace down a chest\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 2.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace a diamond block against each side of the chest. You will need 4 diamond blocks.\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Step 3.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace a redstone block in each of the 4 corners.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 4.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace iron bars running outwards from the diamond blocks. These will define your quarry's mining area. It is only necessary to run it from 2 sides.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Step 5.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace chests on either side of the iron bars or on top of the iron bars for storage of mined items. They must touch the iron bars and be in the defined quarry area.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 6.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nPlace fuel in the centre chest. (Look on page 7)\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\\n\\\"},{\\\"text\\\":\\\"Step 6.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\\nSneak-left-click the chest to create the quarry. It should now begin mining.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Fuel\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\nQuarries will accept the following types of fuel:\\\\n\\\\n- Charcoal\\\\n- Coal\\\\n- Coal Blocks\\\\n- Redstone\\\\n- Redstone Blocks\\\\n\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Fuel Efficiency\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Redstone\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is the most efficient(Provides most energy). Redstone blocks are equivalent to 9 redstone dusts.\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Coal\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is half as efficient as redstone. Coal blocks are equivalent to 9 coal.\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Charcoal\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is half as efficient as coal.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Upgrades\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nQuarries are upgradeable. Place a chest on of the the 4 redstone corners.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Mining delay\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" - A max of 76 emerald blocks can be placed in the upgrade chest to reduce delay between each block(s) mined.\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Blocks mined at a time\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" - A max of 36 diamond blocks can be placed in the upgrade chest to increase the number of blocks mined after each delay.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Efficiency - \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"A max of 100 gold blocks can be placed in the upgrade chest to increase efficiency.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Ender quarry replaces mined blocks with dirt - \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Place a nether star in the upgrade chest to make ender quarrying replace mined blocks with dirt. This will prevent holes under the ground.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Block Filters\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nPlace a shulker box on one of the 4 redstone corners. Then place blocks which you would like to void into this box. Your quarry will still mine those blocks and use energy on them. But it will save storage space!\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Mining Modes\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nSneak-left click your centre chest with an empty hand to toggle between classic and ender mode. Classic mode mines everything. Ender mode ignores stone, grass and dirt, which is a lot faster, but uses 50x energy.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Pausing the Quarry\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nLeft click one of the 4 diamond blocks to pause/unpause your quarry.\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Viewing Quarry Progress\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nSneak-right-click the centre chest with an empty hand to view quarry progress.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Resetting mining level\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nIf you'd like to make the quarry start mining from the top again, sneak-left-click one of the 4 diamond blocks with an empty hand\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Video Tutorial\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\\n\\\\nI also made a video explaining most of this stuff:\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Watch it on youtube by clicking here.\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://www.youtube.com/watch?v=3A0aurYeOUA&feature=youtu.be\\\"}}]\"],title:\"QuarryCraft Guide\",author:\"Warren Hood\",display:{Lore:[\"A guide to building and using QuarryCraft quarries.\"]}}");
			
			if(playerIndex == -1) {
				playersUsed.add(p);
				lastUsed.add(new Date());
			}
			else {
				lastUsed.set(playerIndex, new Date());
			}
		}
		
		else {
			p.sendMessage(Messages.getPleaseWait((long)((QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() - getTimeSinceLastUse(p))/1000.0)) + " " + (long)(((QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() - getTimeSinceLastUse(p)))/1000.0));
		}
	}
}