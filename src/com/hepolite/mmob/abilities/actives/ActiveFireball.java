package com.hepolite.mmob.abilities.actives;

import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveProjectile;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;
import com.hepolite.mmob.projectiles.ProjectileFireball;
import com.hepolite.mmob.settings.Settings;

/**
 * The fireball sends away a projectile that will home in on the target it has specified
 */
public class ActiveFireball extends ActiveProjectile
{
	private float projectileSpeed = 0.0f;
	private float projectileTurnFactor = 0.0f;
	private float projectileStrength = 0.0f;
	private int projectileDuration = 0;
	private float projectileRange = 0.0f;
	private boolean affectPlayersOnly = true;

	public ActiveFireball(MalevolentMob mob, float scale)
	{
		super(mob, "Fireball", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		projectileSpeed = settings.getScaledValue(alternative, "Speed", scale, 0.0f);
		projectileTurnFactor = settings.getScaledValue(alternative, "TurnFactor", scale, 0.0f);
		projectileStrength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		projectileDuration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
		projectileRange = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget >= 7.0f && distanceToTarget <= 40.0f;
	}

	@Override
	protected Projectile createProjectile(LivingEntity target)
	{
		return new ProjectileFireball(mob.getEntity(), target, projectileSpeed, projectileTurnFactor, projectileStrength, projectileDuration, projectileRange, affectPlayersOnly);
	}
}
