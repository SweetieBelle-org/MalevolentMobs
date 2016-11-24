package com.hepolite.mmob.abilities;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * An aura will affect all entities within the range once every second. The effects will be determined by the children of the aura class
 */
public abstract class PassiveAura extends Passive
{
	// Control variables
	protected float range = 0.0f;
	protected boolean affectPlayersOnly = true;

	private int tickTimer = 0;
	protected int updateTime = 20;

	/** Initialization */
	protected PassiveAura(MalevolentMob mob, String name, float scale)
	{
		super(mob, name, Priority.NORMAL, scale);
	}

	/** Loads up some settings from the configuration file */
	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);

		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public void onTick()
	{
		// Apply the effects only every second
		if (++tickTimer >= updateTime)
		{
			tickTimer = 0;

			// Apply the effect to every entity in range
			List<LivingEntity> entities = Common.getEntitiesInRange(mob.getEntity().getLocation(), range);
			for (LivingEntity entity : entities)
			{
				if (!affectPlayersOnly || (entity instanceof Player))
					applyAuraEffect(entity);
			}

			// Display things
			displayAura(mob.getEntity().getEyeLocation(), range);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Every time the effect of the aura is applied to a target, this method is called */
	protected abstract void applyAuraEffect(LivingEntity entity);

	/** This method is called once every second */
	protected abstract void displayAura(Location location, float range);

}
