package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

public class ItemEffectHarvest extends ItemEffect
{
	private float durabilityCostPerHarvest = 0.0f;
	private float durabilityCostPerTill = 0.0f;

	private List<String> lore;

	public ItemEffectHarvest()
	{
		super("Harvest");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerHarvest = settings.getFloat("durabilityCostPerHarvest");
		durabilityCostPerTill = settings.getFloat("durabilityCostPerTill");

		lore = settings.getStringList("lore");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onLeftClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't left-click a block
		if (event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		event.setCancelled(true);

		// Find blocks to work with
		Block centerBlock = event.getClickedBlock();
		Material centerType = centerBlock.getType();

		if (centerType == Material.DIRT || centerType == Material.GRASS || centerType == Material.MYCEL)
		{
			int blocksTilled = 0;
			for (int x = -2; x <= 2; x++)
				for (int z = -2; z <= 2; z++)
					for (int y = -1; y <= 1; y++)
					{
						// If the block is tillable, convert to soil, but only if exposed to air
						Block block = centerBlock.getRelative(x, y, z);
						Material type = block.getType();
						if ((type == Material.DIRT || type == Material.GRASS || type == Material.MYCEL) && block.getRelative(0, 1, 0).getType() == Material.AIR)
						{
							BlockBreakEvent logBlockEvent = new BlockBreakEvent(block, player);
							Common.postEvent(logBlockEvent);
							if (!logBlockEvent.isCancelled())
							{
								blocksTilled++;
								block.setType(Material.SOIL);
							}
						}
					}

			// Damage the hoe due to tilling
			if (blocksTilled > 0)
				damageItem(item, durabilityCostPerTill * (float) blocksTilled);
		}

		// Find crops to harvest and replant
		else if (centerType == Material.CROPS || centerType == Material.POTATO || centerType == Material.CARROT)
		{
			int blocksHarvested = 0;
			int wheat = 0, seeds = 0, potatoes = 0, carrots = 0;

			for (int x = -2; x <= 2; x++)
				for (int z = -2; z <= 2; z++)
					for (int y = -1; y <= 1; y++)
					{
						// If the block is some crop and fully grown, revert to original state and drop crops
						Block block = centerBlock.getRelative(x, y, z);
						Material type = block.getType();
						if (type != Material.CROPS && type != Material.POTATO && type != Material.CARROT)
							continue;

						Byte meta = block.getData();
						if (meta == 7) // The meta of fully grown crops is 7
						{
							blocksHarvested++;

							// Harvest the crops and reset the growth
							block.setData((byte) 0); // The meta of freshly planted crops is 0
							if (type == Material.CROPS)
							{
								wheat += 1 + random.nextInt(2);
								seeds += random.nextInt(3);
							}
							else if (type == Material.POTATO)
								potatoes += 1 + random.nextInt(2);
							else if (type == Material.CARROT)
								carrots += 1 + random.nextInt(2);
						}
					}

			// Spawn the items, if applicable
			Location location = centerBlock.getLocation().add(0.5, 1.0, 0.5);
			if (wheat != 0)
				location.getWorld().dropItem(location, new ItemStack(Material.WHEAT, wheat));
			if (seeds != 0)
				location.getWorld().dropItem(location, new ItemStack(Material.SEEDS, seeds));
			if (potatoes != 0)
				location.getWorld().dropItem(location, new ItemStack(Material.POTATO_ITEM, potatoes));
			if (carrots != 0)
				location.getWorld().dropItem(location, new ItemStack(Material.CARROT_ITEM, carrots));

			// Damage the hoe due to tilling
			if (blocksHarvested > 0)
				damageItem(item, durabilityCostPerHarvest * (float) blocksHarvested);
		}

	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType().toString().toLowerCase().contains("_hoe");
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add("&fWhen left-clicked, this tool will till dirt or harvest mature crops");
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return "";
	}

	@Override
	public void loadFromString(String dataString)
	{
	}
}
