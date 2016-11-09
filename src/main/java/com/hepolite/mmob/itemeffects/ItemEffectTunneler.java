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
 * Effect that can be added to pickaxes; will mine a 3x3 area centered on the block right-clicked
 */
public class ItemEffectTunneler extends ItemEffect
{
	public float durabilityCostPerBlock = 0.0f;

	private List<String> lore;

	public ItemEffectTunneler()
	{
		super("Tunneler");
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
		if (block.getType() != Material.STONE && block.getType() != Material.COBBLESTONE)
			return;

		// Mine out all the blocks of stone that was found
		int blocksMined = 0;
		for (int x = -1; x <= 1; x++)
			for (int z = -1; z <= 1; z++)
				for (int y = -1; y <= 1; y++)
				{
					// Find stone blocks and mine them
					Block newBlock = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);
					if (newBlock.getType() != Material.STONE && newBlock.getType() != Material.COBBLESTONE)
						continue;

					BlockBreakEvent logBlockEvent = new BlockBreakEvent(newBlock, player);
					Common.postEvent(logBlockEvent);
					if (!logBlockEvent.isCancelled())
					{
						blocksMined++;
						for (ItemStack itemToDrop : newBlock.getDrops(item))
							block.getWorld().dropItem(block.getLocation(), itemToDrop);
						newBlock.setType(Material.AIR);
					}
				}

		// Damage the tool, if applicable
		if (blocksMined > 0)
			damageItem(item, (float) blocksMined * durabilityCostPerBlock);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType().toString().toLowerCase().contains("pickaxe");
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add("&fMines a &b3x3&f space of stone centered");
		list.add("&fon the block being right-clicked");
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
