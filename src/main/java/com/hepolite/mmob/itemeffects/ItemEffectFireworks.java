package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.hepolite.mmob.handlers.ProjectileHandler;
import com.hepolite.mmob.projectiles.ProjectileFirework;
import com.hepolite.mmob.settings.Settings;

/**
 * Effect that allows the player to launch fireworks at targets
 */
public class ItemEffectFireworks extends ItemEffectWand
{
	private float smallDamage = 0.0f;
	private int smallCooldown = 0;
	private float largeDamage = 0.0f;
	private int largeCooldown = 0;

	private float costPerSmallRocket = 0.0f;
	private float costPerLargeRocket = 0.0f;
	private float radiusSmallRocket = 0.0f;
	private float radiusLargeRocket = 0.0f;
	private int powerSmallRocket = 0;
	private int powerLargeRocket = 0;
	private float velocitySmallRocket = 0.0f;
	private float velocityLargeRocket = 0.0f;

	private List<String> lore;

	// Colors
	private final static Color lightBlue = DyeColor.LIGHT_BLUE.getFireworkColor();
	private final static Color cyan = DyeColor.CYAN.getFireworkColor();
	private final static Color blue = DyeColor.BLUE.getFireworkColor();
	private final static Color white = DyeColor.WHITE.getFireworkColor();
	private final static Color red = DyeColor.RED.getFireworkColor();
	private final static Color magenta = DyeColor.MAGENTA.getFireworkColor();
	private final static Color purple = DyeColor.PURPLE.getFireworkColor();

	public ItemEffectFireworks()
	{
		super("Fireworks");
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
		costPerSmallRocket = settings.getFloat("small.costPerRocket");
		radiusSmallRocket = settings.getFloat("small.radius");
		velocitySmallRocket = settings.getFloat("small.velocity");
		powerSmallRocket = settings.getInteger("small.rocketPower");
		costPerLargeRocket = settings.getFloat("large.costPerRocket");
		radiusLargeRocket = settings.getFloat("large.radius");
		velocityLargeRocket = settings.getFloat("large.velocity");
		powerLargeRocket = settings.getInteger("large.rocketPower");

		lore = settings.getStringList("lore");
	}

	@Override
	public void onLeftClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		event.setCancelled(true);
		if (!validateCooldown(player))
			return;

		// Validate that there is enough charge left
		if (charge < costPerLargeRocket)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&fNot enough charge to do this, need &c%.1f&f more charge", costPerLargeRocket - charge)));
			return;
		}

		setChargeInItem(item, charge - costPerLargeRocket);
		setCooldownTime(player.getUniqueId(), largeCooldown);

		// Spawn fireworks
		Firework firework = player.getWorld().spawn(player.getEyeLocation(), Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.setPower(powerLargeRocket);

		// Choose a random firework
		switch (random.nextInt(3))
		{
		case 0:
			meta.addEffect(FireworkEffect.builder().with(Type.STAR).withColor(lightBlue).withFade(cyan).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.STAR).withColor(cyan).withFade(blue).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.STAR).withColor(blue).withFade(purple).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.BALL_LARGE).withColor(white, lightBlue).withFade(cyan, blue).withTrail().withFlicker().build());
			break;
		case 1:
			meta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(red).withFade(purple).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.STAR).withColor(lightBlue, cyan).withFade(blue).withFlicker().build());
			meta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(red, purple, magenta).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.BALL_LARGE).withColor(purple, magenta, white).withTrail().withFlicker().build());
			break;
		case 2:
			meta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(purple, magenta, purple).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.STAR).withColor(purple, lightBlue, white).withFade(blue, cyan).withFlicker().build());
			meta.addEffect(FireworkEffect.builder().with(Type.BURST).withColor(white).withFade(lightBlue).withTrail().build());
			meta.addEffect(FireworkEffect.builder().with(Type.BALL_LARGE).withColor(blue, cyan).withFade(cyan, purple).withFlicker().build());
			break;
		default:
		}

		// Finalize rocket
		firework.setFireworkMeta(meta);
		firework.setVelocity(player.getEyeLocation().getDirection().multiply(velocityLargeRocket));
		ProjectileFirework projectile = new ProjectileFirework(player, true, firework, largeDamage, radiusLargeRocket, false);
		ProjectileHandler.addProjectile(projectile);
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		event.setCancelled(true);
		if (!validateCooldown(player))
			return;

		// Validate that there is enough charge left
		if (charge < costPerSmallRocket)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&fNot enough charge to do this, need &c%.1f&f more charge", costPerSmallRocket - charge)));
			return;
		}

		setChargeInItem(item, charge - costPerSmallRocket);
		setCooldownTime(player.getUniqueId(), smallCooldown);

		// Spawn fireworks
		Firework firework = player.getWorld().spawn(player.getEyeLocation(), Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.setPower(powerSmallRocket);

		// Choose a random firework
		switch (random.nextInt(3))
		{
		case 0:
			meta.addEffect(FireworkEffect.builder().with(Type.STAR).withColor(lightBlue).withFade(cyan).withTrail().build());
			break;
		case 1:
			meta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(red).withFade(purple).withTrail().build());
			break;
		case 2:
			meta.addEffect(FireworkEffect.builder().with(Type.BALL).withColor(purple, magenta, purple).withFlicker().build());
			break;
		default:
		}

		// Finalize rocket
		firework.setFireworkMeta(meta);
		firework.setVelocity(player.getEyeLocation().getDirection().multiply(velocitySmallRocket));
		ProjectileFirework projectile = new ProjectileFirework(player, true, firework, smallDamage, radiusSmallRocket, false);
		ProjectileHandler.addProjectile(projectile);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add(String.format("&fCan use up to &b%.1f&f charges worth of fun", 0.5f * maxCharge));
		list.add(String.format("&fCharge left: &b%.1f / %.1f&f [%s]", charge, maxCharge, getName()));
		super.addDescription(list);
		list.add(String.format("&fLeft-click to launch a large rocket"));
		list.add(String.format("&fthat does &b%.1f&f hearts of damage", 0.5f * largeDamage));
		list.add(String.format("&fRight-click to launch a small rocket"));
		list.add(String.format("&fthat does &b%.1f&f hearts of damage", 0.5f * smallDamage));
	}

	@Override
	public String getLore()
	{
		return lore.size() == 0 ? null : lore.get(random.nextInt(lore.size()));
	}

	@Override
	public String saveToString()
	{
		return super.saveToString() + ":" + String.format("%.0f:%d:%.0f:%d", 10.0f * smallDamage, smallCooldown, 10.0f * largeDamage, largeCooldown);
	}

	@Override
	public void loadFromString(String dataString)
	{
		super.loadFromString(dataString);
		String[] components = dataString.split(":");
		smallDamage = 0.1f * Float.parseFloat(components[3]);
		smallCooldown = Integer.parseInt(components[4]);
		largeDamage = 0.1f * Float.parseFloat(components[5]);
		largeCooldown = Integer.parseInt(components[6]);
	}
}
