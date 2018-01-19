package com.hepolite.mmob.abilities.actives;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The teleport ability will teleport the malevolent mob to some location, or teleport other hostile entities around
 */
public class ActiveTeleport extends Active
{
	private float minDistance = 0.0f;
	private float maxDistance = 0.0f;

	private boolean requestSafeTeleport = false;
	private int timeWithoutRequestingSafeTeleport = 0;

	private float healthFactor = 0.0f;

	private float maxSearchDistance = 0.0f;

	public ActiveTeleport(MalevolentMob mob, float scale)
	{
		super(mob, "Teleport", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		minDistance = settings.getScaledValue(alternative, "MinDistance", scale, 0.0f);
		maxDistance = settings.getScaledValue(alternative, "MaxDistance", scale, 0.0f);

		maxSearchDistance = settings.getFloat(alternative, "searchRadius");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		this.healthFactor = healthFactor;
		return distanceToTarget < 100.0f;
	}

	@Override
	public void cast(LivingEntity target)
	{
		Location targetLocation = null;
		Location mobLocation = mob.getEntity().getLocation();
		Location entityLocation = target.getLocation();

		// Attempt to work out the situation and react accordingly
		if (requestSafeTeleport && timeWithoutRequestingSafeTeleport < 50)
		{
			requestSafeTeleport = false;
			double angle = 2.0 * Math.PI * random.nextDouble();
			double distance = minDistance + (maxDistance - minDistance) * random.nextDouble();
			targetLocation = mobLocation.add(distance * Math.cos(angle), 0.0, distance * Math.sin(angle));
		}
		else
		{
			List<Player> nearbyPlayers = Common.getPlayersInRange(mobLocation, maxSearchDistance);

			// Attempt to teleport self away if there are too many players nearby, or find a better location
			// if the mob believes it can take the player alone
			if ((float) nearbyPlayers.size() / (1.0f + healthFactor) <= 1.75f)
			{
				double angle = 2.0 * Math.PI * random.nextDouble();
				targetLocation = entityLocation.add(minDistance * Math.cos(angle), 0.0, minDistance * Math.sin(angle));
			}
			else
			{
				double angle = 2.0 * Math.PI * random.nextDouble(); // TODO: Make this the angle that points away from the target, plus some random factor
				double distance = minDistance + (maxDistance - minDistance) * random.nextDouble();
				targetLocation = mobLocation.add(distance * Math.cos(angle), 0.0, distance * Math.sin(angle));
			}
		}

		// Teleport to target
		if (targetLocation != null)
		{
			targetLocation = Common.getSafeLocation(targetLocation);
			targetLocation.getWorld().playSound(targetLocation, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
			mobLocation.getWorld().playSound(mobLocation, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
			mob.getEntity().teleport(targetLocation, TeleportCause.PLUGIN);
		}
	}

	@Override
	public void onTick()
	{
		super.onTick();
		timeWithoutRequestingSafeTeleport++;
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		// Request a safe location if the damage taken is something that depends on location
		if (event.getCause() == DamageCause.SUFFOCATION || event.getCause() == DamageCause.DROWNING || event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.LAVA)
		{
			timeWithoutRequestingSafeTeleport = 0;
			requestSafeTeleport = true;
			decreaseCooldownTimer(-2.5f);
		}
	}
}
