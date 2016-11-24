package com.hepolite.mmob.dungeons;

import com.hepolite.mmob.mobs.MobRole;

public class DungeonMob
{
	// Control variables
	public String mobType;
	public MobRole role;

	/* Initialization */
	public DungeonMob(String type, MobRole role)
	{
		this.mobType = type;
		this.role = role;
	}

	@Override
	public String toString()
	{
		if (role == null)
			return mobType;
		return mobType + " " + role.getName();
	}
}
