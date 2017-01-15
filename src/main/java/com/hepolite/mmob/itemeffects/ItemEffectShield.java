package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

public abstract class ItemEffectShield extends ItemEffect {
    protected float strength = 0.0f;
    private final DamageCause[] causesToBlock;

    private float durabilityCostPerDamage = 0.0f;

    private List<String> goodLore, badLore;

    protected ItemEffectShield(final String name, final DamageCause[] causesToBlock) {
        super(name);
        this.causesToBlock = causesToBlock;
    }

    @Override
    public void loadSettingsFromConfigFile(final Settings settings) {
        durabilityCostPerDamage = settings.getFloat("durabilityCostPerDamage");

        goodLore = settings.getStringList("lore.good");
        badLore = settings.getStringList("lore.bad");
    }

    @Override
    public void onAttacked(final EntityDamageEvent event, final Player player, final ItemStack item) {
        final boolean isAttackMagic = Common.isAttackMagic(event);
        for (final DamageCause cause : causesToBlock) {
            // SkillAPI attacks are treated as entity attacks, so if we have a bulwark armor we don't want it to block skills that are magic
            if (cause == DamageCause.ENTITY_ATTACK && isAttackMagic)
                continue;
            // Likewise, we want the magic shield block the skills
            if (cause == event.getCause() || cause == DamageCause.MAGIC && isAttackMagic) {
                final double damageBlocked = strength * event.getDamage();
                event.setDamage(event.getDamage() - damageBlocked);
                damageItem(item, durabilityCostPerDamage * damageBlocked);
                return;
            }
        }
    }

    @Override
    public boolean canBeUsedOnItem(final ItemStack item) {
        return isItemArmor(item);
    }

    @Override
    public String getLore() {
        List<String> lores;
        if (strength >= 0.0f)
            lores = goodLore;
        else
            lores = badLore;
        return lores.size() == 0 ? null : lores.get(random.nextInt(lores.size()));
    }

    @Override
    public String saveToString() {
        return String.format("%.0f", 100.0f * strength);
    }

    @Override
    public void loadFromString(final String dataString) {
        strength = 0.01f * Float.parseFloat(dataString);
    }
}
