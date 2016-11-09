package com.hepolite.mmob.abilities.passives;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import com.hepolite.mmob.abilities.PassiveTick;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

/**
 * The regeneration effect allows mobs to regenerate health over time, even during combat
 */
public class PassiveRegeneration extends PassiveTick
{
	float regeneration = 0.0f;

	public PassiveRegeneration(MalevolentMob mob, float scale)
	{
		super(mob, "Regeneration", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		regeneration = settings.getScaledValue(alternative, "Regeneration", scale, 0.0f);
	}

	@Override
	protected void applyTickEffect()
	{
		LivingEntity entity = mob.getEntity();
		entity.setHealth(Math.min(regeneration + entity.getHealth(), entity.getMaxHealth()));
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		resetStartupTimer();
		resetDelayTimer();
	}
}
