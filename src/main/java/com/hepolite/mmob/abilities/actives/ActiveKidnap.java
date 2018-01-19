package com.hepolite.mmob.abilities.actives;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The kidnap ability allows the mob to teleport away with the target, taking it somewhere else
 */
public class ActiveKidnap extends Active
{
	private float minDistance = 0.0f;
	private float maxDistance = 0.0f;

	public ActiveKidnap(MalevolentMob mob, float scale)
	{
		super(mob, "Kidnap", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		minDistance = settings.getScaledValue(alternative, "MinDistance", scale, 0.0f);
		maxDistance = settings.getScaledValue(alternative, "MaxDistance", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		if (distanceToTarget > 4.0f)
			return false;

		// Figure out what the conditions are around the point of interest, run away with a player if applicable
		List<Player> playersNearby = Common.getPlayersInRange(mob.getEntity().getLocation(), 25.0f);
		List<Monster> entitiesNearby = Common.getMonstersInRange(mob.getEntity().getLocation(), 25.0f);

		if (entitiesNearby.size() == 0 || playersNearby.size() >= 3)
			return true;
		return ((float) playersNearby.size() / (float) entitiesNearby.size() > 0.6f);
	}

	@Override
	public void cast(LivingEntity target)
	{
		// Find a position that is safe and teleport the target there
		float distance = minDistance + random.nextFloat() * (maxDistance - minDistance);
		double direction = random.nextDouble() * 2.0 * Math.PI;
		Location location = mob.getEntity().getLocation().add(distance * Math.sin(direction), 0.0, distance * Math.cos(direction));
		location = Common.getSafeLocation(location);
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
		target.teleport(location, TeleportCause.PLUGIN);
		target.getWorld().playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);

		// Find a position that is safe and teleport the self there
		distance = 5.0f + 5.0f * random.nextFloat();
		direction = random.nextDouble() * 2.0 * Math.PI;
		location = location.add(distance * Math.sin(direction), 0.0, distance * Math.cos(direction));
		location = Common.getSafeLocation(location);
		target.getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
		mob.getEntity().teleport(location, TeleportCause.PLUGIN);
		target.getWorld().playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
	}
}
