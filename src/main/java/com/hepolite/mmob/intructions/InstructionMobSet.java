package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InstructionMobSet extends Instruction
{
	public InstructionMobSet()
	{
		super("Set", -1);
		registerSubInstruction(new InstructionMobSetRole());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
	}

	@Override
	protected String getExplanation()
	{
		return "This instruction acts as the core instruction for all assigning instructions. This is not meant to be used, and will do nothing if used.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Attempted to run the 'Mob Set' instruction. Did you mistype something?");
		return false;
	}
}
