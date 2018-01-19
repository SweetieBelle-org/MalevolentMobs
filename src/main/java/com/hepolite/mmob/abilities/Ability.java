package com.hepolite.mmob.abilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.LivingEntity;

import com.hepolite.coreutility.log.Log;
import com.hepolite.mmob.abilities.targets.Target;
import com.hepolite.mmob.abilities.targets.TargetCaster;
import com.hepolite.mmob.abilities.targets.TargetNull;
import com.hepolite.mmob.abilities.triggers.Trigger;
import com.hepolite.mmob.abilities.triggers.TriggerTick;
import com.hepolite.mmob.abilities.triggers.TriggerType;

public class Ability
{
	private final AbilityType type;
	private final Map<String, Target> targets = new HashMap<String, Target>();
	private final Map<String, Trigger> triggers = new HashMap<String, Trigger>();

	public Ability(AbilityType type)
	{
		this.type = type;

		addTarget(new TargetNull());
		addTarget(new TargetCaster());
	}

	/** Invoked every tick */
	public final void onTick(LivingEntity caster, int tick)
	{
		for (Trigger trigger : triggers.values())
		{
			if (trigger.getType() == TriggerType.TICK && tick % ((TriggerTick) trigger).getFrequency() == 0)
				trigger.onTriggered(caster);
		}
	}

	/** Returns the type of the ability */
	public final AbilityType getType()
	{
		return type;
	}

	/** Adds the target to the ability */
	public final void addTarget(Target target)
	{
		if (target == null)
			return;
		Target old = targets.put(target.getName(), target);
		if (old != null)
			Log.warning("The target " + target.getName() + " existed and was overwritten");
	}

	/** Return the target with the given name; returns null if it does not exist */
	public final Target getTarget(String name)
	{
		return targets.get(name);
	}

	/** Adds the trigger to the ability */
	public final void addTrigger(Trigger trigger)
	{
		if (trigger == null)
			return;
		if (trigger.getTarget() == null)
			trigger.setTarget(getTarget("null"));
		Trigger old = triggers.put(trigger.getName(), trigger);
		if (old != null)
			Log.warning("The trigger " + trigger.getName() + " existed and was overwritten");
	}

	/** Return the trigger with the given name; returns null if it does not exist */
	public final Trigger getTriggers(String name)
	{
		return triggers.get(name);
	}
}
