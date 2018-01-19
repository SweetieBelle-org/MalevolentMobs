package com.hepolite.mmob.settings;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;

public class SettingsAbilities
{
	// Control variables
	private final static HashMap<String, Settings> passives = new HashMap<String, Settings>();
	private final static HashMap<String, Settings> actives = new HashMap<String, Settings>();

	/** Initialize all the ability settings; called from MMobSettings */
	public static void initialize()
	{
		// Clear old data
		actives.clear();
		passives.clear();

		// Every first run, all default abilities must be defined. Subsequent runs must define new abilities
		File directory = new File(MMobPlugin.getInstance().getDataFolder() + "/Abilities");
		if (!directory.exists())
			directory.mkdir();
		addDefaultAbilities();

		// Discover all abilities in the ability directory
		File[] files = directory.listFiles();
		for (File file : files)
		{
			String[] parts = file.getName().split(" ");
			if (parts.length != 2)
				Log.log("The file '" + file.getName() + "' is invalid!", Level.WARNING);
			else
			{
				if (parts[0].equalsIgnoreCase("Passive"))
					passives.put(parts[1], new Settings("Abilities", file.getName()));
				else if (parts[0].equalsIgnoreCase("Active"))
					actives.put(parts[1], new Settings("Abilities", file.getName()));
			}
		}
	}

	/** Returns the configuration for the given passive ability */
	public static Settings getPassiveConfig(String ability)
	{
		ability += ".yml";
		Settings setting = passives.get(ability);
		if (setting == null)
		{
			Log.log("Couldn't find config for passive ability '" + ability + "'!", Level.WARNING);
			return null;
		}
		return setting;
	}

