package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Effect that can be added to armor; will reduce all fire-based damages by a certain percent
 */
public class ItemEffectFireward extends ItemEffectShield
{
	public ItemEffectFireward()
	{
		super("Fireward", new DamageCause[] { DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA });
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (strength > 0.0f)
			list.add(String.format("&fReduces fire-based damage by &b%.0f%%&f", 100.0f * strength));
		else if (strength < 0.0f)
			list.add(String.format("&fIncreases fire-based damage by &c%.0f%%&f", -100.0f * strength));
	}
}
