package com.hepolite.mmob.dungeons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hepolite.mmob.settings.Settings;

public class Dungeon
{
	// Control variables
	private String name;
	private World world = null;
	private int age = 0;			// Age of the dungeon, in ticks
	private int restoreTime = 0;	// Which tick the dungeon opens again
	private int activeTime = 0;		// How many ticks the dungeon has been active

	private Vector boundingBoxMin = null;
	private Vector boundingBoxMax = null;

	// Player interaction
	private HashSet<UUID> playerSet = new HashSet<UUID>();
	private String playerEnterMessage = null;
	private String playerEnterClearedMessage = null;
	private String playerLeaveMessage = null;
	private String playerLeaveClearedMessage = null;

	// Conditions for running the dungeon
	private boolean hasSpawned = false;
	private boolean isCleared = false;
	private int cooldownTime = 72000;

	// Dungeon spawning
	private final HashMap<String, DungeonSpawner> spawnPoints = new HashMap<String, DungeonSpawner>();

	/* Initialization */
	public Dungeon(String name)
	{
		this.name = name;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Sets the name of the dungeon */
	public void setName(String name)
	{
		this.name = name;
	}

	/** Returns the name of the dungeon */
	public String getName()
	{
		return name;
	}

	/** Sets the world of the dungeon */
	public void setWorld(World world)
	{
		this.world = world;
	}

	/** Returns the world the dungeon is in */
	public World getWorld()
	{
		return world;
	}

	/** Sets the bounding box of the dungeon */
	public void setBoundingBox(Vector min, Vector max)
	{
		boundingBoxMin = min;
		boundingBoxMax = max;
	}

	/** Sets the message players receives when entering the dungeon */
	public void setEnterMessage(String message)
	{
		playerEnterMessage = message;
	}

	/** Sets the message players receives when leaving the dungeon */
	public void setLeaveMessage(String message)
	{
		playerLeaveMessage = message;
	}

	/** Sets the message players receives when entering the dungeon when it is cleared */
	public void setEnterClearedMessage(String message)
	{
		playerEnterClearedMessage = message;
	}

	/** Sets the message players receives when leaving the dungeon when it is cleared */
	public void setLeaveClearedMessage(String message)
	{
		playerLeaveClearedMessage = message;
	}

	/** Sets the time it takes for the dungeon to be ready to spawn new mobs after being cleared */
	public void setCooldown(int cooldownTime)
	{
		this.cooldownTime = cooldownTime;
	}

	/** Returns the bounding box of the dungeon, the smallest corner */
	public Vector getBoundingBoxMin()
	{
		return boundingBoxMin;
	}

	/** Returns the bounding box of the dungeon, the biggest corner */
	public Vector getBoundingBoxMax()
	{
		return boundingBoxMax;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Creates a new spawner within the dungeon */
	public void createSpawner(String name)
	{
		spawnPoints.put(name, new DungeonSpawner(this, name));
	}

	/** Deletes a spawner within the dungeon, that has the given name */
	public void deleteSpawner(String name)
	{
		spawnPoints.remove(name);
	}

	/** Returns a spawner associated with the dungeon */
	public DungeonSpawner getSpawner(String name)
	{
		return spawnPoints.get(name);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Called on the dungeon every tick */
	public void onTick()
	{
		if (!isValid())
			return;
		age++;
		if (age % 50 == 0)
			processPlayers();

		// Check that the dungeon is open again
		if (isCleared && age >= restoreTime)
		{
			isCleared = false;
			hasSpawned = false;
			activeTime = 0;
		}

		// Process the dungeon if there is a player inside it
		if (!hasSpawned && playerSet.size() != 0)
		{
			hasSpawned = true;
			for (DungeonSpawner location : spawnPoints.values())
				location.spawnMobs();
		}

		// Run dungeon
		if (hasSpawned)
			activeTime++;

		// Put the dungeon on cooldown if stuff has been spawned and there's no one in the dungeon anymore
		if (!isCleared && hasSpawned && playerSet.size() == 0 && activeTime > 20 * 30)
		{
			isCleared = true;
			restoreTime = age + (cooldownTime == -1 ? 1000000000 : cooldownTime);
			for (DungeonSpawner location : spawnPoints.values())
				location.despawnMobs();
		}
	}

	/** Check if the dungeon is valid */
	private boolean isValid()
	{
		return world != null && boundingBoxMin != null && boundingBoxMax != null;
	}

	/** Resets the entire dungeon, bringing it off cooldown and despawning all spawned mobs */
	public void reset()
	{
		for (DungeonSpawner location : spawnPoints.values())
			location.despawnMobs();
		restoreTime = age;
		hasSpawned = false;
		activeTime = 0;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Process the player map, find players that enters/leaves the dungeon */
	private void processPlayers()
	{
		// Find all players who left the dungeon
		for (Iterator<UUID> it = playerSet.iterator(); it.hasNext();)
		{
			Player player = Bukkit.getPlayer(it.next());
			if (player == null || !isPlayerInsideDungeon(player))
			{
				it.remove();

				// Send the player a message when leaving the dungeon, if relevant
				if (player != null)
				{
					if (!isCleared && playerLeaveMessage != null)
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', playerLeaveMessage));
					else if (isCleared && playerLeaveClearedMessage != null)
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', playerLeaveClearedMessage));
				}
			}
		}

		// Find all players entering the dungeon
		List<Player> worldPlayers = world.getPlayers();

		for (Player player : worldPlayers)
		{
			if (!player.isValid() || player.isDead())
				continue;
			if (isPlayerInsideDungeon(player))
			{
				if (!playerSet.contains(player.getUniqueId()))
				{
					playerSet.add(player.getUniqueId());

					// Send the player a message when entering the dungeon, if relevant
					if (!isCleared && playerEnterMessage != null)
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', playerEnterMessage));
					else if (isCleared && playerEnterClearedMessage != null)
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', playerEnterClearedMessage));
				}
			}
		}
	}

	/** Checks if the given player is inside the dungeon */
	private boolean isPlayerInsideDungeon(Player player)
	{
		if (player == null)
			return false;

		Location playerPosition = player.getLocation();
		if (playerPosition.getX() >= boundingBoxMin.getX() && playerPosition.getX() <= boundingBoxMax.getX())
			if (playerPosition.getY() >= boundingBoxMin.getY() && playerPosition.getY() <= boundingBoxMax.getY())
				if (playerPosition.getZ() >= boundingBoxMin.getZ() && playerPosition.getZ() <= boundingBoxMax.getZ())
					return true;
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Loads the dungeon from the given settings file */
	public void loadDungeon(Settings config)
	{
		// Read header data
		age = config.getInteger("Header.age");
		isCleared = config.getBoolean("Header.isCleared");
		restoreTime = config.getInteger("Header.restoreTime");
		cooldownTime = config.getInteger("Header.cooldownTime");

		// Read location data
		if (config.hasProperty("Location.world"))
			world = Bukkit.getWorld(config.getString("Location.world"));
		if (config.hasProperty("Location.minX") && config.hasProperty("Location.minY") && config.hasProperty("Location.minZ"))
			boundingBoxMin = new Vector(config.getInteger("Location.minX"), config.getInteger("Location.minY"), config.getInteger("Location.minZ"));
		if (config.hasProperty("Location.maxX") && config.hasProperty("Location.maxY") && config.hasProperty("Location.maxZ"))
			boundingBoxMax = new Vector(config.getInteger("Location.maxX"), config.getInteger("Location.maxY"), config.getInteger("Location.maxZ"));

		// Read player interaction data
		if (config.hasProperty("Player.Message.enter"))
			playerEnterMessage = config.getString("Player.Message.enter");
		if (config.hasProperty("Player.Message.enterCleared"))
			playerEnterClearedMessage = config.getString("Player.Message.enterCleared");
		if (config.hasProperty("Player.Message.leave"))
			playerLeaveMessage = config.getString("Player.Message.leave");
		if (config.hasProperty("Player.Message.leaveCleared"))
			playerLeaveClearedMessage = config.getString("Player.Message.leaveCleared");

		// Load up spawners
		Set<String> spawners = config.getKeys("Spawners");
		for (String spawner : spawners)
		{
			createSpawner(spawner);
			getSpawner(spawner).loadSpawner(config, "Spawners." + spawner);
		}
	}

	/** Saves the dungeon to the given settings file */
	public void saveDungeon(Settings config)
	{
		// Write down header data
		config.set("Header.age", age);
		config.set("Header.isCleared", isCleared);
		config.set("Header.restoreTime", restoreTime);
		config.set("Header.cooldownTime", cooldownTime);

		// Write down location data
		if (world != null)
			config.set("Location.world", world.getName());
		else
			config.set("Location.world", null);
		if (boundingBoxMin != null)
		{
			config.set("Location.minX", boundingBoxMin.getBlockX());
			config.set("Location.minY", boundingBoxMin.getBlockY());
			config.set("Location.minZ", boundingBoxMin.getBlockZ());
		}
		else
		{
			config.set("Location.minX", null);
			config.set("Location.minY", null);
			config.set("Location.minZ", null);
		}
		if (boundingBoxMax != null)
		{
			config.set("Location.maxX", boundingBoxMax.getBlockX());
			config.set("Location.maxY", boundingBoxMax.getBlockY());
			config.set("Location.maxZ", boundingBoxMax.getBlockZ());
		}
		else
		{
			config.set("Location.maxX", null);
			config.set("Location.maxY", null);
			config.set("Location.maxZ", null);
		}

		// Write down player interaction
		config.set("Player.Message.enter", playerEnterMessage);
		config.set("Player.Message.enterCleared", playerEnterClearedMessage);
		config.set("Player.Message.leave", playerLeaveMessage);
		config.set("Player.Message.leaveCleared", playerLeaveClearedMessage);

		// Write down spawners
		config.set("Spawners", null);
		for (DungeonSpawner spawner : spawnPoints.values())
			spawner.saveSpawner(config, "Spawners." + spawner.getName());
	}

}