	/** Returns the configuration for the given active ability */
	public static Settings getActiveConfig(String ability)
	{
		ability += ".yml";
		Settings setting = actives.get(ability);
		if (setting == null)
		{
			Log.log("Couldn't find config for active ability '" + ability + "'!", Level.WARNING);
			return null;
		}
		return setting;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Adds all the default abilities to the system */
	private static void addDefaultAbilities()
	{
		// Actives
		addBlazingPillar();
		addBlindfold();
		addDecoy();
		addFireball();
		addFireBurst();
		addFracturingBlast();
		addGrenade();
		addGroundSlam();
		addKidnap();
		addLeashOn();
		addLifesteal();
		addLightningStrike();
		addMagicBlast();
		addMagicMirror();
		addSummonMinion();
		addTeleport();
		addToxicSpit();
		addVirulentGrasp();
		addVolley();
		addWeaken();
		addWebbing();
		addWitheringBolt();

		// Passives
		addDeterioratingAura();
		addExoskeleton();
		addExplosion();
		addFireAura();
		addFreezingAura();
		addGuardianAngel();
		addHealer();
		addMother();
		addMount();
		addPoisonMist();
		addPotionEffect();
		addRegeneration();
		addShielding();
		addVampirism();
		addWitherAura();
		addWitherParticles();
	}

	// /////////////////////////////////////////////////////////////// ACTIVE ABILITIES

	private static void addBlazingPillar()
	{
		Settings ability = new Settings("Abilities", "Active Blazing_Pillar.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 20.0);
		ability.addDefault("baseDelay", 30);
		ability.addDefault("baseRange", 4.0);
		ability.addDefault("baseStrength", 3.5);
		ability.addDefault("baseDuration", 100);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addBlindfold()
	{
		Settings ability = new Settings("Abilities", "Active Blindfold.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 24.0);
		ability.addDefault("baseDelay", 35);
		ability.addDefault("baseRange", 5.5);
		ability.addDefault("baseDuration", 200);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addDecoy()
	{
		Settings ability = new Settings("Abilities", "Active Decoy.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 70.0);
		ability.addDefault("baseCount", 2.0);
		ability.addDefault("searchRadius", 25.0f);
		ability.addDefault("maxEntityCount", 8);
		ability.save();
	}

	private static void addFireball()
	{
		Settings ability = new Settings("Abilities", "Active Fireball.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 18.0);
		ability.addDefault("baseSpeed", 1.5);
		ability.addDefault("baseTurnFactor", 0.02);
		ability.addDefault("baseStrength", 4.0);
		ability.addDefault("baseDuration", 80);
		ability.addDefault("baseRange", 2.5);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addFireBurst()
	{
		Settings ability = new Settings("Abilities", "Active Fire_Burst.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 16.0);
		ability.addDefault("baseDelay", 25);
		ability.addDefault("baseRange", 5.0);
		ability.addDefault("baseStrength", 5.0);
		ability.addDefault("baseDuration", 80);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addFracturingBlast()
	{
		Settings ability = new Settings("Abilities", "Active Fracturing_Blast.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 25.0);
		ability.addDefault("baseSpeed", 1.5);
		ability.addDefault("baseStrength", 6.0);
		ability.addDefault("baseRange", 5.5);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addGrenade()
	{
		Settings ability = new Settings("Abilities", "Active Grenade.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 22.0);
		ability.addDefault("baseSpeed", 1.0);
		ability.addDefault("baseStrength", 6.5);
		ability.addDefault("baseDuration", 80);
		ability.addDefault("baseRange", 5.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addGroundSlam()
	{
		Settings ability = new Settings("Abilities", "Active Ground_Slam.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 20.0);
		ability.addDefault("baseStrength", 3.0);
		ability.addDefault("baseKnockup", 1.0);
		ability.addDefault("baseRange", 7.5);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addKidnap()
	{
		Settings ability = new Settings("Abilities", "Active Kidnap.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 95.0);
		ability.addDefault("baseMinDistance", 30.0);
		ability.addDefault("baseMaxDistance", 40.0);
		ability.save();
	}

	private static void addLeashOn()
	{
		Settings ability = new Settings("Abilities", "Active Leash_On.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 35.0);
		ability.addDefault("baseRange", 10.0);
		ability.addDefault("baseDuration", 200);
		ability.save();
	}

	private static void addLifesteal()
	{
		Settings ability = new Settings("Abilities", "Active Lifesteal.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 16.0);
		ability.addDefault("damageType", "percent");
		ability.addDefault("baseStrength", 0.15);
		ability.addDefault("baseRange", 12.0);
		ability.addDefault("baseRequiredHealthFactor", 0.75);
		ability.addDefault("baseDamageToHealthFactor", 1.5);
		ability.save();
	}

	private static void addLightningStrike()
	{
		Settings ability = new Settings("Abilities", "Active Lightning_Strike.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 45.0);
		ability.addDefault("baseDelay", 35);
		ability.addDefault("baseStrength", 6.0);
		ability.addDefault("baseRange", 3.75);
		ability.addDefault("baseStrikeCount", 3.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addMagicBlast()
	{
		Settings ability = new Settings("Abilities", "Active Magic_Blast.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 28.0);
		ability.addDefault("baseDelay", 25);
		ability.addDefault("baseStrength", 5.0);
		ability.addDefault("baseRange", 4.5);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addMagicMirror()
	{
		Settings ability = new Settings("Abilities", "Active Magic_Mirror.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 70.0);
		ability.addDefault("baseRepeatDelay", 300);
		ability.addDefault("baseStrength", 3.0);
		ability.addDefault("baseShieldCooldown", 25);
		ability.save();
	}

	private static void addSummonMinion()
	{
		Settings ability = new Settings("Abilities", "Active Summon_Minion.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 38.0);
		ability.addDefault("baseStartupDelay", 60);
		ability.addDefault("baseCount", 3.0);
		ability.addDefault("types", new String[] { "zombie", "skeleton", "spider", "wither_skeleton" });
		ability.save();
	}

	private static void addTeleport()
	{
		Settings ability = new Settings("Abilities", "Active Teleport.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 35.0);
		ability.addDefault("baseMinDistance", 15.0);
		ability.addDefault("baseMaxDistance", 35.0);
		ability.addDefault("searchRadius", 20.0f);
		ability.addDefault("timeSensitivity", 50);
		ability.save();
	}

	private static void addToxicSpit()
	{
		Settings ability = new Settings("Abilities", "Active Toxic_Spit.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 35.0);
		ability.addDefault("baseSpeed", 1.2);
		ability.addDefault("baseStrength", 3.0);
		ability.addDefault("baseDuration", 60);
		ability.addDefault("baseRange", 3.0);
		ability.addDefault("baseCount", 4.0);
		ability.addDefault("baseInaccuracy", 0.25);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addVirulentGrasp()
	{
		Settings ability = new Settings("Abilities", "Active Virulent_Grasp.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 35.0);
		ability.addDefault("baseDuration", 60);
		ability.save();
	}

	private static void addVolley()
	{
		Settings ability = new Settings("Abilities", "Active Volley.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 25.0);
		ability.addDefault("baseSpeed", 2.0);
		ability.addDefault("baseCount", 6.0);
		ability.addDefault("baseInaccuracy", 0.15);
		ability.addDefault("affectedByGravity", true);
		ability.save();
	}

	private static void addWeaken()
	{
		Settings ability = new Settings("Abilities", "Active Weaken.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 32.0);
		ability.addDefault("baseCount", 1.0);
		ability.addDefault("effects", new String[] { "hunger=20.0=0.75=1.0=0.04", "slow=5.0=0.01=1.0=0.075", "slow_digging=30.0=0.5=1.0=0.1", "weakness=7.0=0.125=2.0=0.06", "health_boost=9.0=0.1=-1.0=-0.035" });
		ability.save();
	}

	private static void addWebbing()
	{
		Settings ability = new Settings("Abilities", "Active Webbing.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 25.0);
		ability.addDefault("baseCount", 3.0);
		ability.addDefault("baseRange", 4.0);
		ability.addDefault("baseRadius", 2.0);
		ability.addDefault("baseDuration", 160);
		ability.save();
	}

	private static void addWitheringBolt()
	{
		Settings ability = new Settings("Abilities", "Active Withering_Bolt.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseCooldown", 22.0);
		ability.addDefault("baseSpeed", 1.75);
		ability.addDefault("baseStrength", 2.0);
		ability.addDefault("baseDuration", 60);
		ability.addDefault("baseRange", 3.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	// /////////////////////////////////////////////////////////////// PASSIVE ABILITIES

	private static void addDeterioratingAura()
	{
		Settings ability = new Settings("Abilities", "Passive Deteriorating_Aura.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 15.0);
		ability.save();
	}

	private static void addExoskeleton()
	{
		Settings ability = new Settings("Abilities", "Passive Exoskeleton.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRegeneration", 1.0);
		ability.addDefault("baseStrength", 16.0);
		ability.addDefault("baseStartupDelay", 200);
		ability.addDefault("baseRepeatDelay", 40);
		ability.save();
	}

	private static void addExplosion()
	{
		Settings ability = new Settings("Abilities", "Passive Explosion.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseStrength", 6.0);
		ability.addDefault("baseRadius", 5.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addFireAura()
	{
		Settings ability = new Settings("Abilities", "Passive Fire_Aura.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 6.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addFreezingAura()
	{
		Settings ability = new Settings("Abilities", "Passive Freezing_Aura.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 8.0);
		ability.addDefault("baseStrength", 1.0);
		ability.addDefault("baseDamage", 2.0);
		ability.addDefault("baseDelay", 5.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addGuardianAngel()
	{
		Settings ability = new Settings("Abilities", "Passive Guardian_Angel.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseLives", 1.0);
		ability.save();
	}

	private static void addHealer()
	{
		Settings ability = new Settings("Abilities", "Passive Healer.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 25.0);
		ability.addDefault("baseHealRate", 1.0);
		ability.addDefault("baseHealthMultiplier", 1.0);
		ability.save();
	}

	private static void addMother()
	{
		Settings ability = new Settings("Abilities", "Passive Mother.yml");
		ability.addDefault("enable", true);
		ability.addDefault("minGroupSize", 2);
		ability.addDefault("maxGroupSize", 3);
		ability.addDefault("repeats", -1);
		ability.addDefault("baseRepeatDelay", 600);
		ability.addDefault("searchRadius", 30.0f);
		ability.addDefault("maxEntityCount", 20);
		ability.save();
	}

	private static void addMount()
	{
		Settings ability = new Settings("Abilities", "Passive Mount.yml");
		ability.addDefault("enable", true);
		ability.addDefault("type", "skeleton");
		ability.addDefault("role", "Archer");
		ability.save();
	}

	private static void addPoisonMist()
	{
		Settings ability = new Settings("Abilities", "Passive Poison_Mist.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 5.0);
		ability.addDefault("baseStrength", 1.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addPotionEffect()
	{
		Settings ability = new Settings("Abilities", "Passive Potion_Effect.yml");
		ability.addDefault("enable", true);
		ability.addDefault("repeats", 1);
		ability.addDefault("baseStartupDelay", 0);
		ability.addDefault("baseRepeatDelay", 0);
		ability.addDefault("effect", "fire_resistance");
		ability.addDefault("baseDuration", 100000);
		ability.addDefault("baseStrength", 1.0);
		ability.save();
	}

	private static void addRegeneration()
	{
		Settings ability = new Settings("Abilities", "Passive Regeneration.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRegeneration", 1.0);
		ability.addDefault("baseStartupDelay", 0);
		ability.addDefault("baseRepeatDelay", 200);
		ability.save();
	}

	private static void addShielding()
	{
		Settings ability = new Settings("Abilities", "Passive Shielding.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseStrength", 15.0);
		ability.save();
	}
	
	private static void addVampirism()
	{
		Settings ability = new Settings("Abilities", "Passive Vampirism.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 8.0);
		ability.addDefault("baseSensitivity", 2.0);
		ability.save();
	}

	private static void addWitherAura()
	{
		Settings ability = new Settings("Abilities", "Passive Wither_Aura.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseRange", 5.0);
		ability.addDefault("baseStrength", 1.0);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}

	private static void addWitherParticles()
	{
		Settings ability = new Settings("Abilities", "Passive Wither_Particles.yml");
		ability.addDefault("enable", true);
		ability.addDefault("baseParticleCount", 3.0);
		ability.addDefault("baseStrength", 2.0);
		ability.addDefault("baseDuration", 50);
		ability.addDefault("baseRange", 6.5);
		ability.addDefault("affectPlayersOnly", true);
		ability.save();
	}
}
