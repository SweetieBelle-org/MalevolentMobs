package com.hepolite.mmob.handlers;

import com.hepolite.mmob.abilities.Active;
import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.abilities.actives.ActiveBlazingPillar;
import com.hepolite.mmob.abilities.actives.ActiveBlindfold;
import com.hepolite.mmob.abilities.actives.ActiveDecoy;
import com.hepolite.mmob.abilities.actives.ActiveFireBurst;
import com.hepolite.mmob.abilities.actives.ActiveFireball;
import com.hepolite.mmob.abilities.actives.ActiveFracturingBlast;
import com.hepolite.mmob.abilities.actives.ActiveGrenade;
import com.hepolite.mmob.abilities.actives.ActiveGroundSlam;
import com.hepolite.mmob.abilities.actives.ActiveKidnap;
import com.hepolite.mmob.abilities.actives.ActiveLeashOn;
import com.hepolite.mmob.abilities.actives.ActiveLifesteal;
import com.hepolite.mmob.abilities.actives.ActiveLightningStrike;
import com.hepolite.mmob.abilities.actives.ActiveMagicBlast;
import com.hepolite.mmob.abilities.actives.ActiveMagicMirror;
import com.hepolite.mmob.abilities.actives.ActiveSummonMinion;
import com.hepolite.mmob.abilities.actives.ActiveTeleport;
import com.hepolite.mmob.abilities.actives.ActiveToxicSpit;
import com.hepolite.mmob.abilities.actives.ActiveVirulentGrasp;
import com.hepolite.mmob.abilities.actives.ActiveVolley;
import com.hepolite.mmob.abilities.actives.ActiveWeaken;
import com.hepolite.mmob.abilities.actives.ActiveWebbing;
import com.hepolite.mmob.abilities.actives.ActiveWitheringBolt;
import com.hepolite.mmob.abilities.passives.PassiveDeterioratingAura;
import com.hepolite.mmob.abilities.passives.PassiveExoskeleton;
import com.hepolite.mmob.abilities.passives.PassiveExplosion;
import com.hepolite.mmob.abilities.passives.PassiveFireAura;
import com.hepolite.mmob.abilities.passives.PassiveFreezingAura;
import com.hepolite.mmob.abilities.passives.PassiveGuardianAngel;
import com.hepolite.mmob.abilities.passives.PassiveHealer;
import com.hepolite.mmob.abilities.passives.PassiveMother;
import com.hepolite.mmob.abilities.passives.PassiveMount;
import com.hepolite.mmob.abilities.passives.PassivePoisonMist;
import com.hepolite.mmob.abilities.passives.PassivePotionEffect;
import com.hepolite.mmob.abilities.passives.PassiveRegeneration;
import com.hepolite.mmob.abilities.passives.PassiveShielding;
import com.hepolite.mmob.abilities.passives.PassiveVampirism;
import com.hepolite.mmob.abilities.passives.PassiveWitherAura;
import com.hepolite.mmob.abilities.passives.PassiveWitherParticles;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;
import com.hepolite.mmob.settings.SettingsAbilities;

