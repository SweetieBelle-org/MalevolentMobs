package com.hepolite.mmob.handlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.utility.Common;

import me.confuser.barapi.BarAPI;

/**
 * This class takes care of processing the malevolent mobs, keeping track of them and making sure that they are all valid. Invalid mobs will also be removed by this class
 */
public class MobHandler {
    // Control variables
    private final static HashMap<LivingEntity, MalevolentMob> mobMap = new HashMap<LivingEntity, MalevolentMob>();
    private final static List<MalevolentMob> mobsToAdd = new LinkedList<MalevolentMob>();

    private static int timerUpdateHealthbar = 0;

    // ///////////////////////////////////////////////////////////////////////////////////////
    // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
    // ///////////////////////////////////////////////////////////////////////////////////////

    /** Called each and every tick to carry out core logic */
    public static void onTick() {
        // Add new mobs
        for (final MalevolentMob mob : mobsToAdd)
            mobMap.put(mob.getEntity(), mob);
        mobsToAdd.clear();

        // Update all mobs and remove invalid mobs
        final List<MalevolentMob> mobsToRemove = new LinkedList<MalevolentMob>();
        for (final MalevolentMob mob : mobMap.values())
            if (mob.getEntity().isValid() && mob.getRole() != null)
                mob.onTick();
            else
                mobsToRemove.add(mob);
        for (final MalevolentMob mob : mobsToRemove) {
            mobMap.remove(mob.getEntity());
            if (mob.getRole() == null)
                Log.log("Detected a mob with no role! Check your config file and make sure it is valid!", Level.WARNING);
        }

        // Send the player boss bar to each nearby player
        if (++timerUpdateHealthbar > MMobPlugin.getSettings().getInteger("General.Bossbar.mobHealthbarUpdateTime")) {
            timerUpdateHealthbar = 0;
            for (final Player player : Bukkit.getServer().getOnlinePlayers())
                updatePlayerBossBar(player);
        }
    }

    /** Called everytime the plugin goes through a full restart */
    public final static void onRestart() {
        mobMap.clear();
        mobsToAdd.clear();
    }

    /** Updates the boss health bar for the given player */
    @SuppressWarnings("deprecation")
    private static void updatePlayerBossBar(final Player player) {
        // Dead player must have the boss bar removed to prevent issues
        if (player.isDead()) {
            BarAPI.removeBar(player);
            return;
        }

        // Find nearest malevolent mob
        double shortestDistance = -1.0f;
        LivingEntity nearestEntity = null;
        final List<LivingEntity> mobs = Common.getMalevolentMobsInRange(player.getLocation(), MMobPlugin.getSettings().getInteger("General.Bossbar.mobHealthbarDistance"));
        for (final LivingEntity mob : mobs) {
            // Ignore certain mobs
            final MalevolentMob mallMob = getMalevolentMob(mob);
            if (mallMob.isBossBarHidden() || mallMob.isDecoy())
                continue;

            final double distance = mob.getLocation().distance(player.getLocation());
            if (distance < shortestDistance || nearestEntity == null) {
                shortestDistance = distance;
                nearestEntity = mob;
            }
        }

        // If a mob was found, assign a health bar to it; otherwise remove the health bar for the given player
        final MalevolentMob mob = getMalevolentMob(nearestEntity);
        if (mob != null) {
            final String title = ChatColor.translateAlternateColorCodes('&', mob.getRole().mobName + (mob.isInfoHidden() ? "" : String.format(ChatColor.WHITE + " - Level %.0f ", mob.getLevel()) + mob.getRole().getName()));
            BarAPI.setMessage(player, title, 100.0f * (float) (nearestEntity.getHealth() / nearestEntity.getMaxHealth()));
            // StatusBarAPI.setStatusBar(player, title, (float) (nearestEntity.getHealth() / nearestEntity.getMaxHealth()));
        } else
            BarAPI.removeBar(player);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // MALEVOLENT MOB // MALEVOLENT MOB // MALEVOLENT MOB // MALEVOLENT MOB // MALEVOLENT MOB //
    // /////////////////////////////////////////////////////////////////////////////////////////

    /** Returns true if the given entity is associated with a malevolent mob */
    public static boolean isMobMalevolent(final LivingEntity entity) {
        if (entity == null)
            return false;
        if (mobsToAdd.size() != 0)
            for (final MalevolentMob mob : mobsToAdd)
                if (mob.getEntity() == entity)
                    return true;
        return mobMap.containsKey(entity);
    }

    /** Returns the malevolent mob associated with the given entity */
    public static MalevolentMob getMalevolentMob(final LivingEntity entity) {
        if (mobsToAdd.size() != 0)
            for (final MalevolentMob mob : mobsToAdd)
                if (mob.getEntity() == entity)
                    return mob;

        if (!isMobMalevolent(entity))
            return null;
        return mobMap.get(entity);
    }

    /** Turns the given entity into a new malevolent mob */
    public static MalevolentMob makeMobMalevolent(final LivingEntity entity) {
        if (entity == null)
            throw new IllegalArgumentException("A null entity can't be made into a malevolent mob!");
        if (isMobMalevolent(entity))
            return getMalevolentMob(entity);

        // Figure out the type of the entity
        final String type = Common.getEntityType(entity);

        // Spawn the mob and assign a random role to it
        final MalevolentMob mob = new MalevolentMob(entity);
        mob.setRandomRole(MMobPlugin.getSettings().getStringList("Spawns." + type + ".roles"));

        // Store the mob
        mobsToAdd.add(mob);
        return mob;
    }

    /** Returns the map of the active malevolent mobs. Do NOT modify the map! */
    public static HashMap<LivingEntity, MalevolentMob> getMalevolentMobMap() {
        return mobMap;
    }
}
