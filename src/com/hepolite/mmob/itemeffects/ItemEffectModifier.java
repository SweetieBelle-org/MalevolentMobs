package com.hepolite.mmob.itemeffects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.settings.Settings;

/**
 * Effect that allows item effects to be changed, swapped, added or extracted from items
 */
public class ItemEffectModifier extends ItemEffect
{
	public ItemEffectModifier()
	{
		super("Modifier");
		// TODO: Finish this class
	}

	@Override
	public void loadSettingsFromConfigFile(Settings settings)
	{
	}

	@Override
	public boolean canBeUsedOnItem(ItemStack item)
	{
		return item.getType() == Material.WRITTEN_BOOK;
	}

	public void onRightClick(PlayerInteractEvent event, Player player, ItemStack item)
	{
		player.sendMessage(ChatColor.AQUA + "Hmm... Maybe not yet...");
		// Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.BLUE + "Modifier");
		// player.openInventory(inventory);
	}

	@Override
	public void addDescription(List<String> list)
	{
		list.add("&fRight-click...?");
	}

	@Override
	public String getLore()
	{
		return "&cBut nothing happened...&r";
	}

	@Override
	public String saveToString()
	{
		return "";
	}

	@Override
	public void loadFromString(String dataString)
	{
	}
}
