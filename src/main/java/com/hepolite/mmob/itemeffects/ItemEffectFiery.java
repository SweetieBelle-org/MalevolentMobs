package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be added to weapons; will deal fire damage, and ignite the target for some duration
 */
public class ItemEffectFiery extends ItemEffect
{
	private int duration = 0;

	private float durabilityCostPerUse = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> lore;

	public ItemEffectFiery()
	{
		super("Fiery");
		incompatibleEffects = new String[] { getName(), "Frost" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUse = settings.getFloat("durabilityCostPerUse");
		bowDurabilityCostMultiplier = settings.getFloat("bowDurabilityCostMultiplier");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		if (event.getEntity() instanceof LivingEntity)
		{
			((LivingEntity) event.getEntity()).setFireTicks(duration);
			if (isItemBow(item))
				damageItem(item, bowDurabilityCostMultiplier * durabilityCostPerUse);
			else
				damageItem(item, durabilityCostPerUse);
		}
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemWeapon(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (duration != 0.0f)
			list.add(String.format("&fIgnites targets for &b%.1f&f seconds", (float) duration / 20.0f));
	}

	@Override
	public String getLore()
	{
		return (lore.size() == 0 ? null : lore.get(random.nextInt(lore.size())));
	}

	@Override
	public String saveToString()
	{
		return String.format("%d", duration);
	}

	@Override
	public void loadFromString(String dataString)
	{
		duration = Integer.parseInt(dataString);
	}
}
