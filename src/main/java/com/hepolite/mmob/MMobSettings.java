package com.hepolite.mmob;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.hepolite.mmob.handlers.DungeonHandler;
import com.hepolite.mmob.handlers.ItemEffectHandler;
import com.hepolite.mmob.handlers.LootDropHandler;
import com.hepolite.mmob.handlers.RoleHandler;
import com.hepolite.mmob.settings.SettingsAbilities;
import com.hepolite.mmob.settings.SettingsDungeons;
import com.hepolite.mmob.settings.SettingsItemEffects;
import com.hepolite.mmob.settings.SettingsLoot;
import com.hepolite.mmob.settings.SettingsRoles;

public class MMobSettings
{
	// Control variables
	protected FileConfiguration config;

	public static boolean isDebugmode = false;

	/** Initialize the settings */
	public MMobSettings()
	{
		// Create config file if it doesn't already exist
		File file = new File(MMobPlugin.getInstance().getDataFolder(), "config.yml");
		if (!file.exists())
		{
			Log.log("Detected no config file, generating one...");
			config = MMobPlugin.getInstance().getConfig();
			addDefaults();
			save();
		}
		else
			Log.log("Found an already existing config file, using it...");
		reload();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Adds default keys to the configuration file */
	public void addDefaults()
	{
		addDefault("General.debugmode", false);
		addDefault("General.Bossbar.mobHealthbarDistance", 80);
		addDefault("General.Bossbar.mobHealthbarUpdateTime", 10);

		addDefault("General.Mobs.maxLevel", 50);
		addDefault("General.Mobs.levelSearchDistance", 150);
		addDefault("General.Mobs.minSpawnDistance", 20);
		addDefault("General.Mobs.healthScale", 4.0);
		addDefault("General.Mobs.damageScale", 1.0);
		addDefault("General.Mobs.armorScale", 1.0);
		addDefault("General.Mobs.targetAquireDistance", 25.0);
		addDefault("General.Mobs.targetLoseDistance", 75.0);

		addDefault("General.Attacks.treatAttackAsRangedDistance", 15.0);
		addDefault("General.Attacks.treatAttackAsMagicDistance", 5.0);
		addDefault("General.Attacks.treatSkillsAsMagic", true);
		addDefault("General.Attacks.attackCooldown", 3.0);
		addDefault("General.Defence.armorEfficiency", 10.0);
		addDefault("General.Defence.firePotionEnchantLevelEquivalent", 7.0);
		addDefault("General.Defence.resistancePotionEnchantLevelEquivalent", 4.0);

		addDefault("Spawns.allow", true);
		addDefault("Spawns.spawnChance", 0.005);
		addDefault("Spawns.blockedWorlds", new String[] { "ExampleWorldName", "AnotherExampleWorldName" });
		// addDefault("Spawns.Creeper.allow", true);
		// addDefault("Spawns.Creeper.chanceMultiplier", 1.0);
		addDefault("Spawns.Enderman.allow", true);
		addDefault("Spawns.Enderman.chanceMultiplier", 2.0);
		addDefault("Spawns.Skeleton.allow", true);
		addDefault("Spawns.Skeleton.chanceMultiplier", 1.0);
		addDefault("Spawns.Spider.allow", true);
		addDefault("Spawns.Spider.chanceMultiplier", 1.0);
		addDefault("Spawns.Witch.allow", true);
		addDefault("Spawns.Witch.chanceMultiplier", 1.5);
		addDefault("Spawns.Zombie.allow", true);
		addDefault("Spawns.Zombie.chanceMultiplier", 0.95);
		// addDefault("Spawns.Zombie Pigman.allow", true);
		// addDefault("Spawns.Zombie Pigman.chanceMultiplier", 1.0);

		// addDefault("Spawns.Creeper.roles", new String[] {});
		addDefault("Spawns.Enderman.roles", new String[] { "Phantom", "Voidling" });
		addDefault("Spawns.Skeleton.roles", new String[] { "Archer", "Knight", "Necromancer" });
		addDefault("Spawns.Spider.roles", new String[] { "Funnel-Web", "Matriarch", "Pyre" });
		addDefault("Spawns.Witch.roles", new String[] { "Spellweaver" });
		addDefault("Spawns.Zombie.roles", new String[] { "Bruiser", "Mage", "Tank" });
		// addDefault("Spawns.Zombie Pigman.roles", new String[] {});
	}

	// ////////////////////////////////////////////////////////////////////////////////////

	/** Assigns one default value to the calling configuration class */
	public void addDefault(String propertyName, Object value)
	{
		if (value == null)
			config.addDefault(propertyName, "...");
		config.addDefault(propertyName, value);
	}

	/** Adds a new value to the config */
	public void add(String propertyName, Object value)
	{
		config.set(propertyName, value);
	}

	/** Removes the given property from the config */
	public void remove(String propertyName)
	{
		config.set(propertyName, null);
	}

	/** Reloads the configuration file */
	public void reload()
	{
		MMobPlugin.getInstance().reloadConfig();
		config = MMobPlugin.getInstance().getConfig();

		// Get general settings
		isDebugmode = getBoolean("General.debugmode");

		// Load up sub-systems
		SettingsAbilities.initialize();
		SettingsRoles.initialize();
		SettingsItemEffects.initialize();
		SettingsLoot.initialize();
		SettingsDungeons.initialize();

		// Notify others of the changes
		RoleHandler.loadRolesFromConfig();
		ItemEffectHandler.loadItemEffectsFromConfig();
		LootDropHandler.loadFromConfig();
		DungeonHandler.loadFromConfig();
	}

	/** Saves the configuration file */
	public void save()
	{
		config.options().copyDefaults(true);

		// Save sub-systems to the config
		DungeonHandler.saveToConfig();

		MMobPlugin.getInstance().saveConfig();
	}

	public boolean hasProperty(String propertyName)
	{
		return config.contains(propertyName);
	}

	public boolean getBoolean(String propertyName)
	{
		return config.getBoolean(propertyName);
	}

	public boolean getBoolean(String primaryPropertyPath, String secondaryPropertyPath, String propertyName)
	{
		if (hasProperty(primaryPropertyPath + "." + propertyName))
			return getBoolean(primaryPropertyPath + "." + propertyName);
		return getBoolean(secondaryPropertyPath + "." + propertyName);
	}

	public int getInteger(String propertyName)
	{
		return config.getInt(propertyName);
	}

	public int getInteger(String primaryPropertyPath, String secondaryPropertyPath, String propertyName)
	{
		return getInteger(primaryPropertyPath, secondaryPropertyPath, propertyName, 0);
	}

	public int getInteger(String primaryPropertyPath, String secondaryPropertyPath, String propertyName, int defaultValue)
	{
		if (hasProperty(primaryPropertyPath + "." + propertyName))
			return getInteger(primaryPropertyPath + "." + propertyName);
		if (hasProperty(secondaryPropertyPath + "." + propertyName))
			return getInteger(secondaryPropertyPath + "." + propertyName);
		return defaultValue;
	}

	public float getFloat(String propertyName)
	{
		return (float) config.getDouble(propertyName);
	}

	public float getFloat(String primaryPropertyPath, String secondaryPropertyPath, String propertyName)
	{
		if (hasProperty(primaryPropertyPath + "." + propertyName))
			return getFloat(primaryPropertyPath + "." + propertyName);
		return getFloat(secondaryPropertyPath + "." + propertyName);
	}

	public String getString(String propertyName)
	{
		String string = config.getString(propertyName);
		return (string == null ? "" : string);
	}

	public String getString(String primaryPropertyPath, String secondaryPropertyPath, String propertyName)
	{
		if (hasProperty(primaryPropertyPath + "." + propertyName))
			return getString(primaryPropertyPath + "." + propertyName);
		return getString(secondaryPropertyPath + "." + propertyName);
	}

	public List<String> getStringList(String propertyName)
	{
		return config.getStringList(propertyName);
	}

	public List<String> getStringList(String primaryPropertyPath, String secondaryPropertyPath, String propertyName)
	{
		if (hasProperty(primaryPropertyPath + "." + propertyName))
			return getStringList(primaryPropertyPath + "." + propertyName);
		return getStringList(secondaryPropertyPath + "." + propertyName);
	}

	public Set<String> getKeys(String propertyPath)
	{
		ConfigurationSection section = config.getConfigurationSection(propertyPath);
		if (section == null)
			return new HashSet<String>();
		return section.getKeys(false);
	}

	/** Returns a value that is scaled, follows standard name convention (value = baseField + scale * scaleField) */
	public float getScaledValue(String primarySection, String secondarySection, String propertyName, float scale, float defaultValue)
	{
		float value = defaultValue;
		if (hasProperty(primarySection + ".base" + propertyName) || hasProperty(secondarySection + ".base" + propertyName))
			value = getFloat(primarySection, secondarySection, ".base" + propertyName);
		value += scale * getFloat(primarySection, secondarySection, ".scale" + propertyName);
		return value;
	}
}
