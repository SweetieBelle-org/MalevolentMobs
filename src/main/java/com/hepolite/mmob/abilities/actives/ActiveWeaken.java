package com.hepolite.mmob.abilities.actives;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public class ActiveWeaken extends Active {
    private final List<PotionEffect> possibleEffects = new LinkedList<PotionEffect>();

    private int effectsToApply = 0;

    public ActiveWeaken(final MalevolentMob mob, final float scale) {
        super(mob, "Weaken", Priority.NORMAL, scale);
    }

    @Override
    public void loadFromConfig(final Settings settings, final Settings alternative) {
        super.loadFromConfig(settings, alternative);

        effectsToApply = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);

        // Read up all the potion effects
        final List<String> effects = settings.getStringList(alternative, "effects");
        for (final String effect : effects) {
            final String[] components = effect.split("=");
            if (components.length == 5) {
                final PotionEffectType type = PotionEffectType.getByName(components[0]);
                if (type != null)
                    try {
                        final float duration = 20.0f * (Float.parseFloat(components[1]) + scale * Float.parseFloat(components[2]));
                        final float strength = Float.parseFloat(components[3]) + scale * Float.parseFloat(components[4]);
                        possibleEffects.add(new PotionEffect(type, (int) duration, (int) strength - 1));
                    } catch (final Exception exception) {
                        Log.log("Invalid format on numbers in string '" + effect + "'!", Level.WARNING);
                    }
                else
                    Log.log("Failed to parse potion effect type '" + components[0] + "', inalid type!", Level.WARNING);
            } else
                Log.log("Failed to parse potion effect string '" + effect + "'!", Level.WARNING);
        }
    }

    @Override
    public boolean canCast(final float healthFactor, final float distanceToTarget, final LivingEntity target) {
        return distanceToTarget <= 15.0f;
    }

    @Override
    public void cast(final LivingEntity target) {
        // Notify players of something happening
        mob.getEntity().getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_CAT_HISS, 1.0f, 0.5f);

        // Find effects to throw on the target
        final List<PotionEffect> mainList = new LinkedList<PotionEffect>(possibleEffects);
        final List<PotionEffect> potionEffectsToApply = new LinkedList<PotionEffect>();

        final int count = Math.min(mainList.size(), effectsToApply);
        for (int i = 0; i < count; i++) {
            final PotionEffect effect = mainList.get(random.nextInt(mainList.size()));
            potionEffectsToApply.add(effect);
            mainList.remove(effect);
        }

        // Apply the effects
        for (final PotionEffect effect : potionEffectsToApply)
            target.addPotionEffect(effect);
    }
}
