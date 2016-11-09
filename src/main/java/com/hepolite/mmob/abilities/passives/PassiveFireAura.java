package com.hepolite.mmob.abilities.passives;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.PassiveAura;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The fire aura will burn all entities within the range
 */
public class PassiveFireAura extends PassiveAura
{
	public PassiveFireAura(MalevolentMob mob, float scale)
	{
		super(mob, "Fire Aura", scale);
	}

	@Override
	protected void applyAuraEffect(LivingEntity entity)
	{
		entity.setFireTicks(50);
	}

	@Override
	protected void displayAura(Location location, float range)
	{
		ParticleEffect.play(ParticleType.FLAME, location, 0.05f, (int) (15.0f * range), 0.5f * range);
	}
}
