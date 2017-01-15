package com.hepolite.mmob.projectiles;

import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.MathHelper;

public class ProjectileFirework extends Projectile {
    private LivingEntity caster = null;
    private Firework firework = null;

    private float strength = 0.0f;
    private float radius = 0.0f;
    private boolean affectPlayersOnly = true;

    public ProjectileFirework(final LivingEntity caster, final boolean factorInGravity, final Firework firework, final float strength, final float radius, final boolean affectPlayersOnly) {
        this.caster = caster;
        position = caster.getEyeLocation();
        this.firework = firework;
        velocity = firework.getVelocity();
        this.strength = strength;
        this.radius = radius;
        this.affectPlayersOnly = affectPlayersOnly;
    }

    @Override
    public void onTick() {
        if (!firework.isDead()) {
            // Make sure the rocket sticks to whatever it hits when flying
            if (Common.getObstruction(position, position.clone().add(velocity), caster) != null)
                velocity.setX(0.0).setY(0.0).setZ(0.0);
            position.add(velocity);

            firework.setVelocity(velocity);

            position.add(MathHelper.gravity.clone().multiply(0.5));
            velocity.add(MathHelper.gravity);
        } else {
            // Blow up everything
            Common.createExplosion(position, strength, radius, affectPlayersOnly, caster);
            kill();
        }
    }
}
