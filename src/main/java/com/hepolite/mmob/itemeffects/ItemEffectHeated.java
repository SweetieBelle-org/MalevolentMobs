package com.hepolite.mmob.itemeffects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that autosmelts whatever is being harvested
 */
public class ItemEffectHeated extends ItemEffect
{
	private final static HashMap<Node, ItemStack> smeltables = new HashMap<Node, ItemStack>();

	private float durabilityCostPerUse = 0.0f;

	private List<String> lore;

	public ItemEffectHeated()
	{
		super("Heated");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUse = settings.getFloat("durabilityCostPerUse");

		// Build the map of items that can be smelted by a furnace
		for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext();)
		{
			Recipe r = it.next();
			if (!(r instanceof FurnaceRecipe))
				continue;
			FurnaceRecipe recipe = (FurnaceRecipe) r;
			smeltables.put(new Node(recipe.getInput().getType(), recipe.getInput().getDurability()), recipe.getResult());
		}

		lore = settings.getStringList("lore");
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event, Player player, ItemStack item)
	{
		// Ignore everything while the player is sneaking
		if (player.isSneaking())
			return;
		event.setCancelled(true);
		Block block = event.getBlock();

		// Figure out what to drop from the block
		int smeltedItems = 0;
		int fortuneLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

		Collection<ItemStack> droppedItems = block.getDrops(item);
		List<ItemStack> itemsToDrop = new LinkedList<ItemStack>();
		for (ItemStack droppedItem : droppedItems)
		{
			Node node = new Node(droppedItem.getType(), droppedItem.getDurability());
			Node nodeAnyMeta = new Node(droppedItem.getType(), (short) 32767);

			ItemStack stack;
			if (smeltables.containsKey(nodeAnyMeta))
				stack = smeltables.get(nodeAnyMeta).clone();
			else if (smeltables.containsKey(node))
				stack = smeltables.get(node).clone();
			else
				stack = droppedItem;
			if (stack != null)
			{
				if (stack != droppedItem)
				{
					if (!stack.getType().isBlock())
						stack.setAmount(Math.min(64, stack.getAmount() + random.nextInt(1 + fortuneLevel)));
					smeltedItems++;
				}
				itemsToDrop.add(stack);
			}
		}
		damageItem(item, smeltedItems * durabilityCostPerUse);

		// Drop items and remove block
		for (ItemStack itemToDrop : itemsToDrop)
			block.getWorld().dropItemNaturally(block.getLocation(), itemToDrop);
		block.setType(Material.AIR);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		// Axe, shovels and pickaxes
		return item.getType().toString().toLowerCase().contains("axe") || item.getType().toString().toLowerCase().contains("spade");
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add("&fAutosmelts the harvested blocks, if they can");
		list.add("&fbe smelted in a regular furnace");
		list.add("&f(This is disabled while sneaking)");
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

	// //////////////////////////////////////////////////////////

	private final static class Node
	{
		private final Material material;
		private final short metadata;

		private Node(Material material, short metadata)
		{
			this.material = material;
			this.metadata = metadata;
		}

		@Override
		public int hashCode()
		{
			return material.ordinal() ^ metadata;
		}

		@Override
		public boolean equals(Object o)
		{
			if (!(o instanceof Node))
				return false;
			Node node = (Node) o;
			return node.material == material && node.metadata == metadata;
		}

		@Override
		public String toString()
		{
			return material.toString() + ":" + metadata;
		}
	}
}
