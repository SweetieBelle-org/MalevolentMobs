package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonSetCooldown extends Instruction
{
	public InstructionDungeonSetCooldown()
	{
		super("Cooldown", 1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<cooldown>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Sets the cooldown of the dungeon (or spawner) in ticks. Use -1 for an infinite number of ticks");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows changing the time it takes for the dungeon (or spawner) to come of cooldown once it has been cleared.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));

		// Assign the cooldown of the dungeon (Or spawner, if that was selected)
		try
		{
			int cooldown = Integer.parseInt(arguments.get(0));
			if (InstructionDungeon.validateSelectedDungeonSpawner(sender))
			{
				String spawner = InstructionDungeon.getSelectedDungeonSpawner(sender);
				dungeon.getSpawner(spawner).setCooldown(cooldown);
				sender.sendMessage(ChatColor.WHITE + "Changed the cooldown for " + spawner + " in " + dungeon.getName() + " to '" + cooldown + "'");
			}
			else
			{
				dungeon.setCooldown(cooldown);
				sender.sendMessage(ChatColor.WHITE + "Changed the cooldown for " + dungeon.getName() + " to '" + cooldown + "'");
			}
		}
		catch (Exception exception)
		{
			sender.sendMessage(ChatColor.RED + "Failed to parse number '" + arguments.get(0) + "'!");
			return true;
		}
		return false;
	}
}
