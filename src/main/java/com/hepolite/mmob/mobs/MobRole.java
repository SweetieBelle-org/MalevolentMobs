package com.hepolite.mmob.mobs;

/**
 * Stores information about the mob, such as name, strength, defense, abilities, attacks and everything else that the mob might have
 */
public class MobRole
{
	// Control variables
	private String name = "Unnamed Role";
	public String mobName = "Unnamed Mob";
	
	public float level = 0.0f;
	public boolean isDecoy = false;

	// Rewards for killing the mob
	public int vanillaExperience = 0;
	public double skillAPIExperience = 0.0;
	public boolean shouldDropLoot = true;
	public String lootDefinitionFile = "";

	// Base stats
	public float baseMeleeArmor = 0.0f;
	public float scaleMeleeArmor = 0.0f;
	public float baseMagicArmor = 0.0f;
	public float scaleMagicArmor = 0.0f;
	public float baseArrowArmor = 0.0f;
	public float scaleArrowArmor = 0.0f;
	public float baseRangedArmor = 0.0f;
	public float scaleRangedArmor = 0.0f;
	public float flatArmorPenetrated = 0.0f;		// Armor penetrated for one attack
	public float percentArmorPenetrated = 0.0f;		// Armor penetrated for one attack

	public float attackCooldownTime = 0.0f;		// Time that must be between attacks that aren't occurring simultaneously, measured in seconds

	/* Initialization */
	public MobRole(String name)
	{
		this.name = name;
	}

	/** Returns the name of the role */
	public String getName()
	{
		return name;
	}
}
