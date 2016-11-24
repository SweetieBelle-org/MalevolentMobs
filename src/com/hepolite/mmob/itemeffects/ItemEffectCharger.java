package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hepolite.mmob.handlers.ItemEffectHandler;

public abstract class ItemEffectCharger extends ItemEffect
{
	protected float charge = 0.0f;
	protected float maxCharge = 0.0f;
	protected float rechargeAmount = 0.0f;

	protected ItemEffectCharger(String name)
	{
		super(name);
	}

	@Override
	public void onTick(Player player, ItemStack item)
	{
		setChargeInItem(item, charge + rechargeAmount);
	}

	@Override
	public boolean mustBeWorn()
	{
		return false;
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%.0f:%.0f", 10.0f * maxCharge, 10.0f * charge, 10.0f * rechargeAmount);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		maxCharge = 0.1f * Float.parseFloat(components[0]);
		charge = 0.1f * Float.parseFloat(components[1]);
		rechargeAmount = 0.1f * Float.parseFloat(components[2]);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (rechargeAmount > 0.0f)
		{
			list.add(String.format("&fRecharges &b%.1f&f points every minute", rechargeAmount));
			list.add("&f(This applies only if the item is in a player inventory)");
		}
		else if (rechargeAmount < 0.0f)
		{
			list.add(String.format("&fDischarges &c%.1f&f points every minute", -rechargeAmount));
			list.add("&f(This applies only if the item is in a player inventory)");
		}
	}

	/** Assigns the given charge value to the given item, such that the charge is persistent */
	protected void setChargeInItem(ItemStack item, float newCharge)
	{
		// Update the item effect tag and the description
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();

		charge = Math.max(0.0f, Math.min(maxCharge, newCharge));
		ItemEffectHandler.saveItemEffect(item, this);

		for (int i = 1; i < lore.size(); i++)
		{
			if (lore.get(i).contains(String.format("[%s]", getName())))
			{
				lore.remove(i);
				lore.add(i, ChatColor.translateAlternateColorCodes('&', String.format("&fCharge left: &b%.1f / %.1f&f [%s]", charge, maxCharge, getName())));
				break;
			}
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
	}
}
