package uu.mgag.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import uu.mgag.entity.EntityMiner;
import uu.mgag.entity.EntityWorker;
import uu.mgag.entity.model.ModelWorker;
import uu.mgag.util.Reference;

public class RenderMiner extends RenderBiped<EntityMiner>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/entity/miner.png");

	public RenderMiner(RenderManager manager)
	{
		super(manager, new ModelWorker(), 0.5f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMiner entity)
	{
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityMiner entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

}
