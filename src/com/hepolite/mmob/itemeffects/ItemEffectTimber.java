package com.hepolite.mmob.itemeffects;

import java.util.LinkedList;
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

/**
 * Effect that can be applied to axes; will chop down several blocks of wood at the same time
 */
public class ItemEffectTimber extends ItemEffect
{
	private int strength = 0;

	private float durabilityCostPerBlock = 0.0f;

	private List<String> lore;

	public ItemEffectTimber()
	{
		super("Timber");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerBlock = settings.getFloat("durabilityCostPerBlock");

		lore = settings.getStringList("lore");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't right-click a block
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block block = event.getClickedBlock();
		if (!block.getType().toString().startsWith("LOG"))
			return;

		// Figure out what the maximum chops can be
		float factor = (durabilityCostPerBlock < 1.0f ? 1.0f : durabilityCostPerBlock);
		int chopsLeft = Math.min(item.getType().getMaxDurability() - (int) ((float) item.getDurability() / factor), strength);
		int blocksChopped = 0;
		Material material = block.getType();
		Byte data = (byte) (block.getData() % 4);

		// Will chop down blocks that are closest to the player first
		List<Location> blocksInSortedOrder = new LinkedList<Location>();
		blocksInSortedOrder.add(event.getClickedBlock().getLocation());

		while (chopsLeft > 0 && blocksInSortedOrder.size() != 0)
		{
			// Find new nearby blocks
			Location location = blocksInSortedOrder.remove(0);
			if (location.getBlock().getType() == Material.AIR)
				continue;

			for (int x = -1; x <= 1; x++)
				for (int z = -1; z <= 1; z++)
					for (int y = -1; y <= 1; y++)
					{
						if (x == 0 && y == 0 && z == 0)
							continue;
						Block newBlock = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
						if (newBlock.getType() == material && (byte) (newBlock.getData() % 4) == data)
							blocksInSortedOrder.add(newBlock.getLocation());
					}

			// Chop down the block
			chopsLeft--;
			BlockBreakEvent logBlockEvent = new BlockBreakEvent(location.getBlock(), player);
			Common.postEvent(logBlockEvent);
			if (!logBlockEvent.isCancelled())
			{
				blocksChopped++;
				location.getBlock().setType(Material.AIR);
			}
		}

		// Finally damage the tool and drop the items
		if (blocksChopped > 0)
		{
			block.getWorld().dropItem(block.getLocation(), new ItemStack(material, blocksChopped, (short) (data % 4)));
			damageItem(item, durabilityCostPerBlock * (float) blocksChopped);
		}
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		String materialType = item.getType().toString().toLowerCase().replace("pickaxe", "");
		return materialType.contains("axe");
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fWill chop down &b%d&f logs when right-clicked on a log", strength));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%d", strength);
	}

	@Override
	public void loadFromString(String dataString)
	{
		strength = Integer.parseInt(dataString);
	}
}
