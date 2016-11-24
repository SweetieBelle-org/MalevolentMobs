package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.settings.Settings;

/**
 * Effect that can be added to weapons; will amplify the damage dealt to the target
 */
public class ItemEffectSharpness extends ItemEffect
{
	private float percentMob = 0.0f;
	private float flatMob = 0.0f;
	private float percentAll = 0.0f;
	private float flatAll = 0.0f;

	private float durabilityCostPerDamage = 0.0f;

	private List<String> loreGoodMob, loreBadMob, loreGoodAll, loreBadAll;

	public ItemEffectSharpness()
	{
		super("Sharpness");
		incompatibleEffects = new String[] { getName(), "Lacerate" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerDamage = settings.getFloat("durabilityCostPerDamage");

		loreGoodMob = settings.getStringList("lore.goodMob");
		loreBadMob = settings.getStringList("lore.badMob");
		loreGoodAll = settings.getStringList("lore.goodAll");
		loreBadAll = settings.getStringList("lore.badAll");
	}

	@Override
	public void onAttacking(EntityDamageByEntityEvent event, Player player, ItemStack item)
	{
		boolean isMalevolentTarget = (event.getEntity() instanceof LivingEntity && MobHandler.isMobMalevolent((LivingEntity) event.getEntity()));

		double factor = (isMalevolentTarget ? 1.0 + percentMob : 1.0) * (1.0 + percentAll);
		double newDamage = factor * event.getDamage() + (isMalevolentTarget ? flatMob : 0.0f) + flatAll;
		damageItem(item, durabilityCostPerDamage * (newDamage - event.getDamage()));
		event.setDamage(newDamage);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemSword(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (percentMob > 0.0f)
			list.add(String.format("&fDeals &b%.0f%%&f more damage to Malevolent mobs", 100.0f * percentMob));
		else if (percentMob < 0.0f)
			list.add(String.format("&fDeals &c%.0f%%&f less damage to Malevolent mobs", -100.0f * percentMob));
		if (flatMob > 0.0f)
			list.add(String.format("&fDeals &b%.1f&f more hearts of damage to Malevolent mobs", 0.5f * flatMob));
		else if (flatMob < 0.0f)
			list.add(String.format("&fDeals &c%.1f&f less hearts of damage to Malevolent mobs", -0.5f * flatMob));

		if (percentAll > 0.0f)
			list.add(String.format("&fDeals &b%.0f%%&f more damage to all targets", 100.0f * percentAll));
		else if (percentAll < 0.0f)
			list.add(String.format("&fDeals &c%.0f%%&f less damage to all targets", -100.0f * percentAll));
		if (flatAll > 0.0f)
			list.add(String.format("&fDeals &b%.1f&f more hearts of damage to all targets", 0.5f * flatAll));
		else if (flatAll < 0.0f)
			list.add(String.format("&fDeals &c%.1f&f less hearts of damage to all targets", -0.5f * flatAll));
	}

	@Override
	public String getLore()
	{
		String lore = "";
		if (percentMob > 0.0f || flatMob > 0.0f)
			lore += loreGoodMob.size() == 0 ? "" : loreGoodMob.get(random.nextInt(loreGoodMob.size()));
		if (percentAll > 0.0f || flatAll > 0.0f)
			lore += loreGoodAll.size() == 0 ? "" : loreGoodAll.get(random.nextInt(loreGoodAll.size()));
		if (percentMob < 0.0f || flatMob < 0.0f)
			lore += loreBadMob.size() == 0 ? "" : loreBadMob.get(random.nextInt(loreBadMob.size()));
		if (percentAll < 0.0f || flatAll < 0.0f)
			lore += loreBadAll.size() == 0 ? "" : loreBadAll.get(random.nextInt(loreBadAll.size()));
		return lore.isEmpty() ? null : lore;
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%.0f:%.0f:%.0f", 100.0f * percentMob, flatMob, 100.0f * percentAll, flatAll);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		percentMob = 0.01f * Float.parseFloat(components[0]);
		flatMob = Float.parseFloat(components[1]);
		percentAll = 0.01f * Float.parseFloat(components[2]);
		flatAll = Float.parseFloat(components[3]);
	}
}
