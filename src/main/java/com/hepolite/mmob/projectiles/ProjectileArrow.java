package com.hepolite.mmob.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;

public class ProjectileArrow extends ProjectileBolt
{
	private Arrow arrow = null;

	public ProjectileArrow(LivingEntity caster, LivingEntity target, float speed, boolean factorInGravity, float inaccuracy)
	{
		super(caster, target, speed, factorInGravity, inaccuracy);
		arrow = caster.launchProjectile(Arrow.class, velocity);
	}

	public ProjectileArrow(LivingEntity caster, boolean factorInGravity, Arrow arrow)
	{
		super(caster, factorInGravity);
		this.arrow = arrow;
		this.velocity = arrow.getVelocity();
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if (arrow.isValid() && isAlive())
			arrow.setVelocity(velocity);
	}

	@Override
	protected void applyEffects(Location location)
	{
	}

	@Override
	protected void displayBolt(Location location)
	{
	}
}
