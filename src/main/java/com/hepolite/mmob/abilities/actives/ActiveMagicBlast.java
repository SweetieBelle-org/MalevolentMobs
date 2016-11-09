package com.hepolite.mmob.abilities.actives;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveArea;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The magic blast is a explosion that appears from nothing, damaging all entities within range
 */
public class ActiveMagicBlast extends ActiveArea
{
	private float strength = 0.0f;
	private float range = 0.0f;
	private boolean affectPlayersOnly = true;

	public ActiveMagicBlast(MalevolentMob mob, float scale)
	{
		super(mob, "Magic Blast", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);

		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 30.0f;
	}

	@Override
	public void applyEffect(Location location)
	{
		Common.createExplosionWithEffect(location, strength, range, affectPlayersOnly, mob.getEntity());
	}

	@Override
	protected void displayArea(Location location)
	{
		location.getWorld().playSound(location, Sound.ENTITY_TNT_PRIMED, 1.0f, 0.0f);
		ParticleEffect.play(ParticleType.FIREWORKS_SPARK, location, 0.05f, (int) (12.0f * range), 0.5f * range);
	}
}
