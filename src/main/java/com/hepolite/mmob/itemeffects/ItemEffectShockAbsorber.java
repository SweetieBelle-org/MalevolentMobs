package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Item effect that can be applied to armor; will reduce all lightning- and explosion-based damage by a certain percent
 */
public class ItemEffectShockAbsorber extends ItemEffectShield
{
	public ItemEffectShockAbsorber()
	{
		super("Shock_Absorber", new DamageCause[] { DamageCause.BLOCK_EXPLOSION, DamageCause.ENTITY_EXPLOSION, DamageCause.LIGHTNING });
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (strength > 0.0f)
			list.add(String.format("&fReduces lightning- and explosion-based damage by &b%.0f%%&f", 100.0f * strength));
		else if (strength < 0.0f)
			list.add(String.format("&fIncreases lightning- and explosion-based damage by &c%.0f%%&f", -100.0f * strength));
	}
}
