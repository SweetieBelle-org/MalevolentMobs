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
public class PassiveWitherParticles extends Passive
{
	private int particleCount = 0;
	private boolean affectPlayersOnly = true;

	private float timer = 0.0f;

	private ProjectilePotionEffect[] particles = null;
	private Location[] particleTargets = null;
	private float[] particleYaw = null;
	private float[] particlePitch = null;
	private float[] particleDistance = null;

	public PassiveWitherParticles(MalevolentMob mob, float scale)
	{
		super(mob, "Wither Particles", Priority.HIGH, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		particleCount = (int) settings.getScaledValue(alternative, "ParticleCount", scale, 1.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");

		float strength = settings.getScaledValue(alternative, "Strength", scale, 1.0f);
		int duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
		PotionEffect effect = new PotionEffect(PotionEffectType.WITHER, duration, (int) strength - 1);

		// Generate all the particles
		particles = new ProjectilePotionEffect[particleCount];
		particleTargets = new Location[particleCount];
		particleYaw = new float[particleCount];
		particlePitch = new float[particleCount];
		particleDistance = new float[particleCount];
		for (int i = 0; i < particleCount; i++)
		{
			particles[i] = new ProjectilePotionEffect(mob.getEntity().getEyeLocation(), effect, affectPlayersOnly, ParticleType.SMOKE_NORMAL);
			particleTargets[i] = mob.getEntity().getEyeLocation();
			particleYaw[i] = 2.0f * (float) Math.PI * random.nextFloat();
			particlePitch[i] = 2.0f * (float) Math.PI * random.nextFloat();
			particleDistance[i] = 1.0f + random.nextFloat() * (settings.getScaledValue(alternative, "Range", scale, 0.0f) - 1.0f);
		}
	}

	@Override
	public void onTick()
	{
		timer += 3.6 * Math.PI / 180.0;
		for (int i = 0; i < particles.length; i++)
		{
			// Compute where the particle is supposed to be
			double posX = particleDistance[i] * Math.cos(timer);
			double posZ = particleDistance[i] * Math.sin(timer);

			double sinX = Math.sin(particlePitch[i]);
			double sinY = Math.sin(particleYaw[i]);
			double cosX = Math.cos(particlePitch[i]);
			double cosY = Math.cos(particleYaw[i]);

			Location orbitCenter = mob.getEntity().getEyeLocation();
			particleTargets[i].setX(orbitCenter.getX() + cosY * posX + cosX * sinY * posZ);
			particleTargets[i].setY(orbitCenter.getY() - sinX * posZ);
			particleTargets[i].setZ(orbitCenter.getZ() - sinY * posX + cosX * cosY * posZ);

			// Move the particle where it is supposed to be
			particles[i].moveTowardsLocation(particleTargets[i], 1.75f, 0.1f);
			particles[i].onTick();
		}
	}

}
