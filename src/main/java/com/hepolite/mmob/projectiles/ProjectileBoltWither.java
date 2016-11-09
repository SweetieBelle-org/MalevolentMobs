package com.hepolite.mmob.projectiles;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.mmob.utility.Common;
import com.hepolite.mmob.utility.ParticleEffect;
import com.hepolite.mmob.utility.ParticleEffect.ParticleType;

/**
 * The withering bolt will apply a withering effect to all targets it hits; the bolt won't be affected by gravity
 */
public class ProjectileBoltWither extends ProjectileBolt
{
	private float range = 0.0f;
	private boolean affectPlayersOnly = true;

	private PotionEffect effect = null;

	private int soundTimer = 0;

	public ProjectileBoltWither(LivingEntity caster, LivingEntity target, float speed, int strength, int duration, float range, boolean affectPlayersOnly)
	{
		super(caster, target, speed, false, 0.0f);

		this.affectPlayersOnly = affectPlayersOnly;
		this.range = range;
		this.effect = new PotionEffect(PotionEffectType.WITHER, duration, strength - 1);
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if (++soundTimer % 10 == 0)
			position.getWorld().playSound(position, Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 0.0f);
	}

	@Override
	protected void applyEffects(Location location)
	{
		location.getWorld().playSound(location, Sound.ENTITY_ENDERDRAGON_HURT, 1.0f, 0.0f);

		// Apply wither to all entities within the range
		List<LivingEntity> entities = Common.getEntitiesInRange(location, range);
		for (LivingEntity entity : entities)
		{
			if (entity == caster)
				continue;

			if (!affectPlayersOnly || entity instanceof Player)
				entity.addPotionEffect(effect);
		}
	}

	@Override
	protected void displayBolt(Location location)
	{
		ParticleEffect.play(ParticleType.SMOKE_LARGE, location, 0.07f, 8, 0.333f);
	}
}
