package com.hepolite.mmob.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;
import com.hepolite.mmob.utility.NBTAPI.NBTList;
import com.hepolite.mmob.utility.NBTAPI.NBTTag;

public class Settings
{
	// Control variables
	private File file;
	protected final FileConfiguration config;

	private boolean wasCreated = false;

	/** Initialize the settings */
	private Settings()
	{
		config = new YamlConfiguration();
	}

	/** Initialize the settings */
	public Settings(String name)
	{
		config = getConfig(null, name);
	}

	/** Initialize the settings */
	public Settings(String folder, String name)
	{
		config = getConfig(folder, name);
	}

	/** Creates a new config file, or grabs an already existing one, with the given name */
	private FileConfiguration getConfig(String folder, String name)
	{
		file = new File(MMobPlugin.getInstance().getDataFolder() + "/" + (folder != null ? folder : ""), name);
		if (!file.exists())
		{
			wasCreated = true;
			Log.log("Didn't find file '" + name + "', creating it...");
		}
		return YamlConfiguration.loadConfiguration(file);
	}

	/** Returns the config file for the given settings */
	public final FileConfiguration getConfig()
	{
		return config;
	}

	/** Saves the config file to disk */
	public final void save()
	{
		try
		{
			config.save(file);
		}
		catch (IOException exception)
		{
			Log.log("Failed to save configuration file '" + file.getName() + "'! ", Level.WARNING);
			Log.log(exception.getLocalizedMessage(), Level.WARNING);
		}
	}

	/** Returns if the setting was created this run or not */
	public boolean wasCreated()
	{
		return wasCreated;
	}

	/** Assigns the given value to the given field */
	public final void set(String field, Object value)
	{
		config.set(field, value);
	}

