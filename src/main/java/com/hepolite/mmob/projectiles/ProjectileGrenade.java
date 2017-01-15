package com.hepolite.mmob.projectiles;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.MathHelper;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

public class ProjectileGrenade extends Projectile
{
	private LivingEntity caster = null;

	private float strength = 0.0f;
	private int duration = 0;
	private float range = 0.0f;
	private boolean affectPlayersOnly = true;

	public ProjectileGrenade(LivingEntity caster, LivingEntity target, float speed, float strength, int duration, float range, boolean affectPlayersOnly)
	{
		this.caster = caster;
		this.position = caster.getEyeLocation();

		// Compute the velocity needed to hit the target
		Vector deltaVelocity = new Vector();
		if (random.nextBoolean() && target instanceof Player)
			deltaVelocity = MMobListener.getPlayerVelocity((Player) target);
		velocity = MathHelper.computeAdvancedPredictionVector(position, target.getEyeLocation(), deltaVelocity, speed, true);

		// Store data
		this.strength = strength;
		this.duration = duration;
		this.range = range;
		this.affectPlayersOnly = affectPlayersOnly;
	}

	@Override
	public void onTick()
	{
		if (--duration <= 0)
		{
			kill();
			Common.createExplosionWithEffect(position, strength, range, affectPlayersOnly, caster);
		}
		else
		{
			// If hitting something, kill momentum
			if (Common.getObstruction(position, position.clone().add(velocity), caster) != null)
				velocity.setX(0.0).setY(0.0).setZ(0.0);
			position.add(velocity);

			position.add(MathHelper.gravity.clone().multiply(0.5));
			velocity.add(MathHelper.gravity);
			
			// Display the grenade
			ParticleEffect.play(ParticleType.CLOUD, position, 0.0f, 12, 0.25f);
		}
	}

}
