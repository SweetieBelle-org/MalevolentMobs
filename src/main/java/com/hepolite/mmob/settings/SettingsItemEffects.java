package com.hepolite.mmob.settings;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;

public class SettingsItemEffects
{
	// Control variables
	private final static HashMap<String, Settings> effects = new HashMap<String, Settings>();

	/** Initialize all the effects settings; called from MMobSettings */
	public static void initialize()
	{
		// Clear old data
		effects.clear();

		// Every first run, all default item effects must be defined. Subsequent runs must define new effects
		File directory = new File(MMobPlugin.getInstance().getDataFolder() + "/Item Effects");
		if (!directory.exists())
			directory.mkdir();
		addDefaultItemEffects();

		// Discover all abilities in the itemEffect directory
		File[] files = directory.listFiles();
		for (File file : files)
			effects.put(file.getName().split("\\.")[0], new Settings("Item Effects", file.getName()));
	}

	/** Returns the configuration for the given item effect */
	public static Settings getConfig(String itemEffect)
	{
		Settings setting = effects.get(itemEffect);
		if (setting == null)
		{
			Log.log("Couldn't find config for item effect '" + itemEffect + "'!", Level.WARNING);
			return null;
		}
		return setting;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Adds all the default abilities to the system */
	private static void addDefaultItemEffects()
	{
		addArmorShredder();
		addBulwark();
		addCharged();
		addDuplicator();
		addEarthmover();
		addEntropyLimit();
		addExplosiveArrows();
		addFiery();
		addFireward();
		addFireworks();
		addFragile();
		addFrost();
		addGrowth();
		addHarvest();
		addHeated();
		addHungry();
		addLacerate();
		addLevity();
		addMagicMirror();
		addMagicShield();
		addModifier();
		addPiercing();
		addPoison();
		addPowerline();
		addRelic();
		addRepair();
		addRunicShield();
		addSaturation();
		addSharpness();
		addShockAbsorber();
		addShroud();
		addThaumicBolt();
		addTimber();
		addTreasure();
		addTunneler();
		addUnmelting();
		addWither();
	}

	private static void addArmorShredder()
	{
		Settings itemEffect = new Settings("Item Effects", "Armor_Shredder.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerPercent", 0.1);
		itemEffect.addDefault("durabilityCostPerPoint", 0.0125);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25);

		itemEffect.addDefault("lore.good", new String[] { "There could be no doubt; #name would strike down the Malevolent creatures.", "The protective layers of the Malevolent creatures would be shredded by #name.#resetname", "Burning with desire, #name would bypass the protective layers of the Malevolent creatures.", "#name would partially ignore the armor of the Malevolent creatures.", "The Malevolent creatures' protections would not stop #name from harming them." });
		itemEffect.addDefault("lore.neutral", new String[] { "Conflicted, however, #name would only partly aid its owner.", "Curiously, #name was torn, unable to decide whether to aid the new owner.", "Torn, #name was not able to take a side." });
		itemEffect.addDefault("lore.bad", new String[] { "And so it was decided; #name could not bring itself to harm the Malevolent creatures.", "Rejecting the new owner, however, #name opposed harm to the Malevolent creatures.", "The Malevolent creatures could not be harmed by #name, however.", "Opposed to the new owner, however, #name would not lend itself against the Malevolent creatures." });

		itemEffect.save();
	}

	private static void addBulwark()
	{
		Settings itemEffect = new Settings("Item Effects", "Bulwark.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore.good", new String[] { "The strength of #name would not yield against mere physical attacks.", "The endurance of #name would prevent the wearer from being harmed by physical attacks.", "Not even swords, bows or punches could harm the wearer of #name.", "#name would prevent the wearer from coming to physical harm.", "#resetnamePhysical attacks would not bypass #name; #name would protect its wearer." });
		itemEffect.addDefault("lore.bad", new String[] { "But physical strikes would be magnified by #name.", "Swords, arrows, even punches, however, would be amplified by #name.", "Cursed however, #name would bring physical harm to its wearer.", "The wearer of #name, however, would be tormented by the curse, amplified physical harm.#resetname" });

		itemEffect.save();
	}

