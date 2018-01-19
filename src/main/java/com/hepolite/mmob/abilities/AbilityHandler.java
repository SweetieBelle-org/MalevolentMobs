package com.hepolite.mmob.abilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.settings.SettingsSimple;
import com.hepolite.mmob.abilities.targets.Target;
import com.hepolite.mmob.abilities.targets.TargetCaster;
import com.hepolite.mmob.abilities.targets.TargetNull;
import com.hepolite.mmob.abilities.targets.TargetType;
import com.hepolite.mmob.abilities.triggers.Trigger;
import com.hepolite.mmob.abilities.triggers.TriggerDamageDealt;
import com.hepolite.mmob.abilities.triggers.TriggerDamageTaken;
import com.hepolite.mmob.abilities.triggers.TriggerDeath;
import com.hepolite.mmob.abilities.triggers.TriggerDeinitialize;
import com.hepolite.mmob.abilities.triggers.TriggerInitialize;
import com.hepolite.mmob.abilities.triggers.TriggerTick;
import com.hepolite.mmob.abilities.triggers.TriggerType;

public class AbilityHandler extends CoreHandler
{
	private final CorePlugin plugin;
	private final Map<String, Ability> abilities = new HashMap<String, Ability>();

	public AbilityHandler(CorePlugin plugin)
	{
		this.plugin = plugin;

		loadAbilities();
	}

	@Override
	public void onRestart()
	{
		loadAbilities();
	}

	/** Loads up all abilities from the config files */
	private final void loadAbilities()
	{
		abilities.clear();
		File directory = new File(plugin.getDataFolder() + "/Abilities");
		for (File file : directory.listFiles())
		{
			try
			{
				abilities.put(file.getName(), loadAbility(file));
				Log.info("Loaded ability " + file.getName());
			}
			catch (Exception e)
			{
				Log.warning("Failed to load ability " + file.getName());
				Log.warning(e.getLocalizedMessage());
			}
		}
	}

	/** Creates a new ability from the given file */
	private final Ability loadAbility(File file)
	{
		Settings settings = new SettingsSimple(plugin, "Abilities", file);
		String typeString = settings.getString("type");
		if (typeString == null)
			throw new IllegalArgumentException("Ability type must be defined");
		AbilityType type = AbilityType.valueOf(typeString.toUpperCase());
		Ability ability = new Ability(type);
		parseAbility(ability, settings);
		return ability;
	}

	/** Parses the given ability */
	private final void parseAbility(Ability ability, Settings settings)
	{
		for (String key : settings.getKeys("target"))
			loadTarget(ability, settings, "target." + key);
		for (String key : settings.getKeys("trigger"))
			loadTrigger(ability, settings, "trigger." + key);
	}

	/** Creates a new target from the given settings and the given property and adds it to the ability */
	private final void loadTarget(Ability ability, Settings settings, String property)
	{
		String[] parts = decompose(settings, property);
		TargetType type = TargetType.valueOf(parts[1].toUpperCase());
		String name = parts[0];

		Target target;
		switch (type)
		{
		case CASTER:
			target = new TargetCaster();
			break;
		case NULL:
			target = new TargetNull();
			break;
		default:
			throw new IllegalArgumentException("Invalid target type " + type.toString() + " in target " + name);
		}

		ability.addTarget(target);
	}

	/** Creates a new trigger from the given settings from the given property */
	private final void loadTrigger(Ability ability, Settings settings, String property)
	{
		String[] parts = decompose(settings, property);
		TriggerType type = TriggerType.valueOf(parts[1].toUpperCase());
		String name = parts[0];

		Trigger trigger;
		switch (type)
		{
		case DAMAGE_DEALT:
			trigger = new TriggerDamageDealt(name);
			break;
		case DAMAGE_TAKEN:
			trigger = new TriggerDamageTaken(name);
			break;
		case DEATH:
			trigger = new TriggerDeath(name);
			break;
		case DEINITIALIZE:
			trigger = new TriggerDeinitialize(name);
			break;
		case INITIALIZE:
			trigger = new TriggerInitialize(name);
			break;
		case TICK:
			int frequency = settings.getInt(property + ".frequency");
			trigger = new TriggerTick(name, frequency);
			break;
		default:
			throw new IllegalArgumentException("Invalid trigger type " + type.toString() + " in trigger " + name);
		}

		Target target = ability.getTarget(settings.getString(property + ".target"));
		String mechanic = settings.getString(property + ".mechanic");
		
		Log.debug("Mechanic loaded: '" + mechanic + "'");

		trigger.setTarget(target);
		ability.addTrigger(trigger);
	}

	/** Returns a two-part string array where the first entry contains the name, and the second entry contains the type */
	private final String[] decompose(Settings settings, String property)
	{
		if (property == null || property.length() == 0)
			throw new IllegalArgumentException("Property cannot be null or empty");
		String[] paths = property.split("\\.");
		if (paths.length == 0)
			throw new IllegalArgumentException("Property '" + property + "' broke in an unexpected way");
		String format = paths[paths.length - 1];
		String[] parts = format.split("=");
		if (parts.length != 2)
			throw new IllegalArgumentException("Invalid format " + format);
		return parts;
	}
}
