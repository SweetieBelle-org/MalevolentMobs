package com.hepolite.mmob.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import com.hepolite.mmob.Log;

/**
 * Used to easily launch firework effects without the awful fireworks launch sound. Will also detonate the fireworks at the given location almost immediately instead of delaying the detonation.
 */
public class FireworksEffect {
    /** Returns the fireworks effect builder */
    public static Builder getFireworksEffectBuilder() {
        return FireworkEffect.builder();
    }

    /** Builds a new firework at the given location */
    public static void createFireworks(final Location location, final FireworkEffect effect) {
        try {
            playFirework(location.getWorld(), location, effect);
        } catch (final Exception e) {
            Log.log("Failed to launch fireworks in world '".concat(location.getWorld().getName()).concat("'"));
        }
    }

    /* [Util] FireworkEffectPlayer v1.0 originally by codename_B, modified by bob7 and Hepolite */
    // internal references, performance improvements
    private static Method firework_getHandle = null;

    /**
     * Play a pretty firework at the location with the FireworkEffect when called
     * 
     * @param world
     * @param loc
     * @param fe
     * @throws Exception
     */
    public static void playFirework(final World world, final Location loc, final FireworkEffect fe) throws Exception {
        final Firework fw = world.spawn(loc, Firework.class);

        if (firework_getHandle == null)
            firework_getHandle = getMethod(fw.getClass(), "getHandle");

        final Object nms_firework = firework_getHandle.invoke(fw, (Object[]) null);

        final FireworkMeta data = fw.getFireworkMeta();
        data.clearEffects();
        data.setPower(1);
        data.addEffect(fe);
        fw.setFireworkMeta(data);

        final Field fieldTicksFlown = nms_firework.getClass().getDeclaredField("ticksFlown");
        fieldTicksFlown.setAccessible(true);
        fieldTicksFlown.set(nms_firework, 5);

        final Field fieldLifespan = nms_firework.getClass().getDeclaredField("expectedLifespan");
        fieldLifespan.setAccessible(true);
        fieldLifespan.set(nms_firework, 6);
    }

    /**
     * Internal method, used as shorthand to grab our method in a nice friendly manner
     * 
     * @param cl
     * @param method
     * @return Method (or null)
     */
    private static Method getMethod(final Class<?> cl, final String method) {
        for (final Method m : cl.getMethods())
            if (m.getName().equals(method))
                return m;
        return null;
    }
}
