package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.hepolite.mmob.handlers.MobHandler;

public class InstructionMobSetRoleReset extends Instruction
{
	public InstructionMobSetRoleReset()
	{
		super("Reset", new int[] { 0, 1 });
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
		syntaxes.add("<health>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Removes your Malevolent state");
		descriptions.add("Removes your Malevolent state and sets your maximum health to the specified value");
	}

	@Override
	protected String getExplanation()
	{
		return "If used by a player, this instruction allows the caller to remove a Malevolent Mob role from themselves. All potion effects will be cleared from the caller as well.";
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		// Only players can use this instruction
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "This command can only be used by a player");
			return true;
		}

		Player player = (Player) sender;
		MobHandler.unmakeMobMalevolent(player);

		if (arguments.size() > 0)
			player.setMaxHealth(Double.parseDouble(arguments.get(0)));
		else
			player.setMaxHealth(20.0);
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());

		player.sendMessage(ChatColor.RED + "You are no longer Malevolent");
		return false;
	}
}
