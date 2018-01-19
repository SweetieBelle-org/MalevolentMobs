package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.mobs.MalevolentMob;

public class InstructionMobSetRole extends Instruction
{
	public InstructionMobSetRole()
	{
		super("Role", 1);
		registerSubInstruction(new InstructionMobSetRoleReset());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<role>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Makes you Malevolent, with the given role");
	}

	@Override
	protected String getExplanation()
	{
		return "If used by a player, this instruction allows the caller to apply a Malevolent Mob role on themselves.";
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
		String role = arguments.get(0);
		MalevolentMob mob = MobHandler.getMalevolentMob(player);
		if (mob != null)
		{
			player.sendMessage(ChatColor.RED + "You already have a role! Reset your role before you can change to a new one.");
			return true;
		}

		mob = MobHandler.makeMobMalevolent(player, role);
		if (mob.getRole() == null)
			player.sendMessage(ChatColor.RED + "Invalid role " + role);
		else
			player.sendMessage(ChatColor.RED + "You now have the Malevolent Mob role " + role);

		return false;
	}
}
