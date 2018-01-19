package com.hepolite.mmob.handlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobSettings;
import com.hepolite.mmob.itemeffects.ItemEffect;
import com.hepolite.mmob.itemeffects.ItemEffectArmorShredder;
import com.hepolite.mmob.itemeffects.ItemEffectBulwark;
import com.hepolite.mmob.itemeffects.ItemEffectCharged;
import com.hepolite.mmob.itemeffects.ItemEffectDuplicator;
import com.hepolite.mmob.itemeffects.ItemEffectEarthmover;
import com.hepolite.mmob.itemeffects.ItemEffectEntropyLimit;
import com.hepolite.mmob.itemeffects.ItemEffectExplosiveArrows;
import com.hepolite.mmob.itemeffects.ItemEffectFiery;
import com.hepolite.mmob.itemeffects.ItemEffectFireward;
import com.hepolite.mmob.itemeffects.ItemEffectFireworks;
import com.hepolite.mmob.itemeffects.ItemEffectFragile;
import com.hepolite.mmob.itemeffects.ItemEffectFrost;
import com.hepolite.mmob.itemeffects.ItemEffectGrowth;
import com.hepolite.mmob.itemeffects.ItemEffectHarvest;
import com.hepolite.mmob.itemeffects.ItemEffectHeated;
import com.hepolite.mmob.itemeffects.ItemEffectHungry;
import com.hepolite.mmob.itemeffects.ItemEffectLacerate;
import com.hepolite.mmob.itemeffects.ItemEffectLevity;
import com.hepolite.mmob.itemeffects.ItemEffectMagicMirror;
import com.hepolite.mmob.itemeffects.ItemEffectMagicShield;
import com.hepolite.mmob.itemeffects.ItemEffectModifier;
import com.hepolite.mmob.itemeffects.ItemEffectPiercing;
import com.hepolite.mmob.itemeffects.ItemEffectPotionEffect;
import com.hepolite.mmob.itemeffects.ItemEffectPowerline;
import com.hepolite.mmob.itemeffects.ItemEffectRelic;
import com.hepolite.mmob.itemeffects.ItemEffectRepair;
import com.hepolite.mmob.itemeffects.ItemEffectRunicShield;
import com.hepolite.mmob.itemeffects.ItemEffectSaturation;
import com.hepolite.mmob.itemeffects.ItemEffectSharpness;
import com.hepolite.mmob.itemeffects.ItemEffectShockAbsorber;
import com.hepolite.mmob.itemeffects.ItemEffectShroud;
import com.hepolite.mmob.itemeffects.ItemEffectThaumicBolt;
import com.hepolite.mmob.itemeffects.ItemEffectTimber;
import com.hepolite.mmob.itemeffects.ItemEffectTreasure;
import com.hepolite.mmob.itemeffects.ItemEffectTunneler;
import com.hepolite.mmob.itemeffects.ItemEffectUnmelting;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsItemEffects;
import com.hepolite.mmob.utility.NBTAPI;
import com.hepolite.mmob.utility.NBTAPI.NBTList;
import com.hepolite.mmob.utility.NBTAPI.NBTTag;
import com.sucy.skill.api.skills.Skill;

public class ItemEffectHandler
{
	// Control variables
	private static HashMap<String, ItemEffect> effectMap = new HashMap<String, ItemEffect>();

	@SuppressWarnings("unused")
	private static ItemEffectHandler instance = new ItemEffectHandler();

	private static int onTickTimer = 0;
	private static int tickCounter = 0;

