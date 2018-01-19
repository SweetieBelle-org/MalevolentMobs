package com.hepolite.mmob.intructions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.mmob.Log;

public abstract class Instruction
{
	private String name = "Unnamed_Instruction";
	private int validArgumentNumbers[] = null;

	private Map<String, Instruction> subInstructions = new TreeMap<String, Instruction>();
	private Instruction parentInstruction = null;

	/** Name is the name of the instruction, argumentCount can be -1 for any number of arguments, otherwise that many arguments are required for the instruction to be valid */
	protected Instruction(String name, int argumentCount)
	{
		this(name, new int[] { argumentCount });
	}

	/** Name is the name of the instruction, argumentCount can be -1 for any number of arguments, otherwise that many arguments are required for the instruction to be valid */
	protected Instruction(String name, int validArgumentNumbers[])
	{
		this.name = name;
		this.validArgumentNumbers = validArgumentNumbers;
	}

	/** Returns the name of the effect */
	public String getName()
	{
		return name;
	}

	/** Returns the full name of the instruction, rooted from the parents */
	public String getFullName()
	{
		if (getParent() != null)
			return getParent().getFullName() + " " + getName();
		return getName();
	}

	/** Returns the parent instruction */
	public Instruction getParent()
	{
		return parentInstruction;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Registers a sub-instruction for the instruction. Example: /mmob instruction subinstruction arguments */
	protected void registerSubInstruction(Instruction instruction)
	{
		if (subInstructions.get(instruction.getName()) != null)
			Log.log("Attempted to register sub-instruction '" + instruction.getName() + "' to instruction '" + instruction + "'. Another sub-instruction with that name has already been registered and will be overwritten!", Level.WARNING);
		subInstructions.put(instruction.getName().toLowerCase(), instruction);

		// Update some parts of the instruction settings
		instruction.parentInstruction = this;
	}

	/** Returns a sub-instruction with the given name if it exists */
	protected Instruction getSubInstruction(String name)
	{
		return subInstructions.get(name.toLowerCase());
	}

	/** Adds all registered sub-instructions to the specified list */
	public void getSubInstructions(List<Instruction> list)
	{
		for (Instruction instruction : subInstructions.values())
		{
			list.add(instruction);
			instruction.getSubInstructions(list);
		}
	}

	/** Basic call; will check for sub-instructions and make sure arguments match before doing anything. Returns true if the instruction failed at something */
	public boolean onUse(CommandSender sender, List<String> arguments)
	{
		// Figure out if there is a valid sub-instruction; if there is, try to invoke it
		Instruction instruction = (arguments.size() >= 1 ? getSubInstruction(arguments.get(0)) : null);
		if (instruction != null)
		{
			arguments.remove(0);
			if (!instruction.onUse(sender, arguments))
				return false;
		}
		// Otherwise run the instruction
		else
		{
			boolean hasValidAmountOfArguments = false;
			for (int argumentCount : validArgumentNumbers)
			{
				if (arguments.size() == argumentCount || argumentCount == -1)
				{
					hasValidAmountOfArguments = true;
					if (!onInvoke(sender, arguments))
						return false;
					break;
				}
			}
			if (!hasValidAmountOfArguments)
			{
				String argumentsExpected = "";
				for (int i = 0; i < validArgumentNumbers.length; i++)
					argumentsExpected += (i == 0 ? "" : ", ") + (validArgumentNumbers[i] == -1 ? "any" : validArgumentNumbers[i]);
				sender.sendMessage(ChatColor.RED + "Invalid number of parameters. Expected '" + argumentsExpected + "' parameters, but received '" + arguments.size() + "'");
			}
		}
		return true;
	}

	/** Adds a syntax for the instruction; all combinations of arguments should have their own syntax added */
	protected abstract void addSyntax(List<String> syntaxes);

	/** Adds a description for the instruction; all combinations of arguments should have their own description added. There must be one description for each syntax */
	protected abstract void addDescription(List<String> descriptions);

	/** Returns a bit of text that explains what the instruction does */
	protected abstract String getExplanation();

	/** The most basic place to execute an instruction; arguments will only be those that are specified. Returns true if the instruction failed at something */
	protected abstract boolean onInvoke(CommandSender sender, List<String> arguments);

	// /////////////////////////////////////////////////////////////////////////////////////////
	// HELPER METHODS // HELPER METHODS // HELPER METHODS // HELPER METHODS // HELPER METHODS //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Helper function to get a unique identifier for the command sender */
	protected static String getSenderIdentifier(CommandSender sender)
	{
		if (sender instanceof Player)
			return ((Player) sender).getUniqueId().toString();
		else if (sender instanceof BlockCommandSender)
			return ((BlockCommandSender) sender).getBlock().getLocation().toString();
		else
			return "CONSOLE";
	}

	/** Helper function to find the location of the command sender. Returns null if the sender is the console */
	protected static Location getSenderLocation(CommandSender sender)
	{
		if (sender instanceof Player)
			return ((Player) sender).getLocation();
		else if (sender instanceof BlockCommandSender)
			return ((BlockCommandSender) sender).getBlock().getLocation();
		else
			return null;
	}

	/** Helper function to parse a location */
	protected static Location parseLocation(String world, String x, String y, String z)
	{
		Location location = parseLocation(x, y, z);
		if (location == null)
			return null;
		World locationWorld = Bukkit.getWorld(world);
		if (locationWorld == null)
		{
			Log.log("Failed to parse world '" + world + "', or no world with that name exists");
			return null;
		}
		location.setWorld(locationWorld);
		return location;
	}

	/** Helper function to parse a location */
	protected static Location parseLocation(String x, String y, String z)
	{
		int px, py, pz;
		try
		{
			px = Integer.parseInt(x);
			py = Integer.parseInt(y);
			pz = Integer.parseInt(z);
		}
		catch (Exception exception)
		{
			Log.log("Unable to parse the coordinate '" + x + ", " + y + ", " + z + "'");
			return null;
		}
		return new Location(null, px, py, pz);
	}

	/** Helper function to parse a location */
	protected static Location parseLocation(String world)
	{
		World locationWorld = Bukkit.getWorld(world);
		if (locationWorld == null)
		{
			Log.log("Failed to parse world '" + world + "', or no world with that name exists");
			return null;
		}
		return new Location(locationWorld, 0.0, 0.0, 0.0);
	}

	/** Compresses all the string in the list to one string, seperated by the given character */
	protected static String compressList(List<String> list, String seperation)
	{
		if (list.isEmpty())
			return null;

		String string = "";
		for (String entry : list)
			string += seperation + entry;
		return string.substring(seperation.length());
	}

	/** Helper function to parse a list of integers. Any number that couldn't be parsed will be replaced with -1 */
	protected static List<Integer> parseInts(List<String> numbers)
	{
		List<Integer> parsedInts = new ArrayList<Integer>();
		for (String number : numbers)
		{
			try
			{
				parsedInts.add(Integer.parseInt(number));
			}
			catch (Exception exception)
			{
				parsedInts.add(-1);
			}
		}
		return parsedInts;
	}
}
