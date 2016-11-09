package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Effect that can be added to armor; will reduce all wither-based damages by a certain percent
 */
public class ItemEffectEntropyLimit extends ItemEffectShield
{
	public ItemEffectEntropyLimit()
	{
		super("Entropy_Limit", new DamageCause[] { DamageCause.WITHER });
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (strength > 0.0f)
			list.add(String.format("&fReduces wither-based damage by &b%.0f%%&f", 100.0f * strength));
		else if (strength < 0.0f)
			list.add(String.format("&fIncreases wither-based damage by &c%.0f%%&f", -100.0f * strength));
	}
}
