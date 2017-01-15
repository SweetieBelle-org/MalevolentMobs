package com.hepolite.mmob.itemeffects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * The treasure item effect will allow the players to get some goodies
 */
public class ItemEffectTreasure extends ItemEffectChest {
    private static HashMap<ItemStack, Float> itemValues = new HashMap<ItemStack, Float>();

    private List<String> lore;

    public ItemEffectTreasure() {
        super("Treasure");
        incompatibleEffects = new String[] { getName(), "Relic" };
    }

    @Override
    public void loadSettingsFromConfigFile(final Settings settings) {
        // Load up the item values from the config file
        itemValues.clear();
        final List<String> items = settings.getStringList("itemValues");
        for (final String item : items)
            try {
                final String[] components = item.split(":");
                itemValues.put(Common.getItemStack(components[0]), Float.parseFloat(components[1]));
            } catch (final Exception exception) {
                Log.log("Failed to parse the item '" + item + "' when reading item values...", Level.WARNING);
                Log.log(exception.getLocalizedMessage(), Level.WARNING);
            }

        lore = settings.getStringList("lore");
    }

    @Override
    protected void addItems(final Inventory inventory, final Location location) {
        final List<Entry<ItemStack, Float>> affordableItems = new LinkedList<Entry<ItemStack, Float>>(itemValues.entrySet());
        while (affordableItems.size() != 0) {
            final int index = random.nextInt(affordableItems.size());
            final Entry<ItemStack, Float> entry = affordableItems.get(index);

            // If the item can't be afforded, remove it from the list
            if (entry.getValue() > value)
                affordableItems.remove(index);
            else {
                value -= entry.getValue();
                inventory.addItem(entry.getKey());
            }
        }
    }

    @Override
    public boolean canBeUsedOnItem(final ItemStack item) {
        return item.getType() == Material.CHEST;
    }

    @Override
    public void addDescription(final List<String> list) {
        list.add(String.format("&fContains &b%.1f&f units worth of treasure", value));
        list.add("&fRight-click on the ground to use the chest");
    }

    @Override
    public String getLore() {
        return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
    }
}
