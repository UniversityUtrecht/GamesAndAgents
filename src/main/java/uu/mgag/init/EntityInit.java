package uu.mgag.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import uu.mgag.Main;
import uu.mgag.entity.EntityBetterZombie;
import uu.mgag.entity.EntityBlacksmith;
import uu.mgag.entity.EntityBuilder;
import uu.mgag.entity.EntityFarmer;
import uu.mgag.entity.EntityLumberjack;
import uu.mgag.entity.EntityMiner;
import uu.mgag.entity.EntitySoldier;
import uu.mgag.util.Reference;

public class EntityInit
{
	public static void registerEntities()
	{
		//registerEntity("Worker", EntityWorker.class, Reference.ENTITY_WORKER, 50, 11437146, 000000);
		registerEntity("Builder", EntityBuilder.class, Reference.ENTITY_BUILDER, 50, 7950015, 000000);
		registerEntity("Farmer", EntityFarmer.class, Reference.ENTITY_FARMER, 50, 15388265, 000000);
		registerEntity("Lumberjack", EntityLumberjack.class, Reference.ENTITY_LUMBERJACK, 50, 6532424, 000000);
		registerEntity("Blacksmith", EntityBlacksmith.class, Reference.ENTITY_BLACKSMITH, 50, 10198432, 000000);
		registerEntity("Miner", EntityMiner.class, Reference.ENTITY_MINER, 50, 6306346, 000000);
		registerEntity("Soldier", EntitySoldier.class, Reference.ENTITY_SOLDIER, 50, 23624718, 000000);
		registerEntity("Better Zombie", EntityBetterZombie.class, Reference.ENTITY_BETTER_ZOMBIE, 50, 4838761, 000000);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 3, true, color1, color2);
	}
}
