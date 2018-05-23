package uu.mgag.util;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import uu.mgag.entity.EntityWorker;
import uu.mgag.entity.render.RenderWorker;

public class RenderHandler
{
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityWorker.class, new IRenderFactory<EntityWorker>()
		{
			@Override
			public Render<? super EntityWorker> createRenderFor(RenderManager manager)
			{
				return new RenderWorker(manager);
			}
		});
	}
}
