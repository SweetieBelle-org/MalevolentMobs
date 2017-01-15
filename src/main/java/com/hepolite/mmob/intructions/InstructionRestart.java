package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.mmob.MMobPlugin;

public class InstructionRestart extends Instruction
{
	public InstructionRestart()
	{
		super("Restart", 0);
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
		return "Does a complete restart of the entire plugin, removing all Malevolent Mobs, resetting all dungeons and reloading all configurations";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Restarting the plugin...");
		MMobPlugin.getInstance().onRestart();
		sender.sendMessage(ChatColor.WHITE + "Restarting complete!");
		return false;
	}
}
