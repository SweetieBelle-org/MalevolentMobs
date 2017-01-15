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

public class ProjectileThaumicBolt extends ProjectileBolt {
    private LivingEntity caster = null;

    private float strength = 0.0f;
    private final float damageIncrease;
    private boolean affectPlayersOnly = true;

    public ProjectileThaumicBolt(final LivingEntity caster, final boolean factorInGravity, final float strength, final float damageIncrease, final boolean affectPlayersOnly) {
        super(caster, factorInGravity);
        this.caster = caster;
        this.strength = strength;
        this.damageIncrease = damageIncrease;
        this.affectPlayersOnly = affectPlayersOnly;
    }

    @Override
    public void onTick() {
        super.onTick();
        strength *= 1.0f + damageIncrease;
    }

    @Override
    protected void applyEffects(final Location location) {
        final List<LivingEntity> entities = Common.getEntitiesInRange(location, 1.0f);
        while (!entities.isEmpty()) {
            final LivingEntity entity = entities.remove(random.nextInt(entities.size()));
            if (!affectPlayersOnly || entity instanceof Player)
                if (Common.doDamage(strength, entity, caster, DamageCause.MAGIC))
                    break;
        }
        ParticleEffect.play(ParticleType.SPELL_WITCH, location, 0.07f, 18, 1.5f);
        location.getWorld().playSound(location, Sound.ENTITY_ENDERDRAGON_HURT, 0.5f, 0.5f);
    }

    @Override
    protected void displayBolt(final Location location) {
        ParticleEffect.play(ParticleType.SPELL_WITCH, location, 0.07f, 12, 0.2f);
    }
}
