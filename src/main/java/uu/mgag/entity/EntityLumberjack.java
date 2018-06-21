package uu.mgag.entity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uu.mgag.entity.ai.*;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityLumberjack extends EntityWorker implements INpc
{	
	private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.BUILDING_MATERIALS);
	private EntityAIAccessChest accessChest = new EntityAIAccessChest(this, 0.6D, Block.getIdFromBlock(Blocks.LOG), 1, true);
	private EntityAIMineResource mineResource = new EntityAIMineResource(this, 0.6D, Blocks.LOG);
    private EntityAIChopWood chopWood = new EntityAIChopWood(this, 0.6D, Blocks.LOG);
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
	
	private void setAdditionalAItasks()
    {
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            //this.tasks.addTask(2, moveToResource);
            this.tasks.addTask(2, moveToSupplyPoint);
            //this.tasks.addTask(2, accessChest);
            this.tasks.addTask(2, chopWood);
        }		
    }
	
	protected void updateAITasks()
	{
	    switch(stage) {
            case DEPOSIT_RESOURCES:
                if (!this.moveToSupplyPoint.active)
                {
                    this.moveToSupplyPoint.active = true;
                    Minecraft.getMinecraft().player.sendChatMessage("Lumberjack: new stage: DEPOSIT_RESOURCES");
                }
                break;
            case GATHER_RESOURCES:
                if (!this.chopWood.active)
                {
                    this.chopWood.active = true;
                    Minecraft.getMinecraft().player.sendChatMessage("Lumberjack: new stage: GATHER_RESOURCES");
                }
                break;
            case NONE:
                moveToNextStage();
                break;
            case IDLE:
                moveToNextStage();
                break;
            default:
                moveToNextStage();
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

    @Override
    public void moveToNextStage() {
        switch(stage) {
            case DEPOSIT_RESOURCES:
                stage = EnumEntityStage.GATHER_RESOURCES;
                break;
            case GATHER_RESOURCES:
                // TODO: Check if it has enough resources before depositing
                stage = EnumEntityStage.DEPOSIT_RESOURCES;
                break;
            case IDLE:
                stage = EnumEntityStage.GATHER_RESOURCES;
                break;
            case NONE:
                stage = EnumEntityStage.IDLE;
                break;
            default:
                stage = EnumEntityStage.NONE;
                break;
        }
    }
}
