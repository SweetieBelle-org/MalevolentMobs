package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hepolite.mmob.MMobPlugin;

/**
 * The configuration reload instruction, reloads the config file when invoked
 */
public class InstructionReloadConfig extends Instruction
{
	public InstructionReloadConfig()
	{
		super("Reload", 0);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Reloads the config file from disk");
	}
	
	@Override
	protected String getExplanation()
	{
		return "Reloads the configuration file from the disk, applying any changes that had been applied to the configuration file. This will overwrite any changes done to the configuration file stored in memory, so it would be a good idea to use '/mmob save' before making changes to the config file on disk.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage("§fReloading the configuration file...");
		MMobPlugin.getSettings().reload();
		sender.sendMessage("§fDone reloading the configuration file!");
		return false;
	}
}
