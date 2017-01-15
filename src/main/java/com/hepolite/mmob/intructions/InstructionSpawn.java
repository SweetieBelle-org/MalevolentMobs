package com.hepolite.mmob.intructions;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.MMobListener;
import com.hepolite.mmob.handlers.MobHandler;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.utility.Common;

/**
 * The spawn instruction will allow the command sender to spawn a Malevolent Mob of any type and role at any location
 */
public class InstructionSpawn extends Instruction
{
	public InstructionSpawn()
	{
		super("Spawn", new int[] { 2, 3, 5, 6 });
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<entity type> <role>");
		syntaxes.add("<entity type> <role> <world>");
		syntaxes.add("<entity type> <role> <x> <y> <z>");
		syntaxes.add("<entity type> <role> <world> <x> <y> <z>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Spawns a Malevolent Mob of the given type and role");
		descriptions.add("Spawns a Malevolent Mob of the given type and role in the given world at the location of the sender");
		descriptions.add("Spawns a Malevolent Mob of the given type and role at the given location in the world the sender is in");
		descriptions.add("Spawns a Malevolent Mob of the given type and role at the given location");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows mobs to be spawned into the world. Note that this instruction is meant only as a debug tool, and not intended to be used as a means of spawning mobs on a normal server - it can be used as such, however, without problems.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		// Get the basic data
		String role = arguments.get(1);
		Location location = null;
		Location senderLocation = getSenderLocation(sender);

		switch (arguments.size())
		{
		case 6:
			location = parseLocation(arguments.get(2), arguments.get(3), arguments.get(4), arguments.get(5));
			break;
		case 5:
			location = parseLocation(arguments.get(2), arguments.get(3), arguments.get(4));
			if (senderLocation != null && location != null)
				location.setWorld(senderLocation.getWorld());
			break;
		case 3:
			location = parseLocation(arguments.get(2));
			if (senderLocation != null && location != null)
				location.add(senderLocation.getX(), senderLocation.getY(), senderLocation.getZ());
			break;
		default:
			location = senderLocation;
		}
		if (location == null)
		{
			sender.sendMessage(ChatColor.RED + "Unable to parse the specified location, or could not obtain the command user's location");
			return true;
		}

		// Spawn mob and make it malevolent
		MMobListener.setSpawnCommandFlag(true);
		Entity entity = Common.spawnEntity(location, arguments.get(0));
		MMobListener.setSpawnCommandFlag(false);

		// The mob must be of the type LivingEntity to be spawned with this command
		if (entity == null || !(entity instanceof LivingEntity))
		{
			sender.sendMessage(ChatColor.RED + "The mob type '" + arguments.get(0) + "' is not a valid mob type");
			if (entity != null)
				entity.remove();
		}
		else
		{
			MalevolentMob mob = MobHandler.getMalevolentMob((LivingEntity) entity);
			if (mob == null)
				sender.sendMessage(ChatColor.RED + "Failed to make the spawned mob malevolent!");
			else
				mob.setRole(role);
		}
		return false;
	}
}
