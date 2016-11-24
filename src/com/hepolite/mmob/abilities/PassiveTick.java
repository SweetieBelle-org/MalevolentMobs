package com.hepolite.mmob.abilities;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

/**
 * The ticking passive applies an effect every specific tick
 */
public abstract class PassiveTick extends Passive
{
	// Control variables
	protected int repeats = -1;
	private int startupDelay = 0;
	private int repeatDelay = 0;

	private int timerStartup = 0;
	private int timerRepeat = 0;

	/** Initialization */
	protected PassiveTick(MalevolentMob mob, String name, Priority priority, float scale)
	{
		super(mob, name, priority, scale);
	}

	/** Loads up some settings from the configuration file */
	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		repeats = (int) settings.getInteger(alternative, "repeats", -1);
		startupDelay = (int) settings.getScaledValue(alternative, "StartupDelay", scale, 0.0f);
		repeatDelay = (int) settings.getScaledValue(alternative, "RepeatDelay", scale, 0.0f);
	}

	@Override
	public void onTick()
	{
		if (repeats == 0)
			return;

		// Only care about the effects once the startup delay has been reached
		if (++timerStartup > startupDelay)
		{
			if (++timerRepeat > repeatDelay)
			{
				applyTickEffect();
				timerRepeat = 0;
				repeats--;
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Every time the effect of the aura is applied to a target, this method is called */
	protected abstract void applyTickEffect();

	/** Resets the delay timer */
	public void resetDelayTimer()
	{
		timerRepeat = 0;
	}

	/** Resets the delay timer */
	public void resetStartupTimer()
	{
		timerStartup = 0;
	}
}
