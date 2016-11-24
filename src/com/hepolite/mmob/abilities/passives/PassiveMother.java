package com.hepolite.mmob.abilities.passives;

import java.util.List;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.abilities.PassiveTick;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * A mother will spawn children of its own type for as long as it is alive
 */
public class PassiveMother extends PassiveTick
{
	private int minGroupSize = 0;
	private int maxGroupSize = 0;

	private float maxSearchDistance = 0.0f;
	private int maxEntityCount = 0;

	public PassiveMother(MalevolentMob mob, float scale)
	{
		super(mob, "Mother", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		minGroupSize = settings.getInteger(alternative, "minGroupSize");
		maxGroupSize = settings.getInteger(alternative, "maxGroupSize");

		maxSearchDistance = settings.getFloat(alternative, "searchRadius");
		maxEntityCount = settings.getInteger(alternative, "maxEntityCount");
	}

	@Override
	protected void applyTickEffect()
	{
		// Make sure there's players nearby and that there's not too many mobs nearby
		List<Player> nearbyPlayers = Common.getPlayersInRange(mob.getEntity().getLocation(), maxSearchDistance);
		if (nearbyPlayers.size() == 0)
			return;
		List<Monster> nearbyMonsters = Common.getMonstersInRange(mob.getEntity().getLocation(), maxSearchDistance);
		if (nearbyMonsters.size() >= maxEntityCount)
			return;

		// Spawn some children
		int children = minGroupSize;
		if (maxGroupSize > minGroupSize)
			children += random.nextInt(maxGroupSize - minGroupSize);

		MMobListener.setSpawnDenyFlag(true);
		for (int i = 0; i < children; i++)
			Common.spawnEntity(mob.getEntity().getLocation(), Common.getEntityType(mob.getEntity()));
		MMobListener.setSpawnDenyFlag(false);

		// Visually display that mobs were spawned
		ParticleEffect.play(ParticleType.HEART, mob.getEntity().getEyeLocation(), 0.05f, 5, 0.5f);
	}
}
