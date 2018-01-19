package com.hepolite.mmob.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.EntityEquipment;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.MMobPlugin;
import com.hepolite.mmob.MMobSettings;
import com.hepolite.mmob.abilities.Ability;
import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.handlers.AbilityHandler;
import com.hepolite.mmob.handlers.LootDropHandler;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.handlers.RoleHandler;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsAbilities;
import com.hepolite.mmob.settings.SettingsRoles;
import com.hepolite.mmob.utility.Common;

public class MalevolentMob
{
	// Generic variables
	protected static Random random = new Random();

	private static int uniqueIndex = 0;
	private MobStatTracker tracker = new MobStatTracker(this);

	// Control variables
	private LivingEntity entity = null;	// The entity associated with the malevolent mob
	private int index = -1;				// The unique index of the mob
	private boolean isInitialized = false;

	private List<MobRole> possibleRoles = new LinkedList<MobRole>();
	private MobRole role = null;

	private boolean hideInfo = false;
	private boolean hideBossBar = false;

	private List<List<Passive>> passives = new ArrayList<List<Passive>>();
	private List<List<Active>> actives = new ArrayList<List<Active>>();

	// Handle targeting
	private boolean isOnPlayerSide = false;
	private boolean isAIControlled = true;

	private List<LivingEntity> nearbyTargets = new LinkedList<LivingEntity>();
	private int targetUpdateTimer = 0;

	// Make sure that players can't killsteal the mob by giving experience based on damage dealt in total
	private HashMap<UUID, Double> damageMap = new HashMap<UUID, Double>();
	private int damageMapUpdateTimer = 20;	// Update the map once per second

	/* Initialization */
	public MalevolentMob(LivingEntity entity)
	{
		this.entity = entity;
		this.index = uniqueIndex++;

		// Prepare abilities
		passives.add(new LinkedList<Passive>());
		passives.add(new LinkedList<Passive>());
		passives.add(new LinkedList<Passive>());
		actives.add(new LinkedList<Active>());
		actives.add(new LinkedList<Active>());
		actives.add(new LinkedList<Active>());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Called when the mob is spawned into the world */
	public void onSpawn()
	{
		isInitialized = true;
		loadRole(role.getName(), false);

		// Handle abilities
		for (int i = 0; i < 3; i++)
		{
			for (Passive passive : passives.get(i))
				passive.onSpawn();
			for (Active active : actives.get(i))
				active.onSpawn();
		}
	}

	/** Called when the mob dies */
	public void onDie(EntityDeathEvent event)
	{
		// Handle abilities
		for (int i = 0; i < 3; i++)
		{
			for (Passive passive : passives.get(i))
				passive.onDie();
			for (Active active : actives.get(i))
				active.onDie();
		}

		// Play death sound and drop loot
		if (!isDecoy())
		{
			entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.0f);

			// Handle drops
			if (role.shouldDropLoot)
				LootDropHandler.dropLoot(this);

			// Handle the hand-out of experience for killing the mob
			double totalDamage = 0.0f;
			for (double damage : damageMap.values())
				totalDamage += damage;

			// For experience to be handed out, it is required that players dealt a significant amount of damage
			for (Entry<UUID, Double> entry : damageMap.entrySet())
			{
				Player player = Bukkit.getPlayer(entry.getKey());
				if (player != null)
				{
					player.giveExp((int) ((double) role.vanillaExperience * entry.getValue() / totalDamage));
					Common.givePlayerSkillAPIExperience(player, Math.round(role.skillAPIExperience * entry.getValue() / totalDamage));
				}
			}
		}

		// Cancel the usual experience drop in favor of the custom one
		event.setDroppedExp(0);
	}

