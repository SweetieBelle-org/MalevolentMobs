package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonReset extends Instruction
{
	public InstructionDungeonReset()
	{
		super("Reset", 0);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("");
	}

	@Override
	protected String getExplanation()
	{
		return "Resets the currently selected dungeon, will despawn all spawned mobs and put the dungeon off cooldown, ready to be activated again";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));
		dungeon.reset();
		sender.sendMessage(ChatColor.WHITE + "Reset dungeon '" + dungeon.getName() + "'");
		return false;
	}
}
