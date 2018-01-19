package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be added to weapons; will turn part of the damage to true damage
 */
@SuppressWarnings("deprecation")
public class ItemEffectPiercing extends ItemEffect
{
	private float percent = 0.0f;
	private float flat = 0.0f;

	private float durabilityCostPerDamage = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> lore;

	public ItemEffectPiercing()
	{
		super("Piercing");
		incompatibleEffects = new String[] { getName(), "Armor_Shredder" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerDamage = settings.getFloat("durabilityCostPerDamage");
		bowDurabilityCostMultiplier = settings.getFloat("bowDurabilityCostMultiplier");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		if (event.getCause() == DamageCause.CUSTOM || !(event.getEntity() instanceof LivingEntity))
			return;

		double armorBlocking = event.getDamage(DamageModifier.ARMOR);
		double trueDamage = Math.max(percent * armorBlocking - flat, armorBlocking);
		event.setDamage(DamageModifier.ARMOR, event.getDamage(DamageModifier.ARMOR) - trueDamage);

		if (isItemBow(item))
			damageItem(item, trueDamage * bowDurabilityCostMultiplier * durabilityCostPerDamage);
		else
			damageItem(item, trueDamage * durabilityCostPerDamage);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemWeapon(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (percent > 0.0f)
			list.add(String.format("&fDeals &b%.0f%%&f of the damage as true damage", 100.0f * percent));
		if (flat > 0.0f)
			list.add(String.format("&fDeals &b%.0f&f points of the damage as true damage", flat));
	}

	@Override
	public String getLore()
	{
		return (lore.size() == 0 ? null : lore.get(random.nextInt(lore.size())));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%.0f", 100.0f * percent, flat);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		percent = 0.01f * Float.parseFloat(components[0]);
		flat = Float.parseFloat(components[1]);
	}
}
