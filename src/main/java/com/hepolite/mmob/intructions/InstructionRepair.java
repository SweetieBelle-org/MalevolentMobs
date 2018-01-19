package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InstructionRepair extends Instruction
{
	public InstructionRepair()
	{
		super("Repair", 0);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Repairs the item that is held");
	}

	@Override
	protected String getExplanation()
	{
		return "If used by a player, the item the player is currently holding in the hands will be repaired, provided the item can be repaired.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		// Only players can use this instruction
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "This command can only be used by a player");
			return true;
		}

		// Player must hold an item in the hand
		Player player = (Player) sender;
		ItemStack itemInHand = player.getEquipment().getItemInMainHand();
		if (itemInHand == null || itemInHand.getType() == Material.AIR)
		{
			sender.sendMessage(ChatColor.RED + "You must hold an item in your hand");
			return true;
		}

		// Repair the item, if possible
		if (itemInHand.getType().getMaxDurability() != 0)
		{
			itemInHand.setDurability((short) 0);
			player.getEquipment().setItemInMainHand(itemInHand);
			sender.sendMessage(ChatColor.AQUA + "Repaired the item that was held! Enjoy!");
		}
		else
			sender.sendMessage(ChatColor.RED + "Your item can't be repaired!");
		return false;
	}
}
