package com.hepolite.mmob.abilities;

import java.util.List;

import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public abstract class Active extends Ability {
    private int timerCooldown = 0;
    private int cooldown = 0;

    private boolean didCast = false;

    protected Active(final MalevolentMob mob, final String name, final Priority priority, final float scale) {
        super(mob, name, priority, scale);
    }

    @Override
    public void loadFromConfig(final Settings settings, final Settings alternative) {
        cooldown = (int) (20.0f * settings.getScaledValue(alternative, "Cooldown", scale, 0.0f));
        timerCooldown = cooldown;
    }

    /** Called every tick */
    @Override
    public void onTick() {
        didCast = false;
        if (timerCooldown++ >= cooldown) {
            final LivingEntity malevolentMob = mob.getEntity();

            // Find a valid target to attack
            final List<LivingEntity> targets = mob.getTargets();
            for (final LivingEntity target : targets) {
                if (target.getWorld() != mob.getEntity().getWorld())
                    continue;

                // Attempt to cast the ability on the target
                @SuppressWarnings("deprecation")
                final float healthFactor = (float) (malevolentMob.getHealth() / malevolentMob.getMaxHealth());
                final float distance = (float) target.getLocation().distance(malevolentMob.getLocation());
                if (canCast(healthFactor, distance, target)) {
                    timerCooldown = 0;
                    didCast = true;
                    cast(target);
                    break;
                }
            }

            // If nothing was cast, delay the cast until a target is within reach
            if (!didCast)
                decreaseCooldownTimer(1.0f);
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////

    /** Conditions required for the active ability to be casted */
    public abstract boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target);

    /** Actions that will be performed when the active is casted */
    public abstract void cast(LivingEntity target);

    // /////////////////////////////////////////////////////////////////////////////////////////

    /** Returns true if the active was cast in the previous tick */
    public boolean isCasted() {
        return didCast;
    }

    /** Resets the cooldown of the active, setting it to zero */
    public void resetCooldown() {
        timerCooldown = 0;
    }

    /** Reduces the cooldown by the given amount, useful to prevent several actives to be activated at once */
    public void decreaseCooldownTimer(final float seconds) {
        timerCooldown -= (int) (20.0f * seconds);
    }
}
