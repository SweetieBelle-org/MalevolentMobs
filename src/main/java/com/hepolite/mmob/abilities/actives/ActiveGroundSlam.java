package com.hepolite.mmob.abilities.actives;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The ground slam active will cause the mob to jump towards its enemies; when landing, it will knock the enemies up into the air and deal damage
 */
public class ActiveGroundSlam extends Active
{
	private boolean onGround = true;
	private int inAirTimer = -1;

	private float strength = 0.0f;
	private float knockupStrength = 0.0f;
	private float minRange = 0.0f;
	private float maxRange = 0.0f;
	private boolean affectPlayersOnly = true;

	public ActiveGroundSlam(MalevolentMob mob, float scale)
	{
		super(mob, "Ground Slam", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		knockupStrength = settings.getScaledValue(alternative, "Knockup", scale, 0.0f);
		maxRange = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		minRange = 0.5f * maxRange;

		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 12.0f && mob.getEntity().isOnGround();
	}

	@Override
	public void cast(LivingEntity target)
	{
		LivingEntity self = mob.getEntity();
		inAirTimer = 0;

		// The attacker will leap towards the target, if it has one
		if (target != null)
		{
			Location difference = target.getLocation().subtract(self.getLocation()).multiply(0.175);
			self.setVelocity(self.getVelocity().add(new Vector(difference.getX(), 0.8, difference.getZ())));
		}
		// Otherwise, the attacker just jumps up into the air
		else
			self.setVelocity(self.getVelocity().add(new Vector(0.0, 0.7, 0.0)));
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if (inAirTimer == -1)
			return;

		// Count the ticks the mob has been in the air
		if (!mob.getEntity().isOnGround())
		{
			onGround = false;
			inAirTimer++;
			mob.getEntity().setFallDistance(0.0f);
		}

		// Check for impact with the ground
		if (!onGround && mob.getEntity().isOnGround())
		{
			if (inAirTimer >= 10)
			{
				mob.getEntity().getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 0.0f);

				// Knock up all entities in range, that are standing on the ground
				List<LivingEntity> entities = Common.getEntitiesInRange(mob.getEntity().getLocation(), maxRange);
				for (LivingEntity entity : entities)
				{
					if (!entity.isOnGround())
						continue;

					float multiplier = 1.0f;
					float distance = (float) mob.getEntity().getLocation().distance(entity.getLocation());
					if (distance > minRange)
						multiplier = (maxRange - distance) / (maxRange - minRange);

					if (!affectPlayersOnly || entity instanceof Player)
					{
						entity.setVelocity(entity.getVelocity().add(new Vector(0.0, knockupStrength * multiplier, 0.0)));
						Common.doDamage(strength * multiplier, entity, mob.getEntity(), DamageCause.ENTITY_ATTACK);
					}
				}
			}
			inAirTimer = -1;
			onGround = true;
		}
	}

}
