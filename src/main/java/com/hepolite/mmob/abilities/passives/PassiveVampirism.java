package com.hepolite.mmob.abilities.passives;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.hepolite.mmob.abilities.PassiveAura;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The vampirism effect will detect when a player regains a certain amount of health. When that happens, the mob will steal the health the player would normally receive, healing itself instead
 */
public class PassiveVampirism extends PassiveAura
{
	private HashMap<String, Double> playerHealthMap = new HashMap<String, Double>();
	private HashSet<String> playersInRange = new HashSet<String>();

	private float sensitivity = 0.0f;

	public PassiveVampirism(MalevolentMob mob, float scale)
	{
		super(mob, "Vampirism", scale);
		updateTime = 0;
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);
		affectPlayersOnly = true;

		sensitivity = settings.getScaledValue(alternative, "Sensitivity", scale, 0.0f);
	}

	@Override
	public void onTick()
	{
		super.onTick();

		// Find all players not in the vicinity and remove them from the health map
		for (Iterator<String> it = playerHealthMap.keySet().iterator(); it.hasNext();)
		{
			if (!playersInRange.contains(it.next()))
				it.remove();
		}
		playersInRange.clear();
	}

	@Override
	protected void applyAuraEffect(LivingEntity entity)
	{
		// If the player was detected in the health map, figure out if the player gained health
		String player = ((Player) entity).getName();
		if (playerHealthMap.containsKey(player))
		{
			double oldHealth = playerHealthMap.get(player);
			if (entity.getHealth() >= oldHealth + sensitivity)
			{
				// Take health from the player and heal self
				double healedAmount = entity.getHealth() - oldHealth;
				Common.doDamage(healedAmount, entity, mob.getEntity(), DamageCause.MAGIC);
				Common.doHeal(healedAmount, mob.getEntity(), RegainReason.MAGIC);
			}
		}

		playerHealthMap.put(player, entity.getHealth());
		playersInRange.add(player);
	}

	@Override
	protected void displayAura(Location location, float range)
	{
		ParticleEffect.play(ParticleType.REDSTONE, location, 0.0f, (int) (5.0f * range), 0.5f * range);
	}
}
