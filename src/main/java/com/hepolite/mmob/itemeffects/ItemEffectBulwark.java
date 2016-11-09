package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Effect that can be put on armor; it will reduce physical damage dealt to the target
 */
public class ItemEffectBulwark extends ItemEffectShield
{
	public ItemEffectBulwark()
	{
		super("Bulwark", new DamageCause[] { DamageCause.ENTITY_ATTACK, DamageCause.CONTACT, DamageCause.PROJECTILE });
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (strength > 0.0f)
			list.add(String.format("&fReduces physical damage by &b%.0f%%&f", 100.0f * strength));
		else if (strength < 0.0f)
			list.add(String.format("&fIncreases physical damage by &c%.0f%%&f", -100.0f * strength));
	}
}
