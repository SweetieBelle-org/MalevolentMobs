package com.hepolite.mmob.abilities;

import com.hepolite.mmob.mobs.MalevolentMob;

public abstract class Passive extends Ability {
    protected Passive(final MalevolentMob mob, final String name, final Priority priority, final float scale) {
        super(mob, name, priority, scale);
    }
}
