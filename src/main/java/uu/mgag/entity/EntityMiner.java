package uu.mgag.entity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.ai.EntityAIAccessChest;
import uu.mgag.entity.ai.EntityAIMoveToBlockPos;
import uu.mgag.entity.ai.EntityAIMoveToMine;
import uu.mgag.entity.ai.EntityAIMoveToReferencePoint;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.enums.EnumBuildingType;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumMineOffset;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityMiner extends EntityWorker implements INpc
{
	private BlockPos minePoint;
	private int counter = 1;
	private Block mined;
	
	private EntityAIMoveToSupplyPoint moveToSupplyPointOres = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.ORES_MINERALS);
	private EntityAIMoveToSupplyPoint moveToSupplyPointBuild = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.BUILDING_MATERIALS);
	private EntityAIAccessChest depositResourcesStone = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Item.getItemFromBlock(Blocks.COBBLESTONE)), 5, true);
	private EntityAIAccessChest depositResourcesIron = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Item.getItemFromBlock(Blocks.IRON_ORE)), 1, true);
	private EntityAIMoveToBlockPos moveToWork = new EntityAIMoveToBlockPos(this, 0.6D);

	public EntityMiner(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

	protected void initEntityAI()
    {
		super.initEntityAI();
		// Add any AI that doesn't need to be instantiated separately
		// AI that needs an identifier goes in setAdditionalAItasks (See Lumberjack)
    }
	
	protected void setAdditionalAItasks()
    {		
		/*1,1,1 is offset of workpoint from foundation*/
		this.moveToWork.setDestination(this.workPoint.add(EnumMineOffset.ENTRANCE.getOffset()).subtract(new BlockPos(1,1,1))); 
		this.minePoint = this.workPoint.add(EnumMineOffset.MINE_SOUTHEAST.getOffset()).subtract(new BlockPos(1,1,1));
		
		Minecraft.getMinecraft().player.sendChatMessage("Mine Point: " + minePoint.toString());
		
		if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, depositResourcesStone);
            this.tasks.addTask(2, depositResourcesIron);
            this.tasks.addTask(2, moveToSupplyPointOres);
            this.tasks.addTask(2, moveToSupplyPointBuild);
            this.tasks.addTask(2, moveToWork);
        }
    }
	
	@Override
	protected void updateAITasks()
	{
		switch (stage) 
		{
		case IDLE:
			moveToNextStage();
			break;
		case NONE:
			moveToNextStage();
			break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			this.moveToWork.activateIfNotRunning();
			break;
		case MOVE_TO_SUPPLY_POINT_ORES:
			this.moveToSupplyPointOres.activateIfNotRunning();
			break;
		case MOVE_TO_SUPPLY_POINT_BUILD:
			this.moveToSupplyPointBuild.activateIfNotRunning();
			break;
		case GATHER_RESOURCES:
			mineBlock();
			moveToNextStage();
			break;
		case DEPOSIT_RESOURCES:
			if (mined == Blocks.STONE) this.depositResourcesStone.activateIfNotRunning();
			else if (mined == Blocks.IRON_ORE) this.depositResourcesIron.activateIfNotRunning();
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
        switch (stage)
        {
        case IDLE:
            stage = EnumEntityStage.MOVE_TO_WORKING_REFERENCE_POINT;
            break;
        case NONE:
            stage = EnumEntityStage.IDLE;
            break;
        case MOVE_TO_WORKING_REFERENCE_POINT:
        	stage = EnumEntityStage.GATHER_RESOURCES;
        	break;
		case MOVE_TO_SUPPLY_POINT_ORES:
			if (mined == Blocks.IRON_ORE) stage = EnumEntityStage.DEPOSIT_RESOURCES;
			else stage = EnumEntityStage.IDLE;
			break;
		case MOVE_TO_SUPPLY_POINT_BUILD:
			if (mined == Blocks.STONE) stage = EnumEntityStage.DEPOSIT_RESOURCES;
			else stage = EnumEntityStage.IDLE;
			break;
        case GATHER_RESOURCES:
        	if (mined == Blocks.STONE) stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_BUILD;
        	else if (mined == Blocks.IRON_ORE) stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_ORES;
        	else stage = EnumEntityStage.IDLE;
        	break;
        case DEPOSIT_RESOURCES:
        	stage = EnumEntityStage.IDLE;
        	break;
        default:
            stage = EnumEntityStage.NONE;
            break;
        }		
	}
	
	private void mineBlock()
	{
		InventoryBasic inventory = this.getWorkerInventory();
		
		mined = world.getBlockState(minePoint).getBlock();
		
		this.world.destroyBlock(minePoint, false);
		
		switch (counter)
		{
		case 1: case 2: case 7: case 8:
			this.minePoint = this.minePoint.west();
			break;
		case 3: case 6:
			this.minePoint = this.minePoint.north();
			break;
		case 4: case 5:
			this.minePoint = this.minePoint.east();
			break;
		case 9:
			this.minePoint = this.minePoint.down().east(2).south(2);
			counter = 0;
			break;
		default:
			break;
		}
		
		if (mined == Blocks.STONE)
		{
			inventory.addItem(new ItemStack(Blocks.COBBLESTONE));
		}
		
		if (mined == Blocks.IRON_ORE)
		{
			inventory.addItem(new ItemStack(Blocks.IRON_ORE));
		}
		
		counter++;
	}

}
