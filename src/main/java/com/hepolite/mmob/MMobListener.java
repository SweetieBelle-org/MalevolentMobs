package com.hepolite.mmob;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import com.hepolite.mmob.handlers.ItemEffectHandler;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.handlers.ProjectileHandler;
import com.hepolite.mmob.utility.Common;

public class MMobListener implements Listener
{
	// Control variables
	private static boolean denySpawns = false;
	private static boolean commandSpawn = false;

	private static Random random = new Random();

	private static HashMap<String, Vector> playerVelocities = new HashMap<String, Vector>();

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Core logic, update each tick */
	public void onTick()
	{
		ItemEffectHandler.onTick();
		ProjectileHandler.onTick();
	}

	/** Handle the spawning of malevolent mobs */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMobSpawn(CreatureSpawnEvent event)
	{
		// If not allowed to spawn malevolent mobs, or if they are denied, don't spawn them
		if (denySpawns)
			return;
		if (!commandSpawn && !MMobPlugin.getSettings().getBoolean("Spawns.allow"))
			return;

		// Figure out the type of the entity, and block the spawn if not allowed to spawn it
		String type = Common.getEntityType(event.getEntity());
		if (!commandSpawn && !MMobPlugin.getSettings().getBoolean("Spawns." + type + ".allow"))
			return;

		// If (un)lucky enough, make the mob malevolent
		float chance = MMobPlugin.getSettings().getFloat("Spawns.spawnChance");
		if (MMobPlugin.getSettings().hasProperty("Spawns." + type + ".chanceMultiplier"))
			chance *= MMobPlugin.getSettings().getFloat("Spawns." + type + ".chanceMultiplier");
		if (commandSpawn || random.nextFloat() < chance)
		{
			// Check that the world is one of the allowed worlds
			List<String> blockedWorlds = MMobPlugin.getSettings().getStringList("Spawns.blockedWorlds");
			String currentWorld = event.getEntity().getWorld().getName();
			if (!commandSpawn && blockedWorlds.contains(currentWorld))
				return;

			// Prevent spawns that are to occur too close to players; this won't prevent commands from spawning them in, though!
			if (!commandSpawn && !MMobSettings.isDebugmode)
			{
				List<Player> players = Common.getPlayersInRange(event.getLocation(), MMobPlugin.getSettings().getFloat("General.Mobs.minSpawnDistance"));
				if (players.size() > 0)
					return;
			}

			// Finalize the mob
			MobHandler.makeMobMalevolent(event.getEntity());
		}
	}

	/** Handle the events where the malevolent mobs die */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMobDie(EntityDeathEvent event)
	{
		// Ignore mobs that definitely aren't malevolent
		if (!(event.getEntity() instanceof LivingEntity))
			return;

		LivingEntity entity = (LivingEntity) event.getEntity();
		if (MobHandler.isMobMalevolent(entity))
			MobHandler.getMalevolentMob(entity).onDie(event);
	}

	/** Handle the events where the malevolent mobs are dealing damage and where the players deal damage. This must occur before damage is applied, hence high priority instead of highest */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onMobDamageGiven(EntityDamageByEntityEvent event)
	{
		Entity attacker = Common.getAttacker(event);

		// Allow players to deal special bonuses with certain items
		if (attacker != null && attacker instanceof Player)
			ItemEffectHandler.onPlayerDealDamage(event, (Player) attacker);

		// Then care only about attacks that originate from a malevolent mob
		if (attacker instanceof LivingEntity && MobHandler.isMobMalevolent((LivingEntity) attacker))
			MobHandler.getMalevolentMob((LivingEntity) attacker).onDamageGiven(event);
	}

	/** Handle the events where the malevolent mobs are damaged and where the players receive damage */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onMobDamageTaken(EntityDamageEvent event)
	{
		// Allow players to get special bonuses with certain items
		if (event.getEntity() instanceof Player)
			ItemEffectHandler.onPlayerTakeDamage(event, (Player) event.getEntity());

		// Ignore mobs that definitely aren't malevolent for the rest
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (MobHandler.isMobMalevolent(entity))
			MobHandler.getMalevolentMob(entity).onDamageTaken(event);
	}

	/** Handle the events where the malevolent mobs are healed */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onMobHealed(EntityRegainHealthEvent event)
	{
		// Ignore mobs that definitely aren't malevolent for the rest
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (MobHandler.isMobMalevolent(entity))
			MobHandler.getMalevolentMob(entity).onHealed(event);
	}

	/** Handle the player use of special items */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		// Figure out what the player is doing, and then let the item handler deal with the rest
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			ItemEffectHandler.onPlayerRightClick(event, event.getPlayer());
		else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
			ItemEffectHandler.onPlayerLeftClick(event, event.getPlayer());
	}

	/** Used to compute the velocity of the players */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		// Find the velocity of the player and store it
		Player player = event.getPlayer();
		Vector deltaPosition = event.getTo().clone().subtract(event.getFrom()).toVector();
		playerVelocities.put(player.getName(), deltaPosition);
	}

	/** Used to track the bow itemstack that was used to fire an arrow */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerFireArrow(EntityShootBowEvent event)
	{
		if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow)
		{
			// Store the bow into the projectile and tick fire effects
			ItemEffectHandler.onPlayerFireArrow(event, (Player) event.getEntity());
			MetadataValue meta = new FixedMetadataValue(MMobPlugin.getInstance(), event.getBow());
			event.getProjectile().setMetadata("[MMob]originBow", meta);
		}
	}

	/** Used to perform various effects when the player breaks blocks */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerBreakBlock(BlockBreakEvent event)
	{
		// Ignore events where a player did nothing
		if (event.getPlayer() == null)
			return;
		ItemEffectHandler.onBlockBreak(event, event.getPlayer());
	}

	/** Used to cancel the explosion from wither skulls and creepers */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(ExplosionPrimeEvent event)
	{
		// Figure out the original entity
		Entity entity = event.getEntity();
		if (event.getEntity() instanceof Projectile)
			entity = (Entity) ((Projectile) event.getEntity()).getShooter();

		// Figure out if the entity is due to a Malevolent entity
		if (!(entity instanceof LivingEntity))
			return;
		if (MobHandler.isMobMalevolent((LivingEntity) entity))
		{
			event.setCancelled(true);
			MobHandler.getMalevolentMob((LivingEntity) entity).onExplode(event);
		}
	}

	// ///////////////////////////////////////////////////////////////////////
	// SETTING/GETTING DATA // SETTING/GETTING DATA // SETTING/GETTING DATA //
	// ///////////////////////////////////////////////////////////////////////

	/** Denies or allows the spawning of malevolent mobs, used by certain abilities to never spawn malevolent mobs */
	public static void setSpawnDenyFlag(boolean denySpawns)
	{
		MMobListener.denySpawns = denySpawns;
	}

	/** Sets if the current mob spawn is due to a command or not */
	public static void setSpawnCommandFlag(boolean commandSpawn)
	{
		MMobListener.commandSpawn = commandSpawn;
	}

	/** Returns the velocity of a player */
	public static Vector getPlayerVelocity(Player player)
	{
		// Need a valid player
		if (player == null)
			return new Vector(0.0, 0.0, 0.0);

		// If the player is stored, get the vector from there
		if (playerVelocities.containsKey(player.getName()))
			return playerVelocities.get(player.getName());
		return new Vector(0.0, 0.0, 0.0);
	}

}
