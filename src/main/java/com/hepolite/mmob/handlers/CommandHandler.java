package com.hepolite.mmob.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.Log;
import com.hepolite.mmob.intructions.Instruction;
import com.hepolite.mmob.intructions.InstructionDungeon;
import com.hepolite.mmob.intructions.InstructionHelp;
import com.hepolite.mmob.intructions.InstructionItemEffect;
import com.hepolite.mmob.intructions.InstructionMob;
import com.hepolite.mmob.intructions.InstructionReloadConfig;
import com.hepolite.mmob.intructions.InstructionRepair;
import com.hepolite.mmob.intructions.InstructionRestart;
import com.hepolite.mmob.intructions.InstructionSaveConfig;
import com.hepolite.mmob.intructions.InstructionSpawn;

public class CommandHandler
{
	private static Map<String, Instruction> instructions = new TreeMap<String, Instruction>();

	public CommandHandler()
	{
		registerInstruction(new InstructionHelp());
		registerInstruction(new InstructionReloadConfig());
		registerInstruction(new InstructionSaveConfig());
		registerInstruction(new InstructionRestart());
		registerInstruction(new InstructionSpawn());
		registerInstruction(new InstructionItemEffect());
		registerInstruction(new InstructionRepair());
		registerInstruction(new InstructionDungeon());
		registerInstruction(new InstructionMob());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Registrates a new instruction for use */
	private void registerInstruction(Instruction instruction)
	{
		if (instructions.get(instruction.getName()) != null)
			Log.log("Attempted to register instruction '" + instruction.getName() + "'. Another instruction with that name has already been registered and will be overwritten!", Level.WARNING);
		instructions.put(instruction.getName().toLowerCase(), instruction);
	}

	/** Returns the instruction with the given name */
	private Instruction getInstruction(String name)
	{
		return instructions.get(name.toLowerCase());
	}

	/** Returns a list of all registered instructions */
	public static List<Instruction> getInstructions()
	{
		List<Instruction> list = new ArrayList<Instruction>();
		for (Instruction instruction : instructions.values())
		{
			list.add(instruction);
			instruction.getSubInstructions(list);
		}
		return list;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	// EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS // EVENTS //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Called whenever a command is performed */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!cmd.getName().equals("mMob"))
			return false;
		if (args.length == 0)
		{
			sender.sendMessage(cmd.getDescription());
			return true;
		}

		// Run the instruction, if it was a valid one
		Instruction instruction = getInstruction(args[0]);
		if (instruction == null)
			sender.sendMessage(ChatColor.RED + "There are no instructions with the name '" + args[0] + "' registered");
		else
		{
			List<String> arguments = new ArrayList<String>(args.length);
			for (String argument : args)
				arguments.add(argument);
			arguments.remove(0);
			instruction.onUse(sender, arguments);
		}
		return true;
	}
}
