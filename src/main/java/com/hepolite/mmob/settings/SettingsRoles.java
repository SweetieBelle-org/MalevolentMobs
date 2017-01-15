package com.hepolite.mmob.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;
import com.hepolite.mmob.MMobSettings;

public class SettingsRoles
{
	// Control variables
	private final static HashMap<String, Settings> roles = new HashMap<String, Settings>();

	/** Initialize all the roles settings; called from MMobSettings */
	public static void initialize()
	{
		// Clear old data
		roles.clear();

		// Every first run, all default mobs must be defined
		File directory = new File(MMobPlugin.getInstance().getDataFolder() + "/Roles");
		if (!directory.exists())
			directory.mkdir();
		addDefaultRoles();

		// Discover all abilities in the ability directory
		File[] files = directory.listFiles();
		for (File file : files)
		{
			roles.put(file.getName().split("\\.")[0], new Settings("Roles", file.getName()));

			if (MMobSettings.isDebugmode)
				Log.log("Discovered role '" + file.getName() + "'!");
		}
	}

	/** Returns a collection that contains all the roles loaded up by the system */
	public static Set<String> getRoles()
	{
		return roles.keySet();
	}

	/** Returns the configuration for the given role */
	public static Settings getConfig(String role)
	{
		Settings setting = roles.get(role);
		if (setting == null)
		{
			Log.log("Couldn't find config for role '" + role + "'!", Level.WARNING);
			return null;
		}
		return setting;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Adds all the default mobs to the system */
	private static void addDefaultRoles()
	{
		// System
		addCommon();

		// Normal mobs
		addTank();
		addBruiser();
		addMage();
		addNecromancer();
		addArcher();
		addKnight();
		addMatriarch();
		addFunnelWeb();
		addPyre();
		addPyreJockey();
		addPhantom();
		addVoidling();
		addSpellweaver();

		// Boss mobs
		addMal();
	}

	private static void addCommon()
	{
		Settings role = new Settings("Roles", "Common.yml");

		role.addDefault("enable", true);
		role.addDefault("General.level", "playerAverage");

		role.addDefault("Loot.baseVanillaExperience", 100);
		role.addDefault("Loot.scaleVanillaExperience", 8);
		role.addDefault("Loot.baseSkillAPIExperience", 75);
		role.addDefault("Loot.scaleSkillAPIExperience", 5);
		role.addDefault("Loot.definitionFile", "Common");
		role.addDefault("Loot.baseChance", 1.0);
		role.addDefault("Loot.items.0", "#random");

		role.save();
	}

	private static void addTank()
	{
		Settings role = new Settings("Roles", "Tank.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 32.0);
		role.addDefault("General.scaleHealth", 1.2);
		role.addDefault("Stats.baseMeleeArmor", 80.0);
		role.addDefault("Stats.scaleMeleeArmor", 13.0);
		role.addDefault("Stats.baseMagicArmor", 60.0);
		role.addDefault("Stats.scaleMagicArmor", 6.0);
		role.addDefault("Stats.baseArrowArmor", 65.0);
		role.addDefault("Stats.scaleArrowArmor", 9.5);
		role.addDefault("Stats.baseRangedArmor", 60.0);
		role.addDefault("Stats.scaleRangedArmor", 9.0);
		role.addDefault("Stats.baseAttackCooldown", 2.5);
		role.addDefault("Equipment.weaponItem", "none");
		role.addDefault("Equipment.helmetItem", "iron_helmet");
		role.addDefault("Equipment.chestplateItem", "iron_chestplate");
		role.addDefault("Equipment.leggingsItem", "iron_leggings");
		role.addDefault("Equipment.bootsItem", "iron_boots");

		role.addDefault("Passives.Deteriorating_Aura.baseChance", -0.7);
		role.addDefault("Passives.Deteriorating_Aura.scaleChance", 0.03);
		role.addDefault("Passives.Explosion.baseChance", 0.4);
		role.addDefault("Passives.Explosion.scaleChance", 0.005);
		role.addDefault("Passives.Fire_Aura.baseChance", 0.2);
		role.addDefault("Passives.Fire_Aura.scaleChance", 0.1);
		role.addDefault("Passives.Fire_Aura.baseRange", 4.0);
		role.addDefault("Passives.Fire_Aura.scaleRange", 0.1);
		role.addDefault("Passives.Mother.baseChance", 0.2);
		role.addDefault("Passives.Regeneration.baseChance", 1.4);
		role.addDefault("Passives.Regeneration.scaleChance", -0.025);

		role.addDefault("Actives.Fire_Burst.baseStrength", 4.0);
		role.addDefault("Actives.Fire_Burst.scaleStrength", 0.1);
		role.addDefault("Actives.Ground_Slam.scaleStrength", 0.075);
		role.addDefault("Actives.Ground_Slam.scaleKnockup", 0.003);
		role.addDefault("Actives.Lifesteal.baseStrength", 0.125);
		role.addDefault("Actives.Lifesteal.scaleStrength", 0.0025);
		role.addDefault("Actives.Magic_Mirror.baseChance", -18);
		role.addDefault("Actives.Magic_Mirror.scaleChance", 1);
		role.addDefault("Actives.Magic_Mirror.baseMaxChance", 0.5);
		role.addDefault("Actives.Magic_Mirror.scaleStrength", 0.0625);
		role.addDefault("Actives.Virulent_Grasp.scaleDuration", 0.75);

		role.save();
	}

	private static void addBruiser()
	{
		Settings role = new Settings("Roles", "Bruiser.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 26.0);
		role.addDefault("General.scaleHealth", 1.1);
		role.addDefault("Stats.baseMeleeArmor", 65.0);
		role.addDefault("Stats.scaleMeleeArmor", 11.5);
		role.addDefault("Stats.baseMagicArmor", 70.0);
		role.addDefault("Stats.scaleMagicArmor", 9.5);
		role.addDefault("Stats.baseArrowArmor", 60.0);
		role.addDefault("Stats.scaleArrowArmor", 9.5);
		role.addDefault("Stats.baseRangedArmor", 50.0);
		role.addDefault("Stats.scaleRangedArmor", 8.5);
		role.addDefault("Stats.baseAttackCooldown", 4.0);

		role.addDefault("Passives.Exoskeleton.baseChance", -0.3);
		role.addDefault("Passives.Exoskeleton.scaleChance", 0.035);
		role.addDefault("Passives.Exoskeleton.scaleStrength", 0.5);
		role.addDefault("Passives.Fire_Aura.baseChance", 0.2);
		role.addDefault("Passives.Fire_Aura.scaleChance", 0.1);
		role.addDefault("Passives.Fire_Aura.baseRange", 4.0);
		role.addDefault("Passives.Fire_Aura.scaleRange", 0.125);
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.baseChance", 1.0);
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.effect", "fire_resistance");
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.baseDuration", 100000);
		role.addDefault("Passives.Potion_Effect-Resistance.baseChance", 0.4);
		role.addDefault("Passives.Potion_Effect-Resistance.effect", "damage_resistance");
		role.addDefault("Passives.Potion_Effect-Resistance.baseDuration", 100000);
		role.addDefault("Passives.Potion_Effect-Resistance.scaleStrength", 0.03);
		role.addDefault("Passives.Vampirism.baseChance", -0.5);
		role.addDefault("Passives.Vampirism.scaleChance", 0.0225);
		role.addDefault("Passives.Wither_Particles.baseChance", 0.6);
		role.addDefault("Passives.Wither_Particles.scaleChance", 0.025);
		role.addDefault("Passives.Wither_Particles.scaleParticleCount", 0.125);

		role.addDefault("Actives.Grenade.scaleStrength", 0.05);
		role.addDefault("Actives.Grenade.scaleRange", 0.1);
		role.addDefault("Actives.Grenade.scaleDuration", -0.25);
		role.addDefault("Actives.Ground_Slam.scaleStrength", 0.1);
		role.addDefault("Actives.Ground_Slam.scaleKnockup", 0.003);
		role.addDefault("Actives.Leash_On.scaleDuration", 2);
		role.addDefault("Actives.Lifesteal.baseStrength", 0.125);
		role.addDefault("Actives.Lifesteal.scaleStrength", 0.0025);
		/* role.addDefault("Actives.Lightning_Strike.baseChance", -10.0); role.addDefault("Actives.Lightning_Strike.scaleChance", 1); role.addDefault("Actives.Lightning_Strike.scaleDelay", -0.2); role.addDefault("Actives.Lightning_Strike.scaleStrikeCount", 0.0625); */
		role.addDefault("Actives.Virulent_Grasp.scaleDuration", 0.75);
		role.addDefault("Actives.Withering_Bolt.baseChance", -18.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	private static void addMage()
	{
		Settings role = new Settings("Roles", "Mage.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 26.0);
		role.addDefault("General.scaleHealth", 1.1);
		role.addDefault("Stats.baseMeleeArmor", 55.0);
		role.addDefault("Stats.scaleMeleeArmor", 11.0);
		role.addDefault("Stats.baseMagicArmor", 110.0);
		role.addDefault("Stats.scaleMagicArmor", 13.5);
		role.addDefault("Stats.baseArrowArmor", 50.0);
		role.addDefault("Stats.scaleArrowArmor", 7.5);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 7.0);
		role.addDefault("Stats.baseAttackCooldown", 4.5);
		role.addDefault("Equipment.weaponItem", "none");

		role.addDefault("Passives.Regeneration.baseChance", 1.4);
		role.addDefault("Passives.Regeneration.scaleChance", -0.025);
		role.addDefault("Passives.Guardian_Angel.baseChance", 0.3);
		role.addDefault("Passives.Guardian_Angel.baseChance", 0.0175);
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.effect", "fire_resistance");
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.baseDuration", 100000);
		role.addDefault("Passives.Wither_Aura.baseChance", 0.2);
		role.addDefault("Passives.Wither_Aura.scaleChance", 0.025);
		role.addDefault("Passives.Wither_Aura.scaleStrength", 0.0375);
		role.addDefault("Passives.Wither_Particles.baseChance", 0.4);
		role.addDefault("Passives.Wither_Particles.scaleChance", 0.02);
		role.addDefault("Passives.Wither_Particles.scaleParticleCount", 0.125);

		role.addDefault("Actives.Blazing_Pillar.baseStrength", 3.5);
		role.addDefault("Actives.Blazing_Pillar.scaleStrength", 0.1);
		role.addDefault("Actives.Blazing_Pillar.scaleDuration", 1.0);
		role.addDefault("Actives.Fireball.baseStrength", 3.0);
		role.addDefault("Actives.Fireball.scaleStrength", 0.1);
		role.addDefault("Actives.Fireball.scaleDuration", 1.0);
		role.addDefault("Actives.Lightning_Strike.baseChance", -24.0);
		role.addDefault("Actives.Lightning_Strike.scaleChance", 1);
		role.addDefault("Actives.Lightning_Strike.scaleDelay", -0.25);
		role.addDefault("Actives.Lightning_Strike.scaleStrikeCount", 0.0625);
		role.addDefault("Actives.Magic_Blast.baseChance", -9.0);
		role.addDefault("Actives.Magic_Blast.scaleChance", 1);
		role.addDefault("Actives.Magic_Blast.baseMaxChance", 0.75);
		role.addDefault("Actives.Magic_Blast.baseStrength", 4.5);
		role.addDefault("Actives.Magic_Blast.scaleStrength", 0.0875);
		role.addDefault("Actives.Withering_Bolt.baseChance", -15.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	private static void addNecromancer()
	{
		Settings role = new Settings("Roles", "Necromancer.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 25.0);
		role.addDefault("General.scaleHealth", 1.0);
		role.addDefault("Stats.baseMeleeArmor", 50.0);
		role.addDefault("Stats.scaleMeleeArmor", 9.5);
		role.addDefault("Stats.baseMagicArmor", 150.0);
		role.addDefault("Stats.scaleMagicArmor", 11.5);
		role.addDefault("Stats.baseArrowArmor", 42.0);
		role.addDefault("Stats.scaleArrowArmor", 6.0);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 5.0);
		role.addDefault("Stats.baseAttackCooldown", 3.5);
		role.addDefault("Equipment.weaponItem", "redstone_torch_on");

		role.addDefault("Passives.Guardian_Angel.baseChance", 0.0);
		role.addDefault("Passives.Guardian_Angel.scaleChance", 0.04);
		role.addDefault("Passives.Mother.scaleRepeatDelay", -2.0);
		role.addDefault("Passives.Regeneration.baseChance", 1.4);
		role.addDefault("Passives.Regeneration.scaleChance", -0.05);
		role.addDefault("Passives.Vampirism.baseChance", -0.3);
		role.addDefault("Passives.Vampirism.scaleChance", 0.02);
		role.addDefault("Passives.Vampirism.baseMaxChance", 0.35);
		role.addDefault("Passives.Wither_Particles.baseChance", 0.4);
		role.addDefault("Passives.Wither_Particles.scaleChance", 0.02);
		role.addDefault("Passives.Wither_Particles.scaleParticleCount", 0.125);

		role.addDefault("Actives.Fracturing_Blast.baseChance", -17.0);
		role.addDefault("Actives.Fracturing_Blast.scaleChance", 1);
		role.addDefault("Actives.Fracturing_Blast.scaleSpeed", 0.015);
		role.addDefault("Actives.Lifesteal.scaleStrength", 0.0025);
		role.addDefault("Actives.Lifesteal.scaleCooldown", -0.075);
		role.addDefault("Actives.Lightning_Strike.baseChance", -20.0);
		role.addDefault("Actives.Lightning_Strike.scaleChance", 1);
		role.addDefault("Actives.Lightning_Strike.scaleStrikeCount", 0.0625);
		role.addDefault("Actives.Summon_Minion.types", new String[] { "skeleton", "skeleton", "zombie", "zombie", "wither_skeleton" });
		role.addDefault("Actives.Withering_Bolt.baseChance", -26.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1.0);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	private static void addArcher()
	{
		Settings role = new Settings("Roles", "Archer.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 28.0);
		role.addDefault("General.scaleHealth", 1.1);
		role.addDefault("Stats.baseMeleeArmor", 55.0);
		role.addDefault("Stats.scaleMeleeArmor", 10.0);
		role.addDefault("Stats.baseMagicArmor", 60.0);
		role.addDefault("Stats.scaleMagicArmor", 10.0);
		role.addDefault("Stats.baseArrowArmor", 75.0);
		role.addDefault("Stats.scaleArrowArmor", 11.0);
		role.addDefault("Stats.baseRangedArmor", 50.0);
		role.addDefault("Stats.scaleRangedArmor", 8.0);
		role.addDefault("Stats.baseAttackCooldown", 4.0);
		role.addDefault("Equipment.weaponItem", "bow");

		role.addDefault("Passives.Freezing_Aura.scaleRange", 0.1);
		role.addDefault("Passives.Freezing_Aura.scaleStrength", 0.03);
		role.addDefault("Passives.Guardian_Angel.baseChance", 0.0);
		role.addDefault("Passives.Guardian_Angel.scaleChance", 0.04);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseChance", 0.4);
		role.addDefault("Passives.Potion_Effect-Invisibility.scaleChance", 0.015);
		role.addDefault("Passives.Potion_Effect-Invisibility.effect", "invisibility");
		role.addDefault("Passives.Potion_Effect-Invisibility.repeats", -1);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseStartupDelay", 200);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseRepeatDelay", 600);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseDuration", 160);
		role.addDefault("Passives.Potion_Effect-Invisibility.scaleDuration", 2.5);
		role.addDefault("Passives.Regeneration.baseChance", 1.4);
		role.addDefault("Passives.Regeneration.scaleChance", -0.02);
		role.addDefault("Passives.Regeneration.baseStartupDelay", 150);
		role.addDefault("Passives.Vampirism.baseChance", -20.0);
		role.addDefault("Passives.Vampirism.scaleChance", 1.0);

		role.addDefault("Actives.Decoy.baseChance", -11.0);
		role.addDefault("Actives.Decoy.scaleChance", 1.0);
		role.addDefault("Actives.Decoy.baseMaxChance", 0.75);
		role.addDefault("Actives.Decoy.baseCount", 1.0);
		role.addDefault("Actives.Fracturing_Blast.scaleSpeed", 0.015);
		role.addDefault("Actives.Volley.scaleCount", 0.1);
		role.addDefault("Actives.Weaken.baseChance", -7.0);
		role.addDefault("Actives.Weaken.scaleChance", 1.0);
		role.addDefault("Actives.Weaken.scaleCount", 0.0375);
		role.addDefault("Actives.Withering_Bolt.baseChance", -16.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1.0);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	private static void addKnight()
	{
		Settings role = new Settings("Roles", "Knight.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 26.0);
		role.addDefault("General.scaleHealth", 1.1);
		role.addDefault("Stats.baseMeleeArmor", 65.0);
		role.addDefault("Stats.scaleMeleeArmor", 11.5);
		role.addDefault("Stats.baseMagicArmor", 70.0);
		role.addDefault("Stats.scaleMagicArmor", 9.5);
		role.addDefault("Stats.baseArrowArmor", 60.0);
		role.addDefault("Stats.scaleArrowArmor", 9.5);
		role.addDefault("Stats.baseRangedArmor", 50.0);
		role.addDefault("Stats.scaleRangedArmor", 8.5);
		role.addDefault("Stats.baseAttackCooldown", 4.0);
		role.addDefault("Equipment.weaponItem", "iron_sword");

		role.addDefault("Passives.Deteriorating_Aura.baseChance", -0.7);
		role.addDefault("Passives.Deteriorating_Aura.scaleChance", 0.04);
		role.addDefault("Passives.Explosion.baseChance", 0.4);
		role.addDefault("Passives.Explosion.scaleChance", 0.005);
		role.addDefault("Passives.Potion_Effect-Speed.effect", "speed");
		role.addDefault("Passives.Potion_Effect-Speed.repeats", -1);
		role.addDefault("Passives.Potion_Effect-Speed.baseStartupDelay", 200);
		role.addDefault("Passives.Potion_Effect-Speed.baseRepeatDelay", 600);
		role.addDefault("Passives.Potion_Effect-Speed.baseDuration", 160);
		role.addDefault("Passives.Potion_Effect-Speed.scaleDuration", 2.5);
		role.addDefault("Passives.Potion_Effect-Speed.scaleStrength", 0.03);
		role.addDefault("Passives.Regeneration.baseChance", 1.4);
		role.addDefault("Passives.Regeneration.scaleChance", -0.025);
		role.addDefault("Passives.Wither_Particles.baseChance", 0.6);
		role.addDefault("Passives.Wither_Particles.scaleChance", 0.025);
		role.addDefault("Passives.Wither_Particles.scaleParticleCount", 0.125);

		role.addDefault("Actives.Decoy.baseChance", -33.0);
		role.addDefault("Actives.Decoy.scaleChance", 1.0);
		role.addDefault("Actives.Decoy.baseMaxChance", 0.7);
		role.addDefault("Actives.Decoy.baseCount", 1.0);
		role.addDefault("Actives.Ground_Slam.scaleStrength", 0.075);
		role.addDefault("Actives.Ground_Slam.scaleKnockup", 0.003);
		role.addDefault("Actives.Lightning_Strike.baseChance", -17.0);
		role.addDefault("Actives.Lightning_Strike.scaleChance", 1);
		role.addDefault("Actives.Lightning_Strike.scaleDelay", -0.25);
		role.addDefault("Actives.Lightning_Strike.scaleStrikeCount", 0.0625);
		role.addDefault("Actives.Magic_Blast.baseChance", -9.0);
		role.addDefault("Actives.Magic_Blast.scaleChance", 1);
		role.addDefault("Actives.Magic_Blast.baseMaxChance", 0.75);
		role.addDefault("Actives.Magic_Blast.baseStrength", 4.5);
		role.addDefault("Actives.Magic_Blast.scaleStrength", 0.0875);
		role.addDefault("Actives.Lifesteal.baseStrength", 0.125);
		role.addDefault("Actives.Lifesteal.scaleStrength", 0.0025);

		role.save();
	}

	private static void addMatriarch()
	{
		Settings role = new Settings("Roles", "Matriarch.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 22.0);
		role.addDefault("General.scaleHealth", 0.9);
		role.addDefault("Stats.baseMeleeArmor", 60.0);
		role.addDefault("Stats.scaleMeleeArmor", 10.0);
		role.addDefault("Stats.baseMagicArmor", 70.0);
		role.addDefault("Stats.scaleMagicArmor", 11.0);
		role.addDefault("Stats.baseArrowArmor", 55.0);
		role.addDefault("Stats.scaleArrowArmor", 8.5);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 6.5);
		role.addDefault("Stats.baseAttackCooldown", 5.0);

		role.addDefault("Passives.Deteriorating_Aura.baseChance", -0.7);
		role.addDefault("Passives.Deteriorating_Aura.scaleChance", 0.03);
		role.addDefault("Passives.Exoskeleton.scaleStrength", 0.333);
		role.addDefault("Passives.Freezing_Aura.baseRange", 6.0);
		role.addDefault("Passives.Freezing_Aura.scaleRange", 0.1);
		role.addDefault("Passives.Freezing_Aura.scaleStrength", 0.03);
		role.addDefault("Passives.Mother.minGroupSize", 2);
		role.addDefault("Passives.Mother.maxGroupSize", 3);
		role.addDefault("Passives.Mother.scaleRepeatDelay", -2.0);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseChance", 0.4);
		role.addDefault("Passives.Potion_Effect-Invisibility.scaleChance", 0.015);
		role.addDefault("Passives.Potion_Effect-Invisibility.effect", "invisibility");
		role.addDefault("Passives.Potion_Effect-Invisibility.repeats", -1);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseStartupDelay", 200);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseRepeatDelay", 600);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseDuration", 160);
		role.addDefault("Passives.Potion_Effect-Invisibility.scaleDuration", 2.5);

		role.addDefault("Actives.Decoy.baseChance", -32.0);
		role.addDefault("Actives.Decoy.scaleChance", 1.0);
		role.addDefault("Actives.Decoy.baseMaxChance", 0.75);
		role.addDefault("Actives.Decoy.baseCount", 1.0);
		role.addDefault("Actives.Ground_Slam.scaleStrength", 0.075);
		role.addDefault("Actives.Ground_Slam.baseKnockup", 0.8);
		role.addDefault("Actives.Summon_Minion.baseChance", -13);
		role.addDefault("Actives.Summon_Minion.scaleChance", 1.0);
		role.addDefault("Actives.Summon_Minion.baseCount", 1.5);
		role.addDefault("Actives.Summon_Minion.scaleCount", 0.025);
		role.addDefault("Actives.Summon_Minion.types", new String[] { "spider", "spider", "cave_spider" });
		role.addDefault("Actives.Toxic_Spit.baseChance", -19.0);
		role.addDefault("Actives.Toxic_Spit.scaleChance", 1.0);
		role.addDefault("Actives.Toxic_Spit.scaleDuration", 0.5);
		role.addDefault("Actives.Toxic_Spit.scaleCount", 0.03);
		role.addDefault("Actives.Webbing.scaleCount", 0.04);
		role.addDefault("Actives.Webbing.scaleRadius", 0.03);

		role.save();
	}

	private static void addFunnelWeb()
	{
		Settings role = new Settings("Roles", "Funnel-Web.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 18.0);
		role.addDefault("General.scaleHealth", 1.0);
		role.addDefault("Stats.baseMeleeArmor", 60.0);
		role.addDefault("Stats.scaleMeleeArmor", 11.0);
		role.addDefault("Stats.baseMagicArmor", 70.0);
		role.addDefault("Stats.scaleMagicArmor", 12.0);
		role.addDefault("Stats.baseArrowArmor", 55.0);
		role.addDefault("Stats.scaleArrowArmor", 9.5);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 7.0);
		role.addDefault("Stats.baseAttackCooldown", 4.0);

		role.addDefault("Passives.Exoskeleton.scaleStrength", 0.5);
		role.addDefault("Passives.Freezing_Aura.baseRange", 6.0);
		role.addDefault("Passives.Freezing_Aura.scaleRange", 0.1);
		role.addDefault("Passives.Freezing_Aura.scaleStrength", 0.03);
		role.addDefault("Passives.Poison_Mist.baseChance", 0.25);
		role.addDefault("Passives.Poison_Mist.scaleChance", 0.03);
		role.addDefault("Passives.Poison_Mist.scaleStrength", 0.0375);
		role.addDefault("Passives.Potion_Effect-Jump.effect", "jump");
		role.addDefault("Passives.Potion_Effect-Jump.baseDuration", 100000);
		role.addDefault("Passives.Potion_Effect-Jump.scaleStrength", 0.03);
		role.addDefault("Passives.Vampirism.baseChance", -0.5);
		role.addDefault("Passives.Vampirism.scaleChance", 0.025);

		role.addDefault("Actives.Lifesteal.baseChance", -14.0);
		role.addDefault("Actives.Lifesteal.scaleChance", 1.0);
		role.addDefault("Actives.Lifesteal.scaleStrength", 0.0025);
		role.addDefault("Actives.Toxic_Spit.baseCooldown", 22.0);
		role.addDefault("Actives.Toxic_Spit.scaleDuration", 0.75);
		role.addDefault("Actives.Toxic_Spit.scaleCount", 0.04);
		role.addDefault("Actives.Weaken.baseChance", -7.0);
		role.addDefault("Actives.Weaken.scaleChance", 1.0);
		role.addDefault("Actives.Weaken.scaleCount", 0.0375);
		role.addDefault("Actives.Webbing.scaleCount", 0.04);
		role.addDefault("Actives.Webbing.scaleRadius", 0.03);
		role.addDefault("Actives.Withering_Bolt.baseChance", -19.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	private static void addPyre()
	{
		Settings role = new Settings("Roles", "Pyre.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 20.0);
		role.addDefault("General.scaleHealth", 1.0);
		role.addDefault("Stats.baseMeleeArmor", 45.0);
		role.addDefault("Stats.scaleMeleeArmor", 7.0);
		role.addDefault("Stats.baseMagicArmor", 40.0);
		role.addDefault("Stats.scaleMagicArmor", 6.0);
		role.addDefault("Stats.baseArrowArmor", 75.0);
		role.addDefault("Stats.scaleArrowArmor", 11.5);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 7.0);
		role.addDefault("Stats.baseAttackCooldown", 4.0);

		role.addDefault("Passives.Exoskeleton.scaleStrength", 0.5);
		role.addDefault("Passives.Fire_Aura.scaleRange", 0.125);
		role.addDefault("Passives.Mount.baseChance", 0.2);
		role.addDefault("Passives.Mount.type", "skeleton");
		role.addDefault("Passives.Mount.role", "Pyre_Jockey");
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.effect", "fire_resistance");
		role.addDefault("Passives.Potion_Effect-Fire_Resistance.baseDuration", 100000);

		role.addDefault("Actives.Blazing_Pillar.baseChance", -15.0);
		role.addDefault("Actives.Blazing_Pillar.scaleChance", 1.0);
		role.addDefault("Actives.Blazing_Pillar.baseStrength", 3.0);
		role.addDefault("Actives.Blazing_Pillar.scaleStrength", 0.1);
		role.addDefault("Actives.Blazing_Pillar.scaleDuration", 1.0);
		role.addDefault("Actives.Fireball.baseChance", -7.0);
		role.addDefault("Actives.Fireball.scaleChance", 1.0);
		role.addDefault("Actives.Fireball.baseStrength", 2.75);
		role.addDefault("Actives.Fireball.scaleStrength", 0.1);
		role.addDefault("Actives.Fireball.scaleDuration", 1.0);
		role.addDefault("Actives.Fire_Burst.baseStrength", 5.0);
		role.addDefault("Actives.Fire_Burst.scaleStrength", 0.1);
		role.addDefault("Actives.Toxic_Spit.baseChance", -22.0);
		role.addDefault("Actives.Toxic_Spit.scaleChance", 1.0);
		role.addDefault("Actives.Toxic_Spit.scaleDuration", 0.5);
		role.addDefault("Actives.Toxic_Spit.scaleCount", 0.03);
		role.addDefault("Actives.Webbing.scaleCount", 0.04);
		role.addDefault("Actives.Webbing.scaleRadius", 0.03);

		role.save();
	}

	private static void addPyreJockey()
	{
		Settings role = new Settings("Roles", "Pyre_Jockey.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Archer");
		role.addDefault("General.baseHealth", 20.0);
		role.addDefault("General.scaleHealth", 0.75);
		role.addDefault("Stats.baseMeleeArmor", 40.0);
		role.addDefault("Stats.scaleMeleeArmor", 6.0);
		role.addDefault("Stats.baseMagicArmor", 40.0);
		role.addDefault("Stats.scaleMagicArmor", 6.0);
		role.addDefault("Stats.baseArrowArmor", 50.0);
		role.addDefault("Stats.scaleArrowArmor", 8.0);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 6.0);

		role.save();
	}

	private static void addPhantom()
	{
		Settings role = new Settings("Roles", "Phantom.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 44.0);
		role.addDefault("General.scaleHealth", 1.0);
		role.addDefault("Stats.baseMeleeArmor", 55.0);
		role.addDefault("Stats.scaleMeleeArmor", 11.5);
		role.addDefault("Stats.baseMagicArmor", 50.0);
		role.addDefault("Stats.scaleMagicArmor", 11.0);
		role.addDefault("Stats.baseArrowArmor", 30.0);
		role.addDefault("Stats.scaleArrowArmor", 4.0);
		role.addDefault("Stats.baseRangedArmor", 40.0);
		role.addDefault("Stats.scaleRangedArmor", 7.0);
		role.addDefault("Stats.baseAttackCooldown", 2.0);

		role.addDefault("Passives.Exoskeleton.scaleStrength", 0.5);
		role.addDefault("Passives.Guardian_Angel.baseChance", 0.0);
		role.addDefault("Passives.Guardian_Angel.scaleChance", 0.04);
		role.addDefault("Passives.Guardian_Angel.baseMaxChance", 0.6);
		role.addDefault("Passives.Potion_Effect-Invisibility.effect", "invisibility");
		role.addDefault("Passives.Potion_Effect-Invisibility.repeats", -1);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseStartupDelay", 200);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseRepeatDelay", 1200);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseDuration", 240);
		role.addDefault("Passives.Potion_Effect-Invisibility.scaleDuration", 4.0);
		role.addDefault("Passives.Potion_Effect-Speed.effect", "speed");
		role.addDefault("Passives.Potion_Effect-Speed.baseStartupDelay", 100000);
		role.addDefault("Passives.Wither_Aura.baseChance", 0.75);
		role.addDefault("Passives.Wither_Aura.scaleChance", 0.025);
		role.addDefault("Passives.Wither_Aura.scaleStrength", 0.0375);

		role.addDefault("Actives.Decoy.baseChance", -22.0);
		role.addDefault("Actives.Decoy.scaleChance", 1.0);
		role.addDefault("Actives.Decoy.baseMaxChance", 0.75);
		role.addDefault("Actives.Decoy.baseCount", 1.0);
		role.addDefault("Actives.Kidnap.baseChance", -13.0);
		role.addDefault("Actives.Kidnap.scaleChance", 1.0);
		role.addDefault("Actives.Teleport.baseCooldown", 26.0);
		role.addDefault("Actives.Teleport.scaleCooldown", -0.125);
		role.addDefault("Actives.Weaken.scaleCount", 0.065);
		role.addDefault("Actives.Withering_Bolt.baseChance", -7.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1.0);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	private static void addVoidling()
	{
		Settings role = new Settings("Roles", "Voidling.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 40.0);
		role.addDefault("General.scaleHealth", 1.0);
		role.addDefault("Stats.baseMeleeArmor", 80.0);
		role.addDefault("Stats.scaleMeleeArmor", 22.0);
		role.addDefault("Stats.baseMagicArmor", 30.0);
		role.addDefault("Stats.scaleMagicArmor", 7.0);
		role.addDefault("Stats.baseArrowArmor", 30.0);
		role.addDefault("Stats.scaleArrowArmor", 4.0);
		role.addDefault("Stats.baseRangedArmor", 35.0);
		role.addDefault("Stats.scaleRangedArmor", 6.0);
		role.addDefault("Stats.baseAttackCooldown", 2.0);

		role.addDefault("Passives.Freezing_Aura.scaleRange", 0.1);
		role.addDefault("Passives.Freezing_Aura.scaleStrength", 0.03);
		role.addDefault("Passives.Mother.baseChance", -28.0);
		role.addDefault("Passives.Mother.scaleChance", 1.0);
		role.addDefault("Passives.Mother.baseMaxChance", 0.4);
		role.addDefault("Passives.Potion_Effect-Invisibility.effect", "invisibility");
		role.addDefault("Passives.Potion_Effect-Invisibility.repeats", -1);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseStartupDelay", 200);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseRepeatDelay", 1200);
		role.addDefault("Passives.Potion_Effect-Invisibility.baseDuration", 240);
		role.addDefault("Passives.Potion_Effect-Invisibility.scaleDuration", 4.0);
		role.addDefault("Passives.Vampirism.baseChance", -0.5);
		role.addDefault("Passives.Vampirism.scaleChance", 0.025);
		role.addDefault("Passives.Wither_Particles.baseChance", 0.6);
		role.addDefault("Passives.Wither_Particles.scaleChance", 0.025);
		role.addDefault("Passives.Wither_Particles.scaleParticleCount", 0.125);
		role.addDefault("Passives.Wither_Particles.scaleRange", 0.075);

		role.addDefault("Actives.Fracturing_Blast.baseChance", -15.0);
		role.addDefault("Actives.Fracturing_Blast.scaleChance", 1);
		role.addDefault("Actives.Fracturing_Blast.scaleSpeed", 0.015);
		role.addDefault("Actives.Lightning_Strike.baseChance", -9.0);
		role.addDefault("Actives.Lightning_Strike.scaleChance", 1);
		role.addDefault("Actives.Lightning_Strike.scaleStrikeCount", 0.0625);
		role.addDefault("Actives.Magic_Blast.baseStrength", 4.5);
		role.addDefault("Actives.Magic_Blast.scaleStrength", 0.0875);
		role.addDefault("Actives.Magic_Mirror.baseChance", -32);
		role.addDefault("Actives.Magic_Mirror.scaleChance", 1);
		role.addDefault("Actives.Magic_Mirror.baseMaxChance", 0.7);
		role.addDefault("Actives.Magic_Mirror.scaleStrength", 0.05);
		role.addDefault("Actives.Summon_Minion.types", new String[] { "angry_wolf", "cave_spider", "silverfish", "skeleton", "witch", "wither_skeleton" });

		role.save();
	}

	private static void addSpellweaver()
	{
		Settings role = new Settings("Roles", "Spellweaver.yml");

		role.addDefault("enable", true);
		role.addDefault("General.parentRole", "Common");
		role.addDefault("General.baseHealth", 26.0);
		role.addDefault("General.scaleHealth", 0.9);
		role.addDefault("Stats.baseMeleeArmor", 60.0);
		role.addDefault("Stats.scaleMeleeArmor", 10.0);
		role.addDefault("Stats.baseMagicArmor", 110.0);
		role.addDefault("Stats.scaleMagicArmor", 13.0);
		role.addDefault("Stats.baseArrowArmor", 50.0);
		role.addDefault("Stats.scaleArrowArmor", 8.0);
		role.addDefault("Stats.baseRangedArmor", 45.0);
		role.addDefault("Stats.scaleRangedArmor", 8.0);
		role.addDefault("Stats.baseAttackCooldown", 2.0);

		role.addDefault("Passives.Healer.scaleHealthMultiplier", 0.01);

		role.addDefault("Actives.Blazing_Pillar.baseChance", -26.0);
		role.addDefault("Actives.Blazing_Pillar.scaleChance", 1.0);
		role.addDefault("Actives.Blazing_Pillar.baseStrength", 3.0);
		role.addDefault("Actives.Blazing_Pillar.scaleStrength", 0.1);
		role.addDefault("Actives.Blazing_Pillar.scaleDuration", 1.0);
		role.addDefault("Actives.Blindfold.scaleDuration", 1.0);
		role.addDefault("Actives.Fireball.baseChance", -21.0);
		role.addDefault("Actives.Fireball.scaleChance", 1.0);
		role.addDefault("Actives.Fireball.baseStrength", 2.75);
		role.addDefault("Actives.Fireball.scaleStrength", 0.1);
		role.addDefault("Actives.Fireball.scaleDuration", 1.0);
		role.addDefault("Actives.Fire_Burst.baseStrength", 5.0);
		role.addDefault("Actives.Fire_Burst.scaleStrength", 0.1);
		role.addDefault("Actives.Fracturing_Blast.baseChance", -15.0);
		role.addDefault("Actives.Fracturing_Blast.scaleChance", 1);
		role.addDefault("Actives.Fracturing_Blast.scaleSpeed", 0.015);
		role.addDefault("Actives.Lightning_Strike.baseChance", -9.0);
		role.addDefault("Actives.Lightning_Strike.scaleChance", 1);
		role.addDefault("Actives.Lightning_Strike.scaleStrikeCount", 0.0625);
		role.addDefault("Actives.Magic_Blast.baseStrength", 4.5);
		role.addDefault("Actives.Magic_Blast.scaleStrength", 0.0875);
		role.addDefault("Actives.Summon_Minion.baseChance", 0.15);
		role.addDefault("Actives.Summon_Minion.scaleChance", 0.025);
		role.addDefault("Actives.Summon_Minion.types", new String[] { "cave_spider", "witch", "wither_skeleton", "wither_skeleton" });
		role.addDefault("Actives.Withering_Bolt.baseChance", -7.0);
		role.addDefault("Actives.Withering_Bolt.scaleChance", 1.0);
		role.addDefault("Actives.Withering_Bolt.scaleDuration", 1.0);

		role.save();
	}

	// //////////////////////////////////////////////////////////////////////////////////

	private static void addMal()
	{
		Settings role = new Settings("Roles", "Mal.yml");

		role.addDefault("enable", true);
		role.addDefault("General.level", "max:2.5");
		role.addDefault("General.customName", "Mal");
		role.addDefault("General.baseHealth", 150.0);
		role.addDefault("General.scaleHealth", 1.0);
		role.addDefault("Stats.baseMeleeArmor", 62.5);
		role.addDefault("Stats.scaleMeleeArmor", 10.7);
		role.addDefault("Stats.baseMagicArmor", 75.0);
		role.addDefault("Stats.scaleMagicArmor", 10.2);
		role.addDefault("Stats.baseArrowArmor", 55.0);
		role.addDefault("Stats.scaleArrowArmor", 9.5);
		role.addDefault("Stats.baseRangedArmor", 35.0);
		role.addDefault("Stats.scaleRangedArmor", 6.2);

		role.save();
	}
}
