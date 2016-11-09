package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be applied to armor, tools and weapons; will repair items over time
 */
public class ItemEffectRepair extends ItemEffect
{
	private float repairAmount = 0.0f;

	private List<String> goodLore, badLore;
	
	public ItemEffectRepair()
	{
		super("Repair");
		incompatibleEffects = new String[] { getName(), "Fragile" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		goodLore = settings.getStringList("lore.good");
		badLore = settings.getStringList("lore.bad");
	}

	@Override
	public void onTick(Player player, ItemStack item)
	{
		if (repairAmount > 0)
			repairItem(item, repairAmount);
		else
			damageItem(item, -repairAmount);
	}

	@Override
	public boolean mustBeWorn()
	{
		return false;
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType().getMaxDurability() > 0;
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (repairAmount > 0.0f)
			list.add(String.format("&fRepairs &b%.1f&f durability points every minute", repairAmount));
		else if (repairAmount < 0.0f)
			list.add(String.format("&fDamages &c%.1f&f durability points every minute", -repairAmount));
		list.add("&f(This applies only if the item is in a player inventory)");
	}

	@Override
	public String getLore()
	{
		List<String> lores;
		if (repairAmount >= 0.0f)
			lores = goodLore;
		else
			lores = badLore;
		return lores.size() == 0 ? null : lores.get(random.nextInt(lores.size()));
	}
	
	@Override
	public String saveToString()
	{
		return String.format("%d", (int) (10.0f * repairAmount));
	}

	@Override
	public void loadFromString(String dataString)
	{
		repairAmount = 0.1f * Float.parseFloat(dataString);
	}
}
