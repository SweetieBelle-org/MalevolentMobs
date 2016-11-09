package com.hepolite.mmob.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.MathHelper;

/**
 * The bolt projectile flies in a straight line towards the target, stopping on any block/entity it passes through on the way
 */
public abstract class ProjectileBolt extends Projectile
{
	protected LivingEntity caster = null;
	private Location startLocation = null;
	private boolean factorInGravity = false;

	protected ProjectileBolt(LivingEntity caster, LivingEntity target, float speed, boolean factorInGravity, float inaccuracy)
	{
		this(caster, factorInGravity);

		// Compute the velocity needed to hit the target
		Vector deltaVelocity = new Vector();
		if (random.nextBoolean() && target instanceof Player)
			deltaVelocity = MMobListener.getPlayerVelocity((Player) target);
		velocity = MathHelper.computeAdvancedPredictionVector(position, target.getEyeLocation(), deltaVelocity, speed, factorInGravity);
		velocity.add(new Vector(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).multiply(inaccuracy));
	}

	protected ProjectileBolt(LivingEntity caster, boolean factorInGravity)
	{
		this.caster = caster;
		this.position = caster.getEyeLocation();
		startLocation = position.clone();

		this.factorInGravity = factorInGravity;
	}

	@Override
	public void onTick()
	{
		// Make sure that the bolt doesn't go too far away
		if (startLocation.distanceSquared(position) > 22500.0)
		{
			kill();
			return;
		}

		// Process the bolt otherwise
		displayBolt(position);

		Location obstructionLocation = Common.getObstruction(position, position.clone().add(velocity), caster);
		if (obstructionLocation != null)
		{
			kill();
			applyEffects(obstructionLocation);
		}
		else
		{
			position.add(velocity);
			if (factorInGravity)
			{
				position.add(MathHelper.gravity.clone().multiply(0.5));
				velocity.add(MathHelper.gravity);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////

	/** Called to apply the effects on the specified location */
	protected abstract void applyEffects(Location location);

	/** Displays the bolt as it travels */
	protected abstract void displayBolt(Location location);
}
