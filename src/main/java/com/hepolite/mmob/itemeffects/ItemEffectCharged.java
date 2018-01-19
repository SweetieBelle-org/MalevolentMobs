package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * Effect that does damage to targets nearby the ht target, splitting the damage
 */
public class ItemEffectCharged extends ItemEffect
{
	private int targets = 0;
	private float damagePercentBoost = 0.0f;
	private float range = 0.0f;

	private float durabilityCostPerDamage = 0.0f;
	private float bowDurabilityCostMultiplier = 0.0f;
	
	private List<String> lore;

	public ItemEffectCharged()
	{
		super("Charged");
		incompatibleEffects = new String[] { getName() };
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
		DamageCause cause = event.getCause();
		if (cause == DamageCause.LIGHTNING)
			return;

		// Calculate damages
		double damage = (1.0 + damagePercentBoost) / (double) targets * event.getDamage();
		double totalDamage = damage;
		event.setDamage(damage);

		// Grab all entities within the area and randomly select up to "targets" amount of them
		List<LivingEntity> entities = Common.getEntitiesInRange(event.getEntity().getLocation(), range);
		int remaining = targets - 1;
		while (remaining > 0 && !entities.isEmpty())
		{
			LivingEntity entity = entities.remove(random.nextInt(entities.size()));
			if (entity == player)
				continue;
			if (Common.doDamage(damage, entity, player, DamageCause.LIGHTNING))
			{
				ParticleEffect.play(ParticleType.SNOWBALL, entity.getEyeLocation(), 0.05f, 10, 0.3f);
				totalDamage += damage;
				remaining--;
			}
		}

		// Damage weapon
		if (isItemBow(item))
			damageItem(item, bowDurabilityCostMultiplier * durabilityCostPerDamage * totalDamage);
		else
			damageItem(item, durabilityCostPerDamage * totalDamage);
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemWeapon(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (damagePercentBoost > 0)
		{
			list.add(String.format("&fDoes &b%.0f%%&f more damage and splits", 100.0f * damagePercentBoost));
			list.add(String.format("&fdamage between &b%d&f targets", targets));
		}
		else if (damagePercentBoost < 0)
		{
			list.add(String.format("&fDoes &c%.0f%%&f less damage and splits", -100.0f * damagePercentBoost));
			list.add(String.format("&fdamage between &b%d&f targets", targets));
		}
		else
			list.add(String.format("&fSplits damage between &c%d&f targets", targets));
		list.add(String.format("&fCan spread to targets up to &b%.1f&f meters away", range));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f:%d:%.0f", 100.0f * damagePercentBoost, targets, 10.0f * range);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		damagePercentBoost = 0.01f * Float.parseFloat(components[0]);
		targets = Integer.parseInt(components[1]);
		range = 0.1f * Float.parseFloat(components[2]);
	}
}
