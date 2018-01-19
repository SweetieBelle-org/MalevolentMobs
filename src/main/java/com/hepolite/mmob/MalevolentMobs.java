package com.hepolite.mmob;

import com.hepolite.coreutility.cmd.CoreCommandHandler;
import com.hepolite.coreutility.cmd.InstructionReload;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.mmob.abilities.AbilityHandler;
import com.hepolite.mmob.mob.MobHandler;
import com.hepolite.mmob.roles.RoleHandler;

public class MalevolentMobs extends CorePlugin
{
	private static MalevolentMobs INSTANCE;
	private AbilityHandler abilityHandler;
	private MobHandler mobHandler;
	private RoleHandler roleHandler;

	@Override
	protected void onInitialize()
	{
		INSTANCE = this;

		CoreCommandHandler commandHandler = setCommandHandler(new CoreCommandHandler(this, "mmob"));
		commandHandler.registerInstruction(new InstructionReload(this, "malevolentmobs.mmob"));

		abilityHandler = (AbilityHandler) addHandler(new AbilityHandler(this));
		mobHandler = (MobHandler) addHandler(new MobHandler());
		roleHandler = (RoleHandler) addHandler(new RoleHandler());
	}

	@Override
	protected void onDeinitialize()
	{}

	@Override
	protected void onTick(int tick)
	{}

	@Override
	protected void onRestart()
	{}

	// ////////////////////////////////////////////////////////////////

	/** Returns the plugin instance */
	public static final MalevolentMobs getInstance()
	{
		return INSTANCE;
	}

	/** Returns the ability handler */
	public static final AbilityHandler getAbilityHandler()
	{
		return INSTANCE.abilityHandler;
	}

	/** Returns the mob handler */
	public static final MobHandler getMobHandler()
	{
		return INSTANCE.mobHandler;
	}

	/** Returns the role handler */
	public static final RoleHandler getRoleHandler()
	{
		return INSTANCE.roleHandler;
	}
}
