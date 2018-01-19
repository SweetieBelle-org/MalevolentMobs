package com.hepolite.mmob.abilities.targets;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class Target
{
	private final TargetType type;
	private final String name;

	protected Target(TargetType type, String name)
	{
		this.type = type;
		this.name = name;
	}

	/** Returns the type of the target */
	public final TargetType getType()
	{
		return type;
	}

	/** Returns the name of the target */
	public final String getName()
	{
		return name;
	}

	/** Returns the collection of all entities that are targeted by this target */
	public abstract Collection<LivingEntity> getTargets(LivingEntity caster, Location origin);
}
