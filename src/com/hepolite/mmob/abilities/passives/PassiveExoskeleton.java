package com.hepolite.mmob.abilities.passives;

import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageEvent;

import com.hepolite.mmob.abilities.PassiveTick;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The exoskeleton gives the mob an additional layer of defense. When attacked, the exoskeleton slowly degenerates. When outside of combat for some duration, the exoskeleton recharges itself. The exoskeleton will absorb all damage until it breaks.
 */
public class PassiveExoskeleton extends PassiveTick
{
	float maxStrength = 0.0f;
	float strength = 0.0f;
	float regeneration = 0.0f;

	public PassiveExoskeleton(MalevolentMob mob, float scale)
	{
		super(mob, "Exoskeleton", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);
		repeats = -1;
		
		regeneration = settings.getScaledValue(alternative, "Regeneration", scale, 0.0f);
		maxStrength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		strength = maxStrength;
	}

	@Override
	protected void applyTickEffect()
	{
		strength = Math.min(maxStrength, strength + regeneration);
	}
	
	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		resetStartupTimer();
		resetDelayTimer();
		
		// Do nothing against natural or magical damage sources
		if (Common.isAttackNatural(event) || Common.isAttackMagic(event))
			return;
		
		// Reduce the strength of the exoskeleton
		float damage = (float) event.getDamage();
		strength -= damage;
		
		// If the shield breaks, do damage
		if (strength > 0.0f)
		{
			event.setDamage(0.0);
			mob.getEntity().getWorld().playSound(mob.getEntity().getEyeLocation(), Sound.BLOCK_ANVIL_LAND, 0.4f, 0.0f);
		}
		else
		{
			event.setDamage(-strength);
			strength = 0.0f;
		}
	}
}
