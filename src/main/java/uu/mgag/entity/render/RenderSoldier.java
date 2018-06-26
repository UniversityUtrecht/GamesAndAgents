package uu.mgag.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import uu.mgag.entity.EntitySoldier;
import uu.mgag.entity.model.ModelWorker;
import uu.mgag.util.Reference;

public class RenderSoldier extends RenderLiving<EntitySoldier>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/entity/farmer.png"); // TODO: this

	public RenderSoldier(RenderManager manager)
	{
		super(manager, new ModelWorker(), 0.5f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntitySoldier entity)
	{
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntitySoldier entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
	}
}