	/** Called each tick, to update core logic */
	public void onTick()
	{
		// Make sure to initialize the mob
		if (!isInitialized)
			onSpawn();

		// Handle sub-systems
		tracker.onTick();

		// Update abilities
		for (int i = 0; i < 3; i++)
		{
			for (Passive passive : passives.get(i))
				passive.onTick();
		}

		boolean castedAnAbility = false;
		if (isAIControlled)
		{
			for (int i = 2; i >= 0; i--)
			{
				for (Active active : actives.get(i))
				{
					active.onTick();
					if (active.isCasted())
					{
						castedAnAbility = true;
						break;
					}
				}
				if (castedAnAbility)
					break;
			}

			// If an active ability was casted, make sure to delay all other "activateable" abilities
			if (castedAnAbility)
			{
				for (int i = 0; i < 3; i++)
				{
					for (Active active : actives.get(i))
						active.decreaseCooldownTimer(role.attackCooldownTime);
				}
			}
		}

		// Handle damage dealt by players, reduce their contribution by a tiny bit per second
		if (!isDecoy() && --damageMapUpdateTimer < 0)
		{
			damageMapUpdateTimer = 20;

			// Reduce total damage dealt with some value and remove all players that haven't contributed much or recently
			List<UUID> entriesToRemove = new LinkedList<UUID>();
			for (Entry<UUID, Double> entry : damageMap.entrySet())
			{
				double value = entry.getValue() - 0.1;
				if (value > 0.0)
					entry.setValue(value);
				else
					entriesToRemove.add(entry.getKey());
			}
			for (UUID uuid : entriesToRemove)
				damageMap.remove(uuid);
		}

		// Locate new targets if applicable
		if (--targetUpdateTimer < 0)
		{
			targetUpdateTimer = 20;

			// Remove targets that are beyond the reach of the mob, or dead
			float range = MMobPlugin.getSettings().getFloat("General.Mobs.targetLoseDistance");
			for (Iterator<LivingEntity> it = nearbyTargets.iterator(); it.hasNext();)
			{
				LivingEntity target = it.next();
				if (!target.isValid() || target.getWorld() != entity.getWorld() || entity.getLocation().distanceSquared(target.getLocation()) >= range * range)
					it.remove();
			}

			// Find all nearby players, including the mob target, and add them
			if (entity instanceof Creature)
			{
				LivingEntity target = ((Creature) entity).getTarget();
				if (target != null && !nearbyTargets.contains(target))
					nearbyTargets.add(0, target);
			}
			List<?> targets = null;
			if (isOnPlayerSide)
				targets = Common.getMonstersInRange(entity.getLocation(), MMobPlugin.getSettings().getFloat("General.Mobs.targetAquireDistance"));
			else
				targets = Common.getPlayersInRange(entity.getLocation(), MMobPlugin.getSettings().getFloat("General.Mobs.targetAquireDistance"));
			for (Object entity : targets)
			{
				if (entity != this.entity && !nearbyTargets.contains((LivingEntity) entity))
					nearbyTargets.add((LivingEntity) entity);
			}
		}
	}

	/** Called when taking any damage */
	public void onDamageTaken(EntityDamageEvent event)
	{
		double rawDamage = event.getDamage();

		// Apply the low-priority abilities
		for (Passive passive : passives.get(0))
			passive.onAttacked(event);
		for (Active active : actives.get(0))
			active.onAttacked(event);

		// Reduce damage
		applyDamageReductions(event);

		// Apply the rest of the passives
		for (int i = 1; i < 3; i++)
		{
			if (!event.isCancelled())
			{
				for (Passive passive : passives.get(i))
					passive.onAttacked(event);
				for (Active active : actives.get(i))
					active.onAttacked(event);
			}
		}

		// Do some additional logic if the attack got through the defenses
		if (!event.isCancelled() && event.getDamage() > 0.0)
		{
			// Kill decoys instantly from any damage source
			if (isDecoy())
				event.setDamage(Math.max(event.getDamage(), 1.2f * entity.getMaxHealth()));
			else
			{
				// Track the damage dealt by all players damaging the mob, as well as natural damage sources
				if (event instanceof EntityDamageByEntityEvent)
				{
					UUID uuid = null;
					Entity attacker = Common.getAttacker((EntityDamageByEntityEvent) event);
					if (attacker instanceof Player)
						uuid = ((Player) attacker).getUniqueId();
					damageMap.put(uuid, event.getDamage() + (damageMap.containsKey(uuid) ? damageMap.get(uuid) : 0.0));
				}
			}
		}

		// Track the damage dealt
		if (!event.isCancelled())
			tracker.recordAttackFromTarget(event, rawDamage);
	}

