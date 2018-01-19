package com.hepolite.mmob.abilities.triggers;

public class TriggerTick extends Trigger
{
	private final int frequency;

	public TriggerTick(String name, int frequency)
	{
		super(TriggerType.TICK, name);

		this.frequency = frequency;
	}

	/** Returns how many ticks must have passed before the trigger can take effect again */
	public final int getFrequency()
	{
		return frequency;
	}
}
