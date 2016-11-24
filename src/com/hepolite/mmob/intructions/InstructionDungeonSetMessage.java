package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeonSetMessage extends Instruction
{
	public InstructionDungeonSetMessage()
	{
		super("Message", -1);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("enter <entering message>");
		syntaxes.add("enterCleared <entering cleared message>");
		syntaxes.add("leave <leaving message>");
		syntaxes.add("leaveCleared <leaving cleared message>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Message displayed when entering dungeon");
		descriptions.add("Message displayed when entering dungeon when cleared");
		descriptions.add("Message displayed when leaving dungeon");
		descriptions.add("Message displayed when leaving dungeon when cleared");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows setting the message a player will receive when entering or leaving the dungeon, either it is cleared or not. If not specifying the message string, the message will be removed";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!InstructionDungeon.validateSelectedDungeon(sender))
			return true;
		Dungeon dungeon = DungeonHandler.getDungeon(InstructionDungeon.getSelectedDungeon(sender));

		// Validate the arguments
		if (arguments.size() == 0)
		{
			sender.sendMessage(ChatColor.RED + "Must specify at least one argument!");
			return true;
		}

		String group = arguments.remove(0);
		String message = compressList(arguments, " ");

		// Assign the message
		if (group.equals("enter"))
			dungeon.setEnterMessage(message);
		else if (group.equals("enterCleared"))
			dungeon.setEnterClearedMessage(message);
		else if (group.equals("leave"))
			dungeon.setLeaveMessage(message);
		else if (group.equals("leaveCleared"))
			dungeon.setLeaveClearedMessage(message);
		else
		{
			sender.sendMessage(ChatColor.RED + "Must specify a proper category (enter, enterCleared, leave, leaveCleared)!");
			return true;
		}
		if (message == null)
			sender.sendMessage(ChatColor.WHITE + "Removed message '" + group + "' for dungeon " + dungeon.getName());
		else
			sender.sendMessage(ChatColor.WHITE + "Changed message '" + group + "' for dungeon " + dungeon.getName() + " to '" + message + "'");
		return false;
	}

}
