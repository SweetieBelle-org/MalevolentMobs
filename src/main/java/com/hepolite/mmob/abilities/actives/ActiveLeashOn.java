package com.hepolite.mmob.abilities.actives;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

/**
 * The leash on active allows the mob to attach itself to the target entity; it will only let go when dead, or the entity has landed on the ground
 */
public class ActiveLeashOn extends Active
{
	private LivingEntity target = null;

	private float range = 0.0f;
	private int duration = 0;

	private int timeActive = 0;
	private int timeOnGround = 0;

	public ActiveLeashOn(MalevolentMob mob, float scale)
	{
		super(mob, "Leash On", Priority.HIGH, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		range = Math.min(10.0f, range);
		duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		boolean isFlying = false;
		if (target instanceof Player)
			isFlying = ((Player) target).isFlying();

		return distanceToTarget <= range && isFlying;
	}

	@Override
	public void cast(LivingEntity target)
	{
		this.target = target;
	}

	@Override
	public void onTick()
	{
		super.onTick();

		if (target != null && target.isValid() && !target.isDead())
		{
			// While the target is valid, stick to it as best as possible
			if (mob.getEntity().getLocation().distanceSquared(target.getLocation()) <= range * range)
			{
				mob.getEntity().setLeashHolder(target);
				mob.getEntity().setFallDistance(0.0f);
			}

			// Tick each time when on ground
			if (target.isOnGround() && ++timeActive > duration)
			{
				if (++timeOnGround > 10)
				{
					mob.getEntity().setLeashHolder(null);
					target = null;
					timeActive = 0;
				}
			}
			else
				timeOnGround = 0;
		}
		else
			target = null;
	}

}
