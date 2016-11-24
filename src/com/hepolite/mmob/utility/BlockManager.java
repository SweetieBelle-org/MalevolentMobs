package com.hepolite.mmob.utility;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockManager
{
	// Control variables
	private static List<BlockNode> nodes = new LinkedList<BlockNode>();

	/** Updates everything related to the block nodes */
	public static void onTick()
	{
		// Iterate over all nodes and remove those that are done
		for (Iterator<BlockNode> it = nodes.iterator(); it.hasNext();)
		{
			BlockNode node = it.next();
			if (--node.duration < 0)
			{
				node.remove();
				it.remove();
			}
		}
	}

	/** Sets the given block in the world to the given material. A duration of -1 means that the block will not be removed automatically */
	public static void setBlock(World world, int x, int y, int z, Material material, boolean replaceEverything, int duration)
	{
		setBlock(world, x, y, z, material, (byte) 0, replaceEverything, duration);
	}

	/** Sets the given block in the world to the given material and metadata. A duration of -1 means that the block will not be removed automatically */
	public static void setBlock(World world, int x, int y, int z, Material material, byte metaData, boolean replaceEverything, int duration)
	{
		// Generate a new node
		BlockNode node = new BlockNode(material, metaData, world, x, y, z, replaceEverything, duration);
		if (node.didSomething && duration != -1)
			nodes.add(node);
	}

	/** Returns true if the location is a changed block */
	public static boolean isLocationNoted(Location location)
	{
		for (BlockNode node : nodes)
		{
			if (node.world == location.getWorld() && node.x == location.getBlockX() && node.y == location.getBlockY() && node.z == location.getBlockZ())
				return true;
		}
		return false;
	}

	/** A simple node that contains information about what block was placed where, how long it should be there and what block it was */
	private static class BlockNode
	{
		// Control variables
		private Material oldMaterial = Material.AIR;
		private byte oldMetaData = (byte) 0;
		private Material newMaterial = Material.AIR;

		private World world = null;
		private int x = -1, y = -1, z = -1;
		private int duration = 0;

		private boolean didSomething = false;

		/* Initialize the node */
		@SuppressWarnings("deprecation")
		public BlockNode(Material newMaterial, Byte metaData, World world, int x, int y, int z, boolean replaceBlocks, int duration)
		{
			// Set the variables
			this.newMaterial = newMaterial;
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
			this.duration = duration;

			// Handle the replacing of the block in the world
			Block block = world.getBlockAt(x, y, z);
			if (block.getType() != newMaterial && (block.getType() == Material.AIR || replaceBlocks))
			{
				oldMaterial = block.getType();
				oldMetaData = block.getData();
				block.setType(newMaterial);
				block.setData(metaData);
				didSomething = true;
			}
		}

		/** Cleans up after the node itself */
		@SuppressWarnings("deprecation")
		public void remove()
		{
			// Handle the removing of the block in the world
			Block block = world.getBlockAt(x, y, z);
			if (block.getType() == newMaterial || block.getType() == Material.AIR || block.getType() == Material.WATER)
			{
				block.setType(oldMaterial);
				block.setData(oldMetaData);
			}
		}
	}
}
