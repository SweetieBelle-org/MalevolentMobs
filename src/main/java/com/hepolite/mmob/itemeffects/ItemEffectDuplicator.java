package com.hepolite.mmob.itemeffects;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.settings.Settings;

/**
 * The duplicator effect allows the players to duplicate blocks in the world, by spawning in a clone of the block they use the effect on
 */
public class ItemEffectDuplicator extends ItemEffectWand
{
	private static HashMap<Material, Float> itemValues = new HashMap<Material, Float>();

	private List<String> lore;
	
	public ItemEffectDuplicator()
	{
		super("Duplicator");
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		// Load up the item values from the config file
		itemValues.clear();
		List<String> items = settings.getStringList("blockValues");
		for (String item : items)
		{
			try
			{
				String[] components = item.split(":");
				itemValues.put(Material.getMaterial(components[0].toUpperCase()), Float.parseFloat(components[1]));
			}
			catch (Exception exception)
			{
				Log.log("Failed to parse the item '" + item + "' when reading item values...", Level.WARNING);
				Log.log(exception.getLocalizedMessage(), Level.WARNING);
			}
		}
		
		lore = settings.getStringList("lore");
	}

	@Override
	public void onLeftClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't right-click a block
		if (event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		event.setCancelled(true);

		// Find the block clicked, and check if this block is registered or not
		Block block = event.getClickedBlock();
		Material material = block.getType();

		if (itemValues.containsKey(material))
			player.sendMessage(String.format("The block '%s' costs %.1f charge to duplicate.", material.toString().toLowerCase().replaceAll("_", " "), itemValues.get(material)));
		else
			player.sendMessage(String.format("A block of type '%s' can't be duplicated.", material.toString().toLowerCase().replaceAll("_", " ")));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't right-click a block
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		event.setCancelled(true);

		// Find the block clicked, and check if this block is registered or not
		Block block = event.getClickedBlock();
		Material material = block.getType();

		if (itemValues.containsKey(material))
		{
			float cost = itemValues.get(material);
			if (charge >= cost)
			{
				player.getWorld().dropItem(player.getLocation(), new ItemStack(material, 1, block.getData()));
				setChargeInItem(item, charge - cost);
			}
			else
				player.sendMessage(String.format("Not enough charge to duplicate '%s', need %.1f more charge", material.toString().toLowerCase().replaceAll("_", " "), cost - charge));
		}
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fCan duplicate up to &b%.1f&f charges worth of blocks", maxCharge));
		list.add(String.format("&fCharge left: &b%.1f / %.1f&f [%s]", charge, maxCharge, getName()));
		super.addDescription(list);
		list.add("&fLeft-click to see how much a block cost, if it can be duplicated.");
		list.add("&fRight-click to duplicate the block.");
	}
	
	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}
}
