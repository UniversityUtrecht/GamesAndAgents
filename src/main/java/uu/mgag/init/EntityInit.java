package uu.mgag.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import uu.mgag.Main;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.Reference;

public class EntityInit
{
	public static void registerEntities()
	{
		registerEntity("Worker", EntityWorker.class, Reference.ENTITY_WORKER, 50, 11437146, 000000);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, color1, color2);
	}
}
