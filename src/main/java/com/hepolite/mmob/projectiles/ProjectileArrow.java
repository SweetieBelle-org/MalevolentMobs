package com.hepolite.mmob.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;

public class ProjectileArrow extends ProjectileBolt {
    private Arrow arrow = null;

    public ProjectileArrow(final LivingEntity caster, final LivingEntity target, final float speed, final boolean factorInGravity, final float inaccuracy) {
        super(caster, target, speed, factorInGravity, inaccuracy);
        arrow = caster.launchProjectile(Arrow.class, velocity);
    }

    public ProjectileArrow(final LivingEntity caster, final boolean factorInGravity, final Arrow arrow) {
        super(caster, factorInGravity);
        this.arrow = arrow;
        velocity = arrow.getVelocity();
    }

    @Override
    public void onTick() {
        super.onTick();
        if (arrow.isValid() && isAlive())
            arrow.setVelocity(velocity);
    }

    @Override
    protected void applyEffects(final Location location) {
    }

    @Override
    protected void displayBolt(final Location location) {
    }
}
