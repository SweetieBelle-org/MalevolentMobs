package com.hepolite.mmob.itemeffects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.NBTAPI;
import com.hepolite.mmob.utility.NBTAPI.NBTTag;

public abstract class ItemEffectChest extends ItemEffect
{
	protected float value = 0;

	protected ItemEffectChest(String name)
	{
		super(name);
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		// Ignore events where the player didn't right-click
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		event.setCancelled(true);

		// Make sure that a chest can be placed where the player is clicking
		Block clickedBlock = event.getClickedBlock();
		Block block = clickedBlock.getRelative(event.getBlockFace());
		if (block.getType() != Material.AIR) // Need to place the chest in air
			return;
		BlockState state = block.getState();
		block.setType(Material.CHEST);
		BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(state.getBlock(), state, block, new ItemStack(Material.CHEST), player, true, EquipmentSlot.HAND);
		Common.postEvent(blockPlaceEvent);

		// If the event was cancelled, restore the chest back to air again, otherwise fill it with goodies
		if (blockPlaceEvent.isCancelled())
			block.setType(Material.AIR);
		else
		{
			damageItem(item, 10000.0);

			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST_FAR, 1.0f, -0.4f);
			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1.0f, 0.6f);

			// Get the chest inventory and copy over the list of valid items
			Inventory inventory = ((Chest) block.getState()).getBlockInventory();
			addItems(inventory, block.getLocation());
			performEasterEgg(inventory, block.getLocation());
		}
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType() == Material.CHEST;
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f", 10.0f * value);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] parts = dataString.split(":");
		value = 0.1f * Float.parseFloat(parts[0]);
	}

	/** Adds the various items into the chest */
	protected abstract void addItems(Inventory inventory, Location location);

	// //////////////////////////////////////////////////////////////////////

	/** Really useless easter egg that is added into the effect */
	private void performEasterEgg(Inventory inventory, Location location)
	{
		if (random.nextFloat() > 0.075f)
			return;

		// Find a random player in the vicinity and add a skull of that player
		List<Player> players = Common.getPlayersInRange(location, 20.0f);
		if (players.size() == 0)
			return;
		Player player = players.get(random.nextInt(players.size()));

		// Generate the skull item
		NBTTag tag = new NBTTag();
		tag.setString("SkullOwner", player.getName());
		ItemStack skull = NBTAPI.setTag(NBTAPI.getItemStack(Material.SKULL_ITEM, 1, (short) 3), tag);

		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lYo&ku W&r&c&lill b&4e th&r&o&c&le &0ne&kxt.&r&c&l.."));
		String[] lore = getEasterEggLore();
		List<String> list = new LinkedList<String>();
		String reset = "&r&8";
		String[] noise = new String[] { "&k", "&k", "&k", "&0", "&0", "&l", "&4", "&o" };
		for (String line : lore)
		{
			for (int i = 0; i < 2 + random.nextInt(4); i++)
			{
				int length = 1 + random.nextInt(8);
				if (line.length() > 2 * length)
				{
					int index = 2 + random.nextInt(line.length() - length - 3);
					while (line.charAt(index - 1) == '&' || line.charAt(index) == '&')
					{
						index--;
						if (index <= 2)
							break;
					}
					line = line.substring(0, index) + noise[random.nextInt(noise.length)] + line.substring(index, index + length) + reset + line.substring(index + length);
				}
			}
			list.add(ChatColor.translateAlternateColorCodes('&', reset + line));
		}
		meta.setLore(list);
		skull.setItemMeta(meta);

		// Finalize everything
		inventory.addItem(skull);
	}

	/** Returns a list of various creepy stories */
	private String[] getEasterEggLore()
	{
		List<String[]> lores = new ArrayList<String[]>();
		lores.add(new String[] { "It emerges from the shell that was its womb,", "glad to be freed from the constraints of its expendable", "vessel. Now, all it needs to do is grow and wait." });
		lores.add(new String[] { "The spores will soon be released and travel on", "the currents of the air and across the lands,", "listening for the voice of another cursed mother." });
		lores.add(new String[] { "No matter where you search through the blurred", "end of the night, she will always have left a", "few steps ahead of you, leaving behind piles of ashes" });
		lores.add(new String[] { "A drained bottle next to a sudsy glass, a layer,", "of smoke on the stagnant air; hers will be the", "joke at which the nodding drinkers still die,", "but you will never hear her voice." });
		lores.add(new String[] { "They are told of the evil that haunts the world", "at night. They are told to always remain silent,", "because if she hear you...", "... she will tear you apart limb for limb." });
		return lores.get(random.nextInt(lores.size()));
	}
}