	private static void addCharged()
	{
		Settings itemEffect = new Settings("Item Effects", "Charged.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 0.5);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.1);

		itemEffect.addDefault("lore", new String[] { "Currents coursed around within #name, striking down those who opposed the carrier.", "Shimmering, #name would strike down those deemed unworthy by the carrier.", "With energy coursing inside #name, #name was ready to strike down any opposition.", "Striking once was not enough for #name; the energy stored inside #name spilled over to nearby souls." });

		itemEffect.save();
	}

	private static void addDuplicator()
	{
		Settings itemEffect = new Settings("Item Effects", "Duplicator.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("blockValues", new String[] { /* NATURAL */"clay:0.6", "cobblestone:0.1", "dirt:0.1", "grass:2.0", "hard_clay:0.7", "log:0.4", "log_2:0.4", "mycel:2.0", "obsidian:1.2", "sand:0.1", "sandstone:0.4", "smooth_brick:0.3", "snow_block:0.5", "stained_clay:0.7", "stone:0.2", /* VEGETATION */"dead_bush:1.0", "double_plant:3.5", "long_grass:0.2", "red_rose:1.5", "sapling:4.5", "yellow_flower:1.5", "water_lily:4.0", /* MANUFACTURED */"glass:0.3", "stained_glass:0.3", "thin_glass:0.1", "stained_glass_pane:0.1" });

		itemEffect.addDefault("lore", new String[] { "Matter and energy, merely the same but in different forms, realized by #name.", "Transmuting ambient energy to matter, unbounded power, eternal substance, #name would provide it all.", "And so it was. #name could transmute raw energy to physical substances.", "Unprecedented, #name was capable of the impossible - transforming ambient energy to physical matter." });

		itemEffect.save();
	}

	private static void addEarthmover()
	{
		Settings itemEffect = new Settings("Item Effects", "Earthmover.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerBlock", 2.25);

		itemEffect.addDefault("lore", new String[] { "Soil, sand, snow, #name would effortlessly dig through such trivialities.", "Hills, dunes, avalances, it would not matter - #name would prove effective.", "To #name, mere dirt, snow, sand, such materials were no more than trivialities.#resetname", "#resetnameThe tasks of moving dirt, snow, sand, previously a tedious excercise, #name would do such with ease.", "#resetnameEven the sturdiest patch of dirt was powerless against #name." });

		itemEffect.save();
	}

	private static void addEntropyLimit()
	{
		Settings itemEffect = new Settings("Item Effects", "Entropy_Limit.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore.good", new String[] { "But #name refused to let its wearer wither away; #name would sacrifice itself.", "The corrosive magic would be absorbed by #name, protecting the wearer.", "#name would wither away itself, before allowing the wearer to be harmed.", "The magical protections of #name would prevent the wearer from withering away." });
		itemEffect.addDefault("lore.bad", new String[] { "#name was flawed, however; it would corrode away the wearer.", "But the withering energies would be amplified by #name.", "And so it was, the wearer would wither away due to #name." });

		itemEffect.save();
	}

	private static void addExplosiveArrows()
	{
		Settings itemEffect = new Settings("Item Effects", "Explosive_Arrows.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUse", 5.0);

		itemEffect.addDefault("lore", new String[] { "Through the magical enhancements applied to #name, the arrows would react strongly to organic matter.", "Inherent energy within #name would be unleashed with explosive force.", "Through primal energy, arrows fired by #name would explode on impact.", "Arcane magic, channeled through #name, ensured that arrows fired by #name proved fatal." });

		itemEffect.save();
	}

	private static void addFiery()
	{
		Settings itemEffect = new Settings("Item Effects", "Fiery.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUse", 2.25);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25f);

		itemEffect.addDefault("lore", new String[] { "Blazing, #name would burn every foe of the wearer.", "The core of #name was burning with fury.", "There was an inferno raging within #name.", "The heat radiated by #name knew no bounds - it would scorch its foes." });

		itemEffect.save();
	}

	private static void addFireward()
	{
		Settings itemEffect = new Settings("Item Effects", "Fireward.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore.good", new String[] { "The intricacy of #name protected the wearer from flames.", "The inherent magic of #name would stop fire from scorching the wearer.", "Not even fire would bypass the protections of #name.", "Determined, #name would stand against even the hottest flame." });
		itemEffect.addDefault("lore.bad", new String[] { "However, #name would burn itself to the wearer.", "Yet fires would strongly scorch the wearer of #name.", "#name would, however, burn, harming the wearer." });

		itemEffect.save();
	}

	private static void addFireworks()
	{
		Settings itemEffect = new Settings("Item Effects", "Fireworks.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("small.costPerRocket", 0.8);
		itemEffect.addDefault("small.radius", 4.0);
		itemEffect.addDefault("small.velocity", 1.75);
		itemEffect.addDefault("small.rocketPower", 1);
		itemEffect.addDefault("large.costPerRocket", 2.7);
		itemEffect.addDefault("large.radius", 8.5);
		itemEffect.addDefault("large.velocity", 1.4);
		itemEffect.addDefault("large.rocketPower", 1);

		itemEffect.addDefault("lore", new String[] { "If there was one thing #name was good at, it was lighting up the sky at night.", "Long ago, a gray wizard crafted a magic that #name absorbed; the sky could be lit up in many colors.", "Impressive lights and colors, that #name could create.", "Through ancient magic, #name could light up the sky." });

		itemEffect.save();
	}

	private static void addFragile()
	{
		Settings itemEffect = new Settings("Item Effects", "Fragile.yml");
		itemEffect.addDefault("enable", true);

		itemEffect.addDefault("lore", new String[] { "Alas, the fragile nature of #name would be its demise.", "But #name would fragment through use, eventually shattering.", "With every use, however, #name would chip away.", "Alas, #name was not meant to last forever, and would eventually shatter." });

		itemEffect.save();
	}

	private static void addFrost()
	{
		Settings itemEffect = new Settings("Item Effects", "Frost.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUse", 3.25);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25f);

		itemEffect.addDefault("lore.Base", new String[] { "Frozen, #name would cause frostburns on the foes.", "Deep within #name lied an immense cold, radiating outwards.", "In its core, #name knew no warmth.", "Not even a smidge of heat was radiated by #name - only ice and coldness." });
		itemEffect.addDefault("lore.Damage", new String[] { "Such cold would prove harmful.", "The freezing touch would leave its mark.", "The effect by #name being unbearable, foes would be harmed." });

		itemEffect.save();
	}

	private static void addGrowth()
	{
		Settings itemEffect = new Settings("Item Effects", "Growth.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerCrop", 1.0);
		itemEffect.addDefault("durabilityCostPerTree", 25.0);
		itemEffect.addDefault("durabilityCostPerGrass", 8.0);
		itemEffect.addDefault("durabilityCostPerFlower", 2.5);
		itemEffect.addDefault("durabilityCostPerCactus", 5.0);

		itemEffect.addDefault("lore", new String[] { "#name would resonate with vegetation, aiding the growth rate.", "#name was in tune with nature, speeding up the growth of vegetation.", "Natural forces emitted from #name, improving the growth of vegetation.", "Intrinsic to #name, vegetation would grow faster in the prescense of #name." });

		itemEffect.save();
	}

	private static void addHarvest()
	{
		Settings itemEffect = new Settings("Item Effects", "Harvest.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerHarvest", 1.5);
		itemEffect.addDefault("durabilityCostPerTill", 2.5);

		itemEffect.addDefault("lore", new String[] { "Tilling soil and harvesting crops, #name would make such tasks mundane.", "Through magic, #name would harvest ripe crops.", "Inherent magic from the core of #name would aid with harvesting crops." });

		itemEffect.save();
	}

	private static void addHeated()
	{
		Settings itemEffect = new Settings("Item Effects", "Heated.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUse", 1.5);

		itemEffect.addDefault("lore", new String[] { "Heat stored in #name would infuse with what #name harvested.", "The inner flame of #name would affect what #name harvested.", "Heated, #name would have interesting effects when used.", "While used, #name would heat the surroundings, with interesting interactions." });

		itemEffect.save();
	}

	private static void addHungry()
	{
		Settings itemEffect = new Settings("Item Effects", "Hungry.yml");
		itemEffect.addDefault("enable", true);

		itemEffect.addDefault("lore", new String[] { "But #name had an unsavory hunger.", "Alas, #name would devour what #name could.", "The ravenous nature of #name, however, was inconvenient.", "#name was hollow, however, seeking to be filled with whatever #name could grasp." });

		itemEffect.save();
	}

	private static void addLacerate()
	{
		Settings itemEffect = new Settings("Item Effects", "Lacerate.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 0.5);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25);

		itemEffect.addDefault("lore", new String[] { "Wounds caused by #name opened more, harming the victim.", "Harmful, #name would carve deep into flesh.", "#name would bring deep wounds to its victims.", "Any foe struck by #name would be deeply wounded." });

		itemEffect.save();
	}

	private static void addLevity()
	{
		Settings itemEffect = new Settings("Item Effects", "Levity.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUse", 0.9);
		itemEffect.addDefault("durabilityCostPerPercent", 0.035);

		itemEffect.addDefault("lore.good", new String[] { "The strength of #name allowed arrows to strike true.", "Arrows fired by #name would fly straight and true.", "Overall, #name would aid its owner in striking from afar.", "Swiftly, #name would allow the owner to hit targets from afar." });
		itemEffect.addDefault("lore.neutral", new String[] { "Arrows from #name would defy gravity, slowly but surely.", "While slow, #name would send arrows straight.", "And thus, #name would allow the owner to hit targets from afar, by defying gravity." });
		itemEffect.addDefault("lore.bad", new String[] { "But #name opposed the owner.", "But the age of #name was an inconvenience.", "A slack string, however, prevented #name from reaching its full potential." });

		itemEffect.save();
	}

	private static void addMagicMirror()
	{
		Settings itemEffect = new Settings("Item Effects", "Magic_Mirror.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore", new String[] { "The thaumostatic construct would allow #name to mirror magic forces towards the caster.", "Magical energies would be mirrored by #name, harming the original caster.", "#name would mirror magical spells, harming the origin of the spells.", "The magical properties of #name ensured that nothing could attack the wearer without consequences." });

		itemEffect.save();
	}

	private static void addMagicShield()
	{
		Settings itemEffect = new Settings("Item Effects", "Magic_Shield.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore.good", new String[] { "Magical spells, #name would shield the wearer from such feeble attacks.", "Magical energies would not be allowed to harm the wearer.#resetname", "The wearer would be protected by a magic shield.", "#resetnameFoes would be unable to harm the wearer with magic; #name would ensure that." });
		itemEffect.addDefault("lore.bad", new String[] { "Alas, #name could only resonate with spells, amplifying their impact", "But magic attacks would be amplified by #name.", "Despite everything, #name would resonate with magic, harming the wearer." });

		itemEffect.save();
	}

	private static void addModifier()
	{
		Settings itemEffect = new Settings("Item Effects", "Modifier.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.save();
	}

	private static void addPiercing()
	{
		Settings itemEffect = new Settings("Item Effects", "Piercing.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.5);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25);

		itemEffect.addDefault("lore", new String[] { "The armor of the foes would not matter to #name.", "No matter the protection worn, #name would carve through them all.", "Physical and magical barriers, none of that mattered to #name." });

		itemEffect.save();
	}

	private static void addPoison()
	{
		Settings itemEffect = new Settings("Item Effects", "Poison.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUsePerLevel", 3.5);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25);

		itemEffect.addDefault("lore", new String[] { "The sting by #name would prove toxic.", "Unknown to others, #name was used along with poison.", "Assassins had long tried to get a hold of #name for its poisonous nature." });

		itemEffect.save();
	}

	private static void addPowerline()
	{
		Settings itemEffect = new Settings("Item Effects", "Powerline.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("lightningBoltDistanceCheck", 15.0);
		itemEffect.addDefault("lightningBoltCooldown", 10);
		itemEffect.addDefault("lightningStrikeDistanceCheck", 50);
		itemEffect.addDefault("lightningStrikeCooldown", 70);

		itemEffect.addDefault("lore", new String[] { "Crackling with energy, #name was merely waiting for the moments to unleash its powers.", "The potential stored within #name was merely dorment, waiting to be channeled.", "Current surged, arching from the surface of #name, waiting to be unleashed.", "Softly, #name was humming strange noises, waiting for its powers to be unleashed." });

		itemEffect.save();
	}

	private static void addRelic()
	{
		Settings itemEffect = new Settings("Item Effects", "Relic.yml");
		itemEffect.addDefault("enable", true);

		itemEffect.addDefault("lore.Base", new String[] { "Curiously, #name contained a set of ancient and forgotten artefacts.", "But #name was not empty - it contained long lost relics.", "Adventurers had long been looking for #name, trying to get a hold of the ancient artefacts it contained." });
		itemEffect.addDefault("lore.Negative", new String[] { "But the contents were cursed.", "There was a cursed placed on #name, however.", "But #name was not without flaws; negative energies had been absorbed by the contents." });

		itemEffect.save();
	}

	private static void addRepair()
	{
		Settings itemEffect = new Settings("Item Effects", "Repair.yml");
		itemEffect.addDefault("enable", true);

		itemEffect.addDefault("lore.good", new String[] { "Strangely, #name seemed unable to be broken; any cuts would quickly mend themselves.", "Magic would prevent #name from breaking, #name would mend itself.", "#name refused to break, however. #name would keep mending itself.", "While carried, #name could not be broken - #name would mend any damage quickly.", "Even during the harshest of uses, #name would not yield, not fail, never break.", "Despite how #name was used, its inherent magic would quickly repair #name.",
				"Against everything, elaborated enchantments allowed #name to resist decaying when used." });
		itemEffect.addDefault("lore.bad", new String[] { "Alas, the fragile nature of #name would soon prove fatal.", "But #name would decay into nothingness with time.", "Alas, #name would crack and wear down, eventually failing the carrier.", "But #name was fatally flawed - #name would wither away." });

		itemEffect.save();
	}

	private static void addRunicShield()
	{
		Settings itemEffect = new Settings("Item Effects", "Runic_Shield.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore", new String[] { "Enchanted, #name would absorb forces that would otherwise harm the wearer.", "Magic and physical forces could not bypass #name; such forces would be absorbed by #name.", "Strengthened through magical protection, #name would stand strong against physical and magical attacks.", "While resting on the wearer, #name would protect the wearer from external harm.", "Forming a shielding layer, #name would protect its wearer." });

		itemEffect.save();
	}

	private static void addSaturation()
	{
		Settings itemEffect = new Settings("Item Effects", "Saturation.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerPoint", 0.3);

		itemEffect.addDefault("lore.good", new String[] { "Overall, #name would prevent the wearer from starving.", "Due to the intrinsic nature of #name, the wearer would somehow be kept fed.", "Extracting nutrients from the ambient atmosphere, #name would ensure the wearer would not starve.", "Interestingly, #name would be useful on long journeys." });
		itemEffect.addDefault("lore.bad", new String[] { "Yet, #name was exceptionally heavy, taking a toll on the wearer.", "But the journey would be harder while wearing #name.", "Although, #name would drain the wearer of energy.", "Every step would be more taxing while wearing #name, however." });

		itemEffect.save();
	}

	private static void addSharpness()
	{
		Settings itemEffect = new Settings("Item Effects", "Sharpness.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 0.5);

		itemEffect.addDefault("lore.goodMob", new String[] { "Undoubtly, #name would strike down the Malevolent creatures.", "Hatred against the Malevolent creatures was prominent within #name.", "#name would pierce the Malevolent creatures.", "#name had one goal - to rend the Malevolent creatures.", "Thus, #name, sharpened, would strike down the Malevolent creatures." });
		itemEffect.addDefault("lore.goodAll", new String[] { "Sharpened, #name would prove fatal to its victims.", "In the hands of its owner, #name would cause harm to those opposing #name.", "Saturated with magic, #name would strike sharply.", "So it was, #name would shred its foes.", "Woe betide all those who dared oppose the owner of #name." });
		itemEffect.addDefault("lore.badMob", new String[] { "But #name refused to strike down the Malevolent creatures.", "Alas, #name could not go against its nature.", "Yet, #name would not allow itself to be used against the Malevolent creatures." });
		itemEffect.addDefault("lore.badAll", new String[] { "#name could not strike sharply, however.", "Alas, #name was flawed - it would falter.", "Saturated with conflict, however, #name was unable to take a side.", "But #name went against its own nature." });

		itemEffect.save();
	}

	private static void addShockAbsorber()
	{
		Settings itemEffect = new Settings("Item Effects", "Shock_Absorber.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 1.0);

		itemEffect.addDefault("lore.good", new String[] { "Not even the powers of nature would bypass #name.", "#name would prevent even the power of lightning from harming the wearer.", "Shockwaves and electrical currents, not even such forces would harm the wearer of #name.", "#name would absorb even the strongest of shockwaves and currents, protecting the wearer." });
		itemEffect.addDefault("lore.bad", new String[] { "Yet the composition of #name would amplify electrical currents.", "Alas, shockwaves would resonate within #name, harming the wearer.", "#name would shock the wearer, however, amplifying currents." });

		itemEffect.save();
	}

	private static void addShroud()
	{
		Settings itemEffect = new Settings("Item Effects", "Shroud.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUse", 4.0);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.4);

		itemEffect.addDefault("lore", new String[] { "Darkness, despair, that is what #name would bring its foes.", "The silence of the night would torment those struck by #name.", "Those struck by #name would be left in darkness.", "Light would not reach those struck by #name." });

		itemEffect.save();
	}

	private static void addThaumicBolt()
	{
		Settings itemEffect = new Settings("Item Effects", "Thaumic_Bolt.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("cooldownTime", 10);
		itemEffect.addDefault("damageIncrease", 0.08f);

		itemEffect.addDefault("lore", new String[] { "Swiftly, #name would strike down foes from afar.", "Through magic, #name would channel energy to harm foes from afar.", "By channeling magic, #name would summon bolts of energy.", "And so it was, #name was glowing softly, pulsating with energy." });

		itemEffect.save();
	}

	private static void addTimber()
	{
		Settings itemEffect = new Settings("Item Effects", "Timber.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerBlock", 1.5);

		itemEffect.addDefault("lore", new String[] { "The strength of #name would allow #name to harvest entire trees.", "Solidly crafted, #name proved effective in cutting wood.", "#name, finely sharpened, would be able to work well with wood." });

		itemEffect.save();
	}

	private static void addTreasure()
	{
		Settings itemEffect = new Settings("Item Effects", "Treasure.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("itemValues", new String[] { /* ORES&MINERALS */"coal:0.2", "coal=10:2.0", "coal=32:6.4", "diamond:6.0", "diamond=2:12.0", "diamond=4:24.0", "diamond=7:42.0", "emerald:6.0", "emerald=5:30.0", "gold_ingot:3.0", "gold_ingot=3:9.0", "gold_ingot=7:21.0", "gold_ingot=20:60.0", "iron_ingot:2.0", "iron_ingot=3:6.0", "iron_ingot=8:16.0", "iron_ingot=30:60.0", "quartz:0.3", "quartz=10:3.0", "quartz=24:7.2", /* MISC */"name_tag:12.0", "name_tag=2:24.0", "saddle:14.0", "skull_item-1:28.0", "skull_item-1=2:56.0" });

		itemEffect.addDefault("lore", new String[] { "Interestingly, #name contained a set of ancient and forgotten treasures.", "But #name was not empty - it contained long lost treasures.", "Adventurers had long been looking for #name, trying to get a hold of the ancient treasures it contained." });

		itemEffect.save();
	}

	private static void addTunneler()
	{
		Settings itemEffect = new Settings("Item Effects", "Tunneler.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerBlock", 2.5);

		itemEffect.addDefault("lore", new String[] { "Stone, rock, #name would effortlessly dig through such trivialities.", "Mountains, rock faces, cliffs, it would not matter - #name would prove effective.", "To #name, mere stone, rock, such materials were no more than trivialities.#resetname", "#resetnameThe tasks of moving stone, rock, previously a tedious excercise, #name would do such with ease.", "#resetnameEven the sturdiest patch of stone was powerless against #name." });

		itemEffect.save();
	}

	private static void addUnmelting()
	{
		Settings itemEffect = new Settings("Item Effects", "Unmelting.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerDamage", 0.0);

		itemEffect.addDefault("lore.good", new String[] { "#name would through magical means resolidify the wearer.", "And so it was, #name would demeltify the wearer." });
		itemEffect.addDefault("lore.bad", new String[] { "But #name could cause damage beyond repair.", "Although #name could be harmful." });

		itemEffect.save();
	}

	private static void addWither()
	{
		Settings itemEffect = new Settings("Item Effects", "Wither.yml");
		itemEffect.addDefault("enable", true);
		itemEffect.addDefault("durabilityCostPerUsePerLevel", 5.0);
		itemEffect.addDefault("bowDurabilityCostMultiplier", 0.25);

		itemEffect.addDefault("lore", new String[] { "Dark energies residing within #name would harm anyone struck by #name.", "Corrosive forces would burst on anyone struck by #name.", "Woe to those struck by #name, as they would perish by dark magic." });

		itemEffect.save();
	}
}
