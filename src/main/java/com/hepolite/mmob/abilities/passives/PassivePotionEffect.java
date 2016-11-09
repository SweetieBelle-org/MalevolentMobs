package com.hepolite.mmob.abilities.passives;

import java.util.logging.Level;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.abilities.PassiveTick;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

/**
 * The potion effect passive allows a potion effect to continuously be applied to the given mob
 */
public class PassivePotionEffect extends PassiveTick
{
	private PotionEffect effect = null;

	public PassivePotionEffect(MalevolentMob mob, float scale)
	{
		super(mob, "Potion Effect", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		// Create a new potion effect
		PotionEffectType type = null;
		try
		{
			type = PotionEffectType.getByName(settings.getString(alternative, "effect"));
			effect = new PotionEffect(type, (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f), (int) settings.getScaledValue(alternative, "Strength", scale, 0.0f) - 1);
		}
		catch (Exception exception)
		{
			Log.log("Attempted to load up invalid potion effect type '" + settings.getString(alternative, "effect") + "'", Level.WARNING);
			return;
		}
	}

	@Override
	protected void applyTickEffect()
	{
		if (effect != null)
			mob.getEntity().addPotionEffect(effect);
	}
}
