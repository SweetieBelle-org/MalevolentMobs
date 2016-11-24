package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonRename extends Instruction
{
	public InstructionDungeonRename()
	{
		super("Rename", 1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<name>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Renames the dungeon to the new name");
	}

	@Override
	protected String getExplanation()
	{
		return "Will rename the selected dungeon to the new name. Will not overwrite any dungeons that has already been defined.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		String newName = arguments.get(0);
		String oldName = InstructionDungeon.getSelectedDungeon(sender);

		// Make sure there's no other dungeon with the given name
		if (DungeonHandler.getDungeon(newName) != null)
		{
			sender.sendMessage(ChatColor.RED + "A dungeon with the name '" + newName + "' already exists!");
			return true;
		}

		DungeonHandler.renameDungeon(oldName, newName);
		InstructionDungeon.setSelectedDungeon(sender, newName);
		sender.sendMessage(ChatColor.WHITE + "Renamed the dungeon '" + oldName + "' to '" + newName + "'");
		return false;
	}
}
