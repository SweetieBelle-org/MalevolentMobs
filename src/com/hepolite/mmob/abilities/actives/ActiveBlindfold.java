package com.hepolite.mmob.abilities.actives;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.abilities.ActiveAreaSplash;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.FireworksEffect;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

public class ActiveBlindfold extends ActiveAreaSplash
{
	private int duration = 0;

	public ActiveBlindfold(MalevolentMob mob, float scale)
	{
		super(mob, "Blindfold", Priority.LOW, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 65.0f;
	}

	@Override
	public void applyEffect(LivingEntity target)
	{
		target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1), true);
	}

	@Override
	protected void displayArea(Location location, float range)
	{
		location.getWorld().playSound(location, Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, -0.333f);
		ParticleEffect.play(ParticleType.SMOKE_NORMAL, location, 0.15f, 25, 1.0f);
	}

	@Override
	protected void displayAttack(Location location, float range)
	{
		Builder builder = FireworksEffect.getFireworksEffectBuilder();
		builder.withColor(Color.BLACK);
		builder.withTrail();
		FireworksEffect.createFireworks(location, builder.build());
	}

}
