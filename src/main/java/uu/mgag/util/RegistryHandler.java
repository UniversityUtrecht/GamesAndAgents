package uu.mgag.util;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import uu.mgag.init.EntityInit;

@EventBusSubscriber
public class RegistryHandler
{
	public static void preInitRegistries()
	{
		EntityInit.registerEntities();
		RenderHandler.registerEntityRenders();
	}
	
	public static void postInitRegistries()
	{
		
	}
	
	public static void initRegistries()
	{
		
	}
}
