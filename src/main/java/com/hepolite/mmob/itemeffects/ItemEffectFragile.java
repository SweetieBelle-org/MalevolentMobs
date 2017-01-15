package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that cause the item it is on to take extra damage when used
 */
public class ItemEffectFragile extends ItemEffect {
    private float durabilityCost;

    private List<String> lore;

    public ItemEffectFragile() {
        super("Fragile");
        incompatibleEffects = new String[] { getName(), "Repair" };
    }

    @Override
    public void loadSettingsFromConfigFile(final Settings settings) {
        lore = settings.getStringList("lore");
    }

    @Override
    public boolean canBeUsedOnItem(final ItemStack item) {
        return item.getType().getMaxDurability() > 0;
    }

    @Override
    public void onLeftClick(final PlayerInteractEvent event, final Player player, final ItemStack item) {
        damageItem(item, durabilityCost);
    }

    @Override
    public void onRightClick(final PlayerInteractEvent event, final Player player, final ItemStack item) {
        damageItem(item, durabilityCost);
    }

    @Override
    public void onAttacked(final EntityDamageEvent event, final Player player, final ItemStack item) {
        damageItem(item, durabilityCost);
    }

    @Override
    public void onBlockBreak(final BlockBreakEvent event, final Player player, final ItemStack item) {
        damageItem(item, durabilityCost);
    }

    @Override
    public void addDescription(final List<String> list) {
        list.add(String.format("&fDamages the item by &c%.2f&f durability when used", durabilityCost));
    }

    @Override
    public String getLore() {
        return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
    }

    @Override
    public String saveToString() {
        return String.format("%d", (int) (100.0f * durabilityCost));
    }

    @Override
    public void loadFromString(final String dataString) {
        durabilityCost = 0.01f * Float.parseFloat(dataString);
    }
}
