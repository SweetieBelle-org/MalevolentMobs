package com.hepolite.mmob.itemeffects;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.ItemEffectHandler;

public abstract class ItemEffectWand extends ItemEffectCharger
{
	private final static HashMap<UUID, CooldownNode> cooldownNodes = new HashMap<UUID, CooldownNode>();

	protected ItemEffectWand(String name)
	{
		super(name);
		incompatibleEffects = new String[] { getName() };
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType() == Material.STICK || item.getType() == Material.BLAZE_ROD || item.getType() == Material.BONE;
	}

	/** Validates the cooldown for the item effect */
	protected final boolean validateCooldown(Player player)
	{
		int time = getCooldownTime(player.getUniqueId());
		if (time > 0)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&fWand is charging, need &c%.1f&f more seconds", (float) time / 20.0f)));
			return false;
		}
		return true;
	}

	// //////////////////////////////////////////////////////////////////////////////

	/** The cooldown node stores which wand the player used, and when it was used. */
	private final static class CooldownNode
	{
		private final HashMap<String, Integer> cooldownTimes = new HashMap<String, Integer>();
	}

	/** Sets the time (in ticks) the user has to wait before being able to use the wand again */
	protected final void setCooldownTime(UUID user, int time)
	{
		// Grab a cooldown node
		if (!cooldownNodes.containsKey(user))
			cooldownNodes.put(user, new CooldownNode());
		CooldownNode node = cooldownNodes.get(user);

		// Store cooldown
		node.cooldownTimes.put(getName(), ItemEffectHandler.getCurrentTickNumber() + time);
	}

	/** Returns the time (in ticks) the user has to wait before being able to use the wand again */
	protected final int getCooldownTime(UUID user)
	{
		CooldownNode node = cooldownNodes.get(user);
		if (node == null || !node.cooldownTimes.containsKey(getName()))
			return -1;
		return node.cooldownTimes.get(getName()) - ItemEffectHandler.getCurrentTickNumber();
	}

	/** Returns whether the user is able to use the wand or not, with regards to cooldown */
	protected final boolean isCooldownTimeOver(UUID user)
	{
		return getCooldownTime(user) <= 0;
	}
}
