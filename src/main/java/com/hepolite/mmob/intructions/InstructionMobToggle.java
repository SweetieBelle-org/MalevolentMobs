package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.mobs.MalevolentMob;

public class InstructionMobToggle extends Instruction
{
	public InstructionMobToggle()
	{
		super("Toggle", 1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("ai");
		syntaxes.add("playerside");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Toggles the AI of the mob role, if enabled active abilities will be cast");
		descriptions.add("Toggles the side the mob is on, if on the player side, it will attack mobs and vice versa");
	}

	@Override
	protected String getExplanation()
	{
		return "This instruction allows the caller to toggle various properties of the currently active role";
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

		Player player = (Player) sender;
		String field = arguments.get(0).toLowerCase();
		MalevolentMob mob = MobHandler.getMalevolentMob(player);
		if (mob == null)
		{
			sender.sendMessage(ChatColor.RED + "You need to have a role active to use this instruction");
			return true;
		}

		switch (field)
		{
		case "ai":
			mob.setAIControlled(!mob.isAIControlled());
			sender.sendMessage(ChatColor.AQUA + "AI controlled: " + mob.isAIControlled());
			break;

		case "playerside":
			mob.setOnPlayerSide(!mob.isOnPlayerSide());
			sender.sendMessage(ChatColor.AQUA + "Player side: " + mob.isOnPlayerSide());
			break;

		default:
			sender.sendMessage(ChatColor.RED + "Unknown field " + field);
		}

		return false;
	}
}