	private ItemEffectHandler()
	{
		registerItemEffect(new ItemEffectFireward());
		registerItemEffect(new ItemEffectArmorShredder());
		registerItemEffect(new ItemEffectSharpness());
		registerItemEffect(new ItemEffectFrost());
		registerItemEffect(new ItemEffectPotionEffect("Wither", PotionEffectType.WITHER));
		registerItemEffect(new ItemEffectPotionEffect("Poison", PotionEffectType.POISON));
		registerItemEffect(new ItemEffectMagicShield());
		registerItemEffect(new ItemEffectEntropyLimit());
		registerItemEffect(new ItemEffectBulwark());
		registerItemEffect(new ItemEffectRepair());
		registerItemEffect(new ItemEffectSaturation());
		registerItemEffect(new ItemEffectTimber());
		registerItemEffect(new ItemEffectTunneler());
		registerItemEffect(new ItemEffectEarthmover());
		registerItemEffect(new ItemEffectGrowth());
		registerItemEffect(new ItemEffectShockAbsorber());
		registerItemEffect(new ItemEffectLacerate());
		registerItemEffect(new ItemEffectHarvest());
		registerItemEffect(new ItemEffectRunicShield());
		registerItemEffect(new ItemEffectDuplicator());
		registerItemEffect(new ItemEffectLevity());
		registerItemEffect(new ItemEffectExplosiveArrows());
		registerItemEffect(new ItemEffectShroud());
		registerItemEffect(new ItemEffectTreasure());
		registerItemEffect(new ItemEffectFragile());
		registerItemEffect(new ItemEffectPowerline());
		registerItemEffect(new ItemEffectFireworks());
		registerItemEffect(new ItemEffectHungry());
		registerItemEffect(new ItemEffectCharged());
		registerItemEffect(new ItemEffectMagicMirror());
		registerItemEffect(new ItemEffectThaumicBolt());
		registerItemEffect(new ItemEffectHeated());
		registerItemEffect(new ItemEffectModifier());
		registerItemEffect(new ItemEffectRelic());
		registerItemEffect(new ItemEffectFiery());
		registerItemEffect(new ItemEffectPiercing());
		registerItemEffect(new ItemEffectUnmelting());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Registrates a new item effect */
	private static void registerItemEffect(ItemEffect effect)
	{
		effectMap.put(effect.getName().toLowerCase(), effect);
	}

	/** Returns an item effect based on name, or null if no effect with the name could be found */
	public static ItemEffect getItemEffect(String name)
	{
		return effectMap.get(name.toLowerCase());
	}

	/** Loads up all the item effect settings from the config */
	public static void loadItemEffectsFromConfig()
	{
		for (ItemEffect effect : effectMap.values())
			effect.loadSettingsFromConfigFile(SettingsItemEffects.getConfig(effect.getName()));
	}

	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Returns the current tick number */
	public static int getCurrentTickNumber()
	{
		return tickCounter;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	// EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Called every tick */
	public static void onTick()
	{
		tickCounter++;

		// All the items are updated once every minute. This time frame is hardcoded, because that is easier to manage
		if (onTickTimer++ > 1200)
		{
			onTickTimer = 0;
			for (Player player : Bukkit.getOnlinePlayers())
				onPlayerTick(player);
		}
	}

	/** Handles the case where the player is ticked in an item effect context */
	public static void onPlayerTick(Player player)
	{
		// Process items that are worn by the player
		EntityEquipment equipment = player.getEquipment();
		ItemStack items[] = { equipment.getHelmet(), equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots() };
		for (ItemStack item : items)
		{
			if (item != null)
			{
				for (ItemEffect effect : getItemEffects(item))
					effect.onTick(player, item);
			}
		}

		// Process items that are in the player's inventory
		items = player.getInventory().getContents();
		for (ItemStack item : items)
		{
			if (item != null)
			{
				for (ItemEffect effect : getItemEffects(item))
				{
					if (!effect.mustBeWorn())
						effect.onTick(player, item);
				}
			}
		}
	}

	/** Handles the case where a player is left-clicking */
	public static void onPlayerLeftClick(PlayerInteractEvent event, Player player)
	{
		ItemStack item = event.getItem();
		for (ItemEffect effect : getItemEffects(item))
			effect.onLeftClick(event, player, item);
	}

	/** Handles the case where a player is right-clicking */
	public static void onPlayerRightClick(PlayerInteractEvent event, Player player)
	{
		ItemStack item = event.getItem();
		for (ItemEffect effect : getItemEffects(item))
			effect.onRightClick(event, player, item);
	}

	/** Handles the case where a player is damaged */
	public static void onPlayerTakeDamage(EntityDamageEvent event, Player player)
	{
		EntityEquipment equipment = player.getEquipment();
		ItemStack items[] = { equipment.getItemInMainHand(), equipment.getItemInOffHand(), equipment.getHelmet(), equipment.getChestplate(), equipment.getLeggings(), equipment.getBoots() };

		for (ItemStack item : items)
		{
			for (ItemEffect effect : getItemEffects(item))
				effect.onAttacked(event, player, item);
		}
	}

	/** Handles the case where a player deals damage */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public static void onPlayerDealDamage(EntityDamageByEntityEvent event, Player player)
	{
		// Skip SkillAPI skill damage from triggering item effects
		if (Skill.isSkillDamage() || event.getDamage() <= 1.0)
			return;

		// Get the itemstack that corresponded to the attack
		ItemStack item = null;
		if (event.getDamager() instanceof Arrow)
		{
			List<MetadataValue> metas = event.getDamager().getMetadata("[MMob]originBow");
			if (metas.size() != 0)
				item = (ItemStack) metas.get(0).value();
		}
		else
			item = player.getEquipment().getItemInMainHand();
		if (item == null || item.getType() == Material.AIR)
			return;

		// Apply effects based on that item
		for (ItemEffect effect : getItemEffects(item))
			effect.onAttacking(event, player, item);
	}

	/** Handles the case where a player fires an arrow */
	public static void onPlayerFireArrow(EntityShootBowEvent event, Player player)
	{
		ItemStack item = event.getBow();
		for (ItemEffect effect : getItemEffects(item))
			effect.onFireArrow(event, player, item);
	}

	/** Handles the case where a player breaks a block */
	public static void onBlockBreak(BlockBreakEvent event, Player player)
	{
		ItemStack item = player.getEquipment().getItemInMainHand();
		for (ItemEffect effect : getItemEffects(item))
			effect.onBlockBreak(event, player, item);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// SPECIAL ITEMS // SPECIAL ITEMS // SPECIAL ITEMS // SPECIAL ITEMS // SPECIAL ITEMS //
	// ////////////////////////////////////////////////////////////////////////////////////

	/** Adds a new item effect to the given itemstack. Returns the updated itemstack */
	public static boolean addItemEffect(ItemStack itemStack, ItemEffect itemEffect)
	{
		// Effect must exist and must be enabled
		Settings settings = SettingsItemEffects.getConfig(itemEffect.getName());
		if (settings == null || !settings.getBoolean("enable"))
			return false;

		NBTTag tag = NBTAPI.hasTag(itemStack) ? NBTAPI.getTag(itemStack) : new NBTTag();
		NBTList mmob = tag.hasKey("mmob") ? tag.getList("mmob") : new NBTList();
		NBTTag display = tag.hasKey("display") ? tag.getTag("display") : new NBTTag();
		NBTList lore = display.hasKey("Lore") ? display.getList("Lore") : new NBTList();

		// Check if the effect to add is incompatible with something else on the item. If it is, don't add it
		for (String effect : itemEffect.getIncompatibleEffects())
			for (int i = 0; i < mmob.size(); i++)
				if (mmob.getString(i).startsWith(effect))
					return false;
		mmob.addString(itemEffect.getName() + ":" + itemEffect.saveToString());

		// Store effect in the item lore
		List<String> description = new LinkedList<String>();
		itemEffect.addDescription(description);

		lore.addString(ChatColor.translateAlternateColorCodes('&', String.format("&9&n%s", itemEffect.getName().replaceAll("_", " "))));
		for (String string : description)
			lore.addString(ChatColor.translateAlternateColorCodes('&', string));

		// Finalize item
		display.setList("Lore", lore);
		tag.setTag("display", display);
		tag.setList("mmob", mmob);
		NBTAPI.setTag(itemStack, tag);

		if (MMobSettings.isDebugmode)
			Log.log("Added item effect '" + itemEffect.getName() + ":" + itemEffect.saveToString() + "' to item '" + itemStack.getType() + "'");
		return true;
	}

	/** Removes all item effects on the given item; returns a version of the item with all effects removed */
	public static void removeItemEffects(ItemStack itemStack)
	{
		NBTTag tag = NBTAPI.hasTag(itemStack) ? NBTAPI.getTag(itemStack) : null;
		if (tag == null || !tag.hasKey("mmob"))
			return;
		tag.remove("mmob");
		NBTAPI.setTag(itemStack, tag);
	}

	/** Returns a list of all item effects on the given item. If no effects were found, an empty list is returned */
	public static List<ItemEffect> getItemEffects(ItemStack itemStack)
	{
		List<ItemEffect> list = new LinkedList<ItemEffect>();

		NBTTag tag = NBTAPI.getTag(itemStack);
		if (tag == null || !tag.hasKey("mmob"))
			return list;
		NBTList mmob = tag.getList("mmob");

		for (int i = 0; i < mmob.size(); i++)
		{
			ItemEffect itemEffect = readItemEffect(mmob.getString(i));
			if (itemEffect != null)
				list.add(itemEffect);
		}
		return list;
	}

	/** Saves the given item effect string to the given item; will not update lore or name, or anything for that matter */
	public static void saveItemEffect(ItemStack itemStack, ItemEffect effect)
	{
		NBTTag tag = NBTAPI.hasTag(itemStack) ? NBTAPI.getTag(itemStack) : new NBTTag();
		NBTList mmob = tag.hasKey("mmob") ? tag.getList("mmob") : new NBTList();

		for (int i = 0; i < mmob.size(); i++)
			if (mmob.getString(i).startsWith(effect.getName()))
			{
				mmob.remove(i);
				break;
			}
		mmob.addString(effect.getName() + ":" + effect.saveToString());

		tag.setList("mmob", mmob);
		NBTAPI.setTag(itemStack, tag);
	}

	/** Returns an item effect from the string, format name:parameter1:parameter2:...:parameterN, or null if the string was invalid or the effect disabled */
	public static ItemEffect readItemEffect(String effect)
	{
		String[] components = effect.split(":");
		if (components.length == 0)
		{
			Log.log("Failed to parse item effect '" + effect + "'", Level.WARNING);
			return null;
		}

		ItemEffect itemEffect = getItemEffect(components[0]);
		if (itemEffect != null)
		{
			// Effect must exist and must be enabled
			Settings settings = SettingsItemEffects.getConfig(itemEffect.getName());
			if (settings == null || !settings.getBoolean("enable"))
				return null;

			// Parse the parameters for the effect
			String dataString = effect.replace(components[0] + ":", "");
			try
			{
				itemEffect.loadFromString(dataString);
			}
			catch (Exception exception)
			{
				Log.log("Failed to parse the item effect '" + components[0] + "' when reading string '" + dataString + "'", Level.WARNING);
				Log.log(exception.getLocalizedMessage(), Level.WARNING);
			}
			return itemEffect;
		}
		return null;
	}
}
