package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Arrows fired from a bow with this effect will explode on impact with any entity
 */
public class ItemEffectExplosiveArrows extends ItemEffect
{
	private float strength = 0.0f;
	private float radius = 0.0f;

	private float durabilityCostPerUse = 0.0f;

	private List<String> lore;

	public ItemEffectExplosiveArrows()
	{
		super("Explosive_Arrows");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUse = settings.getFloat("durabilityCostPerUse");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		if (event.getCause() != DamageCause.PROJECTILE)
			return;
		Common.createExplosionWithEffect(event.getEntity().getLocation(), strength, radius, false, player);
		damageItem(item, durabilityCostPerUse);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemBow(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add("&fArrows hitting entities will explode, where the");
		list.add(String.format("&fexplosion has radius &b%.1fm&f and does &b%.1f&f hearts", radius, 0.5f * strength));
		list.add("&fof damage at the center of the blast");
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%.0f", 10.0f * strength, 10.0f * radius);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		strength = 0.1f * Float.parseFloat(components[0]);
		radius = 0.1f * Float.parseFloat(components[1]);
	}
}
