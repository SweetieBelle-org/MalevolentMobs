package com.hepolite.mmob.abilities.targets;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class TargetNull extends Target
{
	public TargetNull()
	{
		super(TargetType.NULL, "null");
	}

	@Override
	public Collection<LivingEntity> getTargets(LivingEntity caster, Location origin)
	{
		return Collections.emptyList();
	}
}
