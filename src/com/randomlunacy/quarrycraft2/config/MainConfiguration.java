package com.randomlunacy.quarrycraft2.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class MainConfiguration extends Configuration {

    public MainConfiguration() {
        super("config.yml", "config.yml");

        this.config.addDefault("underwater-plants", Arrays.asList(defaultUnderwaterPlants));

        this.loadValues();
    }

    private Boolean doDebug;
    private List<String> underwaterPlants;
    private List<Material> underwaterPlantMaterials = new ArrayList<>();
    private int quarryLimit;
    private int maxQuarryWidth;
    private int maxQuarryLength;
    private boolean doWelcomeMessage;
    private long guideBookCooldown;

    private boolean doWorldGuardProtection;
    private boolean doGriefPreventionProtection;

    private String[] defaultUnderwaterPlants = {"SEAGRASS", "TALL_SEAGRASS", "KELP", "KELP_PLANT"};

    // TODO: Verify that this gives adequate protection in WorldGuard and/or make configurable
    private int maxWGY = 100;
    private int minWGY = 20;

    private void loadValues() {
        // Unpublished
        this.doDebug = this.config.getBoolean("debug", false);
        this.underwaterPlants = this.config.getStringList("underwater-plants");

        // Published
        this.quarryLimit = this.config.getInt("quarry-limit", 5);
        this.maxQuarryWidth = this.config.getInt("max-quarry-width", 50);
        this.maxQuarryLength = this.config.getInt("max-quarry-length", 50);
        this.doWelcomeMessage = this.config.getBoolean("welcome-message-enabled", true);
        this.guideBookCooldown = this.config.getLong("guide-book-cooldown", 300000);
        this.doWorldGuardProtection = this.config.getBoolean("wordlguard-enabled", true);
        this.doGriefPreventionProtection = this.config.getBoolean("greifprevention-enabled", true);

        buildPlantMaterialList();
    }

    private void buildPlantMaterialList() {
        underwaterPlantMaterials.clear();
        if (null == underwaterPlants || underwaterPlants.isEmpty()) {
            this.underwaterPlants = Arrays.asList(defaultUnderwaterPlants);
        }
        for (String value : underwaterPlants) {
            underwaterPlantMaterials.add(Material.getMaterial(value));
        }
    }

    public Boolean isDebugEnabled() {
        return this.doDebug;
    }

    public int getQuarryLimit() {
        return quarryLimit;
    }

    public int getMaxQuarryWidth() {
        return maxQuarryWidth;
    }

    public int getMaxQuarryLength() {
        return maxQuarryLength;
    }

    public boolean isWelcomeMessageEnabled() {
        return doWelcomeMessage;
    }

    public long getGuideBookCooldown() {
        return guideBookCooldown;
    }

    public boolean isWorldGuardProtectionEnabled() {
        return doWorldGuardProtection;
    }

    public boolean isGriefPreventionProtectionEnabled() {
        return doGriefPreventionProtection;
    }

    public int getMaxWGY() {
        return maxWGY;
    }

    public int getMinWGY() {
        return minWGY;
    }

    public List<Material> getUnderwaterPlants() {
        return underwaterPlantMaterials;
    }
}
