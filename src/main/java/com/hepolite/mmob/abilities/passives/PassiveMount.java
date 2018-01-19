package com.hepolite.mmob.abilities.passives;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The rider will sit on the malevolent mob with this passive; any damage dealt to the mob is mitigated to the rider
 */
public class PassiveMount extends Passive
{
	private Entity rider = null;
	private String type = null;
	private String role = "";

	public PassiveMount(MalevolentMob mob, float scale)
	{
		super(mob, "Mount", Priority.LOW, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		type = settings.getString(alternative, "type");
		role = settings.getString(alternative, "role");
	}

	@Override
	public void onSpawn()
	{
		// Spawn the rider
		Location location = mob.getEntity().getLocation();
		MMobListener.setSpawnDenyFlag(true);
		rider = Common.spawnEntity(location, type);
		MMobListener.setSpawnDenyFlag(false);
		mob.getEntity().setPassenger(rider);

		// If possible and relevant, make the mob malevolent
		if (!role.equals("") && !role.equalsIgnoreCase("none") && rider instanceof LivingEntity)
		{
			MalevolentMob mob = MobHandler.makeMobMalevolent((LivingEntity) rider);
			if (mob != null)
				mob.setRole(role);
		}
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		// Mitigate the damage to the rider if the rider is alive
		if (rider != null && rider.isValid())
		{
			if (rider instanceof LivingEntity)
				Common.doDamage(event.getDamage(), (LivingEntity) rider, mob.getEntity(), DamageCause.MAGIC);
			else
			{
				mob.getEntity().eject();
				rider = null;
			}
			event.setCancelled(true);
		}
	}
}
