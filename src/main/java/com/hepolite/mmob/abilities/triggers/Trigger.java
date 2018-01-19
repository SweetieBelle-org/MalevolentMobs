package com.hepolite.mmob.abilities.triggers;

import org.bukkit.entity.LivingEntity;

import com.hepolite.coreutility.log.Log;
import com.hepolite.mmob.abilities.targets.Target;

public abstract class Trigger
{
	private final TriggerType type;
	private final String name;

	private Target target;

	protected Trigger(TriggerType type, String name)
	{
		this.type = type;
		this.name = name;
	}

	/** Returns the type of the trigger */
	public final TriggerType getType()
	{
		return type;
	}

	/** Returns the name of the trigger */
	public final String getName()
	{
		return name;
	}

	/** Returns what the trigger will be targeting */
	public final Target getTarget()
	{
		return target;
	}

	/** Sets what the trigger will be targeting */
	public final void setTarget(Target target)
	{
		if (target != null)
			this.target = target;
		else
			Log.warning("Cannot assign a null target to trigger " + getName());
	}

	/** Invoked whenever the trigger has been triggered */
	public final void onTriggered(LivingEntity caster)
	{
		for (LivingEntity entity : target.getTargets(caster, caster.getEyeLocation()))
		{

		}
	}
}
