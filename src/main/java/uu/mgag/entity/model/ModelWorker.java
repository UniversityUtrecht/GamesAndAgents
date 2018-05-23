package uu.mgag.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelWorker - Either Mojang or a mod author
 * Created using Tabula 7.0.0
 */
public class ModelWorker extends ModelBase {
    public ModelRenderer worker_head;
    public ModelRenderer worker_left_leg;
    public ModelRenderer worker_body;
    public ModelRenderer worker_right_leg;
    public ModelRenderer worker_left_arm;
    public ModelRenderer worker_right_arm;

    public ModelWorker() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.worker_right_arm = new ModelRenderer(this, 40, 16);
        this.worker_right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.worker_right_arm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.worker_head = new ModelRenderer(this, 0, 0);
        this.worker_head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.worker_head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.worker_left_leg = new ModelRenderer(this, 16, 48);
        this.worker_left_leg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.worker_left_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.worker_right_leg = new ModelRenderer(this, 0, 16);
        this.worker_right_leg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.worker_right_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.worker_left_arm = new ModelRenderer(this, 32, 48);
        this.worker_left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.worker_left_arm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.worker_body = new ModelRenderer(this, 16, 16);
        this.worker_body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.worker_body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.worker_right_arm.render(f5);
        this.worker_head.render(f5);
        this.worker_left_leg.render(f5);
        this.worker_right_leg.render(f5);
        this.worker_left_arm.render(f5);
        this.worker_body.render(f5);
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
