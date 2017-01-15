package com.hepolite.mmob.abilities.actives;

import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveProjectile;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;
import com.hepolite.mmob.projectiles.ProjectileGrenade;
import com.hepolite.mmob.settings.Settings;

/**
 * The grenade active will launch a grenade that will detonate after a certain amount of time
 */
public class ActiveGrenade extends ActiveProjectile
{
	private float projectileSpeed = 0.0f;
	private float projectileStrength = 0;
	private int projectileDuration = 0;
	private float projectileRange = 0.0f;
	private boolean affectPlayersOnly = true;

	public ActiveGrenade(MalevolentMob mob, float scale)
	{
		super(mob, "Grenade", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		projectileSpeed = settings.getScaledValue(alternative, "Speed", scale, 0.0f);
		projectileStrength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		projectileDuration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
		projectileRange = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget >= 2.0f && distanceToTarget <= 15.0f;
	}

	@Override
	protected Projectile createProjectile(LivingEntity target)
	{
		return new ProjectileGrenade(mob.getEntity(), target, projectileSpeed, projectileStrength, projectileDuration, projectileRange, affectPlayersOnly);
	}
}
