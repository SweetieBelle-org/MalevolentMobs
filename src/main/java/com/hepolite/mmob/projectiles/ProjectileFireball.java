package com.hepolite.mmob.projectiles;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

public class ProjectileFireball extends Projectile {
    private LivingEntity caster = null;
    private LivingEntity target = null;
    private float speed = 0.0f;
    private float turnFactor = 0.0f;
    private boolean affectPlayersOnly = true;

    private float strength = 0.0f;
    private int duration = 0;
    private float range = 0.0f;

    private int soundTimer = 0;

    public ProjectileFireball(final LivingEntity caster, final LivingEntity target, final float speed, final float turnFactor, final float strength, final int duration, final float range, final boolean affectPlayersOnly) {
        this.caster = caster;
        position = caster.getEyeLocation();
        this.target = target;
        this.speed = speed;
        this.turnFactor = turnFactor;
        this.affectPlayersOnly = affectPlayersOnly;

        this.strength = strength;
        this.duration = duration;
        this.range = range;
    }

    @Override
    public void onTick() {
        // Display the projectile and play a sound as it flies by
        ParticleEffect.play(ParticleType.FLAME, position, 0.04f, 9, 0.333f);
        final Location oldPosition = position.clone();
        moveTowardsLocation(target.getEyeLocation(), speed, turnFactor);

        if (++soundTimer % 20 == 0)
            position.getWorld().playSound(position, Sound.BLOCK_FIRE_AMBIENT, 1.0f, 0.0f);

        // Check for collisions, if any was found, detonate there
        final Location obstructionLocation = Common.getObstruction(oldPosition, position, caster);
        if (obstructionLocation != null) {
            kill();
            applyEffects(obstructionLocation);
        }
    }

    private void applyEffects(final Location location) {
        location.getWorld().playSound(location, Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 0.0f);

        // Damage all entities within the vicinity
        final List<LivingEntity> entities = Common.getEntitiesInRange(location, range);
        for (final LivingEntity entity : entities) {
            if (entity == caster)
                return;

            if (!affectPlayersOnly || entity instanceof Player) {
                Common.doDamage(strength, target, caster, DamageCause.FIRE);
                entity.setFireTicks(duration);
            }
        }
    }
}
