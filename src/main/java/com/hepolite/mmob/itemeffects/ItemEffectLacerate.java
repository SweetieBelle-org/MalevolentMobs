package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be added to weapons; will increase the damage based on the remaining/max health of the target
 */
public class ItemEffectLacerate extends ItemEffect
{
	private float percentMax = 0.0f;
	private float percentLeft = 0.0f;

	private float durabilityCostPerDamage = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> lore;

	public ItemEffectLacerate()
	{
		super("Lacerate");
		incompatibleEffects = new String[] { getName(), "Sharpness" };
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
		// Need a valid entity
		LivingEntity entity = (LivingEntity) (event.getEntity() instanceof LivingEntity ? event.getEntity() : null);
		if (entity == null)
			return;

		double additionalDamage = percentMax * entity.getMaxHealth() + percentLeft * entity.getHealth();
		event.setDamage(additionalDamage + event.getDamage());
		if (isItemBow(item))
			damageItem(item, bowDurabilityCostMultiplier * durabilityCostPerDamage * additionalDamage);
		else
			damageItem(item, durabilityCostPerDamage * additionalDamage);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemWeapon(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (percentMax != 0.0f)
			list.add(String.format("&fDeals &b%.1f%%&f of the target's max health as additional damage", 100.0f * percentMax));
		if (percentLeft != 0.0f)
			list.add(String.format("&fDeals &b%.1f%%&f of the target's current health as additional damage", 100.0f * percentLeft));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%.0f", 1000.0f * percentMax, 1000.0f * percentLeft);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		percentMax = 0.001f * Float.parseFloat(components[0]);
		percentLeft = 0.001f * Float.parseFloat(components[1]);
	}
}
