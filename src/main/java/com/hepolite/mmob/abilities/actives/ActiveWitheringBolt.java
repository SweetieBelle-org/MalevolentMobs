package com.hepolite.mmob.abilities.actives;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.entity.LivingEntity;

import com.hepolite.mmob.abilities.ActiveProjectile;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.projectiles.Projectile;
import com.hepolite.mmob.projectiles.ProjectileBoltWither;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.FireworksEffect;

/**
 * The withering bolt active will launch a bolt that withers the entities it hits
 */
public class ActiveWitheringBolt extends ActiveProjectile
{
	private float projectileSpeed = 0.0f;
	private int projectileStrength = 1;
	private int projectileDuration = 0;
	private float projectileRange = 0.0f;
	private boolean affectPlayersOnly = true;

	public ActiveWitheringBolt(MalevolentMob mob, float scale)
	{
		super(mob, "Withering Bolt", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);

		projectileSpeed = settings.getScaledValue(alternative, "Speed", scale, 0.0f);
		projectileStrength = (int) settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		projectileDuration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
		projectileRange = settings.getScaledValue(alternative, "Range", scale, 0.0f);
		affectPlayersOnly = settings.getBoolean(alternative, "affectPlayersOnly");
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return distanceToTarget >= 20.0f && distanceToTarget <= 70.0f;
	}

	@Override
	protected Projectile createProjectile(LivingEntity target)
	{
		// Make sure the players have a chance to react to the projectile
		Builder builder = FireworksEffect.getFireworksEffectBuilder();
		builder.withColor(Color.BLACK);
		builder.withTrail();
		FireworksEffect.createFireworks(mob.getEntity().getEyeLocation(), builder.build());
		
		// Create the projectile
		return new ProjectileBoltWither(mob.getEntity(), target, projectileSpeed, projectileStrength, projectileDuration, projectileRange, affectPlayersOnly);
	}
}
