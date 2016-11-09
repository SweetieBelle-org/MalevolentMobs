package com.hepolite.mmob.abilities.passives;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.abilities.PassiveAura;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The freezing aura causes the nearby targets to be slowed and take take small amounts of frost damage
 */
public class PassiveFreezingAura extends PassiveAura
{
	private float damage = 0.0f;
	private PotionEffect effect = null;

	private int ticksToDamage = 0;
	private int currentTick = 0;

	public PassiveFreezingAura(MalevolentMob mob, float scale)
	{
		super(mob, "Freezing Aura", scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		ticksToDamage = (int) settings.getScaledValue(alternative, "Delay", scale, 0.0f);
		damage = settings.getScaledValue(alternative, "Damage", scale, 1.0f);

		float strength = settings.getScaledValue(alternative, "Strength", scale, 1.0f);
		effect = new PotionEffect(PotionEffectType.SLOW, 50, (int) strength - 1);
	}

	@Override
	protected void applyAuraEffect(LivingEntity entity)
	{
		entity.addPotionEffect(effect);
		if (currentTick >= ticksToDamage)
			Common.doDamage(damage, entity, mob.getEntity(), DamageCause.MAGIC);
	}

	@Override
	protected void displayAura(Location location, float range)
	{
		ParticleEffect.play(ParticleType.SNOW_SHOVEL, location, 0.05f, (int) (20.0f * range), 0.5f * range);
		
		if (currentTick >= ticksToDamage)
			currentTick = 0;
		else
			currentTick++;
	}

}
