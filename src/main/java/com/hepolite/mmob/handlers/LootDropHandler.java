package com.hepolite.mmob.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.itemeffects.ItemEffect;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsLoot;
import com.hepolite.mmob.settings.SettingsRoles;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.NBTAPI;

public class LootDropHandler
{
	// Control variables
	private final static HashMap<String, NameSection> randomNames = new HashMap<String, NameSection>();
	private final static String nameGroups[] = new String[] { "helmet", "chestplate", "leggings", "boots", "sword", "bow", "misc", "tool", "wand" };

	private final static Random random = new Random();

	/** Performs a drop for the malevolent mob */
	public static void dropLoot(MalevolentMob mob)
	{
		// Must have a valid mob
		if (mob == null)
			return;

		// Detect the loot file for the mob
		Settings settings = SettingsRoles.getConfig(mob.getRole().lootDefinitionFile);
		if (settings == null)
		{
			Log.log("[LootHandler] Was unable to locate the loot definition file '" + mob.getRole().lootDefinitionFile + "'");
			return;
		}

		// Find all items to grant
		Set<String> itemKeys = settings.getKeys("Loot.items");
		for (String itemKey : itemKeys)
		{
			String key = settings.getString("Loot.items." + itemKey);

			// Get the item and drop it
			ItemStack item;
			if (key.equals("#random"))
				item = getRandomItem(getMobLevelGroup(mob));
			else
				item = getItemFromSettings(settings, "Loot.items." + itemKey, getMobLevelGroup(mob));

			if (item != null)
				mob.getEntity().getWorld().dropItem(mob.getEntity().getEyeLocation(), item);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Get the level group of the mob */
	private static String getMobLevelGroup(MalevolentMob mob)
	{
		// Figure out the level of the mob
		Settings settings = SettingsLoot.getConfig("Core");
		Set<String> levelGroups = settings.getKeys("level");

		float highest = 0;
		TreeMap<Float, String> map = new TreeMap<Float, String>();
		for (String levelGroup : levelGroups)
		{
			highest = Math.max(highest, settings.getFloat("level." + levelGroup));
			map.put(settings.getFloat("level." + levelGroup), levelGroup);
		}

		float index = mob.getLevel();
		for (Iterator<Entry<Float, String>> it = map.descendingMap().entrySet().iterator(); it.hasNext();)
		{
			Entry<Float, String> entry = it.next();
			if (index >= entry.getKey())
				return entry.getValue();
		}
		return "INVALID_GROUP";
	}

	/** Returns the group associated with the given item */
	private static String getGroupOfItem(ItemStack item)
	{
		String type = item.getType().toString().toLowerCase();
		if (type.contains("helmet"))
			return "helmet";
		else if (type.contains("chestplate"))
			return "chestplate";
		else if (type.contains("leggings"))
			return "leggings";
		else if (type.contains("boots"))
			return "boots";
		else if (type.contains("sword"))
			return "sword";
		else if (type.contains("bow"))
			return "bow";
		else if (item.getType().getMaxDurability() > 0)
			return "tool";
		else if (type.equals("stick") || type.equals("bone") || type.equals("blaze_rod"))
			return "wand";
		else
			return "misc";
	}

	/** Loads up everything from the config file */
	public static void loadFromConfig()
	{
		loadRandomNamesFromConfig(SettingsLoot.getConfig("Names"));
	}

	/** Loads up a random item from the config file and returns it. Returns null if there were no valid items in the config file, or none could be constructed strictly with regard to their definition */
	public static ItemStack getRandomItem(String levelGroup)
	{
		Settings settings = SettingsLoot.getConfig("Loot");

		Set<String> possibleItems = settings.getKeys(levelGroup);
		while (!possibleItems.isEmpty())
		{
			// Sort out the items in numerical order, so that it is easier to pick a random item from the weight
			float totalCount = 0.0f;

			TreeMap<Float, String> map = new TreeMap<Float, String>();
			for (String possibleItem : possibleItems)
			{
				totalCount += settings.getFloat(levelGroup + "." + possibleItem + ".weight");
				map.put(totalCount, possibleItem);
			}

			// Grab one of the nodes in the tree
			float index = totalCount * random.nextFloat();
			for (Iterator<Entry<Float, String>> it = map.entrySet().iterator(); it.hasNext();)
			{
				Entry<Float, String> entry = it.next();
				if (index < entry.getKey())
				{
					// Load up the item
					ItemStack item = getItemFromSettings(settings, levelGroup + "." + entry.getValue(), levelGroup);
					if (item != null)
						return item;

					// Remove the item from the list and try to find another one
					possibleItems.remove(entry.getValue());
					break;
				}
			}
		}
		return null;
	}

	/** Attempts to load up an item from the given settings file, under the given property. Will return null if the item couldn't be loaded, or the item effects weren't possible to add */
	public static ItemStack getItemFromSettings(Settings settings, String property, String levelGroup)
	{
		try
		{
			Material material = Material.getMaterial(settings.getString(property + ".material").toUpperCase());
			int amount = settings.getInteger(property + ".amount", 1);
			short metadata = (short) settings.getInteger(property + ".metadata", 0);
			ItemStack item = NBTAPI.getItemStack(material, amount, metadata);

			applyBaseNBTData(item, settings, property);
			applyNameAndLore(item, settings, property);
			if (!applyItemEffects(item, settings, property, levelGroup))
				return null;
			applyHistory(item, settings, property);
			applyEnchantments(item, settings, property);

			return item;
		}
		catch (Exception e)
		{
			Log.log("[LootHandler] Failed to parse loot item '" + property + "'!");
			e.printStackTrace();
		}
		return null;
	}

	private static void applyBaseNBTData(ItemStack item, Settings settings, String property)
	{
		if (settings.hasProperty(property + ".nbt"))
			NBTAPI.setTag(item, settings.getTagCompound(property + ".nbt"));
	}

	private static void applyNameAndLore(ItemStack item, Settings settings, String property)
	{
		ItemMeta meta = item.getItemMeta();
		if (settings.hasProperty(property + ".name"))
		{
			String name = settings.getString(property + ".name");
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "#random".equals(name) ? getRandomName(item) : name));
		}
		if (settings.hasProperty(property + ".lore"))
		{
			List<String> lore = settings.getStringList(property + ".lore");
			for (int i = 0; i < lore.size(); i++)
				lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
	}

	private static boolean applyItemEffects(ItemStack item, Settings settings, String property, String levelGroup)
	{
		Set<String> itemEffects = settings.getKeys(property + ".itemeffects");
		for (String itemEffect : itemEffects)
		{
			// Attempt to add all random item effects
			String key = settings.getString(property + ".itemeffects." + itemEffect);
			if (key.startsWith("#random"))
			{
				String group = key.contains(".") ? key.split("\\.")[1] : levelGroup;
				if (!applyRandomItemEffect(item, group))
					return false;
			}
			// Load a specified item effect
			else
			{
				String itemEffectData = SettingsLoot.readItemEffect(settings, property + ".itemeffects." + itemEffect);
				ItemEffect effect = ItemEffectHandler.readItemEffect(itemEffectData);
				if (effect == null || !ItemEffectHandler.addItemEffect(item, effect))
					return false;
			}
		}
		return true;
	}

	private static void applyHistory(ItemStack item, Settings settings, String property)
	{
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasLore())
			meta.setLore(new LinkedList<String>());

		if (!settings.hasProperty(property + ".history"))
			return;
		String historyData = settings.getString(property + ".history");

		// Grab all the words in the history
		List<String> words = new LinkedList<String>();

		if (historyData.equals("#random"))
		{
			for (ItemEffect effect : ItemEffectHandler.getItemEffects(item))
			{
				String lore = effect.getLore();
				if (lore == null)
					continue;
				for (String word : lore.split(" "))
					words.add(word);
			}
		}
		else
		{
			for (String word : historyData.split(" "))
				words.add(word);
		}

		List<String> history = new LinkedList<String>();
		String current = "";
		boolean useName = true;
		while (!words.isEmpty())
		{
			String word = words.remove(0);

			// Determine whether the word is a keyword or not
			if (word.contains("#name"))
			{
				if (useName)
				{
					if (meta.hasDisplayName())
						word = word.replace("#name", ChatColor.stripColor(meta.getDisplayName()));
					else
						word = word.replace("#name", Common.toTitleCase(item.getType().toString().replaceAll("_", " ")));
					String[] parts = word.split(" ");
					if (parts.length > 1)
						for (int i = parts.length - 1; i > 0; i--)
							words.add(0, parts[i]);
					word = parts[0];
					useName = false;
				}
				else
					word = word.replace("#name", "It");
			}
			if (word.contains("#resetname"))
			{
				word = word.replaceAll("#resetname", "");
				useName = true;
			}

			// Add word to the line, if applicable
			boolean newline = (words.size() == 0);
			if ((current + " " + word).length() < 50)
				current += " " + word;
			else
			{
				newline = true;
				words.add(0, word);
			}
			if (newline)
			{
				history.add("&f" + current.substring(1));
				current = "";
			}
		}

		// Apply history to the item
		int index = 0;
		List<String> lore = meta.getLore();
		for (String line : history)
			lore.add(index++, ChatColor.translateAlternateColorCodes('&', line));
		meta.setLore(lore);

		item.setItemMeta(meta);
	}

