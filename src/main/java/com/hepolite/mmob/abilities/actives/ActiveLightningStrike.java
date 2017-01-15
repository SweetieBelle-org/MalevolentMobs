package com.hepolite.mmob.abilities.actives;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveArea;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The lightning strike active sends a bolt of lightning after a certain delay, dealing damage to all entities trapped within the reach
 */
public class ActiveLightningStrike extends ActiveArea {
    // Prevent this from targeting players after they die

    private float strength = 0.0f;
    private float range = 0.0f;
    private int strikes = 0;
    private boolean affectPlayersOnly = true;

    private LivingEntity target = null;
    private int currentStrike = 0;

    public ActiveLightningStrike(final MalevolentMob mob, final float scale) {
        super(mob, "Lightning Strike", Priority.NORMAL, scale);
    }

    @Override
    public void loadFromConfig(final Settings settings, final Settings alternative) {
        super.loadFromConfig(settings, alternative);

        strength = settings.getScaledValue(alternative, "Strength", scale, 0.0f);
        range = settings.getScaledValue(alternative, "Range", scale, 0.0f);
        strikes = (int) settings.getScaledValue(alternative, "StrikeCount", scale, 0.0f);
        affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
    }

    @Override
    public boolean canCast(final float healthFactor, final float distanceToTarget, final LivingEntity target) {
        return distanceToTarget <= 70.0f;
    }

    @Override
    public void cast(final LivingEntity target) {
        super.cast(target);
        this.target = target;
    }

    @Override
    public void applyEffect(final Location location) {
        Common.createLightningStrike(location, strength, range, affectPlayersOnly);

        // Make sure to strike several times at the given target
        if (++currentStrike >= strikes)
            currentStrike = 0;
        else
            cast(target);
    }

    @Override
    protected void displayArea(final Location location) {
        ParticleEffect.play(ParticleType.FIREWORKS_SPARK, location, 0.05f, 25, 0.5f);
    }
}
