package com.hepolite.mmob.abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Causes all entities within the area of effect to be affected by the effects of the active, after the delay has passed
 */
public abstract class ActiveAreaSplash extends ActiveArea
{
	// Control variables
	private float range = 0.0f;
	private boolean affectPlayersOnly = true;

	/** Initialization */
	protected ActiveAreaSplash(MalevolentMob mob, String name, Priority priority, float scale)
	{
		super(mob, name, priority, scale);
	}

	/** Loads up some settings from the configuration file */
	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public void applyEffect(Location location)
	{
		// Find all entities nearby and hit them, don't hit the caster
		List<LivingEntity> entities = Common.getEntitiesInRange(location, range);
		for (LivingEntity entity : entities)
		{
			if (entity == mob.getEntity())
				continue;
			if (!affectPlayersOnly || entity instanceof Player)
				applyEffect(entity);
		}
		displayAttack(location, range);
	}
	
	@Override
	protected void displayArea(Location location)
	{
		displayArea(location, range);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Called to apply effects to a entity within the range of the splash */
	public abstract void applyEffect(LivingEntity target);
	
	/** This method is used when the attack starts, used to display some effects where the attack will take place */
	protected abstract void displayArea(Location location, float range);

	/** This method is used when the attack ends, used to display some effects */
	protected abstract void displayAttack(Location location, float range);
}
