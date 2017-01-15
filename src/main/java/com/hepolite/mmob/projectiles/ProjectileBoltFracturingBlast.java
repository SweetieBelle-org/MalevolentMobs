package com.hepolite.mmob.projectiles;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.FireworksEffect;

public class ProjectileBoltFracturingBlast extends ProjectileBolt {
    private float range = 0.0f;
    private boolean affectPlayersOnly = true;

    private float strength = 0.0f;

    private int displayTimer = 0;
    private FireworkEffect effect = null;

    public ProjectileBoltFracturingBlast(final LivingEntity caster, final LivingEntity target, final float speed, final float strength, final float range, final boolean affectPlayersOnly) {
        super(caster, target, speed, true, 0.0f);

        this.affectPlayersOnly = affectPlayersOnly;
        this.range = range;
        this.strength = strength;

        // Construct the blast effect
        final Builder builder = FireworksEffect.getFireworksEffectBuilder();
        builder.with(Type.BALL);
        builder.withColor(Color.PURPLE, Color.RED);
        builder.withFade(Color.BLACK, Color.ORANGE);
        builder.withTrail();
        effect = builder.build();
    }

    @Override
    protected void applyEffects(final Location location) {
        Common.createExplosionWithEffect(position, strength, range, affectPlayersOnly, caster);
    }

    @Override
    protected void displayBolt(final Location location) {
        if (++displayTimer >= 7) {
            displayTimer = 0;
            FireworksEffect.createFireworks(position, effect);
        }
    }
}