	private static void applyEnchantments(ItemStack item, Settings settings, String property)
	{
		Set<String> enchantments = settings.getKeys(property + ".enchantments");
		for (String enchantment : enchantments)
		{
			String enchantmentName = settings.getString(property + ".enchantments." + enchantment + ".name");
			int level = settings.getInteger(property + ".enchantments." + enchantment + ".level");
			item.addUnsafeEnchantment(Enchantment.getByName(enchantmentName.toUpperCase()), level);
			// item.addEnchantment(Enchantment.getByName(enchantmentName.toUpperCase()), level);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// ITEM EFFECTS // ITEM EFFECTS // ITEM EFFECTS // ITEM EFFECTS // ITEM EFFECTS // ITEM EFFECTS //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Applies a random item effect, which can be applied to the given item, from the configuration file. Returns false if there was no valid item effect added, true otherwise */
	public static boolean applyRandomItemEffect(ItemStack item, String levelGroup)
	{
		// Load up all the relevant effects in the config file
		Settings settings = SettingsLoot.getConfig("Item_Effects");

		Set<String> keys = settings.getKeys(levelGroup);
		List<String> effects = new ArrayList<String>(keys.size());
		for (String key : keys)
			effects.add(SettingsLoot.readItemEffect(settings, levelGroup + "." + key));

		// Check if the effect can be applied to the given item
		while (!effects.isEmpty())
		{
			String effect = effects.remove(random.nextInt(effects.size()));
			ItemEffect itemEffect = ItemEffectHandler.readItemEffect(effect);
			if (itemEffect != null && itemEffect.canBeUsedOnItem(item) && ItemEffectHandler.addItemEffect(item, itemEffect))
				return true;
		}
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	// NAMES OF ITEMS // NAMES OF ITEMS // NAMES OF ITEMS // NAMES OF ITEMS // NAMES OF ITEMS //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Deal with a section in the config file */
	private static class NameSection
	{
		// Control variables
		private final String section;

		private final HashMap<String, NameNode> names = new HashMap<String, NameNode>();

		/** Initialize the section */
		public NameSection(String section)
		{
			this.section = section;
		}

		/** Returns the name of the section */
		public String getSectionName()
		{
			return section;
		}

		/** Returns the number of names stored inside the section */
		public int getNameCount(String group)
		{
			int count = 0;
			for (NameNode node : names.values())
				count += node.getNameCount(group);
			return count;
		}

		/** Returns a random name from this section */
		public String getRandomName(String group)
		{
			// Sort out the nodes in numerical order, so that it is easier to pick a random name
			int totalCount = 0;

			TreeMap<Integer, NameNode> map = new TreeMap<Integer, NameNode>();
			for (NameNode node : names.values())
			{
				totalCount += node.getNameCount(group);
				map.put(totalCount, node);
			}

			// Grab one of the nodes in the tree
			int index = random.nextInt(totalCount);
			for (Iterator<Entry<Integer, NameNode>> it = map.entrySet().iterator(); it.hasNext();)
			{
				Entry<Integer, NameNode> entry = it.next();
				if (index < entry.getKey())
					return entry.getValue().getName(group);
			}
			return "(NAME NOT FOUND)";
		}
	}

	/** Stores a single name, used to easily track the name structure */
	private static class NameNode
	{
		// Control variables
		private final String name;
		private int nameCount = -1;
		private final HashMap<String, HashMap<String, NameSection>> links = new HashMap<String, HashMap<String, NameSection>>();

		/** Initialize the node */
		public NameNode(String name)
		{
			this.name = name;
		}

		/** Returns the number of names this one node contains */
		public int getNameCount(String group)
		{
			// Return the cached value, if it has been computed
			if (nameCount != -1)
				return nameCount;

			// Figure out if the name is containing sub-names or not
			if (links.isEmpty())
				return (name.equals("@name") ? 5 : 1);

			// Count the number of names under the links
			int count = 0;

			String trimmed = name.substring(name.indexOf("#"));
			while (trimmed.length() != 0)
			{
				// Grab the name of the link
				String linkName;
				if (trimmed.contains(" "))
					linkName = trimmed.substring(1, trimmed.indexOf(" "));
				else
					linkName = trimmed.substring(1);

				trimmed = trimmed.replace("#" + linkName, "");
				if (trimmed.contains("#"))
					trimmed = trimmed.substring(trimmed.indexOf("#"));
				else
					trimmed = "";

				// Count how many names there are associated with that link
				for (HashMap<String, NameSection> sections : links.values())
				{
					NameSection section = sections.get(linkName.replaceAll("@group", group));
					if (section != null)
					{
						count += section.getNameCount(group);
						break;
					}
				}
			}
			nameCount = (count == 0 ? 1 : count);
			return nameCount;
		}

		/** Returns the name of the node */
		public String getName(String group)
		{
			// Simplest case, no need to worry about links
			if (!name.contains("#"))
				return name.replace("@group", group);

			// Deal with links etc
			else
			{
				// Start building the final name
				String finalName = name.substring(0, name.indexOf("#"));

				String trimmed = name.substring(name.indexOf("#"));
				while (trimmed.length() != 0)
				{
					// Grab the name of the link
					String linkName;
					if (trimmed.contains(" "))
						linkName = trimmed.substring(1, trimmed.indexOf(" "));
					else
						linkName = trimmed.substring(1);

					finalName += "#";
					trimmed = trimmed.replace("#" + linkName, "");
					if (trimmed.contains("#"))
					{
						finalName += trimmed.substring(0, trimmed.indexOf("#"));
						trimmed = trimmed.substring(trimmed.indexOf("#"));
					}
					else
						trimmed = "";

					// Get the name from the section associated with that link
					for (HashMap<String, NameSection> sections : links.values())
					{
						NameSection section = sections.get(linkName.replaceAll("@group", group));
						if (section != null)
						{
							finalName = finalName.replace("#", section.getRandomName(group));
							break;
						}
					}
				}
				return finalName;
			}
		}
	}

	/** Loads up the name sections from the configuration file */
	private static void loadRandomNamesFromConfig(Settings settings)
	{
		randomNames.clear();

		// Load up all sections in the settings file
		Set<String> sections = settings.getKeys("");
		for (String section : sections)
		{
			Set<String> lists = settings.getKeys(section);
			if (lists.isEmpty())
				randomNames.put(section, new NameSection(section));
			else
			{
				for (String list : lists)
					randomNames.put(section + "." + list, new NameSection(section + "." + list));
			}
		}

		// Build the section structure
		for (NameSection section : randomNames.values())
		{
			// Read up all the names in the section
			List<String> names = settings.getStringList(section.getSectionName());
			for (String name : names)
			{
				// Store the node
				NameNode node = new NameNode(name);
				section.names.put(name, node);

				// Handle all the links
				if (name.contains("#"))
				{
					String trimmed = name.substring(name.indexOf("#"));
					while (trimmed.length() != 0)
					{
						// Grab the name of the link, and locate the next (if any) link
						String linkName;
						if (trimmed.contains(" "))
							linkName = trimmed.substring(1, trimmed.indexOf(" "));
						else
							linkName = trimmed.substring(1);

						trimmed = trimmed.replace("#" + linkName, "");
						if (trimmed.contains("#"))
							trimmed = trimmed.substring(trimmed.indexOf("#"));
						else
							trimmed = "";

						// Store the link
						HashMap<String, NameSection> map = new HashMap<String, NameSection>();
						if (linkName.contains("@group"))
						{
							for (String group : nameGroups)
							{
								String modifiedLinkName = linkName.replaceAll("@group", group);
								map.put(modifiedLinkName, randomNames.get(modifiedLinkName));
							}
						}
						else
							map.put(linkName, randomNames.get(linkName));

						node.links.put(linkName, map);
					}
				}
			}
		}
	}

	/** Returns a random name for the given item */
	private static String getRandomName(ItemStack item)
	{
		// Grab a random item name
		String group = getGroupOfItem(item);
		NameSection section = randomNames.get(group);
		if (section == null)
			return "(GROUP NOT FOUND)";
		return section.getRandomName(group).replaceAll("@name", Common.toTitleCase(item.getType().toString().replaceAll("_", " ")));
	}
}
