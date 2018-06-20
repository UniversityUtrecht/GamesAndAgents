package uu.mgag.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uu.mgag.entity.ai.EntityAIAccessChest;
import uu.mgag.entity.ai.EntityAIMineResource;
import uu.mgag.entity.ai.EntityAIMoveToResource;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityLumberjack extends EntityWorker implements INpc
{	
	private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.BUILDING_MATERIALS);
	private EntityAIAccessChest accessChest = new EntityAIAccessChest(this, 0.6D, Block.getIdFromBlock(Blocks.LOG), 1, true);
	private EntityAIMineResource mineResource = new EntityAIMineResource(this, 0.6D, Blocks.LOG);
	private EntityAIMoveToResource moveToResource = new EntityAIMoveToResource(this, 0.6D, Blocks.LOG);
	
	// STAGES: 0 = movetoresource, 1 = mine, 2 = movetosupply, 3 = deposit
	

	public EntityLumberjack(World worldIn) {
		super(worldIn);
	}

	protected void initEntityAI()
    {
		super.initEntityAI();
		// Add any AI that doesn't need to be instantiated separately
		// AI that needs an identifier goes in setAdditionalAItasks (See Lumberjack)
    }
	
	protected void setAdditionalAItasks()
    {
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, moveToResource);
            this.tasks.addTask(2, moveToSupplyPoint);
            this.tasks.addTask(2, accessChest);
            this.tasks.addTask(2, mineResource);
        }		
    }
	
	protected void updateAITasks()
	{
		/*
		if (stage < 0 || stage > 3) stage = 0;
		
		switch (stage)
		{
			case 0:
				if (!this.moveToResource.active) this.moveToResource.active = true;
				break;
			case 1:
				if (!this.mineResource.active) this.mineResource.active = true;
				break;
			case 2:
				if (!this.moveToSupplyPoint.active) this.moveToSupplyPoint.active = true;
				break;
			case 3:
				if (!this.accessChest.active) this.accessChest.active = true;
				break;
		}
		*/
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

	@Override
	public void moveToNextStage() {
		// TODO Auto-generated method stub
		
	}
}
