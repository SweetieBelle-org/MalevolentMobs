package com.hepolite.mmob.abilities.targets;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class TargetCaster extends Target
{
	public TargetCaster()
	{
		super(TargetType.CASTER, "caster");
	}

	@Override
	public final Collection<LivingEntity> getTargets(LivingEntity caster, Location origin)
	{
		return Collections.singleton(caster);
	}
}
