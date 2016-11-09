package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonCreateSpawner extends Instruction
{
	public InstructionDungeonCreateSpawner()
	{
		super("Spawner", 1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<name>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Creates a new dungeon spawner with the given name");
	}

	@Override
	protected String getExplanation()
	{
		return "This instruction allows the creation of dungeons spawner with a command. If there is a spawner with the given name already defined, it will not be overwritten.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));
		String name = arguments.get(0);

		// Make sure there's not going to be any dungeon overwrite here
		if (dungeon.getSpawner(name) != null)
		{
			sender.sendMessage(ChatColor.RED + "The spawner '" + name + "' has already been defined!");
			return true;
		}

		// Create the new dungeon
		dungeon.createSpawner(name);
		sender.sendMessage(ChatColor.WHITE + "Created spawner '" + name + "'");
		return false;
	}
}
