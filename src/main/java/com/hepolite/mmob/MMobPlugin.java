package com.hepolite.mmob;

/*
 * Things that needs to be repaired:
 *  - Projectiles cause this while outside the map: http://pastebin.com/1pVt7MZr
 * 
 * Dungeon mechanics:
 *  - Spawning boss right away
 *  - Spawning mobs when the player is close enough
 *  - Spawning when zones are cleared
 *  - Other things
 *  
 *  Abandoning defined as all players leaving dungeon
 *  Dungeons goes on cooldown regardless if it was cleared or failed (Can notify about the results)
 *  Clearing conditions:
 *   - Killing the boss (All minions remain until all players leave dungeon)
 * 
 * Things that should be added eventually:
 *  - Add a fire effect, that ignites the targets it is used on
 *  - Make Malevolent Mobs avoid fire, drowning, suffocation and otherwise being trapped by players
 *  - Add Malevolent Wither
 *  - Spawn rates that differ from world to world, as well as chance multipliers'
 *  - Whenever a player grinds Mallmobs, a supermob is spawned. It could be called Mal, the Destroyer of Worlds
 *  - Transferring effects from item to item yields a worse effect and/or adds negative effects
 *  - Add slime sizes to the mob enum type thingy, like with wither skeletons
 *  - Tempest mobs [Skeleton]: Poison aura, invisibility, throwing daggers, grenades, lifesteal, magic mirror, "bleed" attack (Damage over time), "backstab" (Double damage every now and then), speed, resistance 2 at nighttime
 */

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.mmob.handlers.CommandHandler;
import com.hepolite.mmob.handlers.DungeonHandler;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.handlers.ProjectileHandler;
import com.hepolite.mmob.utility.BlockManager;
import com.hepolite.mmob.utility.NBTAPI;

public class MMobPlugin extends JavaPlugin
{
	// Control variables
	private static MMobPlugin instance = null;
	private static MMobSettings settings = null;
	private static MMobListener listener = null;

	private static CommandHandler commandHandler = null;

	int onTickTask = -1;

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onEnable()
	{
		instance = this;
		
		Log.initialize(this);
		NBTAPI.initialize();
		MMobCompatibility.initialize();

		settings = new MMobSettings();
		listener = new MMobListener();
		commandHandler = new CommandHandler();

		// Register listener
		getServer().getPluginManager().registerEvents(listener, this);

		// Set up a task that runs once every tick
		Runnable task = new Runnable()
		{
			@Override
			public void run()
			{
				listener.onTick();
				MobHandler.onTick();
				DungeonHandler.onTick();
				BlockManager.onTick();
			}
		};
		onTickTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, task, 0, 1);
	}

	@Override
	public void onDisable()
	{
		settings.save();

		getServer().getScheduler().cancelTasks(this);

		settings = null;
		listener = null;
		commandHandler = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		return commandHandler.onCommand(sender, cmd, label, args);
	}

	/** Restarts the plugin, reloading all important systems */
	public final void onRestart()
	{
		settings.save();
		settings.reload();
		MobHandler.onRestart();
		ProjectileHandler.onRestart();
	}

	// ///////////////////////////////////////////////////////////////////////
	// GETTING/SETTING DATA // GETTING/SETTING DATA // GETTING/SETTING DATA //
	// ///////////////////////////////////////////////////////////////////////

	/** Returns the instance of the plugin */
	public static MMobPlugin getInstance()
	{
		return instance;
	}

	/** Returns the configuration file of the plugin */
	public static MMobSettings getSettings()
	{
		return settings;
	}
}
