package uu.mgag.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import uu.mgag.entity.EntityBlacksmith;
import uu.mgag.entity.EntityWorker;
import uu.mgag.entity.model.ModelWorker;
import uu.mgag.util.Reference;

public class RenderBlacksmith extends RenderLiving<EntityBlacksmith>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/entity/blacksmith.png");

	public RenderBlacksmith(RenderManager manager)
	{
		super(manager, new ModelWorker(), 0.5f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBlacksmith entity)
	{
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityBlacksmith entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}

}
