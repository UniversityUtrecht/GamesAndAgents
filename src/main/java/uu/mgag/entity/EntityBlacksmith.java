package uu.mgag.entity;

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
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.TownStats;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumSmithOffset;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityBlacksmith extends EntityWorker implements INpc
{

	private EntityAIMoveToSupplyPoint moveToSupplyPointOres = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.ORES_MINERALS);
	private EntityAIMoveToSupplyPoint moveToSupplyPointOther = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.OTHER_SUPPLIES);
	private EntityAIAccessChest takeResourcesIron = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Item.getItemFromBlock(Blocks.IRON_ORE)), 1, false);
    private EntityAIAccessChest depositResourcesIron = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Items.IRON_INGOT), 5, true);
	private EntityAIMoveToBlockPos moveToWork = new EntityAIMoveToBlockPos(this, 0.6D);
	
	public EntityBlacksmith(World worldIn) {
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
		this.moveToWork.setDestination(this.workPoint.add(EnumSmithOffset.FURNACE.getOffset()).subtract(new BlockPos(1,1,1)));
		
		if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, takeResourcesIron);
            this.tasks.addTask(2, depositResourcesIron);
            this.tasks.addTask(2, moveToSupplyPointOres);
            this.tasks.addTask(2, moveToSupplyPointOther);
            this.tasks.addTask(2, moveToWork);
        }
    }
	
	protected void updateAITasks()
	{switch (stage) 
		{
		case IDLE:
			moveToNextStage();
			break;
		case NONE:
			moveToNextStage();
			break;
		case RETURN_HOME:
			this.moveToWork.activateIfNotRunning();
			break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			this.moveToWork.activateIfNotRunning();
			break;
		case MOVE_TO_SUPPLY_POINT_ORES:
			this.moveToSupplyPointOres.activateIfNotRunning();
			break;
		case MOVE_TO_SUPPLY_POINT_OTHER:
			this.moveToSupplyPointOther.activateIfNotRunning();
			break;
		case TAKE_TOOLS:
			this.takeResourcesIron.activateIfNotRunning();
			break;
		case CONVERT_RESOURCES:
			convertToIngot();
			moveToNextStage();
			break;
		case DEPOSIT_RESOURCES:
			this.depositResourcesIron.activateIfNotRunning();
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
            if (TownStats.amount_ore > 0 ) stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_ORES;
            else stage = EnumEntityStage.RETURN_HOME;
            break;
        case NONE:
            stage = EnumEntityStage.IDLE;
            break;
        case RETURN_HOME:
        	if (TownStats.amount_ore > 0 ) stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_ORES;
        	break;
		case MOVE_TO_WORKING_REFERENCE_POINT:
			stage = EnumEntityStage.CONVERT_RESOURCES;
			break;
		case MOVE_TO_SUPPLY_POINT_ORES:
			stage = EnumEntityStage.TAKE_TOOLS;
			break;
		case MOVE_TO_SUPPLY_POINT_OTHER:
			stage = EnumEntityStage.DEPOSIT_RESOURCES;
			break;
		case TAKE_TOOLS:
			stage = EnumEntityStage.MOVE_TO_WORKING_REFERENCE_POINT;
			break;
		case CONVERT_RESOURCES:
			stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_OTHER;
			break;
		case DEPOSIT_RESOURCES:
			stage = EnumEntityStage.IDLE;
			break;
        default:
            stage = EnumEntityStage.NONE;
            break;
        }		
	}
	
	private void convertToIngot()
	{
		InventoryBasic inventory = this.getWorkerInventory();
		
		this.printWorkersInventory();
		
		for(int i = 0; i < inventory.getSizeInventory(); i++)
    	{
    		if(Item.getIdFromItem(inventory.getStackInSlot(i).getItem()) == Item.getIdFromItem(Item.getItemFromBlock(Blocks.IRON_ORE)))
    		{
    			inventory.getStackInSlot(i).setCount(0);
    		}
    	}
		
		inventory.addItem(new ItemStack(Items.IRON_INGOT, 5));
		
		this.printWorkersInventory();
	}
}
