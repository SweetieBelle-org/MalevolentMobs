package com.hepolite.mmob.abilities;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The splash target will allow an active to perform some logic on all entities within range of the given target
 */
public abstract class ActiveTargetSplash extends Active
{
	// Control variables
	protected float range = 0.0f;
	private boolean affectPlayersOnly = true;

	private int delayTimer = -1;
	protected int delay = 0;
	private LivingEntity target = null;

	/** Initialization */
	protected ActiveTargetSplash(MalevolentMob mob, String name, Priority priority, float scale)
	{
		super(mob, name, priority, scale);
	}

	/** Loads up some settings from the configuration file */
	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		delay = (int) settings.getScaledValue(alternative, "Delay", scale, 0.0f);

		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if (delayTimer == -1)
			return;

		// Tick down the delay, if applicable
		if (++delayTimer > delay)
		{
			delayTimer = -1;

			// Find all entities nearby and hit them, don't hit the caster
			List<LivingEntity> entities = Common.getEntitiesInRange(target.getLocation(), range);
			for (LivingEntity entity : entities)
			{
				if (entity == mob.getEntity())
					continue;
				if (!affectPlayersOnly || entity instanceof Player)
					applyEffect(entity);
			}
			displayAttack(target, range);
		}
	}

	@Override
	public void cast(LivingEntity target)
	{
		delayTimer = 0;
		setTargetEntity(target);
		displayArea(this.target, range);
	}

	/** Assigns the entity that will be tagged by the ability */
	protected void setTargetEntity(LivingEntity target)
	{
		this.target = target;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Called to apply the effect on the given entity */
	public abstract void applyEffect(LivingEntity target);

	/** This method is used when the attack starts, used to display some effects where the attack will take place */
	protected abstract void displayArea(LivingEntity target, float range);

	/** This method is used when the attack ends, used to display some effects */
	protected abstract void displayAttack(LivingEntity target, float range);
}
