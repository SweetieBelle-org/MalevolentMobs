package com.hepolite.mmob.abilities.actives;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.abilities.ActiveTick;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The magic mirror will reflect 100% of the damage that the enemies throws at the mob; this mirroring occurs after damage reduction
 */
public class ActiveMagicMirror extends ActiveTick
{
	private int strength = 0;
	private int nodes = 0;
	
	private int shieldCooldown = 0;
	private int cooldownTimer = 0;
	
	private float nodePosition = 0.0f;
	
	public ActiveMagicMirror(MalevolentMob mob, float scale)
	{
		super(mob, "Magic Mirror", Priority.HIGH, scale);
	}
	
	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		super.loadFromConfig(settings, alternative);
		
		strength = (int) settings.getScaledValue(alternative, "Strength", scale, 0.0f);
		shieldCooldown = (int) settings.getScaledValue(alternative, "ShieldCooldown", scale, 0.0f);
		cooldownTimer = shieldCooldown;
	}

	@Override
	public boolean canCast(float healthFactor, float distanceToTarget, LivingEntity target)
	{
		return healthFactor <= 0.6f && distanceToTarget <= 20.0f;
	}

	@Override
	public void cast(LivingEntity target)
	{
		nodes = strength;
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		
		cooldownTimer--;
		
		// Display the nodes
		nodePosition += 2.0f * (float) Math.PI / 120.0f;
		for (int i = 0; i < nodes; i++)
		{
			// Compute the orbit position
			double angle = nodePosition + (double) i / (double) nodes * 2.0 * Math.PI;
			
			Location location = mob.getEntity().getEyeLocation();			
			location.add(1.5 * Math.sin(angle), 0.0, 1.5 * Math.cos(angle));
			
			ParticleEffect.play(ParticleType.PORTAL, location, 0.075f, 8, 0.05f);
		}
	}
	
	@Override
	public void onAttacked(EntityDamageEvent event)
	{
		resetStartupTimer();
		resetDelayTimer();
		
		// The shield can only mirror one attack every now and then
		if (cooldownTimer <= 0 && nodes > 0)
		{
			cooldownTimer = shieldCooldown;
			nodes--;
			
			// Find the attacker
			Entity attacker = null;
			if (event instanceof EntityDamageByEntityEvent)
				attacker = Common.getAttacker((EntityDamageByEntityEvent) event);
			
			// Mirror the damage
			if (attacker instanceof LivingEntity)
			{
				Common.doDamage(event.getDamage(), (LivingEntity) attacker, mob.getEntity(), DamageCause.MAGIC);
				event.setCancelled(true);
				
				mob.getEntity().getWorld().playSound(mob.getEntity().getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0f, 0.0f);
			}
		}
	}

	@Override
	protected void applyTickEffect()
	{
		if (nodes > 0)
			nodes = Math.min(nodes + 1, strength);
	}
}
