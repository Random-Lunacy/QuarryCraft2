package com.randomlunacy.quarrycraft2;

import com.randomlunacy.quarrycraft2.commands.GuideCommand;
import com.randomlunacy.quarrycraft2.commands.ReloadConfigCommand;
import com.randomlunacy.quarrycraft2.config.MainConfiguration;
import com.randomlunacy.quarrycraft2.listeners.BlockPistonListener;
import com.randomlunacy.quarrycraft2.listeners.PlayerClickHandler;
import com.randomlunacy.quarrycraft2.listeners.PlayerJoinListener;
import com.randomlunacy.quarrycraft2.objects.Quarries;
import com.randomlunacy.quarrycraft2.tasks.QuarryCleaner;

import org.bukkit.plugin.java.JavaPlugin;

public class QuarryCraft2 extends JavaPlugin {

    public static final String PLUGIN_NAME = "QuarryCraft2";
    public static final String BUILD_QUARRIES_PERMISSION = "quarrycraft2.build";

    private static QuarryCraft2 pluginInstance;

    private Quarries quarries;

    private MainConfiguration configuration;

    public static synchronized QuarryCraft2 getInstance() {
        return pluginInstance;
    }

    private static synchronized void setInstance(QuarryCraft2 instance) {
        pluginInstance = instance;
    }

    public MainConfiguration getMainConfig() {
        return configuration;
    }

    public Quarries getQuarryList() {
        return this.quarries;
    }

    /**
     * Called when Bukkit server enables the plugin For improved reload behavior, use this as if it was the class constructor
     */
    @Override
    public void onEnable() {
        QuarryCraft2.setInstance(this);

        // Save default configs if they not exist. Then load the configurations into memory
        configuration = new MainConfiguration();
        quarries = new Quarries();

        this.setupCommands();
        this.setupEventListeners();
        this.setupTasks();
    }

    /***
     * Called whenever Bukkit server disables the plugin. For improved reload behavior, try to reset the plugin to it's initial
     * state here.
     */
    @Override
    public void onDisable() {
        quarries.saveQuarries();
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void setupCommands() {
        this.getCommand("qc2reload").setExecutor(new ReloadConfigCommand());
        this.getCommand("qc2guide").setExecutor(new GuideCommand());
    }

    private void setupEventListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockPistonListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerClickHandler(), this);
    }

    private void setupTasks() {
        new QuarryCleaner().runTaskTimer(QuarryCraft2.getInstance(), 10, 10);
    }
}