	/** Called when dealing any damage */
	public void onDamageGiven(EntityDamageByEntityEvent event)
	{
		double rawDamage = event.getDamage();

		// Pass the event through all events
		for (int i = 0; i < 3; i++)
		{
			if (!event.isCancelled())
			{
				for (Passive passive : passives.get(i))
					passive.onAttacking(event);
				for (Active active : actives.get(i))
					active.onAttacking(event);
			}
		}

		// Track the damage dealt
		if (!event.isCancelled())
			tracker.recordAttackOnTarget(event, rawDamage);
	}

	/** Called when gaining any health */
	public void onHealed(EntityRegainHealthEvent event)
	{
		for (int i = 2; i >= 0; i--)
		{
			if (!event.isCancelled())
			{
				for (Passive passive : passives.get(i))
					passive.onHealed(event);
				for (Active active : actives.get(i))
					active.onHealed(event);
			}
		}
	}

	/** Called when a creeper blows up, or a wither skull and ghast fireball hits anything */
	public void onExplode(ExplosionPrimeEvent event)
	{

	}

	// /////////////////////////////////////////////////////////////////////////////
	// ABILITIES // ABILITIES // ABILITIES // ABILITIES // ABILITIES // ABILITIES //
	// /////////////////////////////////////////////////////////////////////////////

	/** Applies the damage reductions based on the the current stats */
	private void applyDamageReductions(EntityDamageEvent event)
	{
		// Get the armor for the mob
		float armor = 0.0f;
		if (Common.isAttackMelee(event))
			armor += role.baseMeleeArmor + getLevel() * role.scaleMeleeArmor;
		if (Common.isAttackMagic(event))
			armor += role.baseMagicArmor + getLevel() * role.scaleMagicArmor;
		if (Common.isAttackArrow(event))
			armor += role.baseArrowArmor + getLevel() * role.scaleArrowArmor;
		if (Common.isAttackRanged(event))
			armor += role.baseRangedArmor + getLevel() * role.scaleRangedArmor;
		armor = armor * (1.0f - role.percentArmorPenetrated) - role.flatArmorPenetrated;
		armor = Math.max(-50, armor);

		role.flatArmorPenetrated = 0.0f;
		role.percentArmorPenetrated = 0.0f;

		// Reduce the damage
		float damage = (float) event.getDamage();
		event.setDamage(damage * (1.0f - armor / (100.0f + armor)));

		if (MMobSettings.isDebugmode)
		{
			String attackType = (Common.isAttackMelee(event) ? "Me" : "") + (Common.isAttackMagic(event) ? "Ma" : "") + (Common.isAttackArrow(event) ? "Ar" : "") + (Common.isAttackRanged(event) ? "Ra" : "");
			Log.log("[MMob] Registered attack '" + attackType + "', dealing '" + damage + "'->'" + event.getDamage() + "' damage due to '" + armor + "' armor (" + String.format("%.1f", 100.0f * armor / (100.0f + armor)) + "%).");
		}
	}

	/** Adds a new passive ability to the mob */
	public Passive addPassive(String passive)
	{
		Passive passiveAbility = AbilityHandler.getPassive(this, passive);
		if (passiveAbility == null)
		{
			Log.log("Attempted to add invalid passive ability '" + passive + "' to mob '" + Integer.toString(index) + "'");
			return null;
		}
		passives.get(passiveAbility.getPriority().ordinal()).add(passiveAbility);
		return passiveAbility;
	}

	/** Adds a new active ability to the mob */
	public Active addActive(String active)
	{
		Active activeAbility = AbilityHandler.getActive(this, active);
		if (activeAbility == null)
		{
			Log.log("Attempted to add invalid active ability '" + active + "' to mob '" + Integer.toString(index) + "'");
			return null;
		}
		actives.get(activeAbility.getPriority().ordinal()).add(activeAbility);
		return activeAbility;
	}

	/** Sets the armor that is to be bypassed on the next attack */
	public void addArmorPenetration(float percentPenetration, float flatPenetration)
	{
		role.percentArmorPenetrated = 1.0f - (1.0f - role.percentArmorPenetrated) * (1.0f - percentPenetration);
		role.flatArmorPenetrated += flatPenetration;
	}

