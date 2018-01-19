package com.hepolite.mmob.abilities.actives;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The lifesteal active allows the mob to steal some health from the target, damaging the target and healing itself
 */
public class ActiveLifesteal extends Active
{
	private float strength = 0.0f;
	private float range = 0.0f;
	private float requiredHealthFactor = 1.0f;
	private float damageToHealthFactor = 0.0f;

	private String damageType = "";

	public ActiveLifesteal(MalevolentMob mob, float scale)
	{
		super(mob, "Lifesteal", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		requiredHealthFactor = settings.getScaledValue(alternative, "RequiredHealthFactor", scale, 0.0f);
		damageToHealthFactor = settings.getScaledValue(alternative, "DamageToHealthFactor", scale, 0.0f);

		damageType = settings.getString(alternative, "damageType");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return healthFactor <= requiredHealthFactor && distanceToTarget < range;
	}

	@Override
	public void cast(LivingEntity target)
	{
		// Compute the damage
		float damage = 0.0f;
		if (damageType.equals("percent"))
			damage = strength * (float) target.getHealth();
		else if (damageType.equals("percentMaxHealth"))
			damage = strength * (float) target.getMaxHealth();
		else
			damage = strength;

		Common.doDamage(damage, target, mob.getEntity(), DamageCause.MAGIC);
		Common.doHeal(damageToHealthFactor * damage, mob.getEntity(), RegainReason.MAGIC);

		// Tell the player they were drained
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_MAGMACUBE_JUMP, 1.0f, 0.333f);
		ParticleEffect.play(ParticleType.HEART, target.getLocation(), 0.04f, 4, 1.2f);
		ParticleEffect.play(ParticleType.REDSTONE, target.getLocation(), 0.04f, 7, 1.2f);
	}

}
