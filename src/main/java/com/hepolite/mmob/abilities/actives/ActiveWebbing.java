package com.hepolite.mmob.abilities.actives;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveArea;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.BlockManager;

public class ActiveWebbing extends ActiveArea
{
	private int count = 0;
	private int radius = 0;
	private int range = 0;
	private int duration = 0;

	public ActiveWebbing(MalevolentMob mob, float scale)
	{
		super(mob, "Webbing", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);
		delay = 0;

		range = (int) settings.getScaledValue(alternative, "Range", scale, 0.0f);
		count = (int) settings.getScaledValue(alternative, "Count", scale, 0.0f);
		radius = (int) settings.getScaledValue(alternative, "Radius", scale, 0.0f);
		duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget <= 10.0f && target.isOnGround();
	}

	@Override
	public void applyEffect(Location areaLocation)
	{
		// Notify the user that something happened
		areaLocation.getWorld().playSound(areaLocation, Sound.ENTITY_SLIME_SQUISH, 1.0f, 1.0f);

		// Spread some web around in a blob
		for (int i = 0; i < count; i++)
		{
			// Compute the position of the blob
			Location location = areaLocation.clone();
			location.setX(location.getX() + 2.0f * (random.nextFloat() - 0.5f) * range);
			location.setZ(location.getZ() + 2.0f * (random.nextFloat() - 0.5f) * range);

			// Spread out some web
			for (int x = -radius; x <= radius; x++)
			{
				for (int z = -radius; z <= radius; z++)
				{
					if (x * x + z * z + random.nextInt(3) < radius * radius)
					{
						// Find the ground, if possible
						boolean foundGround = false;
						int y = location.getBlockY();
						for (int j = 0; i >= -radius; j--)
						{
							Block block = location.getWorld().getBlockAt(location.getBlockX() + x, y + j, location.getBlockZ() + z);
							if (block.getType().isSolid())
							{
								y += j + 1;
								foundGround = true;
								break;
							}
						}
						// If the ground wasn't found, try to find the ceiling
						if (!foundGround)
						{
							for (int j = 1; i <= radius; j++)
							{
								Block block = location.getWorld().getBlockAt(location.getBlockX() + x, y + j, location.getBlockZ() + z);
								if (block.getType().isSolid())
								{
									y += j - 1;
									break;
								}
							}
						}

						// If inside something, find an open spot
						for (int j = 0; i <= radius; j++)
						{
							// Find something above
							Block block = location.getWorld().getBlockAt(location.getBlockX() + x, y + j, location.getBlockZ() + z);
							if (!block.getType().isSolid())
							{
								y += j;
								break;
							}
							// Find something below
							block = location.getWorld().getBlockAt(location.getBlockX() + x, y - j, location.getBlockZ() + z);
							if (!block.getType().isSolid())
							{
								y -= j;
								break;
							}
						}

						// Change the block if it is relevant
						if (random.nextFloat() < 0.75f)
							BlockManager.setBlock(location.getWorld(), location.getBlockX() + x, y, location.getBlockZ() + z, Material.WEB, true, duration);
					}
				}
			}
		}
	}

	@Override
	protected void displayArea(Location location)
	{
	}

}
