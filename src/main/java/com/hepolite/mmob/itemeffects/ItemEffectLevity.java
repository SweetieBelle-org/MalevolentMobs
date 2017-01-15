package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.ProjectileHandler;
import com.hepolite.mmob.projectiles.ProjectileArrow;
import com.hepolite.mmob.settings.Settings;

/**
 * The levity effect causes arrows fired from bows to have no gravity and be faster/deal more damage
 */
public class ItemEffectLevity extends ItemEffect
{
	private boolean ignoreGravity = false;
	private float speedMultiplier = 0.0f;

	private float durabilityCostPerUse = 0.0f;
	private float durabilityCostPerPercent = 0.0f;

	private List<String> loreGood, loreNeutral, loreBad;

	public ItemEffectLevity()
	{
		super("Levity");
		incompatibleEffects = new String[] { getName(), "Sharpness" };
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		durabilityCostPerUse = settings.getFloat("durabilityCostPerUse");
		durabilityCostPerPercent = settings.getFloat("durabilityCostPerPercent");

		loreGood = settings.getStringList("lore.good");
		loreNeutral = settings.getStringList("lore.neutral");
		loreBad = settings.getStringList("lore.bad");
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return isItemBow(item);
	}

	@Override
	public void addDescription(List<String> list)
	{
		if (ignoreGravity)
			list.add("&fArrows fired from this bow will not be affected by gravity.");
		if (speedMultiplier > 0.0f)
		{
			list.add(String.format("&fArrows fired from this bow will be &b%.0f%%&f faster and have", 100.0f * speedMultiplier));
			list.add("&fthe damage &bincreased&f by approximately the same amount.");
		}
		else if (speedMultiplier < 0.0f)
		{
			list.add(String.format("&fArrows fired from this bow will be &c%.0f%%&f slower and have", -100.0f * speedMultiplier));
			list.add("&fthe damage &creduced&f by approximately the same amount.");
		}
	}

	@Override
	public void onFireArrow(EntityShootBowEvent event, Player player, ItemStack item)
	{
		float itemDamage = 0.0f;

		// Remove gravity from arrow, and speed the arrow up
		Arrow arrow = (Arrow) event.getProjectile();
		if (speedMultiplier > 0.0f)
		{
			arrow.setVelocity(arrow.getVelocity().multiply(1.0f + speedMultiplier));
			itemDamage += 100.0f * speedMultiplier * durabilityCostPerPercent;
		}
		if (ignoreGravity)
		{
			ProjectileArrow projectile = new ProjectileArrow(player, false, arrow);
			ProjectileHandler.addProjectile(projectile);
			itemDamage += durabilityCostPerUse;
		}

		// Damage item
		if (itemDamage != 0.0f)
			damageItem(item, itemDamage);
	}

	@Override
	public String getLore()
	{
		List<String> lores;
		if (speedMultiplier >= 0.0f)
			lores = loreGood;
		else if (speedMultiplier < 0.0f && !ignoreGravity)
			lores = loreBad;
		else
			lores = loreNeutral;
		return lores.size() == 0 ? null : lores.get(random.nextInt(lores.size()));
	}

	@Override
	public String saveToString()
	{
		return String.format("%b:%.0f", ignoreGravity, 100.0f * speedMultiplier);
	}

	@Override
	public void loadFromString(String dataString)
	{
		String[] components = dataString.split(":");
		ignoreGravity = Boolean.parseBoolean(components[0]);
		speedMultiplier = 0.01f * Float.parseFloat(components[1]);
	}
}
