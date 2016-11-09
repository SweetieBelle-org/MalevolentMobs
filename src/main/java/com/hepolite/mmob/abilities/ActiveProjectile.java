package com.hepolite.mmob.abilities;

import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.handlers.ProjectileHandler;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;

/**
 * The projectile ability allows an active to send out a projectile that will carry out some logic
 */
public abstract class ActiveProjectile extends Active
{
	protected ActiveProjectile(MalevolentMob mob, String name, Priority priority, float scale)
	{
		super(mob, name, priority, scale);
	}

	@Override
	public void cast(LivingEntity target)
	{
		ProjectileHandler.addProjectile(createProjectile(target));
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	/** Called to create the projectile itself */
	protected abstract Projectile createProjectile(LivingEntity target);
}
