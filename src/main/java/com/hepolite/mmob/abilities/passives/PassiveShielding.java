package com.hepolite.mmob.abilities.passives;

import org.bukkit.event.entity.EntityDamageEvent;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public class PassiveShielding extends Passive
{
	float strength = 0.0f;

	public PassiveShielding(MalevolentMob mob, float scale)
	{
		super(mob, "Shielding", Priority.HIGH, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		event.setDamage(Math.min(strength, event.getDamage()));
	}
}
