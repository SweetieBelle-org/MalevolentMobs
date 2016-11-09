package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Effect that can be added to hoes; will till dirt and harvest mature plants, or act as bonemeal
 */
public class ItemEffectGrowth extends ItemEffect
{
	private float durabilityCostPerCrop = 0.0f;
	private float durabilityCostPerTree = 0.0f;
	private float durabilityCostPerGrass = 0.0f;
	private float durabilityCostPerFlower = 0.0f;
	private float durabilityCostPerCactus = 0.0f;

	private List<String> lore;

	public ItemEffectGrowth()
	{
		super("Growth");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerCrop = settings.getFloat("durabilityCostPerCrop");
		durabilityCostPerTree = settings.getFloat("durabilityCostPerTree");
		durabilityCostPerGrass = settings.getFloat("durabilityCostPerGrass");
		durabilityCostPerFlower = settings.getFloat("durabilityCostPerFlower");
		durabilityCostPerCactus = settings.getFloat("durabilityCostPerCactus");

		lore = settings.getStringList("lore");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't right-click a block
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		event.setCancelled(true);

		// Find blocks to work with
		Block centerBlock = event.getClickedBlock();
		Material centerType = centerBlock.getType();
		Byte meta = centerBlock.getData();

		// Convert dirt to grass
		if (centerType == Material.DIRT)
		{
			BlockBreakEvent logBlockEvent = new BlockBreakEvent(centerBlock, player);
			Common.postEvent(logBlockEvent);
			if (!logBlockEvent.isCancelled())
			{
				centerBlock.setType(Material.GRASS);
				damageItem(item, durabilityCostPerGrass);
			}
		}

		// Grow flowers/tallgrass on grass blocks
		else if (centerType == Material.GRASS)
		{
			// Want to grow crops as if they were grown with bonemeal
			PlayerInteractEvent bonemealEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.INK_SACK, 1, (short) 15), centerBlock, event.getBlockFace());
			Common.postEvent(bonemealEvent);
			if (!bonemealEvent.isCancelled())
			{
				int flowersGrown = 0;
				for (int x = -2; x <= 2; x++)
					for (int z = -2; z <= 2; z++)
						for (int y = 0; y <= 2; y++)
						{
							// Only grow things on grass blocks that have air above them
							Block block = centerBlock.getRelative(x, y, z);
							if (block.getType() == Material.AIR && block.getRelative(0, -1, 0).getType() == Material.GRASS)
							{
								// Grow flower
								if (random.nextFloat() < 0.05f)
								{
									flowersGrown++;
									if (random.nextFloat() < 0.1f) // Ten flowers are available in total; two different block ids are used, though
										block.setType(Material.YELLOW_FLOWER);
									else
									{
										block.setType(Material.RED_ROSE);
										block.setData((byte) random.nextInt(10)); // At most nine different flowers are possible on this block id
									}
								}
								// Grow tallgrass
								else if (random.nextFloat() < 0.2f)
								{
									flowersGrown++;
									block.setType(Material.LONG_GRASS);
									if (random.nextFloat() < 0.8f)
										block.setData((byte) 1); // Normal long grass
									else
										block.setData((byte) 2); // Ferns
								}
							}
						}

				// Damage the hoe due to growing flowers/tallgrass
				if (flowersGrown > 0)
					damageItem(item, durabilityCostPerFlower * (float) flowersGrown);
			}
		}

		// Progress crops' growth
		else if (centerType == Material.CROPS || centerType == Material.CARROT || centerType == Material.POTATO || centerType == Material.MELON_STEM || centerType == Material.PUMPKIN_STEM)
		{
			if (meta < 7) // The crops have a meta of 7 when they are fully grown
			{
				Byte newMeta = (byte) Math.min(7, meta + random.nextInt(3) + 1);
				damageItem(item, (float) (newMeta - meta) * durabilityCostPerCrop);
				centerBlock.setData(newMeta);
			}
		}

		// Grow flowers/tallgrass on grass blocks
		else if (centerType == Material.SAPLING)
		{
			// Select the tree type
			TreeType type = null;
			switch (meta)
			{
			case 0: // Oak trees
				if (random.nextInt(10) == 0 || player.isSneaking())
					type = TreeType.BIG_TREE;
				else
					type = TreeType.TREE;
				break;
			case 1: // Spruce trees
				if (random.nextInt(20) == 0 || player.isSneaking())
					type = TreeType.MEGA_REDWOOD;
				else if (random.nextInt(4) == 0)
					type = TreeType.TALL_REDWOOD;
				else
					type = TreeType.REDWOOD;
				break;
			case 2: // Birch trees
				if (random.nextInt(4) == 0)
					type = TreeType.TALL_BIRCH;
				else
					type = TreeType.BIRCH;
				break;
			case 3: // Jungle trees
				if (random.nextInt(20) == 0 || player.isSneaking())
					type = TreeType.JUNGLE;
				else if (random.nextInt(6) == 0)
					type = TreeType.COCOA_TREE;
				else if (random.nextInt(5) == 0)
					type = TreeType.JUNGLE_BUSH;
				else
					type = TreeType.SMALL_JUNGLE;
				break;
			case 4: // Acacia trees
				type = TreeType.ACACIA;
				break;
			case 5: // Dark Oak trees
				type = TreeType.DARK_OAK;
				break;
			default:
				;
			}

			// Grow the tree, if applicable
			if (type != null)
			{
				BlockBreakEvent logBlockEvent = new BlockBreakEvent(centerBlock, player);
				Common.postEvent(logBlockEvent);
				if (!logBlockEvent.isCancelled())
				{
					centerBlock.setType(Material.AIR);
					if (centerBlock.getWorld().generateTree(centerBlock.getLocation(), type))
						damageItem(item, durabilityCostPerTree);
					else
						centerBlock.getWorld().dropItem(centerBlock.getLocation(), new ItemStack(centerType, 1, meta));
				}
			}
		}

		// Grow cactus and sugar canes
		else if (centerType == Material.CACTUS || centerType == Material.SUGAR_CANE_BLOCK)
		{
			// Figure out how tall the stack is, and grow it up to four blocks
			int count = 0;
			Block currentBlock = centerBlock;
			while (currentBlock.getType() == centerType)
			{
				count++;
				currentBlock = currentBlock.getRelative(0, -1, 0);
			}
			currentBlock = centerBlock.getRelative(0, 1, 0);
			while (currentBlock.getType() == centerType)
			{
				count++;
				currentBlock = currentBlock.getRelative(0, 1, 0);
			}

			// Perform the actual growth
			if (count <= 4 && currentBlock.getType() == Material.AIR)
			{
				currentBlock.setType(centerType);
				damageItem(item, durabilityCostPerCactus);
			}
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
		list.add("&fWhen right-clicked, this tool will accelerate the");
		list.add("&fgrowth of crops, grass and vegetation.");
		list.add("&fIt will also be able to grow trees from saplings");
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
