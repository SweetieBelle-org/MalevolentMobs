package com.hepolite.mmob.abilities.passives;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.PassiveAura;
import com.hepolite.mmob.mobs.MalevolentMob;

public class PassiveAntiFlight extends PassiveAura
{
	public PassiveAntiFlight(MalevolentMob mob, float scale)
	{
		super(mob, "Anti_Flight", scale);
	}

	@Override
	protected void applyAuraEffect(LivingEntity entity)
	{
		
	}

	@Override
	protected void displayAura(Location location, float range)
	{
	}
}
