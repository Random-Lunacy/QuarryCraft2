package com.randomlunacy.quarrycraft2.objects;

import org.bukkit.Material;

public enum QuarryFuel {
    COAL(50f), COAL_BLOCK(450f), CHARCOAL(25f), REDSTONE(100f), REDSTONE_BLOCK(900f), BLAZE_POWDER(200f), BLAZE_ROD(400f), NETHER_STAR(20000f), ENDER_PEARL(900f);

    private float energyValue;

    private QuarryFuel(float energy) {
        this.energyValue = energy;
    }

    public float getEnergyValue() {
        return energyValue;
    }

    public static QuarryFuel getFuel(Material someMat) {
        switch (someMat) {
            case COAL:
                return QuarryFuel.COAL;
            case COAL_BLOCK:
                return QuarryFuel.COAL_BLOCK;
            case CHARCOAL:
                return QuarryFuel.CHARCOAL;
            case REDSTONE:
                return QuarryFuel.REDSTONE;
            case REDSTONE_BLOCK:
                return QuarryFuel.REDSTONE_BLOCK;
            case BLAZE_POWDER:
                return QuarryFuel.BLAZE_POWDER;
            case BLAZE_ROD:
                return QuarryFuel.BLAZE_ROD;
            case NETHER_STAR:
                return QuarryFuel.NETHER_STAR;
            case ENDER_PEARL:
                return QuarryFuel.ENDER_PEARL;
            default:
                return null;
        }
    }
}
