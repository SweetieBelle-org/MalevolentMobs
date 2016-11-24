package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.MMobCompatibility;
import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be applied to armor; will replenish hunger and restore saturation over time
 */
public class ItemEffectSaturation extends ItemEffect
{
	private float hungerRestored = 0;
	private float saturationRestored = 0.0f;

	private float durabilityCostPerPoint = 0.0f;

	private List<String> goodLore, badLore;

	public ItemEffectSaturation()
	{
		super("Saturation");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerPoint = settings.getFloat("durabilityCostPerPoint");

		goodLore = settings.getStringList("lore.good");
		badLore = settings.getStringList("lore.bad");
	}

	@Override
	public void onTick(Player player, ItemStack item)
	{
		if (MMobCompatibility.hasPangaea())
		{
			if (saturationRestored < 0.0f)
				MMobCompatibility.pangaeaChangePlayerSaturation(player, saturationRestored);
			MMobCompatibility.pangaeaChangePlayerHunger(player, hungerRestored);
			if (saturationRestored > 0.0f)
				MMobCompatibility.pangaeaChangePlayerSaturation(player, saturationRestored);
		}
		else
		{
			float oldHunger = player.getFoodLevel();
			float oldSaturation = player.getSaturation();

			if (player.getFoodLevel() < 20)
				player.setFoodLevel(Math.max(0, Math.min(20, (int) hungerRestored + player.getFoodLevel())));
			else if (hungerRestored < 0)
				player.setFoodLevel(Math.max(0, (int) hungerRestored + player.getFoodLevel()));
			player.setSaturation(Math.max(0.0f, Math.min(player.getFoodLevel(), saturationRestored + player.getSaturation())));

			damageItem(item, durabilityCostPerPoint * ((float) (player.getFoodLevel() - oldHunger) + (player.getSaturation() - oldSaturation)));
		}
	}

	@Override
	public void addDescription(List<String> list)
	{
		boolean addedText = true;
		if (hungerRestored > 0 && saturationRestored > 0.0f)
			list.add(String.format("&fRestores &b%.1f&f hunger points and &b%.1f&f saturation every minute", hungerRestored, saturationRestored));
		else if (hungerRestored < 0 && saturationRestored < 0.0f)
			list.add(String.format("&fDrains &c%.1f&f hunger points and &c%.1f&f saturation every minute", -hungerRestored, -saturationRestored));
		else if (hungerRestored > 0 && saturationRestored < 0.0f)
			list.add(String.format("&fRestores &b%.1f&f hunger points and drains &c%.1f&f saturation every minute", hungerRestored, -saturationRestored));
		else if (hungerRestored < 0 && saturationRestored > 0.0f)
			list.add(String.format("&fDrains &c%.1f&f hunger points and restores &b%.1f&f saturation every minute", -hungerRestored, saturationRestored));
		else if (hungerRestored > 0)
			list.add(String.format("&fRestores &b%.1f&f hunger points every minute", hungerRestored));
		else if (hungerRestored < 0)
			list.add(String.format("&fDrains &c%.1f&f hunger points every minute", -hungerRestored));
		else if (saturationRestored > 0.0f)
			list.add(String.format("&fRestores &b%.1f&f saturation every minute", saturationRestored));
		else if (saturationRestored < 0.0f)
			list.add(String.format("&fDrains &c%.1f&f saturation every minute", -saturationRestored));
		else
			addedText = false;

		if (addedText)
			list.add("&f(This applies only to items that are worn or held)");
	}

	@Override
	public String getLore()
	{
		List<String> lores;
		if (hungerRestored + saturationRestored >= 0.0f)
			lores = goodLore;
		else
			lores = badLore;
		return lores.size() == 0 ? null : lores.get(random.nextInt(lores.size()));
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemArmor(item);
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%.0f", 10.0f * hungerRestored, 10.0f * saturationRestored);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		hungerRestored = 0.1f * Integer.parseInt(components[0]);
		saturationRestored = 0.1f * Integer.parseInt(components[1]);
	}
}
