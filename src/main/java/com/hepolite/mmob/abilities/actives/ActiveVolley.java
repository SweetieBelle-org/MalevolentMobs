package com.hepolite.mmob.abilities.actives;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveProjectile;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;
import com.hepolite.mmob.projectiles.ProjectileArrow;
import com.hepolite.mmob.settings.Settings;

/**
 * The volley involves several arrows being fired towards the target
 */
public class ActiveVolley extends ActiveProjectile
{
	private float projectileSpeed = 0.0f;
	private float projectileInaccuracy = 0.0f;

	private int count = 0;
	private boolean affectedByGravity = false;

	public ActiveVolley(MalevolentMob mob, float scale)
	{
		super(mob, "Volley", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		projectileSpeed = settings.getScaledValue(alternative, "Speed", scale, 0.0f);
		projectileInaccuracy = settings.getScaledValue(alternative, "Inaccuracy", scale, 0.0f);

		count = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);
		affectedByGravity = settings.getBoolean(alternative, "affectedByGravity");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget >= 7.5f && distanceToTarget <= 40.0f;
	}

	@Override
	public void cast(LivingEntity target)
	{
		// Play the fire sound, then send the arrow flying
		mob.getEntity().getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);

		for (int i = 0; i < count; i++)
			super.cast(target);
	}

	@Override
	protected Projectile createProjectile(LivingEntity target)
	{
		return new ProjectileArrow(mob.getEntity(), target, projectileSpeed, affectedByGravity, projectileInaccuracy);
	}
}
