package com.hepolite.mmob.projectiles;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * Whenever the potion effect particle passes through an entity, it will apply its potion effect on said entity
 */
public class ProjectilePotionEffect extends Projectile {
    private PotionEffect effect = null;
    private boolean affectPlayersOnly = true;

    private ParticleType particleType = ParticleType.SMOKE_NORMAL;

    public ProjectilePotionEffect(final Location location, final PotionEffect effect, final boolean affectPlayersOnly, final ParticleType particleType) {
        position = location;
        this.effect = effect;
        this.affectPlayersOnly = affectPlayersOnly;
        this.particleType = particleType;
    }

    @Override
    public void onTick() {
        // Apply the effect to each entity within range
        final List<LivingEntity> entities = Common.getEntitiesInLocation(position);
        for (final LivingEntity entity : entities)
            if (!affectPlayersOnly || entity instanceof Player)
                entity.addPotionEffect(effect);

        // Display the particle
        ParticleEffect.play(particleType, position, 0.005f, 6, 0.075f);
    }

    // ///////////////////////////////////////////////////////////////////////////////////

    /** Returns the location of the particle */
    public Location getLocation() {
        return position;
    }

    /** Sets the location of the particles */
    public void setLocation(final Location location) {
        position = location;
    }
}
