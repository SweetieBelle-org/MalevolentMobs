package com.hepolite.mmob.abilities.actives;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The virulent grasp will make the entity hold the target in a solid grasp, letting go after a short amount of time
 */
public class ActiveVirulentGrasp extends Active
{
	private int duration = 0;
	private int durationTimer = -1;

	private LivingEntity target = null;

	public ActiveVirulentGrasp(MalevolentMob mob, float scale)
	{
		super(mob, "Virulent Grasp", Priority.LOW, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 2.0f;
	}

	@Override
	public void cast(LivingEntity target)
	{
		durationTimer = duration;
		this.target = target;
	}

	@Override
	public void onTick()
	{
		super.onTick();

		if (durationTimer != -1)
		{
			durationTimer--;

			// Make sure the target is attached to the mob, and detached when relevant
			if (target != null && target.isValid() && !target.isDead())
				mob.getEntity().setPassenger(target);
			if (durationTimer <= 0)
				mob.getEntity().eject();
		}
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		if (durationTimer != -1)
		{
			// Mitigate all damage to the target, if possible
			if (target != null && target.isValid() && !target.isDead())
			{
				Common.doDamage(event.getDamage(), target, mob.getEntity(), DamageCause.MAGIC);
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event)
	{
		if (durationTimer != -1)
		{
			// Do NOT deal damage to the mob being held in the grasp
			if (event.getEntity() == target)
				event.setCancelled(true);
		}
	}

}
