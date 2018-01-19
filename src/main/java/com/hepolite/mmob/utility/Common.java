package com.hepolite.mmob.utility;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;
import com.hepolite.mmob.MMobSettings;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.utility.MathHelper.Vector3D;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;

public class Common
{
	// ///////////////////////////////////////////////////////////////////////////////////////
	// GENERIC // GENERIC // GENERIC // GENERIC // GENERIC // GENERIC // GENERIC // GENERIC //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Posts an event to the event bus */
	public static void postEvent(Event event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	// ///////////////////////////////////////////////////////////////////////////////////
	// ENTITIES // ENTITIES // ENTITIES // ENTITIES // ENTITIES // ENTITIES // ENTITIES //
	// ///////////////////////////////////////////////////////////////////////////////////

	/** Returns a list of entities within range */
	public static List<LivingEntity> getEntitiesInRange(Location location, float range)
	{
		// Find all nearby living entities and return them
		List<Entity> entitiesInWorld = location.getWorld().getEntities();
		List<LivingEntity> nearbyEntities = new LinkedList<LivingEntity>();
		for (Entity entity : entitiesInWorld)
		{
			if (entity instanceof LivingEntity)
			{
				if (location.distanceSquared(entity.getLocation()) < range * range)
					nearbyEntities.add((LivingEntity) entity);
			}
		}
		return nearbyEntities;
	}

	/** Returns a list of entities at the given location. Assumes a boundingbox 1m*1m*2m for all entities */
	public static List<LivingEntity> getEntitiesInLocation(Location location)
	{
		// Find all nearby players and return them
		List<Entity> entitiesInWorld = location.getWorld().getEntities();
		List<LivingEntity> entities = new LinkedList<LivingEntity>();
		for (Entity entity : entitiesInWorld)
		{
			if (entity instanceof LivingEntity)
			{
				Location position = entity.getLocation();
				if (location.getX() >= position.getX() - 0.5 && location.getX() <= position.getX() + 0.5)
					if (location.getZ() >= position.getZ() - 0.5 && location.getZ() <= position.getZ() + 0.5)
						if (location.getY() >= position.getY() - 0.1 && location.getY() <= position.getY() + 1.9)
							entities.add((LivingEntity) entity);
			}
		}
		return entities;
	}

	/** Returns a list of monsters within range */
	public static List<Monster> getMonstersInRange(Location location, float range)
	{
		// Find all nearby monsters and return them
		List<Entity> entitiesInWorld = location.getWorld().getEntities();
		List<Monster> nearbyEntities = new LinkedList<Monster>();
		for (Entity entity : entitiesInWorld)
		{
			if (entity instanceof Monster)
			{
				if (location.distanceSquared(entity.getLocation()) < range * range)
					nearbyEntities.add((Monster) entity);
			}
		}
		return nearbyEntities;
	}

	/** Returns a list of players within range */
	public static List<Player> getPlayersInRange(Location location, float range)
	{
		List<Player> playersInWorld = location.getWorld().getPlayers();
		List<Player> nearbyPlayers = new LinkedList<Player>();
		for (Player player : playersInWorld)
		{
			if (location.distanceSquared(player.getLocation()) < range * range)
				nearbyPlayers.add(player);
		}
		return nearbyPlayers;
	}

	/** Returns a list of nearby malevolent mobs */
	public static List<LivingEntity> getMalevolentMobsInRange(Location location, float range)
	{
		List<Entity> entitiesInWorld = location.getWorld().getEntities();
		List<LivingEntity> nearbyMalevolentMobs = new LinkedList<LivingEntity>();
		for (Entity entity : entitiesInWorld)
		{
			if (entity instanceof LivingEntity)
			{
				if (MobHandler.isMobMalevolent((LivingEntity) entity) && location.distanceSquared(entity.getLocation()) < range * range)
					nearbyMalevolentMobs.add((LivingEntity) entity);
			}
		}
		return nearbyMalevolentMobs;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// EFFECTS // EFFECTS // EFFECTS // EFFECTS // EFFECTS // EFFECTS // EFFECTS // EFFECTS //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Casts a lightning strike at the given location. It does no damage, it's only the effect */
	public static void effectLightningStrike(Location location)
	{
		location.getWorld().strikeLightningEffect(location);

		// Play the lightning strike sound
		/* location.getWorld().playSound(location, Sound.EXPLODE, 1.0f, 0.0f);
		 * 
		 * // Visually display the lightning strike Object packet = StatusBarAPI.getLightningSpawnPacket(location); List<Player> players = getPlayersInRange(location, MMobPlugin.getSettings().getInteger("General.Effects.Lightningstrike.visibilityRange")); for (Player player : players) { StatusBarAPI.sendPacket(player, packet); player.playSound(player.getEyeLocation(), Sound.AMBIENCE_THUNDER, MMobPlugin.getSettings().getFloat("General.Effects.Lightningstrike.thunderSoundVolume"), 0.0f); } */
	}

	/** Creates an explosion effect at the given location. It does no damage, it's only the effect */
	public static void effectExplosion(Location location, float radius)
	{
		ParticleEffect.play(ParticleType.EXPLOSION_HUGE, location, 0.05f, (int) (0.8f * radius), radius);
		location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.0f);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	// COMBAT // COMBAT // COMBAT // COMBAT // COMBAT // COMBAT // COMBAT // COMBAT // COMBAT //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Heals some damage */
	public static void doHeal(double heal, LivingEntity target, RegainReason reason)
	{
		EntityRegainHealthEvent event = new EntityRegainHealthEvent(target, heal, reason);

		postEvent(event);
		if (!event.isCancelled() && event.getAmount() > 0.0)
			target.setHealth(Math.min(target.getMaxHealth(), target.getHealth() + event.getAmount()));
	}

	/** Applies some damage. Returns true if the damage was NOT cancelled */
	@SuppressWarnings("deprecation")
	public static boolean doDamage(double damage, LivingEntity target, LivingEntity attacker, DamageCause cause)
	{
		if (target == null || target.getHealth() <= 0.0)
			return false;

		// Scale damage, if relevant
		if (MobHandler.isMobMalevolent(attacker))
			damage *= MMobPlugin.getSettings().getFloat("General.Mobs.damageScale");

		// Ignore players in creative mode
		if (target instanceof Player)
		{
			if (((Player) target).getGameMode() == GameMode.CREATIVE)
				return false;
		}

		// Apply damage reductions
		float armorValue = 0.0f;

		EntityEquipment equipment = target.getEquipment();
		if (equipment != null)
		{
			ItemStack[] armor = equipment.getArmorContents();
			for (ItemStack armorPiece : armor)
			{
				if (armorPiece == null || armorPiece.getType() == Material.AIR)
					continue;
				switch (cause)
				{
				case FIRE:
				case FIRE_TICK:
				case LAVA:
					armorValue += armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
					break;
				case ENTITY_EXPLOSION:
				case BLOCK_EXPLOSION:
					armorValue += armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
					break;
				case PROJECTILE:
					armorValue += armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
					break;
				case ENTITY_ATTACK:
				case THORNS:
					armorValue += armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
					break;
				default:
					;
				}
			}
		}

		// Apply potion effects into the damage reduction calculation
		switch (cause)
		{
		case FIRE:
		case FIRE_TICK:
		case LAVA:
			if (target.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
				armorValue += MMobPlugin.getSettings().getFloat("General.Defence.firePotionEnchantLevelEquivalent");
			break;
		case ENTITY_ATTACK:
		case THORNS:
			if (target.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
				armorValue += getPotionEffectLevel(target, PotionEffectType.DAMAGE_RESISTANCE) * MMobPlugin.getSettings().getFloat("General.Defence.resistancePotionEnchantLevelEquivalent");
			break;
		default:
			;
		}

		// Reduce the damage
		armorValue = Math.max(-50.0f, armorValue * MMobPlugin.getSettings().getFloat("General.Defence.armorEfficiency"));
		damage *= 1.0f - armorValue / (armorValue + 100.0f);

		if (MMobSettings.isDebugmode && target instanceof Player)
			Log.log("Registered attack on player '" + ((Player) target).getName() + "', dealing '" + damage / (1.0f - armorValue / (armorValue + 100.0f)) + "'->'" + damage + "' damage due to '" + armorValue + "' armor (" + armorValue / (100.0f + armorValue) + "%).");

		// Get a event and pass it to the event handler
		EntityDamageEvent event = null;
		if (attacker == null)
			event = new EntityDamageEvent(target, cause, damage);
		else
			event = new EntityDamageByEntityEvent(attacker, target, cause, damage);

		postEvent(event);
		if (!event.isCancelled())
		{
			target.setHealth(Math.max(0.0, target.getHealth() - event.getDamage()));
			target.setLastDamageCause(event);
			if (target.getHealth() == 0.0 && !(target instanceof Player))
				postEvent(new EntityDeathEvent(target, null));

			if (target instanceof Player)
				target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
		}
		return !event.isCancelled();
	}

	/** Returns the attacker from the event */
	public static Entity getAttacker(EntityDamageByEntityEvent event)
	{
		Entity attacker = null;
		if (event.getDamager() instanceof Projectile)
		{
			if (((Projectile) event.getDamager()).getShooter() instanceof Entity)
				attacker = (Entity) ((Projectile) event.getDamager()).getShooter();
		}
		else
			attacker = event.getDamager();
		return attacker;
	}

	/** Checks if the given damage even was a magic attack */
	public static boolean isAttackMagic(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.MAGIC)
			return true;
		if (MMobPlugin.getSettings().getBoolean("General.Attacks.treatSkillsAsMagic") && event.getCause() == DamageCause.ENTITY_ATTACK && event instanceof EntityDamageByEntityEvent)
		{
			Entity attacker = getAttacker((EntityDamageByEntityEvent) event);
			if (attacker == null)
				return false;
			float range = MMobPlugin.getSettings().getFloat("General.Attacks.treatAttackAsMagicDistance");
			return attacker.getLocation().distanceSquared(event.getEntity().getLocation()) >= range * range;
		}
		return false;
	}

	/** Checks if the given damage even was a ranged attack. Will not care about the type of attack, only that the attack originated some distance away */
	public static boolean isAttackRanged(EntityDamageEvent event)
	{
		// Any damage event caused by natural causes are not ranged
		if (!(event instanceof EntityDamageByEntityEvent))
			return false;
		DamageCause cause = event.getCause();
		if (cause == DamageCause.ENTITY_EXPLOSION || cause == DamageCause.BLOCK_EXPLOSION || cause == DamageCause.LIGHTNING)
			return false;
		Entity attacker = getAttacker((EntityDamageByEntityEvent) event);

		// If no attacker was discovered from the above, the attacker must be a dispenser or something similar
		if (attacker == null)
			return true;

		float range = MMobPlugin.getSettings().getFloat("General.Attacks.treatAttackAsRangedDistance");
		return attacker.getLocation().distanceSquared(event.getEntity().getLocation()) >= range * range;
	}

	/** Checks if the given damage even was a melee attack */
	public static boolean isAttackMelee(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.ENTITY_ATTACK && !isAttackMagic(event))
			return true;
		return false;
	}

	/** Checks if the given damage even was a arrow attack */
	public static boolean isAttackArrow(EntityDamageEvent event)
	{
		if (event instanceof EntityDamageByEntityEvent)
		{
			if (((EntityDamageByEntityEvent) event).getDamager() instanceof Arrow)
				return true;
		}
		return false;
	}

	/** Checks if the damage was caused by natural sources */
	public static boolean isAttackNatural(EntityDamageEvent event)
	{
		switch (event.getCause())
		{
		case FALL:
		case CONTACT:
		case FIRE:
		case FIRE_TICK:
		case LAVA:
		case DROWNING:
		case LIGHTNING:
		case SUFFOCATION:
		case VOID:
		case MELTING:
		case STARVATION:
			return true;
		default:
			return false;
		}
	}

	/** Creates an explosion at the given location, including an explosion effect. The attacker may be null */
	public static void createExplosionWithEffect(Location location, float strength, float radius, boolean affectPlayersOnly, LivingEntity attacker)
	{
		effectExplosion(location, radius);
		createExplosion(location, strength, radius, affectPlayersOnly, attacker);
	}

	/** Creates an explosion at the given location. The attacker may be null */
	public static void createExplosion(Location location, float strength, float radius, boolean affectPlayersOnly, LivingEntity attacker)
	{
		// Find all nearby entities and damage them if applicable
		List<LivingEntity> entities = getEntitiesInRange(location, radius);
		for (LivingEntity entity : entities)
		{
			if (!affectPlayersOnly || entity instanceof Player)
			{
				// Damage the entity based on distance from the explosion
				double distanceFactor = entity.getLocation().distance(location) / radius;
				double damage = strength * Math.cos(distanceFactor * Math.PI / 2.0);
				doDamage(damage, entity, attacker, DamageCause.ENTITY_EXPLOSION);
			}
		}
	}

	/** Creates a lightning strike at the given location */
	public static void createLightningStrike(Location location, float strength, float radius, boolean affectPlayersOnly)
	{
		createLightningStrike(location, null, strength, radius, affectPlayersOnly);
	}

	/** Creates a lightning strike at the given location */
	public static void createLightningStrike(Location location, LivingEntity attacker, float strength, float radius, boolean affectPlayersOnly)
	{
		// Locate the highest point and strike the lightning there
		double posX = location.getX();
		double posZ = location.getZ();
		double posY = location.getWorld().getHighestBlockYAt((int) (posX + 0.5), (int) (posZ + 0.5));

		List<LivingEntity> entitiesInWorld = location.getWorld().getLivingEntities();
		for (LivingEntity entity : entitiesInWorld)
		{
			double deltaX = entity.getLocation().getX() - location.getX();
			double deltaZ = entity.getLocation().getZ() - location.getZ();
			if (deltaX * deltaX + deltaZ * deltaZ < radius * radius)
			{
				// Only interested in the entity with the greatest altitude that's within range
				if (entity.getLocation().getY() > posY)
				{
					posX = entity.getLocation().getX();
					posY = entity.getLocation().getY();
					posZ = entity.getLocation().getZ();
				}
			}
		}
		location.setX(posX);
		location.setY(posY);
		location.setZ(posZ);

		effectLightningStrike(location);

		// Find all nearby entities and damage them if applicable
		List<LivingEntity> entities = getEntitiesInRange(location, radius);
		for (LivingEntity entity : entities)
		{
			if (!affectPlayersOnly || entity instanceof Player)
			{
				if (doDamage(strength, entity, attacker, DamageCause.LIGHTNING))
					entity.setFireTicks((int) (15.0f * strength));
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// MISC // MISC // MISC // MISC // MISC // MISC // MISC // MISC // MISC // MISC // MISC // MISC //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Returns the level of the player, or -1 if the player has no class/no levels */
	public static int getPlayerLevel(Player player)
	{
		// Grab the SkillAPI plugin and work with it
		/* SkillAPI skillApi = (SkillAPI) Bukkit.getPluginManager().getPlugin("SkillAPI"); if (skillApi == null) return 0;
		 * 
		 * // Get a player and verify that it is valid PlayerSkills playerSills = skillApi.getPlayer(player); if (playerSills == null || !playerSills.hasClass()) return 0; return playerSills.getLevel(); */

		PlayerData data = SkillAPI.getPlayerData(player);
		if (data == null)
			return -1;

		// The level of the player is given as the maximum of all his/her professions
		int maxLevel = -1;
		for (PlayerClass playerClass : data.getClasses())
			maxLevel = Math.max(maxLevel, playerClass.getLevel());
		return maxLevel;
	}

	/** Grants the player some SkillAPI experience */
	public static void givePlayerSkillAPIExperience(Player player, double amount)
	{
		// Obtain information about the player's current skills and classes
		PlayerData data = SkillAPI.getPlayerData(player);
		if (data == null)
			return;
		data.giveExp(amount, ExpSource.MOB);
	}

	/** Returns the level of the specific potion effect on the given entity */
	public static int getPotionEffectLevel(LivingEntity entity, PotionEffectType type)
	{
		if (entity == null || type == null)
			return 0;
		for (PotionEffect effect : entity.getActivePotionEffects())
		{
			if (effect.getType() == type)
				return 1 + effect.getAmplifier();
		}
		return 0;
	}

	/** Returns a safe location near the given location */
	public static Location getSafeLocation(Location location)
	{
		World world = location.getWorld();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		int currentY = y;

		int recursionsLeft = 20;
		while (recursionsLeft > 0)
		{
			currentY = y;

			// Get some blocks in the vicinity that gives a lot of information
			Block currentBlock = world.getBlockAt(x, y, z);
			Block upperBlock = world.getBlockAt(x, y + 1, z);

			// Locate solid ground
			while ((!currentBlock.getType().isSolid() || upperBlock.getType() != Material.AIR) && currentY > location.getBlockY() - 20)
			{
				upperBlock = currentBlock;
				currentBlock = world.getBlockAt(x, --currentY, z);
			}
			// Locate open ground
			upperBlock = world.getBlockAt(x, currentY + 1, z);
			Block lowerBlock = world.getBlockAt(x, currentY - 1, z);
			while ((!lowerBlock.getType().isSolid() || currentBlock.getType() != Material.AIR || upperBlock.getType() != Material.AIR) && currentY < location.getBlockY() + 20)
			{
				lowerBlock = currentBlock;
				currentBlock = upperBlock;
				upperBlock = world.getBlockAt(x, ++currentY, z);
			}
			// If no safe place was found, pick a new position that is nearby and try again
			if (Math.abs(currentY - location.getBlockY()) >= 20)
			{
				recursionsLeft--;
				Random random = new Random();
				x += -16 + random.nextInt(31);
				z += -16 + random.nextInt(31);
			}
			else
				break;
		}
		return new Location(world, (double) x + 0.5, (double) currentY, (double) z + 0.5);
	}

	/** Checks if there is some solid blocks in the path between the two locations, or an entity blocks the path. Returns the location of the obstruction, if any was found; returns null otherwise */
	public static Location getObstruction(Location start, Location end, LivingEntity entityToIgnore)
	{
		// Check for entity collisions
		LivingEntity entity = MathHelper.getEntityInLine(start.getWorld(), new Vector3D(start), new Vector3D(end), entityToIgnore);
		if (entity != null)
			return entity.getLocation();

		// Check for block collisions
		Location origin = start.clone();
		origin.setDirection(end.clone().subtract(start).toVector());

		try
		{
			BlockIterator iterator = new BlockIterator(origin, 0.0, (int) Math.ceil(start.distance(end)));
			while (iterator.hasNext())
			{
				Block block = iterator.next();
				if (block.getType().isSolid())
					return block.getLocation();
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}

	/** Returns an itemstack from the string, or null if the string was invalid. String is on the format itemName-itemMeta=itemCount */
	public static ItemStack getItemStack(String string)
	{
		if (string == null)
			return null;

		// Parse the string which is on the format itemName-itemMeta=itemCount
		// The item meta isn't mandatory, so itemName=itemCount would default to meta 0
		// If there is no item count, the count value would be treated as 0
		String name = "unnamed_item";
		String meta = "0";
		String value = "1";

		String[] components = string.split("=");
		if (components.length == 1)
			components = components[0].split("-");
		else if (components.length == 2)
		{
			value = components[1];
			components = components[0].split("-");
		}

		if (components.length == 1)
			name = components[0];
		else if (components.length == 2)
		{
			name = components[0];
			meta = components[1];
		}

		// Create the item
		Material material = Material.getMaterial(name.toUpperCase());
		if (material == null)
			Log.log(("Invalid item '").concat(name).concat("' with meta '").concat(meta).concat("'"), Level.WARNING);
		else
		{
			try
			{
				ItemStack itemStack = new ItemStack(material, Integer.parseInt(value), Short.parseShort(meta));
				return itemStack;
			}
			catch (Exception exception)
			{
				Log.log("Invalid format on item count or meta!", Level.WARNING);
			}
		}
		return null;
	}

	/** Returns an entity type from the string, or null if the string was invalid */
	@Deprecated
	public static EntityType getEntityType(String type)
	{
		EntityType entityType = null;
		try
		{
			entityType = EntityType.valueOf(type.toUpperCase());
		}
		catch (Exception exception)
		{
			Log.log("The entity type '" + type + "' is not valid");
		}
		return entityType;
	}

	/** Returns a new mob of the type of the given string */
	public static Entity spawnEntity(Location location, String type)
	{
		type = type.replaceAll(" ", "_");

		// Figure out some special characteristics of the mob to spawn
		boolean isSuperchargedCreeper = type.equalsIgnoreCase("supercharged_creeper");
		if (isSuperchargedCreeper)
			type = "creeper";
		boolean isAngryWolf = type.equalsIgnoreCase("angry_wolf");
		if (isAngryWolf)
			type = "wolf";

		// Get the entity type, spawn the entity and assign parameters
		EntityType entityType = getEntityType(type);
		if (entityType == null)
		{
			Log.log("Failed to spawn an entity of type '" + type + "'!");
			return null;
		}

		Entity entity = location.getWorld().spawnEntity(location, entityType);

		if (isSuperchargedCreeper && entity instanceof Creeper)
			((Creeper) entity).setPowered(true);
		else if (isAngryWolf && entity instanceof Wolf)
			((Wolf) entity).setAngry(true);
		return entity;
	}

	/** Return the entity type of a specific mob, on the standard form (Title case, no underscores) */
	public static String getEntityType(Entity entity)
	{
		switch (entity.getType())
		{
		case CREEPER:
			if (entity instanceof Creeper)
			{
				if (((Creeper) entity).isPowered())
					return "Supercharged Creeper";
				else
					return "Creeper";
			}
			break;
		case WOLF:
			if (entity instanceof Wolf)
			{
				if (((Wolf) entity).isAngry())
					return "Angry Wolf";
				else
					return "Wolf";
			}
			break;
		default:
			return toTitleCase(entity.getType().toString().replaceAll("_", " "));
		}
		return "Invalid Entity";
	}

	/** Converts the string to a string where the first letter in every word is capitalized */
	public static String toTitleCase(String string)
	{
		String[] arr = string.toLowerCase().split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++)
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		return sb.toString().trim();
	}
}
