package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonInfo extends Instruction
{
	public InstructionDungeonInfo()
	{
		super("Info", 0);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getExplanation()
	{
		// TODO Auto-generated method stub
		return "Not yet implemented.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		// TODO Auto-generated method stub
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));
		
		return true;
	}
}
