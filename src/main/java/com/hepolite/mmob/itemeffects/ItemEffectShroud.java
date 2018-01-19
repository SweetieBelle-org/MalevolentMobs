package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.settings.Settings;

/**
 * Item effects with the shroud effect will blind the targets that are attacked
 */
public class ItemEffectShroud extends ItemEffect
{
	private int duration = 0;

	private float durabilityCostPerUse = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> lore;

	public ItemEffectShroud()
	{
		super("Shroud");
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUse = settings.getFloat("durabilityCostPerBlock");
		bowDurabilityCostMultiplier = settings.getFloat("bowDurabilityCostMultiplier");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		if (event.getEntity() instanceof LivingEntity)
		{
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0));
			if (isItemBow(item))
				damageItem(item, bowDurabilityCostMultiplier * durabilityCostPerUse);
			else
				damageItem(item, durabilityCostPerUse);
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
		list.add(String.format("&fBlinds targets for &b%.2f&f seconds", (float) duration / 20.0f));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%d", duration);
	}

	@Override
	public void loadFromString(String dataString)
	{
		duration = Integer.parseInt(dataString);
	}
}
