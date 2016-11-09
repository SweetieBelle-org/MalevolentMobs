package com.hepolite.mmob.abilities;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

/**
 * The active area ability allows an active to affect a specified area
 */
public abstract class ActiveArea extends Active
{
	// Control variables
	private int delayTimer = -1;
	protected int delay = 0;
	private Location targetLocation = null;

	/** Initialization */
	protected ActiveArea(MalevolentMob mob, String name, Priority priority, float scale)
	{
		super(mob, name, priority, scale);
	}

	/** Loads up some settings from the configuration file */
	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		delay = (int) settings.getScaledValue(alternative, "Delay", scale, 0.0f);
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
			applyEffect(targetLocation);
		}
	}

	@Override
	public void cast(LivingEntity target)
	{
		delayTimer = 0;
		targetLocation = target.getLocation();

		displayArea(targetLocation);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Called to apply the effect on the given entity */
	public abstract void applyEffect(Location location);

	/** This method is used when the attack starts, used to display some effects where the attack will take place */
	protected abstract void displayArea(Location location);
}
