package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonSelect extends Instruction
{
	public InstructionDungeonSelect()
	{
		super("Select", new int[] { 0, 1 });
		registerSubInstruction(new InstructionDungeonSelectSpawner());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<name>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Selects the given dungeon");
	}

	@Override
	protected String getExplanation()
	{
		return "This allows you to select a dungeon, where all subsequent dungeon instructions will target the selected dungeon.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		// Deselect dungeon, if relevant
		if (arguments.size() == 0)
		{
			InstructionDungeon.setSelectedDungeon(sender, null);
			return true;
		}

		String name = arguments.get(0);

		// Make sure the dungeon exists
		if (DungeonHandler.getDungeon(name) == null)
		{
			sender.sendMessage(ChatColor.RED + "The dungeon '" + name + "' doesn't exist!");
			return true;
		}

		// Create the new dungeon
		InstructionDungeon.setSelectedDungeon(sender, name);
		sender.sendMessage(ChatColor.WHITE + "Selected dungeon '" + name + "'");
		return false;
	}
}
