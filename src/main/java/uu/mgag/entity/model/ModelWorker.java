package uu.mgag.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelVillager - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelWorker extends ModelBase {
    public ModelRenderer villager_head;
    public ModelRenderer villager_right_arm;
    public ModelRenderer villager_left_arm;
    public ModelRenderer villager_left_leg;
    public ModelRenderer villager_body;
    public ModelRenderer villager_right_leg;

    public ModelWorker() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.villager_body = new ModelRenderer(this, 16, 20);
        this.villager_body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.villager_body.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
        this.villager_left_leg = new ModelRenderer(this, 0, 22);
        this.villager_left_leg.mirror = true;
        this.villager_left_leg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.villager_left_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.villager_head = new ModelRenderer(this, 0, 0);
        this.villager_head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.villager_head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.villager_left_arm = new ModelRenderer(this, 44, 22);
        this.villager_left_arm.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.villager_left_arm.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(villager_left_arm, -0.7499679795819634F, 0.0F, 0.0F);
        this.villager_right_leg = new ModelRenderer(this, 0, 22);
        this.villager_right_leg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.villager_right_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.villager_right_arm = new ModelRenderer(this, 44, 22);
        this.villager_right_arm.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.villager_right_arm.addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(villager_right_arm, -0.7499679795819634F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.villager_body.render(f5);
        this.villager_left_leg.render(f5);
        this.villager_head.render(f5);
        this.villager_left_arm.render(f5);
        this.villager_right_leg.render(f5);
        this.villager_right_arm.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
