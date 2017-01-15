package com.hepolite.mmob.abilities.passives;

import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.ProjectilePotionEffect;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The withering particles passive generates a set of particles swirling around the entity with this passive; each particle can wither nearby entities
 */
public class PassiveWitherParticles extends Passive {
    private int particleCount = 0;
    private boolean affectPlayersOnly = true;

    private float timer = 0.0f;

    private ProjectilePotionEffect[] particles = null;
    private Location[] particleTargets = null;
    private float[] particleYaw = null;
    private float[] particlePitch = null;
    private float[] particleDistance = null;

    public PassiveWitherParticles(final MalevolentMob mob, final float scale) {
        super(mob, "Wither Particles", Priority.HIGH, scale);
    }

    @Override
    public void loadFromConfig(final Settings settings, final Settings alternative) {
        particleCount = (int) settings.getScaledValue(alternative, "ParticleCount", scale, 1.0f);
        affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");

        final float strength = settings.getScaledValue(alternative, "Strength", scale, 1.0f);
        final int duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
        final PotionEffect effect = new PotionEffect(PotionEffectType.WITHER, duration, (int) strength - 1);

        // Generate all the particles
        particles = new ProjectilePotionEffect[particleCount];
        particleTargets = new Location[particleCount];
        particleYaw = new float[particleCount];
        particlePitch = new float[particleCount];
        particleDistance = new float[particleCount];
        for (int i = 0; i < particleCount; i++) {
            particles[i] = new ProjectilePotionEffect(mob.getEntity().getEyeLocation(), effect, affectPlayersOnly, ParticleType.SMOKE_NORMAL);
            particleTargets[i] = mob.getEntity().getEyeLocation();
            particleYaw[i] = 2.0f * (float) Math.PI * random.nextFloat();
            particlePitch[i] = 2.0f * (float) Math.PI * random.nextFloat();
            particleDistance[i] = 1.0f + random.nextFloat() * (settings.getScaledValue(alternative, "Range", scale, 0.0f) - 1.0f);
        }
    }

    @Override
    public void onTick() {
        timer += 3.6 * Math.PI / 180.0;
        for (int i = 0; i < particles.length; i++) {
            // Compute where the particle is supposed to be
            final double posX = particleDistance[i] * Math.cos(timer);
            final double posZ = particleDistance[i] * Math.sin(timer);

            final double sinX = Math.sin(particlePitch[i]);
            final double sinY = Math.sin(particleYaw[i]);
            final double cosX = Math.cos(particlePitch[i]);
            final double cosY = Math.cos(particleYaw[i]);

            final Location orbitCenter = mob.getEntity().getEyeLocation();
            particleTargets[i].setX(orbitCenter.getX() + cosY * posX + cosX * sinY * posZ);
            particleTargets[i].setY(orbitCenter.getY() - sinX * posZ);
            particleTargets[i].setZ(orbitCenter.getZ() - sinY * posX + cosX * cosY * posZ);

            // Move the particle where it is supposed to be
            particles[i].moveTowardsLocation(particleTargets[i], 1.75f, 0.1f);
            particles[i].onTick();
        }
    }

}
