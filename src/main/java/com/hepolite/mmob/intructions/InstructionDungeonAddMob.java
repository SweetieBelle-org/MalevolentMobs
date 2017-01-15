package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.dungeons.DungeonSpawner;
import com.hepolite.mmob.handlers.DungeonHandler;
import com.hepolite.mmob.handlers.RoleHandler;
import com.hepolite.mmob.mobs.MobRole;

public class InstructionDungeonAddMob extends Instruction
{
	public InstructionDungeonAddMob()
	{
		super("Mob", new int[] { 1, 2 });
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<type>");
		syntaxes.add("<type> <role>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Adds a normal mob to the spawner");
		descriptions.add("Adds a Malevolent Mob mob to the spawner");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows adding various mobs that will spawn at the currently selected spawner.";
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
		DungeonSpawner spawner = dungeon.getSpawner(InstructionDungeon.getSelectedDungeonSpawner(sender));

		// Grab arguments
		String type = arguments.get(0);
		MobRole role = (arguments.size() == 2 ? RoleHandler.getRole(arguments.get(1)) : null);

		// Add a normal mob
		if (arguments.size() == 1)
		{
			spawner.addMob(type, null);
			sender.sendMessage(ChatColor.WHITE + "Added mob '" + type + "' to " + spawner.getName() + "(in " + dungeon.getName() + ")");
		}
		else
		{
			if (role == null)
			{
				sender.sendMessage(ChatColor.RED + "The role '" + arguments.get(1) + "' is invalid!");
				return true;
			}
			spawner.addMob(type, role);
			sender.sendMessage(ChatColor.WHITE + "Added mob '" + type + "' with role '" + role.getName() + "' to " + spawner.getName() + "(in " + dungeon.getName() + ")");
		}
		return false;
	}
}
