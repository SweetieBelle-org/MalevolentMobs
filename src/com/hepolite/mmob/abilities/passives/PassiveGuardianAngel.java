package com.hepolite.mmob.abilities.passives;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageEvent;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The guardian angel grants the mob additional lives, the number depends on how effective it is
 */
public class PassiveGuardianAngel extends Passive
{
	int lives = 0;
	int timer = 0;

	public PassiveGuardianAngel(MalevolentMob mob, float scale)
	{
		super(mob, "Guardian Angel", Priority.HIGH, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		lives = (int) settings.getScaledValue(alternative, "Lives", scale, 1.0f);
	}

	@Override
	public void onTick()
	{
		if (lives > 0 && ++timer > 50)
		{
			timer = 0;
			ParticleEffect.play(ParticleType.CLOUD, mob.getEntity().getEyeLocation(), 0.05f, 15, 1.75f);
		}
	}

	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		if (lives > 0 && event.getDamage() >= mob.getEntity().getHealth())
		{
			event.setDamage(event.getDamage() - mob.getEntity().getHealth());

			// Display what happened
			Location location = mob.getEntity().getEyeLocation();
			ParticleEffect.play(ParticleType.CLOUD, location, 0.0f, 40, 2.25f);
			mob.getEntity().getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.0f);

			// Restore health
			mob.getEntity().setHealth(mob.getEntity().getMaxHealth());
			lives--;
		}
	}
}
