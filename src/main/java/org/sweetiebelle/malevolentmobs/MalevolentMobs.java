package org.sweetiebelle.malevolentmobs;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MalevolentMobs.MODID, name = MalevolentMobs.NAME, version = MalevolentMobs.VERSION)
public class MalevolentMobs
{
    public static final String MODID = "malevolentmobs";
    public static final String NAME = "Malevolent Mobs";
    public static final String VERSION = "0.0.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}