package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.mmob.handlers.ItemEffectHandler;
import com.hepolite.mmob.itemeffects.ItemEffect;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsItemEffects;

/**
 * The ItemEffect instruction will allow any held item to gain any given item effect
 */
public class InstructionItemEffect extends Instruction
{
	public InstructionItemEffect()
	{
		super("ItemEffect", -1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<effect> <parameters>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Adds the specified effect to the held item");
	}

	@Override
	protected String getExplanation()
	{
		return "If used by a player, this instruction will add a specific item effect to the item the player holds in their hand. Effects can be added to any item, even those that would not normally be able to have the given effect.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		// Only players can use this instruction, and at least one argument must be provided
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cThis command can only be used by a player");
			return true;
		}
		if (arguments.size() == 0)
		{
			sender.sendMessage("§cAt least one argument must be provided");
			return true;
		}

		// Player must hold an item in the hand
		Player player = (Player) sender;
		ItemStack itemInHand = player.getEquipment().getItemInMainHand();
		if (itemInHand == null || itemInHand.getType() == Material.AIR)
		{
			sender.sendMessage("§cYou must hold an item in your hand");
			return true;
		}

		// Grab the item effect from the item effect manager
		ItemEffect effect = ItemEffectHandler.getItemEffect(arguments.get(0));
		if (effect == null)
		{
			sender.sendMessage("§cInvalid item effect '" + arguments.get(0) + "'");
			return true;
		}

		// Effect must exist and must be enabled
		Settings settings = SettingsItemEffects.getConfig(effect.getName());
		if (settings == null)
		{
			sender.sendMessage("§cThe item effect '" + effect.getName() + "' doesn't exist");
			return true;
		}
		if (!settings.getBoolean("enable"))
		{
			sender.sendMessage("§cThe item effect '" + effect.getName() + "' is not enabled");
			return true;
		}

		// Build up the parameter list for the effect
		String dataString = "";
		for (int i = 1; i < arguments.size(); i++)
		{
			if (!dataString.isEmpty())
				dataString += ":";
			dataString += arguments.get(i);
		}
		try
		{
			effect.loadFromString(dataString);
		}
		catch (Exception exception)
		{
			sender.sendMessage("§cInvalid item effect data string '" + dataString + "' for item effect '" + effect.getName() + "'");
			return true;
		}

		if (ItemEffectHandler.addItemEffect(itemInHand, effect))
		{
			sender.sendMessage("§fAdded item effect '" + effect.getName() + "' with parameters '" + dataString + "' to item '" + itemInHand.getType() + "'");
			if (!effect.canBeUsedOnItem(itemInHand))
				sender.sendMessage("§cNote: The item effect '" + effect.getName() + "' can't normally be added to '" + itemInHand.getType().toString().toLowerCase() + "'! It might behave in unexpected ways.");
		}
		else
			sender.sendMessage("§cCouldn't add item effect '" + effect.getName() + "' with parameters '" + dataString + "' to item '" + itemInHand.getType() + "'");
		return false;
	}
}
