package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ItemEffectUnmelting extends ItemEffectShield
{
	public ItemEffectUnmelting()
	{
		super("Unmelting", new DamageCause[] { DamageCause.MELTING });
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (strength > 0.0f)
			list.add(String.format("&fReduces risk of melting by &b%.0f%%&f", 100.0f * strength));
		else if (strength < 0.0f)
			list.add(String.format("&fIncreases risk of melting by &c%.0f%%&f", -100.0f * strength));
	}
}
