package com.hepolite.mmob.abilities.actives;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.abilities.ActiveTargetSplash;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The fire burst is a burst of fire from the casting entity, dealing fire damage to all entities in the vicinity
 */
public class ActiveFireBurst extends ActiveTargetSplash
{
	private float strength = 0.0f;
	private int duration = 0;

	public ActiveFireBurst(MalevolentMob mob, float scale)
	{
		super(mob, "Fire Burst", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 1.5f * range;
	}

	@Override
	protected void setTargetEntity(LivingEntity target)
	{
		// Want to detonate the effect at the caster, not the target
		super.setTargetEntity(mob.getEntity());
	}

	@Override
	public void applyEffect(LivingEntity target)
	{
		Common.doDamage(strength, target, mob.getEntity(), DamageCause.FIRE);
		target.setFireTicks(duration);
	}

	@Override
	protected void displayArea(LivingEntity target, float range)
	{
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.333f);
		ParticleEffect.play(ParticleType.SMOKE_LARGE, target.getLocation(), 0.15f, 25, 1.0f);
	}

	@Override
	protected void displayAttack(LivingEntity target, float range)
	{
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 0.0f);
		ParticleEffect.play(ParticleType.FLAME, target.getLocation(), 0.15f, 35, 1.0f);
	}
}
