package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * Effect that allow the player to attack with electricity and summon a lightning bolt where they click
 */
public class ItemEffectPowerline extends ItemEffectWand
{
	private float chargeCostPerLightningBolt = 0.0f;
	private float chargeCostPerLightningStrike = 0.0f;
	private float lightningStrikeRadius = 0.0f;

	private float lightningBoltDistanceCheck = 0.0f;
	private int lightningBoltCooldown = 0;
	private float lightningStrikeDistanceCheck = 0.0f;
	private int lightningStrikeCooldown = 0;

	private List<String> lore;

	public ItemEffectPowerline()
	{
		super("Powerline");
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		lightningBoltDistanceCheck = settings.getFloat("lightningBoltDistanceCheck");
		lightningBoltCooldown = settings.getInteger("lightningBoltCooldown");
		lightningStrikeDistanceCheck = settings.getFloat("lightningStrikeDistanceCheck");
		lightningStrikeCooldown = settings.getInteger("lightningStrikeCooldown");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onLeftClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		event.setCancelled(true);
		if (!validateCooldown(player))
			return;

		// Validate that there is enough charge left
		if (charge < chargeCostPerLightningBolt)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&fNot enough charge to do this, need &c%.1f&f more charge", chargeCostPerLightningBolt - charge)));
			return;
		}

		// Find obstructions, do nothing if no target was found
		Location start = player.getEyeLocation();
		Location end = start.clone().add(start.getDirection().multiply(lightningBoltDistanceCheck));
		Location location = Common.getObstruction(start, end, player);
		if (location == null)
			return;

		for (LivingEntity entity : Common.getEntitiesInLocation(location))
		{
			if (Common.doDamage(chargeCostPerLightningBolt, entity, player, DamageCause.LIGHTNING))
			{
				setChargeInItem(item, charge - chargeCostPerLightningBolt);
				setCooldownTime(player.getUniqueId(), lightningBoltCooldown);
				ParticleEffect.play(ParticleType.SNOWBALL, entity.getEyeLocation(), 0.05f, 8, 0.7f);
				entity.getWorld().playSound(entity.getEyeLocation(), Sound.ENTITY_FIREWORK_BLAST, 0.6f, 0.0f);
				break;
			}
		}
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		event.setCancelled(true);
		if (!validateCooldown(player))
			return;

		// Validate that there is enough charge left
		if (charge < chargeCostPerLightningStrike)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&fNot enough charge to do this, need &c%.1f&f more charge", chargeCostPerLightningStrike - charge)));
			return;
		}

		// Get the block the player is aiming at
		Location start = player.getEyeLocation();
		Location end = start.clone().add(start.getDirection().multiply(lightningStrikeDistanceCheck));
		Location location = Common.getObstruction(start, end, player);
		if (location == null)
			return;

		setChargeInItem(item, charge - chargeCostPerLightningStrike);
		setCooldownTime(player.getUniqueId(), lightningStrikeCooldown);
		Common.createLightningStrike(location, player, chargeCostPerLightningStrike, lightningStrikeRadius, false);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fCan use up to &b%.1f&f charges worth of damage", 0.5f * maxCharge));
		list.add(String.format("&fCharge left: &b%.1f / %.1f&f [%s]", charge, maxCharge, getName()));
		super.addDescription(list);
		list.add("&fLeft-click to use a close-range lightning bolt that");
		list.add(String.format("&fdoes &b%.1f&f hearts of damage to one target", 0.5f * chargeCostPerLightningBolt));
		list.add(String.format("&fRight-click to summon a lightning strike that does &b%.1f&f hearts", 0.5f * chargeCostPerLightningStrike));
		list.add(String.format("&fof damage to all targets within &b%.1fm&f of where you aim", lightningStrikeRadius));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return super.saveToString() + ":" + String.format("%.0f:%.0f:%.0f", 10.0f * chargeCostPerLightningBolt, 10.0f * chargeCostPerLightningStrike, 10.0f * lightningStrikeRadius);
	}

	@Override
	public void loadFromString(String dataString)
	{
		super.loadFromString(dataString);
		String[] components = dataString.split(":");
		chargeCostPerLightningBolt = 0.1f * Float.parseFloat(components[3]);
		chargeCostPerLightningStrike = 0.1f * Float.parseFloat(components[4]);
		lightningStrikeRadius = 0.1f * Float.parseFloat(components[5]);
	}
}
