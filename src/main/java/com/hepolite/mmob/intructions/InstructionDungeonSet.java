package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InstructionDungeonSet extends Instruction
{
	public InstructionDungeonSet()
	{
		super("Set", -1);
		registerSubInstruction(new InstructionDungeonSetWorld());
		registerSubInstruction(new InstructionDungeonSetBbox());
		registerSubInstruction(new InstructionDungeonSetMessage());
		registerSubInstruction(new InstructionDungeonSetCooldown());
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
		return "This instruction acts as the core instruction for most dungeon-changing instructions. This is not meant to be used, and will do nothing if used.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Attempted to run the 'Dungeon Set' instruction. Did you mistype something?");
		return false;
	}
}
