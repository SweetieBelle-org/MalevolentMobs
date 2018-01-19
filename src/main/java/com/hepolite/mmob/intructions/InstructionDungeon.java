package com.hepolite.mmob.intructions;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.dungeons.Dungeon;
import com.hepolite.mmob.handlers.DungeonHandler;

public class InstructionDungeon extends Instruction
{
	// Control variables
	private final static HashMap<String, String> selectedDungeons = new HashMap<String, String>();
	private final static HashMap<String, String> selectedDungeonSpawners = new HashMap<String, String>();

	/* Initialization */
	public InstructionDungeon()
	{
		super("Dungeon", -1);
		registerSubInstruction(new InstructionDungeonList());
		registerSubInstruction(new InstructionDungeonInfo());
		registerSubInstruction(new InstructionDungeonCreate());
		registerSubInstruction(new InstructionDungeonDelete());
		registerSubInstruction(new InstructionDungeonSelect());
		registerSubInstruction(new InstructionDungeonRename());
		registerSubInstruction(new InstructionDungeonRemove());
		registerSubInstruction(new InstructionDungeonReset());
		registerSubInstruction(new InstructionDungeonSet());
		registerSubInstruction(new InstructionDungeonAdd());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
	}

	@Override
	protected String getExplanation()
	{
		return "This instruction acts as the core instruction for all dungeon-related instructions. This is not meant to be used, and will do nothing if used.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Attempted to run the 'Dungeon' instruction. Did you mistype something?");
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	/** Returns the dungeon the given command sender has selected */
	public static String getSelectedDungeon(CommandSender sender)
	{
		return selectedDungeons.get(getSenderIdentifier(sender));
	}

	/** Returns the dungeon spawner the given command sender has selected */
	public static String getSelectedDungeonSpawner(CommandSender sender)
	{
		return selectedDungeonSpawners.get(getSenderIdentifier(sender));
	}

	/** Sets the dungeon the given command sender has selected */
	public static void setSelectedDungeon(CommandSender sender, String dungeon)
	{
		if (dungeon == null)
			selectedDungeons.remove(getSenderIdentifier(sender));
		else
			selectedDungeons.put(getSenderIdentifier(sender), dungeon);
	}

	/** Sets the dungeon the given command sender has selected */
	public static void setSelectedDungeonSpawner(CommandSender sender, String spawner)
	{
		if (spawner == null)
			selectedDungeonSpawners.remove(getSenderIdentifier(sender));
		else
			selectedDungeonSpawners.put(getSenderIdentifier(sender), spawner);
	}

	/** Validates that the given sender has selected a proper dungeon. Returns true if the dungeon is valid */
	public static boolean validateSelectedDungeon(CommandSender sender)
	{
		String dungeon = getSelectedDungeon(sender);
		if (DungeonHandler.getDungeon(dungeon) == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid dungeon selected!");
			return false;
		}
		return true;
	}

	/** Returns true if the given dungeon spawner exists */
	public static boolean validateSelectedDungeonSpawner(CommandSender sender)
	{
		Dungeon dungeon = DungeonHandler.getDungeon(getSelectedDungeon(sender));
		if (dungeon == null)
			return false;
		return dungeon.getSpawner(getSelectedDungeonSpawner(sender)) != null;
	}
}
