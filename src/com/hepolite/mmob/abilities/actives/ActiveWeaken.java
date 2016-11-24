package com.hepolite.mmob.abilities.actives;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public class ActiveWeaken extends Active
{
	private List<PotionEffect> possibleEffects = new LinkedList<PotionEffect>();

	private int effectsToApply = 0;

	public ActiveWeaken(MalevolentMob mob, float scale)
	{
		super(mob, "Weaken", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		effectsToApply = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);

		// Read up all the potion effects
		List<String> effects = settings.getStringList(alternative, "effects");
		for (String effect : effects)
		{
			String[] components = effect.split("=");
			if (components.length == 5)
			{
				PotionEffectType type = PotionEffectType.getByName(components[0]);
				if (type != null)
				{
					try
					{
						float duration = 20.0f * (Float.parseFloat(components[1]) + scale * Float.parseFloat(components[2]));
						float strength = Float.parseFloat(components[3]) + scale * Float.parseFloat(components[4]);
						possibleEffects.add(new PotionEffect(type, (int) duration, (int) strength - 1));
					}
					catch (Exception exception)
					{
						Log.log("Invalid format on numbers in string '" + effect + "'!", Level.WARNING);
					}
				}
				else
					Log.log("Failed to parse potion effect type '" + components[0] + "', inalid type!", Level.WARNING);
			}
			else
				Log.log("Failed to parse potion effect string '" + effect + "'!", Level.WARNING);
		}
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 15.0f;
	}

	@Override
	public void cast(LivingEntity target)
	{
		// Notify players of something happening
		mob.getEntity().getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 0.5f);
		
		// Find effects to throw on the target
		List<PotionEffect> mainList = new LinkedList<PotionEffect>(possibleEffects);
		List<PotionEffect> potionEffectsToApply = new LinkedList<PotionEffect>();

		int count = Math.min(mainList.size(), effectsToApply);
		for (int i = 0; i < count; i++)
		{
			PotionEffect effect = mainList.get(random.nextInt(mainList.size()));
			potionEffectsToApply.add(effect);
			mainList.remove(effect);
		}

		// Apply the effects
		for (PotionEffect effect : potionEffectsToApply)
			target.addPotionEffect(effect);
	}
}
