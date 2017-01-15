package com.hepolite.mmob.dungeons;

import com.hepolite.mmob.mobs.MobRole;

public class DungeonMob {
    // Control variables
    public String mobType;
    public MobRole role;

    /* Initialization */
    public DungeonMob(final String type, final MobRole role) {
        mobType = type;
        this.role = role;
    }

    @Override
    public String toString() {
        if (role == null)
            return mobType;
        return mobType + " " + role.getName();
    }
}
