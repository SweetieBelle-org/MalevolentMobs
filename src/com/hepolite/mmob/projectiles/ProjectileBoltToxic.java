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

public class ProjectileBoltToxic extends ProjectileBolt
{
	private float range = 0.0f;
	private boolean affectPlayersOnly = true;

	private PotionEffect effect = null;

	private int soundTimer = 0;

	public ProjectileBoltToxic(LivingEntity caster, LivingEntity target, float speed, int strength, int duration, float range, float inaccuracy, boolean affectPlayersOnly)
	{
		super(caster, target, speed, true, inaccuracy);

		this.affectPlayersOnly = affectPlayersOnly;
		this.range = range;
		this.effect = new PotionEffect(PotionEffectType.POISON, duration, strength - 1);
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if (++soundTimer % 10 == 0)
			position.getWorld().playSound(position, Sound.ENTITY_SILVERFISH_STEP, 1.0f, 0.0f);
	}

	@Override
	protected void applyEffects(Location location)
	{
		location.getWorld().playSound(location, Sound.ENTITY_SPIDER_STEP, 1.0f, -0.5f);

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
		ParticleEffect.play(ParticleType.CRIT, location, 0.07f, 12, 0.333f);
	}
}
