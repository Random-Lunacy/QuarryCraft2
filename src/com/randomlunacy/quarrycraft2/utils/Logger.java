package com.randomlunacy.quarrycraft2.utils;

import java.util.logging.Level;

import com.randomlunacy.quarrycraft2.QuarryCraft2;

public class Logger {
    private Logger() {
    }

    public static void logDebug(String message) {
        if (Boolean.TRUE.equals(QuarryCraft2.getInstance().getMainConfig().isDebugEnabled())) {
            QuarryCraft2.getInstance().getServer().getLogger().log(Level.INFO, message);
        }
    }

    public static void logNotice(String message) {
        QuarryCraft2.getInstance().getServer().getLogger().log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        QuarryCraft2.getInstance().getServer().getLogger().log(Level.WARNING, message);
    }

    public static void logSevere(String message) {
        QuarryCraft2.getInstance().getServer().getLogger().log(Level.SEVERE, message);
    }
}
