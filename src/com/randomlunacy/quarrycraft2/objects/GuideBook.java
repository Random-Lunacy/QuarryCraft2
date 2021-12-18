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

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class GuideBook {
    private final Map<Player, Date> usedMap = new HashMap<>(); // TODO: Add persistence of usedMap for tracking Guidebook use

    private long getTimeSinceLastUse(Player p) {
        if (usedMap.containsKey(p)) {
            return new Date().getTime() - usedMap.get(p).getTime();
        }

        // Player has not used, return time longer than cooldown
        return QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown() + 1;
    }

    // TODO: Move guidebook contents to external JSON, to make maintenance easier
    public void giveGuideBook(Player p) {
        if (getTimeSinceLastUse(p) > QuarryCraft2.getInstance().getMainConfig().getGuideBookCooldown()) {

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            bookMeta.setTitle("QuarryCraft2 Guide");
            bookMeta.setAuthor("Nepokama & Mekle");
            bookMeta.setLore(List.of("A guide to building and using quarries using QuarryCraft2."));

            // Title page (16 Total pages)
            TextComponent text = new TextComponent(ChatColor.BOLD + "QuarryCraft2 Guide" + ChatColor.RESET
                    + " \n\n\n\n\n\n\n\n\n This plugin was written by Random Lunacy\n based on code by Warren Hood");
            bookMeta.spigot().addPage(new ComponentBuilder(text).create());

            // Contents Page
            text = new TextComponent(
                    ChatColor.ITALIC + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "    Contents\n" + ChatColor.RESET);
            ComponentBuilder pageBuilder = new ComponentBuilder(text);
            pageBuilder.append(
                    new ComponentBuilder("Building the Quarry\n").event(new ClickEvent(Action.CHANGE_PAGE, "3")).create());
            pageBuilder.append(new ComponentBuilder("Fuel\n").event(new ClickEvent(Action.CHANGE_PAGE, "7")).create());
            pageBuilder
                    .append(new ComponentBuilder("Fuel Efficiency\n").event(new ClickEvent(Action.CHANGE_PAGE, "8")).create());
            pageBuilder.append(new ComponentBuilder("Upgrades\n").event(new ClickEvent(Action.CHANGE_PAGE, "9")).create());
            pageBuilder.append(new ComponentBuilder("BlockFilters\n").event(new ClickEvent(Action.CHANGE_PAGE, "12")).create());
            pageBuilder.append(new ComponentBuilder("Mining Modes\n").event(new ClickEvent(Action.CHANGE_PAGE, "13")).create());
            pageBuilder.append(
                    new ComponentBuilder("Pausing the quarry\n").event(new ClickEvent(Action.CHANGE_PAGE, "14")).create());
            pageBuilder.append(
                    new ComponentBuilder("Viewing progress\n").event(new ClickEvent(Action.CHANGE_PAGE, "14")).create());

            bookMeta.spigot().addPage(pageBuilder.create());

            // Page 3
            text = new TextComponent(
                    ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Building the Quarry" + ChatColor.RESET + "\n");
            text = new TextComponent(text,
                    new TextComponent(ChatColor.BOLD + "Step 1." + ChatColor.RESET + "\nPlace down a chest\n"));
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 2." + ChatColor.RESET
                    + "\nPlace a diamond block against each side of the chest. You will need 4 diamond blocks.\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            // Page 4
            text = new TextComponent(
                    ChatColor.BOLD + "Step 3." + ChatColor.RESET + "\nPlace a redstone block in each of the 4 corners\n");
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 4." + ChatColor.RESET
                    + "\nPlace iron bars running outwards from the diamond blocks. These will define your quarry's mining area. It is only necessary to run it from 2 sides.\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            // Page 5
            text = new TextComponent(ChatColor.BOLD + "Step 5." + ChatColor.RESET
                    + "\nPlace chests on either side of the iron bars, or on top of the iron bars for storage of mined items. They must touch the iron bars and be in the defined quarry area.\n");
            text = new TextComponent(text, new TextComponent(ChatColor.BOLD + "Step 6." + ChatColor.RESET
                    + "\nPlace fuel in the center chest. (Look on page 7 for details)\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            // Page 6
            text = new TextComponent(ChatColor.BOLD + "Step 7." + ChatColor.RESET
                    + "\nSneak-left-click the chest to create the quarry. It should now begin mining\n");
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            // Page 7
            text = new TextComponent(ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Fuel" + ChatColor.RESET + "\n");
            text = new TextComponent(text, new TextComponent("Quarries will accept the following types of fuel:\n"));
            text = new TextComponent(text, new TextComponent("- Charcoal\n"));
            text = new TextComponent(text, new TextComponent("- Coal\n"));
            text = new TextComponent(text, new TextComponent("- Coal Blocks\n"));
            text = new TextComponent(text, new TextComponent("- Redstone\n"));
            text = new TextComponent(text, new TextComponent("- Redstone Blocks\n"));
            pageBuilder = new ComponentBuilder(text);
            bookMeta.spigot().addPage(pageBuilder.create());

            // TODO: Add remaining Guidebook pages

            book.setItemMeta(bookMeta);

            p.getInventory().addItem(book);
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
