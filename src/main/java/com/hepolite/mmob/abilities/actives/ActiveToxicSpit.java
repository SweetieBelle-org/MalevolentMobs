package com.hepolite.mmob.abilities.actives;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveProjectile;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;
import com.hepolite.mmob.projectiles.ProjectileBoltToxic;
import com.hepolite.mmob.settings.Settings;

/**
 * The toxic spit involves several blobs of toxic being spit towards the target, dealing poison damage
 */
public class ActiveToxicSpit extends ActiveProjectile
{
	private float projectileSpeed = 0.0f;
	private int projectileStrength = 1;
	private int projectileDuration = 0;
	private float projectileRange = 0.0f;
	private float projectileInaccuracy = 0.0f;
	private boolean affectPlayersOnly = true;

	private int count = 0;

	public ActiveToxicSpit(MalevolentMob mob, float scale)
	{
		super(mob, "Toxic Spit", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		projectileSpeed = settings.getScaledValue(alternative, "Speed", scale, 0.0f);
		projectileStrength = (int) settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		projectileDuration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
		projectileRange = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		projectileInaccuracy = settings.getScaledValue(alternative, "Inaccuracy", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");

		count = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget >= 7.5f && distanceToTarget <= 22.5f;
	}

	@Override
	public void cast(LivingEntity target)
	{
		mob.getEntity().getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_SLIME_ATTACK, 1.0f, -0.5f);
		
		for (int i = 0; i < count; i++)
			super.cast(target);
	}

	@Override
	protected Projectile createProjectile(LivingEntity target)
	{
		return new ProjectileBoltToxic(mob.getEntity(), target, projectileSpeed, projectileStrength, projectileDuration, projectileRange, projectileInaccuracy, affectPlayersOnly);
	}

}
