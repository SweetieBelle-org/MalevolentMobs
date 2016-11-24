package com.hepolite.mmob.abilities.passives;

import java.util.List;

import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The healer passive means that any health regeneration that would have taken place is passed on to damaged mobs in the vicinity
 */
public class PassiveHealer extends Passive
{
	// Control variables
	private float range = 0.0f;
	private float healRate = 0.0f;
	private float healthMultiplier = 0.0f;

	public PassiveHealer(MalevolentMob mob, float scale)
	{
		super(mob, "Healer", Priority.HIGH, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		range = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		healRate = settings.getScaledValue(alternative, "HealRate", scale, 0.0f);
		healthMultiplier = settings.getScaledValue(alternative, "HealthMultiplier", scale, 0.0f);
	}

	@Override
	public void onHealed(EntityRegainHealthEvent event)
	{
		// If a custom event is passed in, that probably means that the healing source was due to this very same effect, which could cause a loop and crash
		if (event.getRegainReason() == RegainReason.CUSTOM)
		{
			event.setCancelled(true);
			return;
		}

		// Figure out how much of the healing is sacrificed, and cancel it if needed
		double healAmount = healRate * event.getAmount();
		event.setAmount(event.getAmount() - healAmount);
		if (event.getAmount() <= 0.0)
			event.setCancelled(true);

		// Find nearby mobs and heal them instead
		healAmount *= healthMultiplier;
		if (healAmount > 0.0)
		{
			List<Monster> monsters = Common.getMonstersInRange(mob.getEntity().getLocation(), range);
			for (Monster monster : monsters)
			{
				if (monster == mob.getEntity() || monster.isDead() || !monster.isValid())
					continue;
				if (monster.getHealth() < monster.getMaxHealth())
				{
					double neededHealth = monster.getMaxHealth() - monster.getHealth();
					Common.doHeal(Math.min(neededHealth, healAmount), monster, RegainReason.CUSTOM);

					healAmount -= neededHealth;
					if (healAmount <= 0.0)
						break;
				}
			}
		}
	}
}
