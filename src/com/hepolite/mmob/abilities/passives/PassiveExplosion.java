package com.hepolite.mmob.abilities.passives;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Mobs with the explosion passive will blow up on death
 */
public class PassiveExplosion extends Passive
{
	private float strength = 0.0f;
	private float radius = 0.0f;
	private boolean affectPlayersOnly = true;

	public PassiveExplosion(MalevolentMob mob, float scale)
	{
		super(mob, "Explosion", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		radius = settings.getScaledValue(alternative, "Radius", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public void onDie()
	{
		Common.createExplosionWithEffect(mob.getEntity().getEyeLocation(), strength, radius, affectPlayersOnly, mob.getEntity());
	}
	
	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		LivingEntity entity = mob.getEntity();
		if (entity.getHealth() < 0.15 * entity.getMaxHealth())
			entity.getWorld().playSound(entity.getEyeLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 0.0f);
	}
}
