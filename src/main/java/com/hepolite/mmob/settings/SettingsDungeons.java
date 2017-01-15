package com.hepolite.mmob.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import com.hepolite.mmob.MMobPlugin;

public class SettingsDungeons {
    // Control variables
    private final static HashMap<String, Settings> dungeons = new HashMap<String, Settings>();

    /** Initialize all the roles settings; called from MMobSettings */
    public static void initialize() {
        // Every first run, create the dungeon folder
        final File directory = new File(MMobPlugin.getInstance().getDataFolder() + "/Dungeons");
        if (!directory.exists())
            directory.mkdir();

        // Load up all dungeons in the dungeon directory
        final File[] files = directory.listFiles();
        for (final File file : files)
            dungeons.put(file.getName().split("\\.")[0], new Settings("Dungeons", file.getName()));
    }

    /** Returns the configuration for the given dungeon */
    public static Settings getConfig(final String dungeon) {
        Settings setting = dungeons.get(dungeon);
        if (setting == null)
            setting = new Settings("Dungeons", dungeon + ".yml");
        return setting;
    }

    /** Deletes the configuration file for the given dungeon */
    public static void removeDungeon(final String name) {
        final File file = new File(MMobPlugin.getInstance().getDataFolder() + "/Dungeons", name + ".yml");
        if (file.exists())
            file.delete();
        dungeons.remove(name);
    }

    /** Return a set of all the dungeons that were found in the dungeons directory */
    public static Set<String> getDungeonNames() {
        return dungeons.keySet();
    }
}
