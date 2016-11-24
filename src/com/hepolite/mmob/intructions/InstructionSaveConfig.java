package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hepolite.mmob.MMobPlugin;

/**
 * The configuration save instruction, saves the config file when invoked
 */
public class InstructionSaveConfig extends Instruction
{
	public InstructionSaveConfig()
	{
		super("Save", 0);
	}
	
	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Saves the config file to disk");
	}
	
	@Override
	protected String getExplanation()
	{
		return "Saves the configuration file that is stored in memory to disk, such that any changes done using commands are persistent.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage("§fSaving the configuration file...");
		MMobPlugin.getSettings().save();
		sender.sendMessage("§fDone saving the configuration file!");
		return false;
	}
}
