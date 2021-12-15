package com.randomlunacy.quarrycraft2.objects;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class GuideBook {
    private final Map<Player, Date> usedMap = new HashMap<>();

    private long getTimeSinceLastUse(Player p) {
        if (usedMap.containsKey(p))
        {
            return new Date().getTime() - usedMap.get(p).getTime();
        }

        // Player has not used, return time longer than cooldown
        return QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() + 1;
    }

    public void giveGuideBook(Player p) {
        if (getTimeSinceLastUse(p) > QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown()) {

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            bookMeta.setTitle("QuarryCraft2 Guide");
            bookMeta.setAuthor("Nepokama & Mekle");
            bookMeta.setLore(List.of("A guide to building and using QuarryCraft2 quarries."));

            //Title page (16 Total pages)
            TextComponent text = new TextComponent(ChatColor.BOLD + "QuarryCraft2 Guide" + ChatColor.RESET + " \n\n\n\n\n\n\n\n\n This plugin was coded by: ??????");
            bookMeta.spigot().addPage(new ComponentBuilder(text).create());

            //Contents Page
            text = new TextComponent(ChatColor.ITALIC + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "    Contents\n" + ChatColor.RESET);
            ComponentBuilder pageBuilder = new ComponentBuilder(text);
            pageBuilder.append(new ComponentBuilder("Building the Quarry\n").event(new ClickEvent(Action.CHANGE_PAGE, "3")).create());
            pageBuilder.append(new ComponentBuilder("Fuel\n").event(new ClickEvent(Action.CHANGE_PAGE, "7")).create());
            pageBuilder.append(new ComponentBuilder("Fuel Efficiency\n").event(new ClickEvent(Action.CHANGE_PAGE, "8")).create());
            pageBuilder.append(new ComponentBuilder("Upgrades\n").event(new ClickEvent(Action.CHANGE_PAGE, "9")).create());
            pageBuilder.append(new ComponentBuilder("BlockFilters\n").event(new ClickEvent(Action.CHANGE_PAGE, "12")).create());
            pageBuilder.append(new ComponentBuilder("Mining Modes\n").event(new ClickEvent(Action.CHANGE_PAGE, "13")).create());
            pageBuilder.append(new ComponentBuilder("Pausing the quarry\n").event(new ClickEvent(Action.CHANGE_PAGE, "14")).create());
            pageBuilder.append(new ComponentBuilder("Viewing progress\n").event(new ClickEvent(Action.CHANGE_PAGE, "14")).create());

            bookMeta.spigot().addPage(pageBuilder.create());

            //Page 3
            text = new TextComponent(ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Building the Quarry" + ChatColor.RESET + "\n");
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 1." + ChatColor.RESET + "\nPlace down a chest\n"));
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 2." + ChatColor.RESET + "\nPlace a diamond block against each side of the chest. You will need 4 diamond blocks.\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            //Page 4
            text = new TextComponent(ChatColor.BOLD + "Step 3." + ChatColor.RESET + "\nPlace a redstone block in each of the 4 corners\n");
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 4." + ChatColor.RESET + "\nPlace iron bars running outwards from the diamond blocks. These will define your quarry's mining area. It is only necessary to run it from 2 sides.\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            //Page 5
            text = new TextComponent(ChatColor.BOLD + "Step 5." + ChatColor.RESET + "\nPlace chests on either side of the iron bars, or on top of the iron bars for storage of mined items. They must touch the iron bars and be in the defined quarry area.\n");
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 6." + ChatColor.RESET + "\nPlace fuel in the center chest. (Look on page 7 for details)\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            //Page 6
            text = new TextComponent(ChatColor.BOLD + "Step 7." + ChatColor.RESET + "\nSneak-left-click the chest to create the quarry. It should now begin mining\n");
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            //Page 7
            text = new TextComponent(ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Fuel" + ChatColor.RESET + "\n");
            text = new TextComponent(text, new TextComponent("Quarries will accept the following types of fuel:\n"));
            text = new TextComponent(text, new TextComponent("- Charcoal\n"));
            text = new TextComponent(text, new TextComponent("- Coal\n"));
            text = new TextComponent(text, new TextComponent("- Coal Blocks\n"));
            text = new TextComponent(text, new TextComponent("- Redstone\n"));
            text = new TextComponent(text, new TextComponent("- Redstone Blocks\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());
            
            //TODO: Add remaining Guidbook pages

            book.setItemMeta(bookMeta);

            p.getInventory().addItem(book);
            /*
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @a[name=" + p.getName()
                    + "] written_book{pages:[\"[\\\"\\\",{\\\"text\\\":\\\"QuarryCraft Guide\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\n\\\nThis plugin was coded by Warren Hood.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\n\\\"},{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020 \\\",\\\"bold\\\":true,\\\"italic\\\":true},{\\\"text\\\":\\\"Contents\\\",\\\"bold\\\":true,\\\"italic\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"3. Building the quarry\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":3}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"7. Fuel\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":7}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"8. Fuel Efficiency\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":8}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"9. Upgrades\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":9}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"12. Block Filters\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":12}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"13. Mining Modes\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":13}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"14. Pausing the quarry\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":14}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"14. Viewing progress\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":14}},{\\\"text\\\":\\\"\\\n\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"16. Video Tutorial\\\",\\\"bold\\\":true,\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":16}}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Building the quarry\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 1.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nPlace down a chest\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 2.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nPlace a diamond block against each side of the chest. You will need 4 diamond blocks.\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Step 3.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nPlace a redstone block in each of the 4 corners.\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 4.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nPlace iron bars running outwards from the diamond blocks. These will define your quarry's mining area. It is only necessary to run it from 2 sides.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Step 5.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nPlace chests on either side of the iron bars or on top of the iron bars for storage of mined items. They must touch the iron bars and be in the defined quarry area.\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Step 6.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nPlace fuel in the centre chest. (Look on page 7)\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\n\\\"},{\\\"text\\\":\\\"Step 6.\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"\\\nSneak-left-click the chest to create the quarry. It should now begin mining.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Fuel\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\nQuarries will accept the following types of fuel:\\\n\\\n- Charcoal\\\n- Coal\\\n- Coal Blocks\\\n- Redstone\\\n- Redstone Blocks\\\n\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Fuel Efficiency\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Redstone\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is the most efficient(Provides most energy). Redstone blocks are equivalent to 9 redstone dusts.\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Coal\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is half as efficient as redstone. Coal blocks are equivalent to 9 coal.\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Charcoal\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" is half as efficient as coal.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Upgrades\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nQuarries are upgradeable. Place a chest on of the the 4 redstone corners.\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Mining delay\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" - A max of 76 emerald blocks can be placed in the upgrade chest to reduce delay between each block(s) mined.\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Blocks mined at a time\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" - A max of 36 diamond blocks can be placed in the upgrade chest to increase the number of blocks mined after each delay.\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Efficiency - \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"A max of 100 gold blocks can be placed in the upgrade chest to increase efficiency.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Ender quarry replaces mined blocks with dirt - \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Place a nether star in the upgrade chest to make ender quarrying replace mined blocks with dirt. This will prevent holes under the ground.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020 \\\\u0020\\\"},{\\\"text\\\":\\\"Block Filters\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nPlace a shulker box on one of the 4 redstone corners. Then place blocks which you would like to void into this box. Your quarry will still mine those blocks and use energy on them. But it will save storage space!\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Mining Modes\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nSneak-left click your centre chest with an empty hand to toggle between classic and ender mode. Classic mode mines everything. Ender mode ignores stone, grass and dirt, which is a lot faster, but uses 50x energy.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Pausing the Quarry\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nLeft click one of the 4 diamond blocks to pause/unpause your quarry.\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Viewing Quarry Progress\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nSneak-right-click the centre chest with an empty hand to view quarry progress.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Resetting mining level\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nIf you'd like to make the quarry start mining from the top again, sneak-left-click one of the 4 diamond blocks with an empty hand\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\\u0020 \\\\u0020\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"Video Tutorial\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\\n\\\nI also made a video explaining most of this stuff:\\\n\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Watch it on youtube by clicking here.\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"https://www.youtube.com/watch?v=3A0aurYeOUA&feature=youtu.be\\\"}}]\"],title:\"QuarryCraft Guide\",author:\"Warren Hood\",display:{Lore:[\"A guide to building and using QuarryCraft quarries.\"]}}");
            */

            usedMap.put(p, new Date());
        }

        else {
            p.sendMessage(Messages.getPleaseWait(
                    (long) ((QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() - getTimeSinceLastUse(p))
                            / 1000.0))
                    + " "
                    + (long) (((QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() - getTimeSinceLastUse(p)))
                            / 1000.0));
        }
    }
}
