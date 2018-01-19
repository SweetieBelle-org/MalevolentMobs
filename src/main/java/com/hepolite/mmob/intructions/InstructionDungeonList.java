package com.hepolite.mmob.intructions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonList extends Instruction
{
	public InstructionDungeonList()
	{
		super("List", new int[] { 0, 1 });
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
		syntaxes.add("<world>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Lists all dungeons that exist");
		descriptions.add("Lists all dungeons that exist within the given world");
	}

	@Override
	protected String getExplanation()
	{
		return "This instruction allows the caller to view all dungeons that currently exist within the dungeon system.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Collection<Dungeon> dungeons = DungeonHandler.getDungeons();
		Collection<Dungeon> dungeonsToShow = new ArrayList<Dungeon>();

		switch (arguments.size())
		{
		case 1:
			String world = arguments.get(0);
			for (Dungeon dungeon : dungeons)
			{
				if (dungeon.getWorld() != null && dungeon.getWorld().getName().equalsIgnoreCase(world))
					dungeonsToShow.add(dungeon);
			}
			sender.sendMessage(ChatColor.WHITE + "Dungeons found in world " + ChatColor.AQUA + world + ChatColor.WHITE + ":");
			break;

		default:
			dungeonsToShow.addAll(dungeons);
			sender.sendMessage(ChatColor.WHITE + "Dungeons found:");
		}

		if (dungeonsToShow.isEmpty())
			sender.sendMessage(ChatColor.RED + "No dungeons were found");
		else
		{
			for (Dungeon dungeon : dungeonsToShow)
				sender.sendMessage(ChatColor.WHITE + " - " + dungeon.getName());
		}

		return false;
	}
}
