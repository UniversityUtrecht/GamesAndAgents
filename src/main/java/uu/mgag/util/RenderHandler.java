package uu.mgag.util;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import uu.mgag.entity.EntityBlacksmith;
import uu.mgag.entity.EntityBuilder;
import uu.mgag.entity.EntityFarmer;
import uu.mgag.entity.EntityLumberjack;
import uu.mgag.entity.EntityMiner;
import uu.mgag.entity.EntityWorker;
import uu.mgag.entity.render.RenderBlacksmith;
import uu.mgag.entity.render.RenderBuilder;
import uu.mgag.entity.render.RenderFarmer;
import uu.mgag.entity.render.RenderLumberjack;
import uu.mgag.entity.render.RenderMiner;
import uu.mgag.entity.render.RenderWorker;

public class RenderHandler
{
	public static void registerEntityRenders()
	{
		/*RenderingRegistry.registerEntityRenderingHandler(EntityWorker.class, new IRenderFactory<EntityWorker>()
		{
			@Override
			public Render<? super EntityWorker> createRenderFor(RenderManager manager)
			{
				return new RenderWorker(manager);
			}
		});*/
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBlacksmith.class, new IRenderFactory<EntityBlacksmith>()
		{
			@Override
			public Render<? super EntityBlacksmith> createRenderFor(RenderManager manager)
			{
				return new RenderBlacksmith(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBuilder.class, new IRenderFactory<EntityBuilder>()
		{
			@Override
			public Render<? super EntityBuilder> createRenderFor(RenderManager manager)
			{
				return new RenderBuilder(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityFarmer.class, new IRenderFactory<EntityFarmer>()
		{
			@Override
			public Render<? super EntityFarmer> createRenderFor(RenderManager manager)
			{
				return new RenderFarmer(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLumberjack.class, new IRenderFactory<EntityLumberjack>()
		{
			@Override
			public Render<? super EntityLumberjack> createRenderFor(RenderManager manager)
			{
				return new RenderLumberjack(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMiner.class, new IRenderFactory<EntityMiner>()
		{
			@Override
			public Render<? super EntityMiner> createRenderFor(RenderManager manager)
			{
				return new RenderMiner(manager);
			}
		});
	}
}
