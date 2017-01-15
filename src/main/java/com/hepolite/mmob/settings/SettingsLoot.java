package com.hepolite.mmob.settings;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobPlugin;
import com.hepolite.mmob.utility.NBTAPI.NBTTag;

public class SettingsLoot {
    // Control variables
    private final static HashMap<String, Settings> categories = new HashMap<String, Settings>();

    private final static Random random = new Random();

    /** Initialize all the effects settings; called from MMobSettings */
    public static void initialize() {
        // Clear old data
        categories.clear();

        // Every first run, all default item effects must be defined. Subsequent runs must define new effects
        final File directory = new File(MMobPlugin.getInstance().getDataFolder() + "/Loot");
        if (!directory.exists()) {
            directory.mkdir();
            addDefaultSettings();
        }

        // Discover all abilities in the ability directory
        final File[] files = directory.listFiles();
        for (final File file : files)
            categories.put(file.getName().split("\\.")[0], new Settings("Loot", file.getName()));
    }

    /** Returns the configuration for the given category */
    public static Settings getConfig(final String category) {
        final Settings setting = categories.get(category);
        if (setting == null) {
            Log.log("Couldn't find config for loot category '" + category + "'!", Level.WARNING);
            return null;
        }
        return setting;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////
    // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA // DEFAULT DATA //
    // ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Adds all the default abilities to the system */
    private static void addDefaultSettings() {
        addDefaultConfigs();
        addDefaultRandomNames();
        addDefaultRandomItemEffects();
        addDefaultRandomLoot();
    }

    private static void addDefaultConfigs() {
        final Settings setting = new Settings("Loot", "Core.yml");

        // Loot levels
        setting.addDefault("level.weak", 1);
        setting.addDefault("level.low", 10);
        setting.addDefault("level.medium", 20);
        setting.addDefault("level.high", 32);
        setting.addDefault("level.epic", 48);
        setting.addDefault("level.legendary", 70);

        setting.save();
    }

    private static void addDefaultRandomNames() {
        final Settings setting = new Settings("Loot", "Names.yml");

        // Keywords:
        // @name - The name of the item itself, in Title Capitalization
        // @group - The group the item belongs to (helmet, chestplate, leggings, boots, sword, bow, misc, tool, wand)

        // Definitions
        setting.addDefault("item", new String[] { "&9#special.@group", "&9#adjective.@group #object.@group", "&9#object.@group of #context.@group", "&9The #owner.prefix #object.@group", "&9#object.@group of the #owner.postfix", "&9#owner.people #adjective.@group #object.@group" });

        setting.addDefault("owner.postfix", new String[] { "Conqueror", "Dragon", "Elders", "Foe", "Forgotten", "Guard", "Mage", "Phoenix", "Princess", "Queen", "Shepherd", "Troll", "Wicked" });
        setting.addDefault("owner.prefix", new String[] { "Angel's", "Basilisk's", "Drone's", "Goddesses'", "Gryphon's", "Fool's", "Forgotten's", "Fortune's", "Hag's", "Liar's", "Peasant's", "Phoenix'", "Seer's", "Serpent's", "Unicorn's", "Warden's", "Warrior's" });
        setting.addDefault("owner.people", new String[] { "Alternia's", "Atalanta's", "Caramel's", "Ceymi's", "Doppel's", "Ebony's", "Flitterwing's", "Meadow Song's", "Midnight Wind's", "Nictis'", "Novel Tale's", "Pincer's", "Praegus'", "Senyx'", "Sky's", "Spark Wheel's", "Stormclaw's", "Tryp's", "Vola Nocturn's" });
        setting.addDefault("owner.object", new String[] { "Ancient Tree's", "Desolated Realm's", "Forgotten Mine's", "Sleeping Rock's", "Twisted Mind's", "Wicked Soul's" });

        setting.addDefault("object.common", new String[] { "@name" });
        setting.addDefault("object.armor", new String[] { "#object.common", "Armor" });
        setting.addDefault("object.helmet", new String[] { "#object.armor", "Hat", "Helm", "Headcover", "Headguard", "Cap" });
        setting.addDefault("object.chestplate", new String[] { "#object.armor", "Battleplate", "Breastplate", "Cuirass", "Tunic", "Vest" });
        setting.addDefault("object.leggings", new String[] { "#object.armor", "Gaiter", "Pants", "Plateleg", "Kilt", "Legwraps", "Skirt" });
        setting.addDefault("object.boots", new String[] { "#object.armor", "Footguards", "Footpads", "Footwear", "Sabatons", "Sandals", "Shoes", "Walkers" });
        setting.addDefault("object.weapon", new String[] { "#object.common", "Vengeance" });
        setting.addDefault("object.sword", new String[] { "#object.weapon", "Blade", "Dagger", "Saber", "Schimitar", "Rapier", "Cutlass" });
        setting.addDefault("object.bow", new String[] { "#object.weapon", "Crossbow", "Flatbow", "Longbow" });
        setting.addDefault("object.misc", new String[] { "#object.common" });
        setting.addDefault("object.tool", new String[] { "#object.misc", "Device", "Gadget", "Tool", "Utensil" });
        setting.addDefault("object.wand", new String[] { "#object.misc", "Cane", "Scepter", "Staff", "Wand" });

        setting.addDefault("adjective.common", new String[] { "Acceptable", "Almost Worthless", "Arcane", "Bewitched", "Bright", "Crystalized", "Common", "Cute", "Everlasting", "Fancy", "Glacial", "Glancing", "Greater", "Goofy", "Hardened", "Infused", "Perfected", "Phantom", "Prime", "Shadow", "Shining", "Slim", "Solid", "Sparkling", "Tough", "Ugly", "Uncommon" });
        setting.addDefault("adjective.armor", new String[] { "#adjective.common", "Atuned", "Battleworn", "Bruised", "Rune-Forged", "Wind-Forged" });
        setting.addDefault("adjective.helmet", new String[] { "#adjective.armor", "Everclear" });
        setting.addDefault("adjective.chestplate", new String[] { "#adjective.armor" });
        setting.addDefault("adjective.leggings", new String[] { "#adjective.armor" });
        setting.addDefault("adjective.boots", new String[] { "#adjective.armor" });
        setting.addDefault("adjective.weapon", new String[] { "#adjective.common", "Elongated", "Infernal", "Malevolent", "Malicious", "Sharpened", "Tyrannical", "Wrathful" });
        setting.addDefault("adjective.bow", new String[] { "#adjective.weapon", "Ironbark", "Metallic", "Greatwood", "Yew" });
        setting.addDefault("adjective.sword", new String[] { "#adjective.weapon", "Sharpened", "Magical" });
        setting.addDefault("adjective.misc", new String[] { "#adjective.common" });
        setting.addDefault("adjective.tool", new String[] { "#adjective.misc", "Improvised", "Unbreaking" });
        setting.addDefault("adjective.wand", new String[] { "#adjective.misc", "Corrupted", "Engraved", "Grand", "Greatwood", "Moonlit", "Primal", "Refined", "Silverwood", "Soulless" });

        setting.addDefault("context.common", new String[] { "Awareness", "Bravery", "Chaos", "Cuteness", "Divinity", "Glee", "Haunting", "Honor", "Last Hope", "Lost Worlds", "Misfortune", "Nightmares", "Secrets", "Solitude", "the Ancestors", "Triviality", "Wisdom" });
        setting.addDefault("context.armor", new String[] { "#context.common", "Glory", "Protection", "Solitude", "Stealth" });
        setting.addDefault("context.helmet", new String[] { "#context.armor", "All-Seeing", "Intellect", "Intelligence", "Omniscience", "Revealing", "Thinking", "Unseeing" });
        setting.addDefault("context.chestplate", new String[] { "#context.armor", "Neverfracture", "Vitality" });
        setting.addDefault("context.leggings", new String[] { "#context.armor" });
        setting.addDefault("context.boots", new String[] { "#context.armor", "Fleeing", "Hallowed Freedom", "Jumping", "Running", "Swiftness" });
        setting.addDefault("context.weapon", new String[] { "#context.common", "Dark Whispers", "Ending Hope", "Everlasting Blight", "Hunting", "Hope", "Justice", "Storms", "Undoing" });
        setting.addDefault("context.sword", new String[] { "#context.weapon", "Decapitation", "Etheral Essence" });
        setting.addDefault("context.bow", new String[] { "#context.weapon", "Assasination", "Silencing" });
        setting.addDefault("context.misc", new String[] { "#context.common", "Infinite Fortune", "Shame", "the Hoard" });
        setting.addDefault("context.tool", new String[] { "#context.misc", "Production", "Task Performing", "Trials", "World Changes" });
        setting.addDefault("context.wand", new String[] { "#context.misc", "Forgotten", "Sorcery", "Thaumaturgy", "Wizardry" });

        setting.addDefault("special.helmet", new String[] { "Burden of Nightmares", "Mask of Many Faces", "Mindguard", "Seer of Prophecies", "Silencer of Cries", "Voice of Honor", "Whisper of the Forest" });
        setting.addDefault("special.chestplate", new String[] { "Blight of Dishonor", "Broken Sorrow", "Faded Punishment", "Raiment of Regrets", "Shattered Glory", "Stormgate", "Tribute of Forging" });
        setting.addDefault("special.leggings", new String[] { "Deflector of Hope", "Final Stride", "Last Hope", "Legacy of the Sun", "Lifemender", "Neverlasting Sorrow", "Protector's Last Glory" });
        setting.addDefault("special.boots", new String[] { "Eversoft Steps", "Lone Wanderer", "Movement of Inception", "Muffled Steps", "Silent Hope", "Sunlight's Embrace", "Lightfeet", "Featherfall" });
        setting.addDefault("special.sword", new String[] { "The Cleaver", "Hope of the Burning Sun", "Edge of Mercy", "Dawnbreaker", "Jollywokker", "The Plunger of Hope", "Lightblade", "Blazing Fury", "Daggerfall", "Moan of Disbelief", "The Tsar's Wrath" });
        setting.addDefault("special.bow", new String[] { "Final Whisper", "Vengeance", "The Ongoing Storm", "Tyrant's Injustice", "Mourning Yew", "Stormbringer", "Siren's Call", "Lightwing" });
        setting.addDefault("special.misc", new String[] { "The #owner.object #object.misc" });
        setting.addDefault("special.tool", new String[] { "#special.misc", "Imagination", "Little Buddy", "The Big Blue", "Thingymabob" });
        setting.addDefault("special.wand", new String[] { "#special.misc", "Dreambinder", "Eternal Will", "Hailstorm", "Inertia", "Reflection", "Thunderfury Pole", "Twisted Visage", "Worldshaper" });

        // Define the various possible names for the items
        setting.addDefault("helmet", new String[] { "#item" });
        setting.addDefault("chestplate", new String[] { "#item" });
        setting.addDefault("leggings", new String[] { "#item" });
        setting.addDefault("boots", new String[] { "#item" });
        setting.addDefault("sword", new String[] { "#item" });
        setting.addDefault("bow", new String[] { "#item" });
        setting.addDefault("misc", new String[] { "#item" });
        setting.addDefault("tool", new String[] { "#item" });
        setting.addDefault("wand", new String[] { "#item" });

        setting.save();
    }

    private static void addDefaultRandomItemEffects() {
        final Settings setting = new Settings("Loot", "Item_Effects.yml");

        int i;
        String name;

        // Weak item effects
        i = 0;
        name = "weak.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i0", "i50/70");
        writeItemEffect(setting, name + i++, "Bulwark", "i6/10");
        writeItemEffect(setting, name + i++, "Charged", "i40/50", "i2/3", "i40/48");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i8/12");
        writeItemEffect(setting, name + i++, "Fireward", "i8/12");
        writeItemEffect(setting, name + i++, "Growth");
        writeItemEffect(setting, name + i++, "Levity", "b1", "i0");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i25/32");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i8/12");
        writeItemEffect(setting, name + i++, "Fireward", "i8/12");
        writeItemEffect(setting, name + i++, "Relic", "i100/140", "high", "i60/85", "negative");
        writeItemEffect(setting, name + i++, "Repair", "i40/60");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i25/35", "#0", "i12/18");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i100/130", "#0", "i3/5");
        writeItemEffect(setting, name + i++, "Sharpness", "i8/12", "i0/5", "i0", "i0");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i8/12");
        writeItemEffect(setting, name + i++, "Timber", "i4/6");
        writeItemEffect(setting, name + i++, "Treasure", "i100/150");

