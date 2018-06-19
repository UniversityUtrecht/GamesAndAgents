package uu.mgag.entity;

import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import uu.mgag.entity.ai.EntityAIAccessChest;
import uu.mgag.entity.ai.EntityAIHarvestCrops;
import uu.mgag.entity.ai.EntityAIMoveToReferencePoint;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.entity.ai.EntityAIReplantCrops;
import uu.mgag.entity.ai.EntityAISpawnEntity;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumBuildingType;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityFarmer extends EntityWorker implements INpc
{
	private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.FOOD_INGREDIENTS);
	private EntityAIAccessChest takeSeeds = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Items.WHEAT_SEEDS), 1, false);
	private EntityAIAccessChest depositResources = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Items.WHEAT), 1, true);
	private EntityAIHarvestCrops harvestCrops = new EntityAIHarvestCrops(this, 0.6D, Blocks.WHEAT);
	private EntityAIReplantCrops replantCrops = new EntityAIReplantCrops(this, 0.6D);
	private EntityAIMoveToReferencePoint moveToReferencePoint = new EntityAIMoveToReferencePoint(this, 0.6D, EnumBuildingType.FARM);
	
	private EntityAISpawnEntity testAI = new EntityAISpawnEntity(this); // TODO: this.world should not work...

	public EntityFarmer(World worldIn) {
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
            
            this.tasks.addTask(2, moveToSupplyPoint);
            this.tasks.addTask(2, depositResources);
            this.tasks.addTask(2, harvestCrops);
            this.tasks.addTask(2, replantCrops);
            this.tasks.addTask(2, moveToReferencePoint);
            this.tasks.addTask(2, takeSeeds);
            
            this.tasks.addTask(2, testAI);
        }
    }
	
	protected void updateAITasks()
	{
		System.out.println(stage + " " + this.moveToSupplyPoint.active);
		switch (stage) {
		case DEPOSIT_RESOURCES:
			this.depositResources.activateIfNotRunning();
			break;
		case GATHER_RESOURCES:
			this.harvestCrops.activateIfNotRunning();
			break;
		case IDLE:
			moveToNextStage();
			break;
		case MOVE_TO_SUPPLY_POINT:
			this.moveToSupplyPoint.activateIfNotRunning();
			break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			this.moveToReferencePoint.activateIfNotRunning();
			break;
		case NONE:
			moveToNextStage();
			break;
		case POST_GATHER_RESOURCES: // Replant crops
			this.replantCrops.activateIfNotRunning();
			break;
		case TAKE_TOOLS: // Take seeds, TODO: take tools
			if(!this.testAI.active)
				//this.testAI.active = true;
				this.testAI.activate(this.getPosition().south());
			
			//this.takeSeeds.activateIfNotRunning();
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
    public void moveToNextStage()
    {
    	switch (stage) {
		case DEPOSIT_RESOURCES:
			if(this.hasItemInInventory(Item.getIdFromItem(Items.WHEAT_SEEDS), 1)) // Already has seeds?
			{
				stage = EnumEntityStage.MOVE_TO_WORKING_REFERENCE_POINT;
			}
			else
			{
				stage = EnumEntityStage.TAKE_TOOLS;
			}
			break;
		case GATHER_RESOURCES:
			stage = EnumEntityStage.POST_GATHER_RESOURCES;
			break;
		case IDLE:
			stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT;
			break;
		case MOVE_TO_SUPPLY_POINT:
			stage = EnumEntityStage.DEPOSIT_RESOURCES;
			break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			stage = EnumEntityStage.GATHER_RESOURCES;
			break;
		case NONE:
			stage = EnumEntityStage.IDLE;
			break;
		case POST_GATHER_RESOURCES:
			stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT;
			break;
		case TAKE_TOOLS:
			stage = EnumEntityStage.MOVE_TO_WORKING_REFERENCE_POINT;
			break;
		default:
			stage = EnumEntityStage.NONE;
			break;
		}
    }
}
