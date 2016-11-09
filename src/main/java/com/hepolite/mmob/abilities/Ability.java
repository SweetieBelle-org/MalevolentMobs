package com.hepolite.mmob.abilities;

import java.util.Random;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public abstract class Ability
{
	/** The priority of the ability; low priority is applied BEFORE damage reduction due to armor, high priority is applied after normal priority, which is applied AFTER damage reduction */
	public enum Priority
	{
		LOW, NORMAL, HIGH;
	}

	protected static Random random = new Random();

	// Control variables
	protected MalevolentMob mob = null;

	private String name = "unnamed ability";
	protected float scale = 0.0f;
	private Priority priority = Priority.NORMAL;

	/* Initialization */
	protected Ability(MalevolentMob mob, String name, Priority priority, float scale)
	{
		this.mob = mob;
		this.name = name;
		this.scale = scale;
		this.priority = priority;
	}

	/** Loads up the passive from the given configuration path */
	public abstract void loadFromConfig(Settings settings, Settings alternative);

	// ////////////////////////////////////////////////////////////////////////////////////

	/** Called when the mob just spawned */
	public void onSpawn()
	{
	}

	/** Called when the mob just died */
	public void onDie()
	{
	}

	/** Called every tick for as long as the mob is alive */
	public void onTick()
	{
	}

	/** Called when the mob just attacked */
	public void onAttack()
	{
	}

	/** Called when the mob was just attacked */
	public void onAttacked(EntityDamageEvent event)
	{
	}

	/** Called when the mob just dealt some damage */
	public void onAttacking(EntityDamageByEntityEvent event)
	{
	}

	/** Called when the mob just gained some health */
	public void onHealed(EntityRegainHealthEvent event)
	{
	}

	/** Called when a creeper is about to explode, or when a wither skull and ghast fireball hits something */
	public void onExplode(ExplosionPrimeEvent event)
	{
	}

	// ///////////////////////////////////////////////////////////////////////////////////

	/** Returns the name of the ability */
	public String getName()
	{
		return name;
	}

	/** Returns the priority of the ability */
	public Priority getPriority()
	{
		return priority;
	}
}
