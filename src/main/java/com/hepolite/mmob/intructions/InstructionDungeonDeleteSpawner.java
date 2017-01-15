package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonDeleteSpawner extends Instruction
{
	public InstructionDungeonDeleteSpawner()
	{
		super("Spawner", -1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Deletes the currently selected dungeon spawner");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows removal of dungeons spawners that exists, but are undesired or flawed. Once the spawner has been deleted, it will be gone forever.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeonSpawner(sender))
		{
			sender.sendMessage(ChatColor.RED + "Invalid dungeon spawner selected!");
			return true;
		}
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));
		String spawner = InstructionDungeon.getSelectedDungeonSpawner(sender);

		// Validate that the user wants to remove the dungeon
		if (arguments.size() != 1 || !arguments.get(0).equals("confirm"))
		{
			sender.sendMessage(ChatColor.RED + "Please confirm that you want to delete the spawner '" + spawner + "'");
			sender.sendMessage(ChatColor.RED + "Use '/mMob Dungeon Delete Spawner confirm' to delete the spawner. Note that there is no way to get it back once it has been deleted!");
			return true;
		}

		sender.sendMessage(ChatColor.WHITE + "Deleted the spawner '" + spawner + "'");
		dungeon.deleteSpawner(spawner);
		return false;
	}
}
