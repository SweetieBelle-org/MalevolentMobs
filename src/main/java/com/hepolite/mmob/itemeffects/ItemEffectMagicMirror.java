package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;

/**
 * Effect that returns some incoming magic damage, as magic damage
 */
public class ItemEffectMagicMirror extends ItemEffect
{
	protected float strength = 0.0f;

	private float durabilityCostPerDamage = 0.0f;
	
	private List<String> lore;

	public ItemEffectMagicMirror()
	{
		super("Magic_Mirror");
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
		if (Common.isAttackMagic(event))
		{
			Entity attacker = Common.getAttacker((EntityDamageByEntityEvent) event);
			if (attacker != null && attacker instanceof LivingEntity)
			{
				double damage = strength * event.getDamage();
				if (damage >= 1.0)
				{
					if (Common.doDamage(damage, (LivingEntity) attacker, player, DamageCause.CUSTOM))
						damageItem(item, durabilityCostPerDamage * damage);
				}
			}
		}
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemArmor(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fMirrors &b%.0f%%&f of incoming magic-based damage to the attacker", 100.0f * strength));
	}
	
	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%.0f", 100.0f * strength);
	}

	@Override
	public void loadFromString(String dataString)
	{
		strength = 0.01f * Float.parseFloat(dataString);
	}
}
