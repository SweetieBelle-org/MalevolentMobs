package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonCreate extends Instruction
{
	public InstructionDungeonCreate()
	{
		super("Create", 1);
		registerSubInstruction(new InstructionDungeonCreateSpawner());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<name>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Creates a new dungeon with the given name");
	}

	@Override
	protected String getExplanation()
	{
		return "This instruction allows the creation of dungeons with a command. If there is a dungeon with the given name already defined, it will not be overwritten.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		String name = arguments.get(0);

		// Make sure there's not going to be any dungeon overwrite here
		if (DungeonHandler.getDungeon(name) != null)
		{
			sender.sendMessage(ChatColor.RED + "The dungeon '" + name + "' has already been defined!");
			return true;
		}

		// Create the new dungeon
		DungeonHandler.createDungeon(name);
		sender.sendMessage(ChatColor.WHITE + "Created dungeon '" + name + "'");
		return false;
	}
}
