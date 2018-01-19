package com.hepolite.mmob.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsDungeons;

public class DungeonHandler
{
	// Control variables
	private static HashMap<String, Dungeon> dungeonMap = new HashMap<String, Dungeon>();

	/** Creates a new dungeon and fills it with blank information */
	public static void createDungeon(String name)
	{
		if (getDungeon(name) != null)
			Log.log("Attempted to create a new dungeon with name '" + name + "', but this dungeon already exists. The old dungeon is overwritten");
		dungeonMap.put(name, new Dungeon(name));
	}

	/** Removes the given dungeon from the system */
	public static void removeDungeon(String name)
	{
		if (!dungeonMap.containsKey(name))
			Log.log("Attempted to remove a dungeon with name '" + name + "', but this dungeon doesn't exist");
		else
		{
			SettingsDungeons.removeDungeon(name);
			dungeonMap.remove(name);
		}
	}

	/** Returns the dungeon with the given name if it exists; returns null otherwise */
	public static Dungeon getDungeon(String name)
	{
		return dungeonMap.get(name);
	}

	/** Returns all dungeons within the system */
	public static Collection<Dungeon> getDungeons()
	{
		Collection<Dungeon> dungeons = new ArrayList<Dungeon>();
		for (Entry<String, Dungeon> entry : dungeonMap.entrySet())
			dungeons.add(entry.getValue());
		return dungeons;
	}

	/** Renames the given dungeon to the new name */
	public static void renameDungeon(String oldName, String newName)
	{
		Dungeon dungeon = getDungeon(oldName);
		if (dungeon != null)
		{
			if (getDungeon(newName) != null)
				Log.log("Attempted to rename dungeon '" + oldName + "' to '" + newName + "' which already exists. The other dungeon is overwritten");

			removeDungeon(oldName);
			dungeonMap.put(newName, dungeon);
			dungeon.setName(newName);
		}
		else
			Log.log("Attempted to rename the dungeon '" + oldName + "' which doesn't exist");
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Handles the core processing of the dungeons */
	public static void onTick()
	{
		for (Dungeon dungeon : dungeonMap.values())
			dungeon.onTick();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Loads the entire dungeon system from disk */
	public static void loadFromConfig()
	{
		dungeonMap.clear();
		for (String name : SettingsDungeons.getDungeonNames())
		{
			createDungeon(name);
			Dungeon dungeon = getDungeon(name);
			dungeon.loadDungeon(SettingsDungeons.getConfig(name));
		}
	}

	/** Saves the entire dungeon system to disk */
	public static void saveToConfig()
	{
		for (Dungeon dungeon : dungeonMap.values())
		{
			Settings settings = SettingsDungeons.getConfig(dungeon.getName());
			dungeon.saveDungeon(settings);
			settings.save();
		}
	}
}
