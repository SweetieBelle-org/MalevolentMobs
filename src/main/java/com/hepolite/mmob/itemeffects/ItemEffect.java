package com.hepolite.mmob.itemeffects;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hepolite.mmob.handlers.ItemEffectHandler;
import com.hepolite.mmob.settings.Settings;

public abstract class ItemEffect {
    // Control variables
    private String name = "UNNAMED_EFFECT";
    protected final static Random random = new Random();
    protected String[] incompatibleEffects = new String[] {};

    protected ItemEffect(final String name) {
        this.name = name;
    }

    /** Returns the name of the effect */
    public String getName() {
        return name;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////
    // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
    // ///////////////////////////////////////////////////////////////////////////////////////

    /** Event where the effect is ticked (Occurs once every minute) */
    public void onTick(final Player player, final ItemStack item) {
    }

    /** Event where the player is left-clicking with the item effect active */
    public void onLeftClick(final PlayerInteractEvent event, final Player player, final ItemStack item) {
    }

    /** Event where the player is right-clicking with the item effect active */
    public void onRightClick(final PlayerInteractEvent event, final Player player, final ItemStack item) {
    }

    /** Event where the player attacking with the item effect active */
    public void onAttacking(final EntityDamageByEntityEvent event, final Player player, final ItemStack item) {
    }

    /** Event where the player is attacked with the item effect active */
    public void onAttacked(final EntityDamageEvent event, final Player player, final ItemStack item) {
    }

    /** Event where the player fires arrows from a bow */
    public void onFireArrow(final EntityShootBowEvent event, final Player player, final ItemStack item) {
    }

    /** Event where the player breaks a block */
    public void onBlockBreak(final BlockBreakEvent event, final Player player, final ItemStack item) {
    }

    /** Allows the effect to add a description of what it does to the item it is added to */
    public abstract void addDescription(List<String> list);

    /** Allows the effect to add some lore to the item; return null to add no lore */
    public abstract String getLore();

    /** Allows the effect to be saved into a given item. The format should be 'parameter1:parameter2:...' */
    public abstract String saveToString();

    /** Allows the effect to be loaded from a given item. The format should be 'parameter1:parameter2:...' */
    public abstract void loadFromString(String dataString);

    /** Loads up some basic settings from the config file */
    public abstract void loadSettingsFromConfigFile(Settings settings);

    // /////////////////////////////////////////////////////////////////////////////////////////////

    /** Returns an array of item effects that are incompatible with this effect */
    public String[] getIncompatibleEffects() {
        return incompatibleEffects;
    }

    /** Returns true if the item must be worn to take effect */
    public boolean mustBeWorn() {
        return true;
    }

    /** Returns true if the item effect can be added to the given item */
    public boolean canBeUsedOnItem(final ItemStack item) {
        return true;
    }

    /** Returns true if the given item is armor */
    public boolean isItemArmor(final ItemStack item) {
        final String materialName = item.getType().toString().toLowerCase();
        return materialName.contains("helmet") || materialName.contains("chestplate") || materialName.contains("leggings") || materialName.contains("boots");
    }

    /** Returns true if the given item is a sword */
    public boolean isItemSword(final ItemStack item) {
        final String materialName = item.getType().toString().toLowerCase();
        return materialName.contains("sword");
    }

    /** Returns true if the given item is a bow */
    public boolean isItemBow(final ItemStack item) {
        return item.getType() == Material.BOW;
    }

    /** Returns true if the given item is a weapon */
    public boolean isItemWeapon(final ItemStack item) {
        return isItemSword(item) || isItemBow(item);
    }

    /** Returns true if the given item is a tool (axe, shovel or pickaxe) */
    public boolean isItemTool(final ItemStack item) {
        final String materialName = item.getType().toString().toLowerCase();
        return materialName.contains("axe") || materialName.contains("hoe") || materialName.contains("spade");
    }

    /** Applies damage to the item, removing it if it was completely destroyed; returns the updated item */
    protected void damageItem(final ItemStack item, double amount) {
        // If the item can be damaged, damage it
        if (item != null && amount > 0.0) {
            // Figure out how much to damage the item
            final float enchantmentLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
            if (enchantmentLevel > 0.0f)
                amount *= 3.0f / (3.0f + enchantmentLevel); // TODO: Move this hardcoded number into the config file

            final double remainder = amount - Math.floor(amount);
            item.setDurability((short) (item.getDurability() + Math.floor(amount) + (random.nextFloat() < remainder ? 1.0 : 0.0)));

            // Remove item if it was used up
            if (item.getDurability() >= item.getType().getMaxDurability())
                if (item.getAmount() <= 1) {
                    ItemEffectHandler.removeItemEffects(item);
                    final ItemMeta meta = item.getItemMeta();
                    meta.setLore(new LinkedList<String>());
                    item.setItemMeta(meta);
                } else {
                    item.setAmount(item.getAmount() - 1);
                    item.setDurability((short) 0);
                }
        }
    }

    /** Repairs the item */
    protected void repairItem(final ItemStack item, final double amount) {
        // If the item can be repaired, repair it
        if (item != null && amount > 0.0) {
            final double remainder = amount - Math.floor(amount);
            item.setDurability((short) Math.max(0.0, item.getDurability() - Math.floor(amount) - (random.nextFloat() < remainder ? 1.0 : 0.0)));
        }
    }
}
