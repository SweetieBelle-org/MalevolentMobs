package com.hepolite.mmob.abilities.actives;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.abilities.ActiveAreaSplash;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.FireworksEffect;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The blazing pillar is a column of fire that deals fire damage to all entities caught in its grasp
 */
public class ActiveBlazingPillar extends ActiveAreaSplash
{
	private float strength = 0.0f;
	private int duration = 0;

	public ActiveBlazingPillar(MalevolentMob mob, float scale)
	{
		super(mob, "Blazing Pillar", Priority.NORMAL, scale);
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
		return distanceToTarget <= 30.0;
	}

	@Override
	public void applyEffect(LivingEntity target)
	{
		Common.doDamage(strength, target, mob.getEntity(), DamageCause.FIRE);
		target.setFireTicks(duration);
	}

	@Override
	protected void displayArea(Location location, float range)
	{
		ParticleEffect.play(ParticleType.SMOKE_LARGE, location, 0.05f, (int) (30.0f * range), range);
		location.getWorld().playSound(location, Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.0f);
	}

	@Override
	protected void displayAttack(Location location, float range)
	{
		Builder builder = FireworksEffect.getFireworksEffectBuilder();
		builder.with(Type.BURST);
		builder.withColor(Color.RED);
		builder.withColor(Color.ORANGE);
		builder.withColor(Color.YELLOW);
		builder.withFade(Color.OLIVE);
		builder.withFade(Color.RED);
		builder.withFade(Color.ORANGE);
		builder.withTrail();
		FireworksEffect.createFireworks(location, builder.build());
	}

}
