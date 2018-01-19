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
 * Effect that can be added to weapons; will slow targets and apply some damage to them
 */
public class ItemEffectFrost extends ItemEffect
{
	private int strength = 1;
	private int duration = 0;
	private float damage = 0.0f;

	private float durabilityCostPerUse = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;

	private List<String> loreBase, loreDamage;

	public ItemEffectFrost()
	{
		super("Frost");
		incompatibleEffects = new String[] { getName(), "Fiery" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUse = settings.getFloat("durabilityCostPerUse");
		bowDurabilityCostMultiplier = settings.getFloat("bowDurabilityCostMultiplier");

		loreBase = settings.getStringList("lore.Base");
		loreDamage = settings.getStringList("lore.Damage");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		event.setDamage(damage + event.getDamage());

		if (event.getEntity() instanceof LivingEntity)
		{
			((LivingEntity) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, strength - 1));
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
		list.add(String.format("&fSlows down targets by &b%d%%&f for &b%.1f&f seconds", 15 * strength, (float) duration / 20.0f));
		if (damage != 0.0f)
			list.add(String.format("&fDeals &b%.1f&f more hearts of damage to all targets", 0.5f * damage));
	}

	@Override
	public String getLore()
	{
		return (loreBase.size() == 0 ? null : loreBase.get(random.nextInt(loreBase.size()))) + (loreDamage.size() == 0 || damage <= 0.0f ? "" : " " + loreDamage.get(random.nextInt(loreDamage.size())));
	}

	@Override
	public String saveToString()
	{
		return String.format("%d:%d:%.0f", 15 * strength, duration, damage);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		strength = (int) Math.round(Float.parseFloat(components[0]) / 15.0f);
		duration = Integer.parseInt(components[1]);
		damage = Float.parseFloat(components[2]);
	}
}