public class AbilityHandler
{
	/** Returns a new instance of a passive ability, based on name */
	public static Passive getPassive(MalevolentMob mob, String name)
	{
		Settings settings = SettingsAbilities.getPassiveConfig(name);
		if (!settings.getBoolean("enable"))
			return null;

		Passive passive = null;
		if (name.equals("Fire_Aura"))
			passive = new PassiveFireAura(mob, mob.getLevel());
		else if (name.equals("Wither_Aura"))
			passive = new PassiveWitherAura(mob, mob.getLevel());
		else if (name.equals("Poison_Mist"))
			passive = new PassivePoisonMist(mob, mob.getLevel());
		else if (name.equals("Exoskeleton"))
			passive = new PassiveExoskeleton(mob, mob.getLevel());
		else if (name.equals("Mother"))
			passive = new PassiveMother(mob, mob.getLevel());
		else if (name.equals("Guardian_Angel"))
			passive = new PassiveGuardianAngel(mob, mob.getLevel());
		else if (name.equals("Regeneration"))
			passive = new PassiveRegeneration(mob, mob.getLevel());
		else if (name.equals("Potion_Effect"))
			passive = new PassivePotionEffect(mob, mob.getLevel());
		else if (name.equals("Explosion"))
			passive = new PassiveExplosion(mob, mob.getLevel());
		else if (name.equals("Vampirism"))
			passive = new PassiveVampirism(mob, mob.getLevel());
		else if (name.equals("Deteriorating_Aura"))
			passive = new PassiveDeterioratingAura(mob, mob.getLevel());
		else if (name.equals("Wither_Particles"))
			passive = new PassiveWitherParticles(mob, mob.getLevel());
		else if (name.equals("Freezing_Aura"))
			passive = new PassiveFreezingAura(mob, mob.getLevel());
		else if (name.equals("Mount"))
			passive = new PassiveMount(mob, mob.getLevel());
		else if (name.equals("Healer"))
			passive = new PassiveHealer(mob, mob.getLevel());
		else if (name.equals("Shielding"))
			passive = new PassiveShielding(mob, mob.getLevel());

		return passive;
	}

	/** Returns a new instance of an active ability, based on name */
	public static Active getActive(MalevolentMob mob, String name)
	{
		Settings settings = SettingsAbilities.getActiveConfig(name);
		if (!settings.getBoolean("enable"))
			return null;

		Active active = null;
		if (name.equals("Lifesteal"))
			active = new ActiveLifesteal(mob, mob.getLevel());
		else if (name.equals("Blazing_Pillar"))
			active = new ActiveBlazingPillar(mob, mob.getLevel());
		else if (name.equals("Fire_Burst"))
			active = new ActiveFireBurst(mob, mob.getLevel());
		else if (name.equals("Magic_Mirror"))
			active = new ActiveMagicMirror(mob, mob.getLevel());
		else if (name.equals("Magic_Blast"))
			active = new ActiveMagicBlast(mob, mob.getLevel());
		else if (name.equals("Virulent_Grasp"))
			active = new ActiveVirulentGrasp(mob, mob.getLevel());
		else if (name.equals("Ground_Slam"))
			active = new ActiveGroundSlam(mob, mob.getLevel());
		else if (name.equals("Fireball"))
			active = new ActiveFireball(mob, mob.getLevel());
		else if (name.equals("Leash_On"))
			active = new ActiveLeashOn(mob, mob.getLevel());
		else if (name.equals("Lightning_Strike"))
			active = new ActiveLightningStrike(mob, mob.getLevel());
		else if (name.equals("Withering_Bolt"))
			active = new ActiveWitheringBolt(mob, mob.getLevel());
		else if (name.equals("Weaken"))
			active = new ActiveWeaken(mob, mob.getLevel());
		else if (name.equals("Toxic_Spit"))
			active = new ActiveToxicSpit(mob, mob.getLevel());
		else if (name.equals("Summon_Minion"))
			active = new ActiveSummonMinion(mob, mob.getLevel());
		else if (name.equals("Fracturing_Blast"))
			active = new ActiveFracturingBlast(mob, mob.getLevel());
		else if (name.equals("Kidnap"))
			active = new ActiveKidnap(mob, mob.getLevel());
		else if (name.equals("Webbing"))
			active = new ActiveWebbing(mob, mob.getLevel());
		else if (name.equals("Decoy"))
			active = new ActiveDecoy(mob, mob.getLevel());
		else if (name.equals("Teleport"))
			active = new ActiveTeleport(mob, mob.getLevel());
		else if (name.equals("Volley"))
			active = new ActiveVolley(mob, mob.getLevel());
		else if (name.equals("Grenade"))
			active = new ActiveGrenade(mob, mob.getLevel());
		else if (name.equals("Blindfold"))
			active = new ActiveBlindfold(mob, mob.getLevel());

		return active;
	}
}
