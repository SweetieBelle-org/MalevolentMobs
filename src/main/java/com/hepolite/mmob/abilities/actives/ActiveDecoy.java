package com.hepolite.mmob.abilities.actives;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageEvent;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The decoy ability will spawn a series of decoys that are all similar to the true mob, except they die on taking any damage
 */
public class ActiveDecoy extends Active
{
	private int count = 0;
	private int timeSinceLastAttack = 0;

	private float maxSearchDistance = 0.0f;
	private int maxEntityCount = 0;

	public ActiveDecoy(MalevolentMob mob, float scale)
	{
		super(mob, "Decoy", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		count = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);

		maxSearchDistance = settings.getFloat(alternative, "searchRadius");
		maxEntityCount = settings.getInteger(alternative, "maxEntityCount");
	}

	// TODO: Override onDie and kill all decoys there

	@Override
	public void onTick()
	{
		super.onTick();
		timeSinceLastAttack++;
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		// Special rule: Decoys can't create more decoys (Nice attempt, mobs, but you're not getting this one so easily!)
		return timeSinceLastAttack < 20 && !mob.isDecoy() && distanceToTarget < 50.0f && healthFactor < 0.5f;
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		timeSinceLastAttack = 0;
	}

	@Override
	public void cast(LivingEntity target)
	{
		// Make sure that there's not too many mobs nearby
		List<Monster> nearbyMonsters = Common.getMonstersInRange(mob.getEntity().getLocation(), maxSearchDistance);
		if (nearbyMonsters.size() >= maxEntityCount)
			return;

		// Spawn the decoys
		for (int i = 0; i < count; i++)
			mob.createDecoy(mob.getEntity().getLocation());
	}
}
