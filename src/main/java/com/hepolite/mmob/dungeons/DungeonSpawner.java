package com.hepolite.mmob.dungeons;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.handlers.RoleHandler;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.mobs.MobRole;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

public class DungeonSpawner
{
	// Control variables
	private final Dungeon dungeon;
	private String name;

	private final static Random random = new Random();

	// Timing and clearing
	private boolean isCleared = false;
	private int cooldownTime = -1;
	private int restoreTime = 0;

	// Location
	private Vector boundingBoxMin = null;
	private Vector boundingBoxMax = null;

	// Spawning
	private List<DungeonMob> mobsToSpawn = new LinkedList<DungeonMob>();
	private List<Entity> spawnedEntities = new LinkedList<Entity>();

	/* Initialization */
	public DungeonSpawner(Dungeon dungeon, String name)
	{
		this.dungeon = dungeon;
		this.name = name;
	}

	/** Called from the dungeon to spawn all the mobs from this spawner */
	public void spawnMobs()
	{
		for (DungeonMob mob : mobsToSpawn)
		{
			// Get the location of the spawn
			double x = boundingBoxMin.getX() + random.nextDouble() * (boundingBoxMax.getX() - boundingBoxMin.getX());
			double y = boundingBoxMin.getY() + random.nextDouble() * (boundingBoxMax.getY() - boundingBoxMin.getY());
			double z = boundingBoxMin.getZ() + random.nextDouble() * (boundingBoxMax.getZ() - boundingBoxMin.getZ());
			Location location = new Location(dungeon.getWorld(), x, y, z);

			// Spawn the mob, either as malevolent or normal
			if (mob.role != null)
				MMobListener.setSpawnCommandFlag(true);
			else
				MMobListener.setSpawnDenyFlag(true);
			Entity entity = Common.spawnEntity(location, mob.mobType);
			if (entity != null)
			{
				spawnedEntities.add(entity);
				if (mob.role != null && entity instanceof LivingEntity)
				{
					MalevolentMob mallmob = MobHandler.makeMobMalevolent((LivingEntity) entity);
					mallmob.setRole(mob.role);
				}
			}
			MMobListener.setSpawnCommandFlag(false);
			MMobListener.setSpawnDenyFlag(false);
		}
	}

	/** Called from the dungeon to despawn all the mobs from this spawner */
	public void despawnMobs()
	{
		for (Entity entity : spawnedEntities)
			entity.remove();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Returns the name of the spawner */
	public String getName()
	{
		return name;
	}

	/** Sets the bounding box of the location */
	public void setBoundingBox(Vector min, Vector max)
	{
		boundingBoxMin = min;
		boundingBoxMax = max;
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

	/** Sets the time it takes for the location to be ready to spawn new mobs after being cleared */
	public void setCooldown(int cooldownTime)
	{
		this.cooldownTime = cooldownTime;
	}

	/** Adds a mob to the spawner */
	public void addMob(String type, MobRole role)
	{
		if (type == null)
			return;
		mobsToSpawn.add(new DungeonMob(type, role));
	}

	/** Removes a mob from the spawner */
	public void removeMob(String type, MobRole role)
	{
		if (type == null)
			return;
		for (Iterator<DungeonMob> it = mobsToSpawn.iterator(); it.hasNext();)
		{
			DungeonMob mob = it.next();
			if (mob.mobType.equals(type) && role == mob.role)
			{
				it.remove();
				break;
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Loads the spawner from the config file, from the given property */
	public void loadSpawner(Settings config, String property)
	{
		// Read header data
		isCleared = config.getBoolean(property + ".Header.isCleared");
		restoreTime = config.getInteger(property + ".Header.restoreTime");
		cooldownTime = config.getInteger(property + ".Header.cooldownTime");

		// Read location data
		if (config.hasProperty(property + ".Location.minX") && config.hasProperty(property + ".Location.minY") && config.hasProperty(property + ".Location.minZ"))
			boundingBoxMin = new Vector(config.getInteger(property + ".Location.minX"), config.getInteger(property + ".Location.minY"), config.getInteger(property + ".Location.minZ"));
		if (config.hasProperty(property + ".Location.maxX") && config.hasProperty(property + ".Location.maxY") && config.hasProperty(property + ".Location.maxZ"))
			boundingBoxMax = new Vector(config.getInteger(property + ".Location.maxX"), config.getInteger(property + ".Location.maxY"), config.getInteger(property + ".Location.maxZ"));

		// Read mob data
		List<String> mobs = config.getStringList(property + ".Mobs");
		for (String mob : mobs)
		{
			String[] parts = mob.split(" ");
			if (parts.length == 1)
				addMob(parts[0], null);
			else if (parts.length == 2)
				addMob(parts[0], RoleHandler.getRole(parts[1]));
		}
	}

	/** Saves the spawner to the config file, under the given property */
	public void saveSpawner(Settings config, String property)
	{
		// Write down header data
		config.set(property + ".Header.isCleared", isCleared);
		config.set(property + ".Header.restoreTime", restoreTime);
		config.set(property + ".Header.cooldownTime", cooldownTime);

		// Write down location data
		if (boundingBoxMin != null)
		{
			config.set(property + ".Location.minX", boundingBoxMin.getBlockX());
			config.set(property + ".Location.minY", boundingBoxMin.getBlockY());
			config.set(property + ".Location.minZ", boundingBoxMin.getBlockZ());
		}
		else
		{
			config.set(property + ".Location.minX", null);
			config.set(property + ".Location.minY", null);
			config.set(property + ".Location.minZ", null);
		}
		if (boundingBoxMax != null)
		{
			config.set(property + ".Location.maxX", boundingBoxMax.getBlockX());
			config.set(property + ".Location.maxY", boundingBoxMax.getBlockY());
			config.set(property + ".Location.maxZ", boundingBoxMax.getBlockZ());
		}
		else
		{
			config.set(property + ".Location.maxX", null);
			config.set(property + ".Location.maxY", null);
			config.set(property + ".Location.maxZ", null);
		}

		// Write down the mobs that are to spawn
		List<String> mobs = new LinkedList<String>();
		for (DungeonMob mob : mobsToSpawn)
			mobs.add(mob.toString());
		config.set(property + ".Mobs", mobs);
	}
}
