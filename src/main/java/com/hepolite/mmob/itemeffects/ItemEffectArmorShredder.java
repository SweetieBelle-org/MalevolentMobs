package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be added to weapons; will shred the armor of malevolent mobs, reducing their defense
 */
public class ItemEffectArmorShredder extends ItemEffect
{
	private float percent = 0.0f;
	private float flat = 0.0f;

	private float durabilityCostPerPercent = 0.0f;
	private float durabilityCostPerPoint = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> goodLore, neutralLore, badLore;

	public ItemEffectArmorShredder()
	{
		super("Armor_Shredder");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerPercent = 100.0f * settings.getFloat("durabilityCostPerPercent");
		durabilityCostPerPoint = settings.getFloat("durabilityCostPerPoint");
		bowDurabilityCostMultiplier = settings.getFloat("bowDurabilityCostMultiplier");

		goodLore = settings.getStringList("lore.good");
		neutralLore = settings.getStringList("lore.neutral");
		badLore = settings.getStringList("lore.bad");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		if (event.getEntity() instanceof LivingEntity && MobHandler.isMobMalevolent((LivingEntity) event.getEntity()))
		{
			MobHandler.getMalevolentMob((LivingEntity) event.getEntity()).addArmorPenetration(percent, flat);
			if (isItemBow(item))
				damageItem(item, bowDurabilityCostMultiplier * (durabilityCostPerPercent * percent + durabilityCostPerPoint * flat));
			else
				damageItem(item, durabilityCostPerPercent * percent + durabilityCostPerPoint * flat);
		}
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
			list.add(String.format("&fShreds &b%.0f%%&f of the Malevolent mobs' armor", 100.0f * percent));
		else if (percent < 0.0f)
			list.add(String.format("&fDoubles &c%.0f%%&f of the Malevolent mobs' armor", -100.0f * percent));
		if (flat > 0.0f)
			list.add(String.format("&fPenetrates &b%.0f&f points of the Malevolent mobs' armor", flat));
		else if (flat < 0.0f)
			list.add(String.format("&fDoubles &c%.0f&f points of the Malevolent mobs' armor", -flat));
	}

	@Override
	public String getLore()
	{
		List<String> lores;
		if (percent >= 0.0f && flat >= 0.0f)
			lores = goodLore;
		else if (percent <= 0.0f && flat <= 0.0f)
			lores = badLore;
		else
			lores = neutralLore;
		return lores.size() == 0 ? null : lores.get(random.nextInt(lores.size()));
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