	/** Assigns one default value to the calling configuration class */
	public void addDefault(String propertyName, Object value)
	{
		if (!wasCreated || config.contains(propertyName))
			return;

		if (value == null)
			config.set(propertyName, "...");
		config.set(propertyName, value);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// GET DATA // GET DATA // GET DATA // GET DATA // GET DATA // GET DATA // GET DATA // GET DATA //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Returns the configuration base file for the given key */
	public Settings getBaseConfig(String propertyPath)
	{
		Settings settings = new Settings();
		Set<String> fields = getKeys(propertyPath);
		for (String field : fields)
			settings.set(field, config.get(propertyPath + "." + field));
		return settings;
	}

	/** Checks if the given property exists within the config */
	public boolean hasProperty(String propertyName)
	{
		return config.contains(propertyName);
	}

	public boolean getBoolean(String propertyName)
	{
		return config.getBoolean(propertyName);
	}

	public boolean getBoolean(Settings alternative, String propertyName)
	{
		if (hasProperty(propertyName))
			return getBoolean(propertyName);
		if (alternative != null && alternative.hasProperty(propertyName))
			return alternative.getBoolean(propertyName);
		return false;
	}

	public int getInteger(String propertyName)
	{
		return config.getInt(propertyName);
	}

	public int getInteger(String propertyName, int defaultValue)
	{
		if (hasProperty(propertyName))
			return getInteger(propertyName);
		return defaultValue;
	}

	public int getInteger(Settings alternative, String propertyName)
	{
		return getInteger(alternative, propertyName, 0);
	}

	public int getInteger(Settings alternative, String propertyName, int defaultValue)
	{
		if (hasProperty(propertyName))
			return getInteger(propertyName);
		if (alternative != null && alternative.hasProperty(propertyName))
			return alternative.getInteger(propertyName);
		return defaultValue;
	}

	public long getLong(String propertyName)
	{
		return config.getLong(propertyName);
	}

	public short getShort(String propertyName)
	{
		return (short) config.getInt(propertyName);
	}

	public byte getByte(String propertyName)
	{
		return (byte) config.getInt(propertyName);
	}

	public float getFloat(String propertyName)
	{
		return (float) config.getDouble(propertyName);
	}

	public float getFloat(Settings alternative, String propertyName)
	{
		return getFloat(alternative, propertyName, 0.0f);
	}

	public float getFloat(Settings alternative, String propertyName, float defaultValue)
	{
		if (hasProperty(propertyName))
			return getFloat(propertyName);
		if (alternative != null && alternative.hasProperty(propertyName))
			return alternative.getFloat(propertyName);
		return defaultValue;
	}

	public double getDouble(String propertyName)
	{
		return config.getDouble(propertyName);
	}

	public String getString(String propertyName)
	{
		String string = config.getString(propertyName);
		return (string == null ? "" : string);
	}

	public String getString(Settings alternative, String propertyName)
	{
		if (hasProperty(propertyName))
			return getString(propertyName);
		if (alternative != null)
			return alternative.getString(propertyName);
		return "";
	}

	public List<String> getStringList(String propertyName)
	{
		return config.getStringList(propertyName);
	}

	public List<String> getStringList(Settings alternative, String propertyName)
	{
		if (hasProperty(propertyName))
			return getStringList(propertyName);
		if (alternative != null && alternative.hasProperty(propertyName))
			return alternative.getStringList(propertyName);
		return new LinkedList<String>();
	}

	public Set<String> getKeys(String propertyPath)
	{
		ConfigurationSection section = config.getConfigurationSection(propertyPath);
		if (section == null)
			return new HashSet<String>();
		return section.getKeys(false);
	}

	/** Returns a value that is scaled, follows standard name convention (value = baseField + scale * scaleField) */
	public float getScaledValue(String propertyName, float scale, float defaultValue)
	{
		return getFloat("base" + propertyName) + scale * getFloat("scale" + propertyName);
	}

	/** Returns a value that is scaled, follows standard name convention (value = baseField + scale * scaleField) */
	public float getScaledValue(String propertyPath, String propertyName, float scale, float defaultValue)
	{
		return getFloat(propertyPath + "base" + propertyName) + scale * getFloat(propertyPath + "scale" + propertyName);
	}

	/** Returns a value that is scaled, follows standard name convention (value = baseField + scale * scaleField) */
	public float getScaledValue(Settings alternative, String propertyName, float scale, float defaultValue)
	{
		return getFloat(alternative, "base" + propertyName, defaultValue) + scale * getFloat(alternative, "scale" + propertyName);
	}

	/** Writes a NBT Tag to the configuration */
	public void setTagCompound(String field, NBTTag tag)
	{
		set(field, null);
		for (String key : tag.getKeys())
		{
			String type = tag.format(key);

			set(field + "." + key + ".type", type);
			if (type.equals("tag"))
				setTagCompound(field + "." + key + ".value", tag.getTag(key));
			else if (type.equals("list"))
				setTagList(field + "." + key + ".value", tag.getList(key));
			else if (type.equals("string"))
				set(field + "." + key + ".value", tag.getString(key));
			else if (type.equals("int"))
				set(field + "." + key + ".value", tag.getInt(key));
			else if (type.equals("long"))
				set(field + "." + key + ".value", tag.getLong(key));
			else if (type.equals("short"))
				set(field + "." + key + ".value", tag.getShort(key));
			else if (type.equals("byte"))
				set(field + "." + key + ".value", tag.getByte(key));
			else if (type.equals("float"))
				set(field + "." + key + ".value", tag.getFloat(key));
			else if (type.equals("double"))
				set(field + "." + key + ".value", tag.getDouble(key));
		}
	}

	/** Loads up a NBT Tag from the configuration */
	public NBTTag getTagCompound(String field)
	{
		NBTTag tag = new NBTTag();

		Set<String> keys = getKeys(field);
		for (String key : keys)
		{
			String type = getString(field + "." + key + ".type");
			if (type.equals("tag"))
				tag.setTag(key, getTagCompound(field + "." + key + ".value"));
			else if (type.equals("list"))
				tag.setList(key, getTagList(field + "." + key + ".value"));
			else if (type.equals("string"))
				tag.setString(key, getString(field + "." + key + ".value"));
			else if (type.equals("int"))
				tag.setInt(key, getInteger(field + "." + key + ".value"));
			else if (type.equals("long"))
				tag.setLong(key, getLong(field + "." + key + ".value"));
			else if (type.equals("short"))
				tag.setShort(key, getShort(field + "." + key + ".value"));
			else if (type.equals("byte"))
				tag.setByte(key, getByte(field + "." + key + ".value"));
			else if (type.equals("float"))
				tag.setFloat(key, getFloat(field + "." + key + ".value"));
			else if (type.equals("double"))
				tag.setDouble(key, getDouble(field + "." + key + ".value"));
		}
		return tag;
	}

	/** Writes a NBT List to the configuration */
	public void setTagList(String field, NBTList tag)
	{
		set(field, null);
		for (int i = 0; i < tag.size(); i++)
		{
			String format = tag.format(i);

			set(field + "." + i + ".format", format);
			if (format.equals("tag"))
				setTagCompound(field + "." + i + ".value", tag.getTag(i));
			else if (format.equals("list"))
				setTagList(field + "." + i + ".value", tag.getList(i));
			else if (format.equals("string"))
				set(field + "." + i + ".value", tag.getString(i));
			else if (format.equals("int"))
				set(field + "." + i + ".value", tag.getInt(i));
			else if (format.equals("long"))
				set(field + "." + i + ".value", tag.getLong(i));
			else if (format.equals("short"))
				set(field + "." + i + ".value", tag.getShort(i));
			else if (format.equals("byte"))
				set(field + "." + i + ".value", tag.getByte(i));
			else if (format.equals("float"))
				set(field + "." + i + ".value", tag.getFloat(i));
			else if (format.equals("double"))
				set(field + "." + i + ".value", tag.getDouble(i));
		}
	}

	/** Loads up a NBT List from the configuration */
	public NBTList getTagList(String field)
	{
		NBTList tag = new NBTList();

		for (String key : getKeys(field))
		{
			String format = getString(field + "." + key + ".format");
			if (format.equals("tag"))
				tag.addTag(getTagCompound(field + "." + key + ".value"));
			else if (format.equals("list"))
				tag.addList(getTagList(field + "." + key + ".value"));
			else if (format.equals("string"))
				tag.addString(getString(field + "." + key + ".value"));
			else if (format.equals("int"))
				tag.addInt(getInteger(field + "." + key + ".value"));
			else if (format.equals("long"))
				tag.addLong(getLong(field + "." + key + ".value"));
			else if (format.equals("short"))
				tag.addShort(getShort(field + "." + key + ".value"));
			else if (format.equals("byte"))
				tag.addByte(getByte(field + "." + key + ".value"));
			else if (format.equals("float"))
				tag.addFloat(getFloat(field + "." + key + ".value"));
			else if (format.equals("double"))
				tag.addDouble(getDouble(field + "." + key + ".value"));
		}
		return tag;
	}
}
