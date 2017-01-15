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
 * Effect that can be added on weapons; will apply a potion effect to the target. Designed to be used with wither and poison only, but other effects are possible
 */
public class ItemEffectPotionEffect extends ItemEffect
{
	private PotionEffectType type;
	private int strength = 1;
	private int duration = 0;

	private float durabilityCostPerUsePerLevel = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> lore;

	public ItemEffectPotionEffect(String name, PotionEffectType type)
	{
		super(name);
		this.type = type;
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUsePerLevel = settings.getFloat("durabilityCostPerUsePerLevel");
		bowDurabilityCostMultiplier = settings.getFloat("bowDurabilityCostMultiplier");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		if (event.getEntity() instanceof LivingEntity)
		{
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(type, duration, strength - 1));
			if (isItemBow(item))
				damageItem(item, bowDurabilityCostMultiplier * (float) strength * durabilityCostPerUsePerLevel);
			else
				damageItem(item, (float) strength * durabilityCostPerUsePerLevel);
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
		list.add("&f" + String.format(getName() + "s targets (Strength &b%d&f) for &b%.1f&f seconds", strength, (float) duration / 20.0f));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%d:%d", strength, duration);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		strength = Integer.parseInt(components[0]);
		duration = Integer.parseInt(components[1]);
	}
}