        // Low item effects
        i = 0;
        name = "low.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i0", "i70/100");
        writeItemEffect(setting, name + i++, "Bulwark", "i10/15");
        writeItemEffect(setting, name + i++, "Charged", "i50/60", "i2/3", "i48/55");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i12/18");
        writeItemEffect(setting, name + i++, "Explosive_Arrows", "i48/55", "i20/24");
        writeItemEffect(setting, name + i++, "Fireward", "i12/18");
        writeItemEffect(setting, name + i++, "Frost", "i15", "i35/45", "i0");
        writeItemEffect(setting, name + i++, "Growth");
        writeItemEffect(setting, name + i++, "Levity", "b1", "i0");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i33/45");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i12/18");
        writeItemEffect(setting, name + i++, "Poison", "i1", "i35/45");
        writeItemEffect(setting, name + i++, "Relic", "i100/140", "epic", "i60/85", "negative");
        writeItemEffect(setting, name + i++, "Repair", "i60/70");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i35/50", "#0", "i17/27");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i130/150", "#0", "i4/6");
        writeItemEffect(setting, name + i++, "Saturation", "i10/20", "i10/15");
        writeItemEffect(setting, name + i++, "Sharpness", "i12/18", "i0/5", "i0", "i0");
        writeItemEffect(setting, name + i++, "Sharpness", "i0", "i0", "i0", "i2/3");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i12/18");
        writeItemEffect(setting, name + i++, "Shroud", "i30/45");
        writeItemEffect(setting, name + i++, "Thaumic_Bolt", "i1000/1150", "#0", "i335/345", "i32/37", "i37/40");
        writeItemEffect(setting, name + i++, "Timber", "i6/9");
        writeItemEffect(setting, name + i++, "Treasure", "i150/200");
        writeItemEffect(setting, name + i++, "Wither", "i1", "i35/45");

        // Medium item effects
        i = 0;
        name = "medium.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i15/20", "i0");
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i0", "i100/140");
        writeItemEffect(setting, name + i++, "Bulwark", "i15/20");
        writeItemEffect(setting, name + i++, "Charged", "i80/100", "i3/4", "i55/62");
        writeItemEffect(setting, name + i++, "Earthmover");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i18/25");
        writeItemEffect(setting, name + i++, "Explosive_Arrows", "i55/65", "i24/30");
        writeItemEffect(setting, name + i++, "Fireward", "i18/25");
        writeItemEffect(setting, name + i++, "Fireworks", "i580/640", "#0", "i130/136", "i46/52", "i28/33", "i140/160", "i64/74");
        writeItemEffect(setting, name + i++, "Frost", "i15", "i35/45", "i0");
        writeItemEffect(setting, name + i++, "Frost", "i30", "i50/75", "i0");
        writeItemEffect(setting, name + i++, "Growth");
        writeItemEffect(setting, name + i++, "Harvest");
        writeItemEffect(setting, name + i++, "Heated");
        writeItemEffect(setting, name + i++, "Lacerate", "i32/36", "i0");
        writeItemEffect(setting, name + i++, "Lacerate", "i0", "i50/55");
        writeItemEffect(setting, name + i++, "Levity", "b1", "i5/10");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i45/60");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i18/25");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i18/25");
        writeItemEffect(setting, name + i++, "Poison", "i1", "i45/55");
        writeItemEffect(setting, name + i++, "Powerline", "i1200/1300", "#0", "i200/225", "i18/26", "i85/100", "i11/13");
        writeItemEffect(setting, name + i++, "Relic", "i140/200", "epic", "i60/85", "negative");
        writeItemEffect(setting, name + i++, "Repair", "i70/80");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i50/65", "#0", "i23/33");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i150/180", "#0", "i4/6");
        writeItemEffect(setting, name + i++, "Saturation", "i10/20", "i15/18");
        writeItemEffect(setting, name + i++, "Sharpness", "i18/22", "i4/8", "i0", "i0");
        writeItemEffect(setting, name + i++, "Sharpness", "i0", "i0", "i4/6", "i2/3");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i18/25");
        writeItemEffect(setting, name + i++, "Shroud", "i45/60");
        writeItemEffect(setting, name + i++, "Thaumic_Bolt", "i1150/1250", "#0", "i345/360", "i37/40", "i40/43");
        writeItemEffect(setting, name + i++, "Timber", "i9/15");
        writeItemEffect(setting, name + i++, "Treasure", "i200/350");
        writeItemEffect(setting, name + i++, "Tunneler");
        writeItemEffect(setting, name + i++, "Wither", "i1", "i45/55");

        // High item effects
        i = 0;
        name = "high.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i20/30", "i0");
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i0", "i150/210");
        writeItemEffect(setting, name + i++, "Bulwark", "i20/25");
        writeItemEffect(setting, name + i++, "Charged", "i100/115", "i3/4", "i62/70");
        writeItemEffect(setting, name + i++, "Earthmover");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i25/30");
        writeItemEffect(setting, name + i++, "Explosive_Arrows", "i64/80", "i30/36");
        writeItemEffect(setting, name + i++, "Fireward", "i25/30");
        writeItemEffect(setting, name + i++, "Fireworks", "i640/720", "#0", "i136/140", "i52/60", "i32/36", "i160/180", "i74/82");
        writeItemEffect(setting, name + i++, "Frost", "i15", "i35/45", "i0");
        writeItemEffect(setting, name + i++, "Frost", "i45", "i65/80", "i0");
        writeItemEffect(setting, name + i++, "Growth");
        writeItemEffect(setting, name + i++, "Harvest");
        writeItemEffect(setting, name + i++, "Heated");
        writeItemEffect(setting, name + i++, "Lacerate", "i36/40", "i0");
        writeItemEffect(setting, name + i++, "Lacerate", "i0", "i55/60");
        writeItemEffect(setting, name + i++, "Levity", "b1", "i10/15");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i60/70");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i25/30");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i25/30");
        writeItemEffect(setting, name + i++, "Poison", "i2", "i50/60");
        writeItemEffect(setting, name + i++, "Powerline", "i1400/1600", "#0", "i225/240", "i25/30", "i105/120", "i13/16");
        writeItemEffect(setting, name + i++, "Relic", "i160/220", "legendary", "i60/85", "negative");
        writeItemEffect(setting, name + i++, "Repair", "i80/95");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i65/75", "#0", "i30/36");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i180/200", "#0", "i4/6");
        writeItemEffect(setting, name + i++, "Saturation", "i10/20", "i18/22");
        writeItemEffect(setting, name + i++, "Sharpness", "i22/27", "i4/8", "i0", "i0");
        writeItemEffect(setting, name + i++, "Sharpness", "i0", "i0", "i4/6", "i3/4");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i25/30");
        writeItemEffect(setting, name + i++, "Shroud", "i55/70");
        writeItemEffect(setting, name + i++, "Thaumic_Bolt", "i1250/1400", "#0", "i360/380", "i40/45", "i43/48");
        writeItemEffect(setting, name + i++, "Timber", "i15/22");
        writeItemEffect(setting, name + i++, "Treasure", "i300/650");
        writeItemEffect(setting, name + i++, "Tunneler");
        writeItemEffect(setting, name + i++, "Wither", "i2", "i50/60");

        // Epic item effects
        i = 0;
        name = "epic.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i30/40", "i0");
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i0", "i210/250");
        writeItemEffect(setting, name + i++, "Bulwark", "i25/30");
        writeItemEffect(setting, name + i++, "Charged", "i160/190", "i4/5", "i70/80");
        writeItemEffect(setting, name + i++, "Duplicator", "i50", "#0", "i5");
        writeItemEffect(setting, name + i++, "Duplicator", "i500", "#0", "i2");
        writeItemEffect(setting, name + i++, "Earthmover");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i30/35");
        writeItemEffect(setting, name + i++, "Explosive_Arrows", "i76/90", "i36/45");
        writeItemEffect(setting, name + i++, "Fireward", "i30/35");
        writeItemEffect(setting, name + i++, "Fireworks", "i720/800", "#0", "i140/144", "i60/65", "i32/36", "i180/195", "i74/82");
        writeItemEffect(setting, name + i++, "Frost", "i60", "i75/90", "i0");
        writeItemEffect(setting, name + i++, "Growth");
        writeItemEffect(setting, name + i++, "Harvest");
        writeItemEffect(setting, name + i++, "Heated");
        writeItemEffect(setting, name + i++, "Lacerate", "i40/46", "i0");
        writeItemEffect(setting, name + i++, "Lacerate", "i0", "i63/69");
        writeItemEffect(setting, name + i++, "Levity", "b1", "i15/20");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i70/85");
        writeItemEffect(setting, name + i++, "Magic_Mirror", "i30/35");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i30/35");
        writeItemEffect(setting, name + i++, "Poison", "i3", "i50/60");
        writeItemEffect(setting, name + i++, "Powerline", "i1600/1750", "#0", "i235/250", "i30/36", "i120/140", "i16/18");
        writeItemEffect(setting, name + i++, "Relic", "i220/280", "legendary", "i60/85", "negative");
        writeItemEffect(setting, name + i++, "Repair", "i95/115");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i75/90", "#0", "i38/48");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i200/220", "#0", "i4/6");
        writeItemEffect(setting, name + i++, "Saturation", "i20/30", "i22/25");
        writeItemEffect(setting, name + i++, "Sharpness", "i27/32", "i5/10", "i0", "i0");
        writeItemEffect(setting, name + i++, "Sharpness", "i0", "i0", "i8/12", "i3/4");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i30/35");
        writeItemEffect(setting, name + i++, "Shroud", "i70/80");
        writeItemEffect(setting, name + i++, "Thaumic_Bolt", "i1400/1500", "#0", "i380/420", "i45/50", "i48/52");
        writeItemEffect(setting, name + i++, "Timber", "i22/30");
        writeItemEffect(setting, name + i++, "Treasure", "i500/1000");
        writeItemEffect(setting, name + i++, "Tunneler");
        writeItemEffect(setting, name + i++, "Wither", "i3", "i50/60");

        // Legendary item effects
        i = 0;
        name = "legendary.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i40/50", "i210/250");
        writeItemEffect(setting, name + i++, "Bulwark", "i35/40");
        writeItemEffect(setting, name + i++, "Charged", "i235/270", "i5/6", "i85/100");
        writeItemEffect(setting, name + i++, "Duplicator", "i100", "#0", "i10");
        writeItemEffect(setting, name + i++, "Duplicator", "i1000", "#0", "i3");
        writeItemEffect(setting, name + i++, "Earthmover");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i45/50");
        writeItemEffect(setting, name + i++, "Explosive_Arrows", "i100/120", "i46/55");
        writeItemEffect(setting, name + i++, "Fireward", "i45/50");
        writeItemEffect(setting, name + i++, "Fireworks", "i900/1000", "#0", "i150/160", "i65/75", "i32/36", "i210/230", "i74/82");
        writeItemEffect(setting, name + i++, "Frost", "i90", "i45/60", "i0");
        writeItemEffect(setting, name + i++, "Growth");
        writeItemEffect(setting, name + i++, "Harvest");
        writeItemEffect(setting, name + i++, "Heated");
        writeItemEffect(setting, name + i++, "Lacerate", "i55/60", "i0");
        writeItemEffect(setting, name + i++, "Lacerate", "i0", "i80/90");
        writeItemEffect(setting, name + i++, "Levity", "b1", "i25/30");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i90/100");
        writeItemEffect(setting, name + i++, "Magic_Mirror", "i45/50");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i45/50");
        writeItemEffect(setting, name + i++, "Poison", "i4", "i65/80");
        writeItemEffect(setting, name + i++, "Powerline", "i1900/2000", "#0", "i240/250", "i30/36", "i120/140", "i22/25");
        writeItemEffect(setting, name + i++, "Relic", "i275/350", "legendary", "i50/75", "negative");
        writeItemEffect(setting, name + i++, "Repair", "i140/160");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i110/120", "#0", "i50/65");
        writeItemEffect(setting, name + i++, "Runic_Shield", "i250/300", "#0", "i5/7");
        writeItemEffect(setting, name + i++, "Saturation", "i30/40", "i28/31");
        writeItemEffect(setting, name + i++, "Sharpness", "i35/40", "i12/15", "i0", "i0");
        writeItemEffect(setting, name + i++, "Sharpness", "i0", "i0", "i15/18", "i4/5");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i45/50");
        writeItemEffect(setting, name + i++, "Shroud", "i100/120");
        writeItemEffect(setting, name + i++, "Thaumic_Bolt", "i1500/1600", "#0", "i450/480", "i50/60", "i55/60");
        writeItemEffect(setting, name + i++, "Timber", "i40/50");
        writeItemEffect(setting, name + i++, "Treasure", "i2500/3000");
        writeItemEffect(setting, name + i++, "Tunneler");
        writeItemEffect(setting, name + i++, "Wither", "i4", "i60/75");

        // Negative item effects
        i = 0;
        name = "negative.effect";
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i-15/-5", "i0");
        writeItemEffect(setting, name + i++, "Armor_Shredder", "i0", "i-60/-20");
        writeItemEffect(setting, name + i++, "Bulwark", "i-15/-4");
        writeItemEffect(setting, name + i++, "Entropy_Limit", "i-20/-8");
        writeItemEffect(setting, name + i++, "Fireward", "i-20/-8");
        writeItemEffect(setting, name + i++, "Fragile", "i10/40");
        writeItemEffect(setting, name + i++, "Hungry", "i5/15");
        writeItemEffect(setting, name + i++, "Levity", "b0", "i-15/-5");
        writeItemEffect(setting, name + i++, "Magic_Shield", "i-20/-8");
        writeItemEffect(setting, name + i++, "Repair", "i-12/-4");
        writeItemEffect(setting, name + i++, "Saturation", "i0", "i-12/-7");
        writeItemEffect(setting, name + i++, "Sharpness", "i0", "i0", "i-10/-5", "i-3/-2");
        writeItemEffect(setting, name + i++, "Sharpness", "i-20/-8", "i-5/-2", "i0", "i0");
        writeItemEffect(setting, name + i++, "Shock_Absorber", "i-20/-8");

        setting.save();
    }

    private static void addDefaultRandomLoot() {
        final Settings setting = new Settings("Loot", "Loot.yml");

        String[] groups;
        String name;
        int i;
        final List<String[]> effectList = new LinkedList<String[]>();
        final List<Object[]> enchantmentList = new LinkedList<Object[]>();

        // Example item
        effectList.add(new String[] { "Armor_Shredder", "i40/55", "i0" });
        effectList.add(new String[] { "Levity", "b1", "i-15/-5" });
        enchantmentList.add(new Object[] { Enchantment.ARROW_DAMAGE, 5 });
        enchantmentList.add(new Object[] { Enchantment.DURABILITY, 3 });
        final NBTTag tag = new NBTTag();
        tag.setString("somekey", "Some random string");
        tag.setFloat("otherkey", -3.14f);
        tag.setInt("anotherkey", 42);
        writeItem(setting, "EXAMPLE.A unique identifier here", Material.BOW, 42, 3, 10.0f, "Any name or #random for a random name", new String[] { "Lore line 1", "Lore line 2" }, "Any history or #random for a random history", 1, 1, effectList, enchantmentList, tag);
        effectList.clear();

        // Weak, low and medium items
        groups = new String[] { "weak", "low", "medium" };
        for (final String group : groups) {
            name = group + ".item";
            i = 0;
            writeItem(setting, name + i++, Material.IRON_HELMET, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_CHESTPLATE, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_LEGGINGS, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_BOOTS, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_SWORD, 0, 1, 15.0f, 1, 0);
            writeItem(setting, name + i++, Material.BOW, 0, 1, 15.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_PICKAXE, 0, 1, 6.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_SPADE, 0, 1, 6.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_AXE, 0, 1, 6.0f, 1, 0);
            writeItem(setting, name + i++, Material.IRON_HOE, 0, 1, 4.0f, 1, 0);
            writeItem(setting, name + i++, Material.CHEST, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.STICK, 0, 1, 1.5f, 1, 0);
            writeItem(setting, name + i++, Material.BONE, 0, 1, 1.5f, 1, 0);
            writeItem(setting, name + i++, Material.BLAZE_ROD, 0, 1, 1.5f, 1, 0);
        }

        // High, epic and legendary items
        groups = new String[] { "high", "epic", "legendary" };
        for (final String group : groups) {
            name = group + ".item";
            i = 0;
            writeItem(setting, name + i++, Material.DIAMOND_HELMET, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_CHESTPLATE, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_LEGGINGS, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_BOOTS, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_SWORD, 0, 1, 15.0f, 1, 0);
            writeItem(setting, name + i++, Material.BOW, 0, 1, 15.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_PICKAXE, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_SPADE, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_AXE, 0, 1, 10.0f, 1, 0);
            writeItem(setting, name + i++, Material.DIAMOND_HOE, 0, 1, 7.5f, 1, 0);
            writeItem(setting, name + i++, Material.CHEST, 0, 1, 8.0f, 1, 0);
            writeItem(setting, name + i++, Material.STICK, 0, 1, 2.0f, 1, 0);
            writeItem(setting, name + i++, Material.BONE, 0, 1, 2.0f, 1, 0);
            writeItem(setting, name + i++, Material.BLAZE_ROD, 0, 1, 2.0f, 1, 0);

            writeItem(setting, name + i++, Material.DIAMOND_HELMET, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_CHESTPLATE, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_LEGGINGS, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_BOOTS, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_SWORD, 0, 1, 3.0f, 2, 1);
            writeItem(setting, name + i++, Material.BOW, 0, 1, 3.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_PICKAXE, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_SPADE, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_AXE, 0, 1, 2.0f, 2, 1);
            writeItem(setting, name + i++, Material.DIAMOND_HOE, 0, 1, 2.0f, 2, 1);

            writeItem(setting, name + i++, Material.GOLDEN_APPLE, 1, 3, 2.75f, 0, 0);
        }

        // Legendary
        name = "legendary.item_special";
        i = 0;
        writeItem(setting, name + i++, Material.DIAMOND_HELMET, 0, 1, 0.1f, "&9The Mithril Helmet of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_CHESTPLATE, 0, 1, 0.1f, "&9The Mithril Chestplate of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_LEGGINGS, 0, 1, 0.1f, "&9The Mithril Leggings of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_BOOTS, 0, 1, 0.1f, "&9The Mithril Boots of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_SWORD, 0, 1, 0.15f, "&9The Mithril Sword of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.BOW, 0, 1, 0.15f, "&9The Glacial Arch of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_PICKAXE, 0, 1, 0.1f, "&9The Mithril Pickaxe of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_SPADE, 0, 1, 0.1f, "&9The Mithril Spade of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_AXE, 0, 1, 0.1f, "&9The Mithril Axe of Legends", null, "#random", 3, 0, null, null, null);
        writeItem(setting, name + i++, Material.DIAMOND_HOE, 0, 1, 0.08f, "&9The Mithril Hoe of Legends", null, "#random", 3, 0, null, null, null);

        setting.save();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////

    /** Writes a complex item to the given settings file, under the given property */
    public static void writeItem(final Settings setting, final String property, final Material material, final int metadata, final int amount, final float weight, final int randomItemEffects, final int randomNegativeItemEffects) {
        writeItem(setting, property, material, metadata, amount, weight, "#random", null, "#random", randomItemEffects, randomNegativeItemEffects, null, null, null);
    }

    /** Writes a complex item to the given settings file, under the given property */
    public static void writeItem(final Settings setting, final String property, final Material material, final int metadata, final int amount, final float weight, final String name, final String[] lore, final String history, final int randomItemEffects, final int randomNegativeItemEffects, final List<String[]> itemEffectData, final List<Object[]> enchantmentData, final NBTTag tag) {
        // Write item header data
        setting.addDefault(property + ".name", name);
        if (lore != null)
            setting.addDefault(property + ".lore", lore);
        setting.addDefault(property + ".history", history);
        setting.addDefault(property + ".weight", weight);
        setting.addDefault(property + ".material", material.toString().toLowerCase());
        if (metadata != 0)
            setting.addDefault(property + ".metadata", (short) metadata);
        if (amount != 1)
            setting.addDefault(property + ".amount", amount);

        // Write item effects
        int effectIndex = 0;
        if (itemEffectData != null)
            for (final String[] data : itemEffectData) {
                final String[] parameters = new String[data.length - 1];
                for (int i = 1; i < data.length; i++)
                    parameters[i - 1] = data[i];
                writeItemEffect(setting, property + ".itemeffects." + effectIndex++, data[0], parameters);
            }
        for (int i = 0; i < randomItemEffects; i++)
            setting.addDefault(property + ".itemeffects." + effectIndex++, "#random");
        for (int i = 0; i < randomNegativeItemEffects; i++)
            setting.addDefault(property + ".itemeffects." + effectIndex++, "#random.negative");

        // Write enchantments
        if (enchantmentData != null) {
            int enchantmentIndex = 0;
            for (final Object[] data : enchantmentData)
                writeEnchantment(setting, property + ".enchantments." + enchantmentIndex++, (Enchantment) data[0], (int) data[1]);
        }

        // Write tag
        if (tag != null)
            setting.setTagCompound(property + ".nbt", tag);
    }

    /** Writes a simple enchantment to the given settings file, under the given property */
    public static void writeEnchantment(final Settings setting, final String property, final Enchantment enchantment, final int level) {
        setting.addDefault(property + ".name", enchantment.getName().toLowerCase());
        setting.addDefault(property + ".level", level);
    }

    /** Writes a simple item effect to the given settings file, under the given property */
    public static void writeItemEffect(final Settings setting, final String property, final String name, final String... parameters) {
        setting.addDefault(property + ".name", name);
        for (int i = 0; i < parameters.length; i++)
            setting.addDefault(property + ".parameters." + i, parameters[i]);
    }

    /** Parses a simple item effect to a usual item effect data string from the given settings file, under the given property */
    public static String readItemEffect(final Settings setting, final String property) {
        String effect = setting.getString(property + ".name");
        final Set<String> parameters = setting.getKeys(property + ".parameters");
        final Object[] parsedParameters = new Object[parameters.size()];

        if (parameters.size() == 0)
            effect += ":";

        for (int i = 0; i < parameters.size(); i++) {
            String parameter = setting.getString(property + ".parameters." + i);
            try {
                final char type = parameter.charAt(0);
                parameter = parameter.substring(1);
                switch (type) {
                    case '#': // Copy a previously defined parameter
                        parsedParameters[i] = parsedParameters[Integer.parseInt(parameter)];
                        break;

                    case 'i': // Parse an integer parameter
                        final String[] parts = parameter.split("/");
                        if (parts.length == 1)
                            parsedParameters[i] = Integer.parseInt(parts[0]);
                        else {
                            final int min = Integer.parseInt(parts[0]);
                            final int max = Integer.parseInt(parts[1]);
                            parsedParameters[i] = Integer.toString(min + (max <= min ? 0 : random.nextInt(max - min + 1)));
                        }
                        break;

                    case 'b': // Parse a boolean parameter
                        parsedParameters[i] = parameter.equals("1") || parameter.equals("true") ? true : false;
                        break;

                    default:
                        parsedParameters[i] = type + parameter;
                }
                effect += ":" + parsedParameters[i];
            } catch (final Exception exception) {
                Log.log("[SettingsLoot] Failed to parse parameter '" + parameter + "' under '" + effect + "'!");
            }
        }
        return effect;
    }
}
