package com.hepolite.mmob.abilities.actives;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.abilities.ActiveTick;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The summon active allows the mob to call upon assistance from a series of minions
 */
public class ActiveSummonMinion extends ActiveTick
{
	private int count = 0;
	private List<String> entityTypes = null;

	private Location[] locations = null;
	private boolean doSpawns = false;

	public ActiveSummonMinion(MalevolentMob mob, float scale)
	{
		super(mob, "Summon Minion", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);
		repeats = 1;

		count = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);
		entityTypes = settings.getStringList(alternative, "types");

		locations = new Location[count];
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget < 20.0f;
	}

	@Override
	protected void applyTickEffect()
	{
		if (!doSpawns)
			return;
		doSpawns = false;

		// Get a specific type of mob
		/*EntityType type = Common.getEntityType(entityTypes.get(random.nextInt(entityTypes.size())));
		EntityType type = null;
		try
		{
			String name = entityTypes.get(random.nextInt(entityTypes.size())).toUpperCase();
			type = EntityType.valueOf(name);
		}
		catch (Exception exception)
		{
			Log.log("Failed to obtain the entity type when summoning a minion!", Level.WARNING);
			Log.log(exception.getLocalizedMessage());
		}
		if (type == null)
			return;*/
		
		// Get the type to spawn
		String type = entityTypes.get(random.nextInt(entityTypes.size()));

		// Spawn the mobs
		MMobListener.setSpawnDenyFlag(true);
		for (int i = 0; i < count; i++)
			//locations[i].getWorld().spawnEntity(locations[i], type);
			Common.spawnEntity(locations[i], type);
		MMobListener.setSpawnDenyFlag(false);
	}

	@Override
	public void cast(LivingEntity target)
	{
		// Restore the attack
		resetStartupTimer();
		repeats = 1;
		doSpawns = true;

		// Choose some random locations and indicate something happens there
		for (int i = 0; i < count; i++)
		{
			locations[i] = mob.getEntity().getLocation();
			ParticleEffect.play(ParticleType.PORTAL, locations[i], 0.05f, 20, 1.5f);
		}
	}

}
