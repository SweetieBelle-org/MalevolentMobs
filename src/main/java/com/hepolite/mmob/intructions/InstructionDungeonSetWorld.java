package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonSetWorld extends Instruction
{
	public InstructionDungeonSetWorld()
	{
		super("World", new int[] { 0, 1 });
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
		descriptions.add("Places the dungeon in the world the caller is in");
		descriptions.add("Places the dungeon in the given world");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the dungeon to be moved from one world to another. Will not change the coordinates of the dungeon.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));

		World world = null;

		// If no argument is passed, grab the location of the sender
		if (arguments.size() == 0)
		{
			Location location = getSenderLocation(sender);
			if (location == null)
			{
				sender.sendMessage(ChatColor.RED + "This cannot be done from console!");
				return true;
			}
			world = location.getWorld();
		}
		else
			world = Bukkit.getWorld(arguments.get(0));

		// Validate world
		if (world == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid world" + (arguments.size() == 0 ? "" : " '" + arguments.get(0) + "'") + "!");
			return true;
		}
		dungeon.setWorld(world);
		sender.sendMessage(ChatColor.WHITE + "Moved " + dungeon.getName() + " to world '" + world.getName() + "'");
		return false;
	}

}
