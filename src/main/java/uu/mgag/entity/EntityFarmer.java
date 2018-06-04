package uu.mgag.entity;

import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uu.mgag.entity.ai.EntityAIAccessChest;
import uu.mgag.entity.ai.EntityAIHarvestCrops;
import uu.mgag.entity.ai.EntityAIMoveToResource;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityFarmer extends EntityWorker implements INpc
{
	
	private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.FOOD_INGREDIENTS);
	private EntityAIAccessChest accessChest = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Items.WHEAT), 1, true);
	private EntityAIHarvestCrops harvestCrops = new EntityAIHarvestCrops(this, 0.6D, Blocks.WHEAT);
	private EntityAIMoveToResource moveToResource = new EntityAIMoveToResource(this, 0.6D, Blocks.FARMLAND); // Could go to wrong FARMLAND...

	public EntityFarmer(World worldIn) {
		super(worldIn);

		this.stage = -1;
	}

	protected void initEntityAI()
    {
		super.initEntityAI();
		// Add any AI that doesn't need to be instantiated separately
		// AI that needs an identifier goes in setAdditionalAItasks (See Lumberjack)
    }
	
	private void setAdditionalAItasks()
    {
		if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, moveToResource);
            this.tasks.addTask(2, moveToSupplyPoint);
            this.tasks.addTask(2, accessChest);
            this.tasks.addTask(2, harvestCrops);
        }
    }
	
	protected void updateAITasks()
	{
		if (stage < 0 || stage > 3) stage = 0;
		
		switch (stage)
		{
			case 0:
				if (!this.moveToResource.active)
				{
					this.moveToResource.active = true;
				}
				break;
			case 1:
				if (!this.harvestCrops.active) 
				{
					this.harvestCrops.active = true;
				}
				break;
			case 2:
				if (!this.moveToSupplyPoint.active) 
				{
					this.moveToSupplyPoint.active = true;
				}
				break;
			case 3:
				if (!this.accessChest.active) 
				{
					this.accessChest.active = true;
				}
				break;
		}
		
		super.updateAITasks();
		// This is called every tick and should have the AI switching code (See Lumberjack)
	}

	/**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setAdditionalAItasks();
    }
}
