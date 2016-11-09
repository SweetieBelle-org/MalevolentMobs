package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * The runic shield applies a protective shield that blocks almost all forms of damage completely, for as long as the shield has some charge left
 */
public class ItemEffectRunicShield extends ItemEffectCharger
{
	private float durabilityCostPerDamage = 0.0f;
	
	private List<String> lore;

	public ItemEffectRunicShield()
	{
		super("Runic_Shield");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerDamage = settings.getFloat("durabilityCostPerDamage");
		
		lore = settings.getStringList("lore");
	}

	@Override
	public void onAttacked(EntityDamageEvent event, Player player, ItemStack item)
	{
		// Skip certain damage sources
		DamageCause cause = event.getCause();
		if (cause == DamageCause.STARVATION || cause == DamageCause.SUFFOCATION || cause == DamageCause.DROWNING || cause == DamageCause.FALL || cause == DamageCause.WITHER || cause == DamageCause.POISON)
			return;

		// Apply protection against most forms of damage
		double damageBlocked = Math.min(charge, event.getDamage());
		event.setDamage(event.getDamage() - damageBlocked);
		damageItem(item, durabilityCostPerDamage * damageBlocked);

		if (damageBlocked > 0.0)
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 0.75f, 0.0f);

		// Update the charge
		setChargeInItem(item, charge - (float) damageBlocked);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemArmor(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fBlocks up to &b%.2f&f hearts of damage", 0.5f * maxCharge));
		list.add(String.format("&fCharge left: &b%.1f / %.1f&f [%s]", charge, maxCharge, getName()));
		super.addDescription(list);
	}
	
	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}
}
