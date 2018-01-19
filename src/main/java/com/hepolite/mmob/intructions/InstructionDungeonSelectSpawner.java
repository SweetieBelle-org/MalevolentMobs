package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonSelectSpawner extends Instruction
{
	public InstructionDungeonSelectSpawner()
	{
		super("Spawner", new int[] { 0, 1 });
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<name>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Selects the given dungeon spawner");
	}

	@Override
	protected String getExplanation()
	{
		return "This allows you to select a dungeon spawner, where all subsequent spawner instructions will target the selected spawner.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));

		// Deselect dungeon spawner, if relevant
		if (arguments.size() == 0)
		{
			InstructionDungeon.setSelectedDungeonSpawner(sender, null);
			return true;
		}

		String name = arguments.get(0);

		// Validate spawner
		if (dungeon.getSpawner(name) == null)
		{
			sender.sendMessage(ChatColor.RED + "The dungeon spawner '" + name + "' doesn't exist!");
			return true;
		}

		// Select the dungeon spawner
		InstructionDungeon.setSelectedDungeonSpawner(sender, name);
		sender.sendMessage(ChatColor.WHITE + "Selected dungeon spawner '" + name + "'");
		return false;
	}
}
