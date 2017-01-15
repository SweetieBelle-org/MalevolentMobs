package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.LootDropHandler;
import com.hepolite.mmob.settings.Settings;

public class ItemEffectRelic extends ItemEffectChest
{
	private String group = "";
	private float negativeChance = 0.0f;
	private String negativeGroup = "";

	private List<String> loreBase, loreNegative;

	public ItemEffectRelic()
	{
		super("Relic");
		incompatibleEffects = new String[] { getName(), "Treasure" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		loreBase = settings.getStringList("lore.Base");
		loreNegative = settings.getStringList("lore.Negative");
	}

	@Override
	protected void addItems(Inventory inventory, Location location)
	{
		float chance = value - (float) Math.floor(value);
		int count = (int) Math.floor(value) + (random.nextFloat() < chance ? 1 : 0);
		for (int i = 0; i < count; i++)
		{
			ItemStack item = LootDropHandler.getRandomItem(group);
			if (item != null && random.nextFloat() < negativeChance)
				LootDropHandler.applyRandomItemEffect(item, negativeGroup);
			if (item != null)
				inventory.addItem(item);
		}
	}

	@Override
	public void addDescription(List<String> list)
	{
		float chance = value - (float) Math.floor(value);

		if (Math.floor(value) == 1.0f)
			list.add(String.format("&fContains &b%d&f %s relic", (int) Math.floor(value), group));
		else
			list.add(String.format("&fContains &b%d&f %s relics", (int) Math.floor(value), group));
		if (chance > 0.0f)
			list.add(String.format("&fwith a &b%.0f%%&f chance of another relic", 100.0f * chance));
		if (negativeChance > 0.0f)
			list.add(String.format("&fThere is a &c%.0f%%&f chance for negative side effects", 100.0f * negativeChance));
		list.add("&fRight-click on the ground to use the chest");
	}

	@Override
	public String getLore()
	{
		return (loreBase.size() == 0 ? null : loreBase.get(random.nextInt(loreBase.size()))) + (loreNegative.size() == 0 && negativeChance > 0.0f ? "" : " " + loreNegative.get(random.nextInt(loreNegative.size())));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%s:%.0f:%s", 100.0f * value, group, 100.0f * negativeChance, negativeGroup);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] parts = dataString.split(":");
		value = 0.01f * Float.parseFloat(parts[0]);
		group = parts[1];
		negativeChance = 0.01f * Float.parseFloat(parts[2]);
		negativeGroup = parts[3];
	}
}
