package com.hepolite.mmob.abilities.actives;

import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveProjectile;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;
import com.hepolite.mmob.projectiles.ProjectileBoltFracturingBlast;
import com.hepolite.mmob.settings.Settings;

public class ActiveFracturingBlast extends ActiveProjectile
{
	private float projectileSpeed = 0.0f;
	private int projectileStrength = 1;
	private float projectileRange = 0.0f;
	private boolean affectPlayersOnly = true;

	public ActiveFracturingBlast(MalevolentMob mob, float scale)
	{
		super(mob, "Fracturing Blast", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		projectileSpeed = settings.getScaledValue(alternative, "Speed", scale, 0.0f);
		projectileStrength = (int) settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		projectileRange = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget >= 20.0f && distanceToTarget <= 70.0f;
	}

	@Override
	protected Projectile createProjectile(LivingEntity target)
	{
		return new ProjectileBoltFracturingBlast(mob.getEntity(), target, projectileSpeed, projectileStrength, projectileRange, affectPlayersOnly);
	}
}