	/** Creates a decoy of the malevolent mob at the given location */
	public LivingEntity createDecoy(Location location)
	{
		// Spawn the mob to turn into a clone
		MMobListener.setSpawnDenyFlag(true);
		LivingEntity livingEntity = (LivingEntity) Common.spawnEntity(location, Common.getEntityType(entity));
		MMobListener.setSpawnDenyFlag(false);

		// Create the malevolent version of this mob and copy over attributes
		MalevolentMob mob = MobHandler.makeMobMalevolent(livingEntity);
		if (mob != null)
		{
			mob.setRole(getRole());
			mob.setLevel(getLevel());
			mob.role.isDecoy = true;
		}
		else
			Log.log("Attempted to spawn a decoy, but failed to make it Malevolent!", Level.WARNING);

		livingEntity.setMaxHealth(entity.getMaxHealth());
		livingEntity.setHealth(entity.getHealth());
		return livingEntity;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	// ROLES // ROLES // ROLES // ROLES // ROLES // ROLES // ROLES // ROLES // ROLES // ROLES //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Wipes the role from the given mod */
	public void removeRole()
	{
		role = null;
	}

	/** Sets the role of the given mob. The mob will receive the given role and that role only, even if it already has a role */
	public void setRole(String role)
	{
		setRole(RoleHandler.getRole(role));
	}

	/** Sets all the possible roles the mob can have */
	public void setRandomRole(List<String> roles)
	{
		for (String role : roles)
			addPossibleRole(RoleHandler.getRole(role));

		if (possibleRoles.size() > 0)
		{
			setRole(possibleRoles.get(random.nextInt(possibleRoles.size())));
			possibleRoles.clear();
		}
	}

	/** Returns the role the mob has */
	public MobRole getRole()
	{
		return role;
	}

	/** Adds a role the mob can have */
	private void addPossibleRole(MobRole role)
	{
		if (role == null)
		{
			Log.log("Attempted to add a null role to a malevolent mob!", Level.WARNING);
			return;
		}

		Settings settings = SettingsRoles.getConfig(role.getName());
		if (settings == null || !settings.getBoolean("enable"))
			return;
		possibleRoles.add(role);
	}

	/** Sets the role of the given mob. The mob will receive the given role and that role only, even if it already has a role */
	public void setRole(MobRole role)
	{
		if (role == null)
		{
			Log.log("Attempted to assign a null role to a malevolent mob!", Level.WARNING);
			return;
		}
		if (!SettingsRoles.getConfig(role.getName()).getBoolean("enable"))
		{
			Log.log("Attempted to load role '" + role.getName() + "', which is not enabled!");
			return;
		}
		this.role = new MobRole(role.getName());
		possibleRoles.clear();
	}

	/** Loads up the current role and assigns it the the calling mob */
	private void loadRole(String roleName, boolean isParentRole)
	{
		if (role == null)
		{
			Log.log("Attempted to load up a null role for malevolent mob '" + Integer.toString(index) + "'!", Level.WARNING);
			role = new MobRole("BROKEN_ROLE");
		}
		if (MMobSettings.isDebugmode)
			Log.log("Loading up role '" + roleName + "'...");

		MMobSettings pluginSettings = MMobPlugin.getSettings();
		Settings settings = SettingsRoles.getConfig(roleName);

		// Load parent configurations
		if (settings.hasProperty("General.parentRole"))
			loadRole(settings.getString("General.parentRole"), true);

		// Load up role header data, such as custom name, level, health and so on.
		if (settings.hasProperty("General.level"))
			setLevel(getLevelFromType(settings.getString("General.level")));

		if (settings.hasProperty("General.customName"))
		{
			entity.setCustomName(ChatColor.translateAlternateColorCodes('&', settings.getString("General.customName")));
			role.mobName = settings.getString("General.customName");
		}
		else if (entity.getCustomName() == null)
		{
			String entityType = Common.getEntityType(entity);
			entity.setCustomName("Malevolent " + entityType.replaceAll("_", " "));
			role.mobName = entity.getCustomName();
		}

		if (settings.hasProperty("General.hideInfo"))
			hideInfo = settings.getBoolean("General.hideInfo");
		if (settings.hasProperty("General.hideBossBar"))
			hideBossBar = settings.getBoolean("General.hideBossBar");

		isAIControlled = (settings.hasProperty("General.ai") ? settings.getBoolean("General.ai") : true);
		isOnPlayerSide = (settings.hasProperty("General.playerSide") ? settings.getBoolean("General.playerSide") : false);

		// Load up the health of the mob
		if (settings.hasProperty("General.baseHealth"))
		{
			float scale = pluginSettings.getFloat("General.Mobs.healthScale");
			float health = scale * settings.getScaledValue("General.", "Health", getLevel(), (float) entity.getMaxHealth() / (isParentRole ? 1.0f : scale));

			entity.setMaxHealth(health);
			entity.setHealth(entity.getMaxHealth());
		}

		// Load up experience and loot dropped
		if (settings.hasProperty("Loot.baseVanillaExperience"))
			role.vanillaExperience = (int) settings.getScaledValue("Loot.", "VanillaExperience", getLevel(), 0.0f);
		if (settings.hasProperty("Loot.baseSkillAPIExperience"))
			role.skillAPIExperience = settings.getScaledValue("Loot.", "SkillAPIExperience", getLevel(), 0.0f);

		if (settings.hasProperty("Loot.baseChance"))
			role.shouldDropLoot = (random.nextFloat() < settings.getScaledValue("Loot.", "Chance", getLevel(), 0.0f));
		if (settings.hasProperty("Loot.definitionFile"))
			role.lootDefinitionFile = settings.getString("Loot.definitionFile");

		// Load up the equipment from the mob
		if (settings.hasProperty("Equipment"))
		{
			if (settings.hasProperty("Equipment.allowPickup"))
				entity.setCanPickupItems(settings.getBoolean("Equipment.allowPickup"));

			EntityEquipment equipment = getEntity().getEquipment();

			String weaponItem = settings.getString("Equipment.weaponItem");
			String weaponSideItem = settings.getString("Equipment.weaponSideItem");
			String helmetItem = settings.getString("Equipment.helmetItem");
			String chestplateItem = settings.getString("Equipment.chestplateItem");
			String leggingsItem = settings.getString("Equipment.leggingsItem");
			String bootsItem = settings.getString("Equipment.bootsItem");

			if (!weaponItem.equals(""))
				equipment.setItemInMainHand(weaponItem.equals("none") ? null : Common.getItemStack(weaponItem));
			if (!weaponSideItem.equals(""))
				equipment.setItemInOffHand(weaponSideItem.equals("none") ? null : Common.getItemStack(weaponSideItem));
			if (!helmetItem.equals(""))
				equipment.setHelmet(helmetItem.equals("none") ? null : Common.getItemStack(helmetItem));
			if (!chestplateItem.equals(""))
				equipment.setChestplate(chestplateItem.equals("none") ? null : Common.getItemStack(chestplateItem));
			if (!leggingsItem.equals(""))
				equipment.setLeggings(leggingsItem.equals("none") ? null : Common.getItemStack(leggingsItem));
			if (!bootsItem.equals(""))
				equipment.setBoots(bootsItem.equals("none") ? null : Common.getItemStack(bootsItem));

			if (settings.hasProperty("Equipment.weaponDropChance"))
				equipment.setItemInMainHandDropChance(settings.getFloat("Equipment.weaponDropChance"));
			if (settings.hasProperty("Equipment.weaponSideDropChance"))
				equipment.setItemInOffHandDropChance(settings.getFloat("Equipment.weaponSideDropChance"));
			if (settings.hasProperty("Equipment.helmetDropChance"))
				equipment.setHelmetDropChance(settings.getFloat("Equipment.helmetDropChance"));
			if (settings.hasProperty("Equipment.chestplateDropChance"))
				equipment.setChestplateDropChance(settings.getFloat("Equipment.chestplateDropChance"));
			if (settings.hasProperty("Equipment.leggingsDropChance"))
				equipment.setLeggingsDropChance(settings.getFloat("Equipment.leggingsDropChance"));
			if (settings.hasProperty("Equipment.bootsDropChance"))
				equipment.setBootsDropChance(settings.getFloat("Equipment.bootsDropChance"));
		}

		// Load up all the abilities
		loadRoleAbilities(roleName, "Passives");
		loadRoleAbilities(roleName, "Actives");

		// Load up stats from the role
		float armorScale = pluginSettings.getFloat("General.Mobs.armorScale");
		if (settings.hasProperty("Stats.baseMeleeArmor"))
			role.baseMeleeArmor = armorScale * settings.getFloat("Stats.baseMeleeArmor");
		if (settings.hasProperty("Stats.scaleMeleeArmor"))
			role.scaleMeleeArmor = armorScale * settings.getFloat("Stats.scaleMeleeArmor");
		if (settings.hasProperty("Stats.baseMagicArmor"))
			role.baseMagicArmor = armorScale * settings.getFloat("Stats.baseMagicArmor");
		if (settings.hasProperty("Stats.scaleMagicArmor"))
			role.scaleMagicArmor = armorScale * settings.getFloat("Stats.scaleMagicArmor");
		if (settings.hasProperty("Stats.baseArrowArmor"))
			role.baseArrowArmor = armorScale * settings.getFloat("Stats.baseArrowArmor");
		if (settings.hasProperty("Stats.scaleArrowArmor"))
			role.scaleArrowArmor = armorScale * settings.getFloat("Stats.scaleArrowArmor");
		if (settings.hasProperty("Stats.baseRangedArmor"))
			role.baseRangedArmor = armorScale * settings.getFloat("Stats.baseRangedArmor");
		if (settings.hasProperty("Stats.scaleRangedArmor"))
			role.scaleRangedArmor = armorScale * settings.getFloat("Stats.scaleRangedArmor");

		if (settings.hasProperty("Stats.baseAttackCooldown") || settings.hasProperty("Stats.scaleAttackCooldown"))
			role.attackCooldownTime = settings.getScaledValue("General.", "AttackCooldown", getLevel(), pluginSettings.getFloat("General.Attacks.attackCooldown"));
	}

	/** Loads up the abilities */
	private void loadRoleAbilities(String roleName, String abilityGroup)
	{
		Settings settings = SettingsRoles.getConfig(roleName);
		String abilityPath = abilityGroup + ".";

		// Load up all the abilities from the role
		Set<String> abilities = settings.getKeys(abilityGroup);
		for (String abilityName : abilities)
		{
			// Check if there's more than one of this ability
			String abilityIndex = "";
			if (abilityName.contains("-"))
			{
				String[] components = abilityName.split("-");
				abilityName = components[0];
				abilityIndex = "-" + components[1];
			}

			// Grab the relevant configuration files
			Settings baseAbilitySettings = null;
			if (abilityGroup.equals("Passives"))
				baseAbilitySettings = SettingsAbilities.getPassiveConfig(abilityName);
			else if (abilityGroup.equals("Actives"))
				baseAbilitySettings = SettingsAbilities.getActiveConfig(abilityName);

			// Grabbing the relevant sub-section of the config file
			Settings mobPart = settings.getBaseConfig(abilityPath + abilityName + abilityIndex);

			// If there's a chance of the ability being added, take that into consideration
			float chance = mobPart.getScaledValue(baseAbilitySettings, "Chance", getLevel(), 1.0f);
			float maxChance = mobPart.getScaledValue(baseAbilitySettings, "MaxChance", getLevel(), 1.0f);
			if (random.nextFloat() < Math.min(maxChance, chance))
			{
				// Load up the ability and get the data about it from the configuration file
				Ability ability = null;
				if (abilityGroup.equals("Passives"))
					ability = addPassive(abilityName);
				else if (abilityGroup.equals("Actives"))
					ability = addActive(abilityName);
				if (ability != null)
					ability.loadFromConfig(mobPart, baseAbilitySettings);

				if (MMobSettings.isDebugmode)
				{
					if (ability != null)
						Log.log("Added the '" + abilityName + "' (" + abilityGroup + ") ability to mob '" + Integer.toString(index) + "'");
					else if (settings.getBoolean("General.debugmode"))
						Log.log("Was unable to add the ability '" + abilityName + "' (" + abilityGroup + ") to mob '" + Integer.toString(index) + "'!", Level.WARNING);
				}
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// DATA // DATA // DATA // DATA // DATA // DATA // DATA // DATA // DATA // DATA // DATA //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Returns the entity associated with the malevolent mob */
	public LivingEntity getEntity()
	{
		return entity;
	}

	/** Returns the index of the mob */
	public int getIndex()
	{
		return index;
	}

	/** Returns the stat tracker associated with the malevolent mob */
	public MobStatTracker getStatTracker()
	{
		return tracker;
	}

	/** Returns true if the role and level should be omitted when displaying the mob name */
	public boolean isInfoHidden()
	{
		return hideInfo;
	}

	/** Returns true if the entire boss bar should be disabled for the mob */
	public boolean isBossBarHidden()
	{
		return hideBossBar;
	}

	/** Returns the level of the mob */
	public float getLevel()
	{
		return role.level;
	}

	/** Sets the level of the mob */
	private void setLevel(float level)
	{
		role.level = level;
	}

	/** Returns a level of the mob based on the given parameter. Valid parameters: random, playerAverage, max:factor, level_number. Returns 1.0f if something went wrong */
	private float getLevelFromType(String type)
	{
		if (type == null)
			return 1.0f;
		float maxLevel = MMobPlugin.getSettings().getFloat("General.Mobs.maxLevel");

		// Return a completely random level, from 1 to maxLevel
		if (type.equals("random"))
			return 1.0f + random.nextFloat() * (maxLevel - 1.0f);

		// Return a value based on the average level of the players in the vicinity
		else if (type.equals("playerAverage"))
		{
			float totalPlayerLevel = 0.0f;
			int playersCounted = 0;

			List<Player> players = Common.getPlayersInRange(entity.getLocation(), MMobPlugin.getSettings().getFloat("General.Mobs.levelSearchDistance"));
			for (Player player : players)
			{
				int playerLevel = Common.getPlayerLevel(player);
				if (playerLevel != -1)
				{
					totalPlayerLevel += playerLevel;
					playersCounted++;
				}
			}

			// If there were no relevant nearby players, default to level 1
			if (playersCounted == 0)
				return 1.0f;
			return Math.min(maxLevel, totalPlayerLevel / (float) playersCounted);
		}

		// Maximum level, times a factor
		else if (type.startsWith("max:"))
		{
			String components[] = type.split(":");
			try
			{
				return maxLevel * Float.parseFloat(components[1]);
			}
			catch (Exception exception)
			{
				Log.log("Attempted to parse string '" + type.replace("max:", "") + "' as a float while loading level; invalid format.", Level.WARNING);
			}
		}

		// Attempt to convert the string into a pure number
		else
		{
			try
			{
				return Float.parseFloat(type);
			}
			catch (Exception exception)
			{
				Log.log("Attempted to parse string '" + type + "' as a float while loading level; invalid format.", Level.WARNING);
			}
		}
		return 1.0f;
	}

	/** Returns a list of the passives the mob has */
	public List<Passive> getPassives()
	{
		List<Passive> passives = new LinkedList<Passive>();
		for (int i = 0; i < 3; i++)
		{
			for (Passive passive : this.passives.get(i))
				passives.add(passive);
		}
		return passives;
	}

	/** Returns a list of the actives the mob has */
	public List<Active> getActives()
	{
		List<Active> actives = new LinkedList<Active>();
		for (int i = 0; i < 3; i++)
		{
			for (Active active : this.actives.get(i))
				actives.add(active);
		}
		return actives;
	}

	/** Returns a list of targets detected by the mob */
	public List<LivingEntity> getTargets()
	{
		return nearbyTargets;
	}

	/** Returns true if the mob is a decoy */
	public boolean isDecoy()
	{
		return role.isDecoy;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Sets the AI state of the mob */
	public void setAIControlled(boolean ai)
	{
		isAIControlled = ai;
	}

	/** Returns true if the mob is controlled by AI */
	public boolean isAIControlled()
	{
		return isAIControlled;
	}
	
	/** Sets the side state of the mob */
	public void setOnPlayerSide(boolean onPlayerSide)
	{
		isOnPlayerSide = onPlayerSide;
	}

	/** Returns true if the mob is controlled by AI */
	public boolean isOnPlayerSide()
	{
		return isOnPlayerSide;
	}
}
