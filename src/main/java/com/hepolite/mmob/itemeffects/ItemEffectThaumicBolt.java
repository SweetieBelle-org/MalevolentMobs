package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.ProjectileHandler;
import com.hepolite.mmob.projectiles.ProjectileThaumicBolt;
import com.hepolite.mmob.settings.Settings;

/**
 * Effect that launches a magic arrow that does medium magic damage to the target it hits
 */
public class ItemEffectThaumicBolt extends ItemEffectWand
{
	private float damage = 0.0f;
	private float speed = 0.0f;

	private int cooldownTime = 0;
	private float damageIncrease = 0.0f;

	private List<String> lore;

	public ItemEffectThaumicBolt()
	{
		super("Thaumic_Bolt");
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		cooldownTime = settings.getInteger("cooldownTime");
		damageIncrease = settings.getFloat("damageIncrease");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onLeftClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		event.setCancelled(true);
		if (!validateCooldown(player))
			return;

		// Validate that there is enough charge left
		if (charge < damage)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&fNot enough charge to do this, need &c%.1f&f more charge", damage - charge)));
			return;
		}

		// Get the block the player is aiming at
		setChargeInItem(item, charge - damage);
		setCooldownTime(player.getUniqueId(), cooldownTime);

		ProjectileThaumicBolt projectile = new ProjectileThaumicBolt(player, false, damage, damageIncrease, false);
		projectile.setVelocity(player.getEyeLocation().getDirection().multiply(speed));
		ProjectileHandler.addProjectile(projectile);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_HURT, 0.5f, -0.7f);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fCan use up to &b%.1f&f charges worth of damage", 0.5f * maxCharge));
		list.add(String.format("&fCharge left: &b%.1f / %.1f&f [%s]", charge, maxCharge, getName()));
		super.addDescription(list);
		list.add("&fLeft-click to launch a thaumic bolt that");
		list.add(String.format("&fdoes &b%.1f&f hearts of damage to one target", 0.5f * damage));
		list.add("&fDamage increases the further the bolt travels");
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return super.saveToString() + ":" + String.format("%.0f:%.0f", 10.0f * damage, 10.0f * speed);
	}

	@Override
	public void loadFromString(String dataString)
	{
		super.loadFromString(dataString);
		String[] components = dataString.split(":");
		damage = 0.1f * Float.parseFloat(components[3]);
		speed = 0.1f * Float.parseFloat(components[4]);
	}
}
