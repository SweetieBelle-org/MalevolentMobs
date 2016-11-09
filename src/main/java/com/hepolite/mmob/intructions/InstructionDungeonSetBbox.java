package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.dungeons.DungeonSpawner;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonSetBbox extends Instruction
{
	public InstructionDungeonSetBbox()
	{
		super("BBox", new int[] { 0, 3, 6 });
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
		syntaxes.add("<x> <y> <z>");
		syntaxes.add("<x1> <y1> <z1> <x2> <y2> <z2>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Removes the bounding box from the dungeon (or spawner)");
		descriptions.add("Assigns one corner of the dungeon (or spawner). Must be performed twice!");
		descriptions.add("Assigns the bounding box of the dungeon (or spawner)");
	}

	@Override
	protected String getExplanation()
	{
		return "Assigns the size of the dungeon (or spawner), as well as where in the world it is located.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));
		DungeonSpawner spawner = dungeon.getSpawner(InstructionDungeon.getSelectedDungeonSpawner(sender));

		// Set the bounding box of the dungeon
		List<Integer> numbers = parseInts(arguments);
		if (numbers.size() == 6)
		{
			Vector min = new Vector(Math.min(numbers.get(0), numbers.get(3)), Math.min(numbers.get(1), numbers.get(4)), Math.min(numbers.get(2), numbers.get(5)));
			Vector max = new Vector(Math.max(numbers.get(0), numbers.get(3)), Math.max(numbers.get(1), numbers.get(4)), Math.max(numbers.get(2), numbers.get(5)));
			if (spawner == null)
			{
				dungeon.setBoundingBox(min, max);
				sender.sendMessage(ChatColor.WHITE + "Changed " + dungeon.getName() + "'s bounding box to '" + min + "'; '" + max + "'");
			}
			else
			{
				spawner.setBoundingBox(min, max);
				sender.sendMessage(ChatColor.WHITE + "Changed " + spawner.getName() + "'s (in " + dungeon.getName() + ") bounding box to '" + min + "'; '" + max + "'");
			}
		}
		else if (numbers.size() == 3)
		{
			Vector v = new Vector(numbers.get(0), numbers.get(1), numbers.get(2));
			if (spawner == null)
			{
				if (dungeon.getBoundingBoxMin() == null)
					dungeon.setBoundingBox(v, null);
				else if (dungeon.getBoundingBoxMax() == null)
				{
					Vector b = dungeon.getBoundingBoxMin();
					Vector min = new Vector(Math.min(v.getX(), b.getX()), Math.min(v.getY(), b.getY()), Math.min(v.getZ(), b.getZ()));
					Vector max = new Vector(Math.max(v.getX(), b.getX()), Math.max(v.getY(), b.getY()), Math.max(v.getZ(), b.getZ()));
					dungeon.setBoundingBox(min, max);
				}
				else
					dungeon.setBoundingBox(v, null);
				sender.sendMessage(ChatColor.WHITE + "Changed " + dungeon.getName() + "'s bounding box to '" + dungeon.getBoundingBoxMin() + "'; '" + dungeon.getBoundingBoxMax() + "'");
			}
			else
			{
				if (spawner.getBoundingBoxMin() == null)
					spawner.setBoundingBox(v, null);
				else if (spawner.getBoundingBoxMax() == null)
				{
					Vector b = spawner.getBoundingBoxMin();
					Vector min = new Vector(Math.min(v.getX(), b.getX()), Math.min(v.getY(), b.getY()), Math.min(v.getZ(), b.getZ()));
					Vector max = new Vector(Math.max(v.getX(), b.getX()), Math.max(v.getY(), b.getY()), Math.max(v.getZ(), b.getZ()));
					spawner.setBoundingBox(min, max);
				}
				else
					spawner.setBoundingBox(v, null);
				sender.sendMessage(ChatColor.WHITE + "Changed " + spawner.getName() + "'s (in " + dungeon.getName() + ") bounding box to '" + spawner.getBoundingBoxMin() + "'; '" + spawner.getBoundingBoxMax() + "'");
			}
		}
		else
		{
			if (spawner == null)
			{
				dungeon.setBoundingBox(null, null);
				sender.sendMessage(ChatColor.WHITE + "Removed " + dungeon.getName() + "'s bounding box");
			}
			else
			{
				spawner.setBoundingBox(null, null);
				sender.sendMessage(ChatColor.WHITE + "Removed " + spawner.getName() + "'s (in " + dungeon.getName() + ") bounding box");
			}
		}
		return false;
	}

}
