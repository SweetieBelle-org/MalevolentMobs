package com.hepolite.mmob.itemeffects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Effect that eats food every now and then from the inventory
 */
public class ItemEffectHungry extends ItemEffect
{
	private float chance = 0.0f;

	private List<String> lore;

	public ItemEffectHungry()
	{
		super("Hungry");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		lore = settings.getStringList("lore");
	}

	@Override
	public boolean mustBeWorn()
	{
		return false;
	}

	@Override
	public void onTick(Player player, ItemStack item)
	{
		if (random.nextFloat() >= chance)
			return;

		List<Integer> foodSlots = new ArrayList<Integer>();
		List<Integer> miscSlots = new ArrayList<Integer>();
		List<Integer> equipmentSlots = new ArrayList<Integer>();

		PlayerInventory inventory = player.getInventory();
		for (int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack inventoryItem = inventory.getItem(i);
			if (inventoryItem == null)
				continue;
			Material type = inventoryItem.getType();
			if (type.isEdible())
				foodSlots.add(i);
			else if (type.getMaxDurability() != 0)
				equipmentSlots.add(i);
			else
				miscSlots.add(i);
		}

		List<Integer> list = foodSlots.size() != 0 ? foodSlots : (miscSlots.size() != 0 ? miscSlots : equipmentSlots);
		if (list.size() != 0)
		{
			ItemMeta meta = item.getItemMeta();
			String nameEater = meta.hasDisplayName() ? meta.getDisplayName() : Common.toTitleCase(item.getType().name().replaceAll("_", " "));
			String nameVictim = consume(player, inventory, list.get(random.nextInt(list.size())));
			player.sendMessage(ChatColor.WHITE + nameVictim + ChatColor.RESET + ChatColor.RED + " was consumed by " + ChatColor.WHITE + nameEater + ChatColor.RESET + ChatColor.RED + "!");
		}
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fHas a &c%.0f%%&f chance of eating food or other items", 100.0f * chance));
		list.add("&ffrom the inventory every minute");
		list.add("&f(This applies only if the item is in a player inventory)");
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f", 100.0f * chance);
	}

	@Override
	public void loadFromString(String dataString)
	{
		chance = 0.01f * Float.parseFloat(dataString);
	}

	/** Consumes the item at the given location in the given inventory; returns the name of the consumed item */
	private final String consume(Player player, Inventory inventory, int slot)
	{
		ItemStack item = inventory.getItem(slot);
		ItemMeta meta = item.getItemMeta();
		String name = meta.hasDisplayName() ? meta.getDisplayName() : Common.toTitleCase(item.getType().name().replaceAll("_", " "));
		int amount = item.getAmount() - 1;
		item.setAmount(amount);
		inventory.setItem(slot, (amount > 0 ? item : null));
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 0.7f, 1.0f);
		return name;
	}
}
