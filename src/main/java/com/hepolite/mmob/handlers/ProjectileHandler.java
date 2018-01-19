package com.hepolite.mmob.handlers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hepolite.mmob.projectiles.Projectile;

public class ProjectileHandler
{
	private final static List<Projectile> projectiles = new LinkedList<Projectile>();

	/** Adds a new projectile to the system */
	public static void addProjectile(Projectile projectile)
	{
		if (projectile != null)
			projectiles.add(projectile);
	}

	/** Updates the system every tick */
	public static void onTick()
	{
		// Update all projectiles, remove dead ones
		for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();)
		{
			Projectile projectile = it.next();
			if (projectile.isAlive())
				projectile.onTick();
			else
				it.remove();
		}
	}

	/** Resets all the projectiles */
	public final static void onRestart()
	{
		projectiles.clear();
	}
}
