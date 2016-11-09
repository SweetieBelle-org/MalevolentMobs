package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonDelete extends Instruction
{
	public InstructionDungeonDelete()
	{
		super("Delete", -1);
		registerSubInstruction(new InstructionDungeonDeleteSpawner());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Deletes the currently selected dungeon");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows removal of dungeons that exists, but are undesired or flawed. Once the dungeon has been deleted, it will be gone forever.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));

		// Validate that the user wants to remove the dungeon
		if (arguments.size() != 1 || !arguments.get(0).equals("confirm"))
		{
			sender.sendMessage(ChatColor.RED + "Please confirm that you want to delete the dungeon '" + dungeon.getName() + "'");
			sender.sendMessage(ChatColor.RED + "Use '/mMob Dungeon Delete confirm' to delete the dungeon. Note that there is no way to get it back once it has been deleted!");
			return true;
		}

		sender.sendMessage(ChatColor.WHITE + "Deleted the dungeon '" + dungeon.getName() + "'");
		DungeonHandler.removeDungeon(dungeon.getName());
		return false;
	}
}
