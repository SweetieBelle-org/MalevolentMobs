package com.hepolite.mmob.projectiles;

import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.MathHelper;

public class ProjectileFirework extends Projectile
{
	private LivingEntity caster = null;
	private Firework firework = null;

	private float strength = 0.0f;
	private float radius = 0.0f;
	private boolean affectPlayersOnly = true;

	public ProjectileFirework(LivingEntity caster, boolean factorInGravity, Firework firework, float strength, float radius, boolean affectPlayersOnly)
	{
		this.caster = caster;
		this.position = caster.getEyeLocation();
		this.firework = firework;
		this.velocity = firework.getVelocity();
		this.strength = strength;
		this.radius = radius;
		this.affectPlayersOnly = affectPlayersOnly;
	}

	@Override
	public void onTick()
	{
		if (!firework.isDead())
		{
			// Make sure the rocket sticks to whatever it hits when flying
			if (Common.getObstruction(position, position.clone().add(velocity), caster) != null)
				velocity.setX(0.0).setY(0.0).setZ(0.0);
			position.add(velocity);

			firework.setVelocity(velocity);

			position.add(MathHelper.gravity.clone().multiply(0.5));
			velocity.add(MathHelper.gravity);
		}
		else
		{
			// Blow up everything
			Common.createExplosion(position, strength, radius, affectPlayersOnly, caster);
			kill();
		}
	}
}
