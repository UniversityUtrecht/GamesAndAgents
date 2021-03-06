package uu.mgag.entity;

import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import uu.mgag.entity.ai.EntityAIAccessChest;
import uu.mgag.entity.ai.EntityAIGrowCrops;
import uu.mgag.entity.ai.EntityAIHarvestCrops;
import uu.mgag.entity.ai.EntityAIMoveToBlockPos;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.entity.ai.EntityAIReplantCrops;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityFarmer extends EntityWorker implements INpc
{
	private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.FOOD_INGREDIENTS);
	private EntityAIAccessChest takeSeeds = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Items.WHEAT_SEEDS), 1, false);
	private EntityAIAccessChest depositResources = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Items.WHEAT), 5, true);
	private EntityAIHarvestCrops harvestCrops = new EntityAIHarvestCrops(this, 0.6D, Blocks.WHEAT);
	private EntityAIReplantCrops replantCrops = new EntityAIReplantCrops(this, 0.6D);
	private EntityAIGrowCrops growCrops = new EntityAIGrowCrops(this, 0.6D);
    private EntityAIMoveToBlockPos moveToWork = new EntityAIMoveToBlockPos(this, 0.6D);     

	public EntityFarmer(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void setAdditionalAItasks()
    {
		this.moveToWork.setDestination(workPoint);	
		
		if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, moveToSupplyPoint);
            this.tasks.addTask(2, depositResources);
            this.tasks.addTask(2, harvestCrops);
            this.tasks.addTask(2, replantCrops);
            this.tasks.addTask(2, growCrops);
            this.tasks.addTask(2, moveToWork);
            this.tasks.addTask(2, takeSeeds);
        }
    }
	
	@Override
	protected void updateAITasks()
	{
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
		case MOVE_TO_SUPPLY_POINT_FOOD:
			this.moveToSupplyPoint.activateIfNotRunning();
			break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			this.moveToWork.activateIfNotRunning();
			//this.moveToReferencePoint.activateIfNotRunning();
			break;
		case NONE:
			moveToNextStage();
			break;
		case REPLANT_RESOURCES: // Replant crops
			this.replantCrops.activateIfNotRunning();
			break;
		case GROW_RESOURCES:
			this.growCrops.activateIfNotRunning();
			break;
		case TAKE_TOOLS: // Take seeds, TODO: take tools
			this.takeSeeds.activateIfNotRunning();
			break;
		default:
			moveToNextStage();
			break;
		
		}
		
		super.updateAITasks();
		// This is called every tick and should have the AI switching code (See Lumberjack)
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
			stage = EnumEntityStage.REPLANT_RESOURCES;
			break;
		case IDLE:
			stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_FOOD;
			break;
		case MOVE_TO_SUPPLY_POINT_FOOD:
			stage = EnumEntityStage.DEPOSIT_RESOURCES;
			break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			stage = EnumEntityStage.GATHER_RESOURCES;
			break;
		case NONE:
			stage = EnumEntityStage.IDLE;
			break;
		case REPLANT_RESOURCES:
			stage = EnumEntityStage.GROW_RESOURCES;
			break;
		case GROW_RESOURCES:
			stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_FOOD;
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
