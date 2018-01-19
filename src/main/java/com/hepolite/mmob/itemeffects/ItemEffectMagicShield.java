package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Effect that can be added to armor; will reduce all magic-based damages by a certain percent
 */
public class ItemEffectMagicShield extends ItemEffectShield
{
	public ItemEffectMagicShield()
	{
		super("Magic_Shield", new DamageCause[] { DamageCause.MAGIC });
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (strength > 0.0f)
			list.add(String.format("&fReduces magic-based damage by &b%.0f%%&f", 100.0f * strength));
		else if (strength < 0.0f)
			list.add(String.format("&fIncreases magic-based damage by &c%.0f%%&f", -100.0f * strength));
	}
}
