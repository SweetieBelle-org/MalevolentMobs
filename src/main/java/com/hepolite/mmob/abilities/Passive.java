package com.hepolite.mmob.abilities;

import com.hepolite.mmob.mobs.MalevolentMob;

public abstract class Passive extends Ability
{
	protected Passive(MalevolentMob mob, String name, Priority priority, float scale)
	{
		super(mob, name, priority, scale);
	}
}
