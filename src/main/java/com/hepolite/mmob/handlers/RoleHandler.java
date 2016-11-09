package com.hepolite.mmob.handlers;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.MMobSettings;
import com.hepolite.mmob.mobs.MobRole;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsRoles;

public class RoleHandler
{
	// Control variables
	private static HashMap<String, MobRole> roleMap = new HashMap<String, MobRole>();

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Load up all the roles in the configuration file into the map */
	public static void loadRolesFromConfig()
	{
		roleMap.clear();

		// Find all roles to load up
		Set<String> roles = SettingsRoles.getRoles();
		for (String role : roles)
			loadRoleFromConfig(SettingsRoles.getConfig(role), role);
	}

	/** Loads up the specified role */
	private static void loadRoleFromConfig(Settings settings, String name)
	{
		// Need a valid role
		if (!settings.hasProperty("enable"))
		{
			Log.log("Attempted to load up role '" + name + "' which doesn't exist! Did you make sure to add 'enable=true/false' to the mob?", Level.WARNING);
			return;
		}
		if (MMobSettings.isDebugmode)
			Log.log("Loading up role '" + name + "'...");

		// Create the new role
		MobRole mobRole = new MobRole(name);
		roleMap.put(name, mobRole);
	}

	/** Returns a role from the given string, if one exists */
	public static MobRole getRole(String role)
	{
		role = role.replaceAll(" ", "_");
		if (!roleMap.containsKey(role))
		{
			Log.log("Attempted to look up role '" + role + "', which doesn't exist!");
			return null;
		}
		return roleMap.get(role);
	}
}
