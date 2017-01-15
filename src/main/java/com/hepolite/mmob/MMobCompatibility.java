package com.hepolite.mmob;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MMobCompatibility {
    private static Plugin pangaea;
    private static Object pangaeaHungerManager;
    private static Method pangaeaHungerManagerChangeSaturation, pangaeaHungerManagerChangeHunger;

    /** Initializes the compatibility system */
    @SuppressWarnings("unchecked")
    public final static void initialize() {
        pangaea = Bukkit.getPluginManager().getPlugin("Pangaea");

        if (hasPangaea()) {
            Log.log("Detected Pangaea! Attempting to obtain relevant resources...");
            try {
                @SuppressWarnings("rawtypes")
                final Class pangaea = Class.forName("com.hepolite.pangaea.Pangaea");
                @SuppressWarnings("rawtypes")
                final Class pangaeaHungerManagerClass = Class.forName("com.hepolite.pangaea.hunger.HungerManager");
                final Field hungerManager = pangaea.getDeclaredField("hungerManager");
                hungerManager.setAccessible(true);
                pangaeaHungerManager = hungerManager.get(MMobCompatibility.pangaea);
                pangaeaHungerManagerChangeSaturation = pangaeaHungerManagerClass.getDeclaredMethod("changeSaturation", Player.class, float.class);
                pangaeaHungerManagerChangeHunger = pangaeaHungerManagerClass.getDeclaredMethod("changeHunger", Player.class, float.class);
            } catch (final Exception e) {
                Log.log("Failed to properly grab all needed resources from Pangaea!", Level.WARNING);
                e.printStackTrace();
            }
        }
    }

    /** Returns true if Pangaea is loaded up */
    public final static boolean hasPangaea() {
        return pangaea != null;
    }

    /** Returns the Pangaea plugin instance */
    public final static Plugin getPangaea() {
        return pangaea;
    }

    /** Changes a player's saturation via Pangaea */
    public final static void pangaeaChangePlayerSaturation(final Player player, final float amount) {
        try {
            pangaeaHungerManagerChangeSaturation.invoke(pangaeaHungerManager, player, amount);
        } catch (final Exception e) {
        }
    }

    /** Changes a player's hunger via Pangaea */
    public final static void pangaeaChangePlayerHunger(final Player player, final float amount) {
        try {
            pangaeaHungerManagerChangeHunger.invoke(pangaeaHungerManager, player, amount);
        } catch (final Exception e) {
        }
    }
}
