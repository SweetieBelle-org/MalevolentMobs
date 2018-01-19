package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Effect that can be added to shovels; will dig out a 3x3 area of dirt, grass and gravel centered on the block being right-clicked
 */
public class ItemEffectEarthmover extends ItemEffect
{
	public float durabilityCostPerBlock = 0.0f;

	private List<String> lore;

	public ItemEffectEarthmover()
	{
		super("Earthmover");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerBlock = settings.getFloat("durabilityCostPerBlock");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't right-click a block
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block block = event.getClickedBlock();
		if (block.getType() != Material.DIRT && block.getType() != Material.GRASS && block.getType() != Material.MYCEL && block.getType() != Material.GRAVEL && block.getType() != Material.SAND && block.getType() != Material.SNOW && block.getType() != Material.SNOW_BLOCK)
			return;

		// Mine out all the blocks of stone that was found
		int blocksDug = 0;
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				for (int y = -1; y <= 1; y++)
				{
					// Find stone blocks and mine them
					Block newBlock = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);
					if (newBlock.getType() != Material.DIRT && newBlock.getType() != Material.GRASS && newBlock.getType() != Material.MYCEL && newBlock.getType() != Material.GRAVEL && newBlock.getType() != Material.SAND && newBlock.getType() != Material.SNOW && newBlock.getType() != Material.SNOW_BLOCK)
						continue;

					BlockBreakEvent logBlockEvent = new BlockBreakEvent(newBlock, player);
					Common.postEvent(logBlockEvent);
					if (!logBlockEvent.isCancelled())
					{
						blocksDug++;
						for (ItemStack itemToDrop : newBlock.getDrops(item))
							block.getWorld().dropItem(block.getLocation(), itemToDrop);
						newBlock.setType(Material.AIR);
					}
				}

		// Damage the tool, if applicable
		if (blocksDug > 0)
			damageItem(item, (float) blocksDug * durabilityCostPerBlock);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType().toString().toLowerCase().contains("spade");
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add("&fDigs a &b3x3&f space of dirt, grass, sand, gravel, snow");
		list.add("&fand mycelium centered on the block being right-clicked");
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
