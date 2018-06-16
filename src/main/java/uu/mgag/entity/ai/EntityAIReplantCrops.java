package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.enums.EnumBuildingType;

public class EntityAIReplantCrops extends EntityAIMoveToBlock {
	private final EntityWorker worker;
	public boolean active;
	
	public EntityAIReplantCrops(EntityWorker workerIn, double speedIn) {
		super(workerIn, speedIn, 16);
        worker = workerIn;
		this.active = false;
		this.runDelay = 10;
		this.setMutexBits(7);
		
    }
	
	@Override
	/**
     * Returns whether the EntityAIBase should begin execution.
     */
	public boolean shouldExecute()
    {
		if(super.runDelay > 190) // 200+random is default runDelay for failed actions
		{
			this.active = false;
			this.worker.moveToNextStage(); // TODO: horrible fix for failed objective
			this.runDelay = 10;
			System.out.println("Could not find suitable location, moving to next task.");
			return false;
		}
		return active && super.shouldExecute() && this.worker.hasItemInInventory(Item.getIdFromItem(Items.WHEAT_SEEDS), 1);
    }

	@Override
	/**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return active && super.shouldContinueExecuting();
    }
	
	public void updateTask()
    {
		
		super.updateTask();
        this.worker.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.worker.getVerticalFaceSpeed());
        if (this.getIsAboveDestination())
        {
        	BlockPos blockPos = this.destinationBlock.up();
            IBlockState iblockstate = this.worker.world.getBlockState(blockPos);
            if (iblockstate.getMaterial() == Material.AIR) 
            {
            	InventoryBasic inventorybasic = this.worker.getWorkerInventory();
                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    if (!itemstack.isEmpty())
                    {
                        if (itemstack.getItem() == Items.WHEAT_SEEDS) // TODO: generalize this to all crops
                        {
                        	this.worker.world.setBlockState(blockPos.down(), Blocks.FARMLAND.getDefaultState());
                            this.worker.world.setBlockState(blockPos, Blocks.WHEAT.getDefaultState(), 3);
                            
                            itemstack.shrink(1);
                            if (itemstack.isEmpty())
                            {
                                inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
                            }
                        }
                        break;
                    }
                }
            }
            
            this.active = false;
    		this.worker.moveToNextStage();
            this.runDelay = 10;
        }
    }

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block == Blocks.DIRT || block == Blocks.FARMLAND || block == Blocks.GRASS)
        {
			pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            if(iblockstate.getMaterial() == Material.AIR && isBlockInsideFarm(worldIn, pos))
            {
            	return true;
            }
        }
		return false;
	}
	
	private boolean isBlockInsideFarm(World worldIn, BlockPos pos)
	{
		if(this.worker.referencePointDestination != null && 
				pos.getX() <= this.worker.referencePointDestination.getX() &&  pos.getX() >= this.worker.referencePointDestination.getX()+EnumBuildingType.FARM.getSizeX() &&
				pos.getY() >= this.worker.referencePointDestination.getY() &&  pos.getY() <= this.worker.referencePointDestination.getY()+EnumBuildingType.FARM.getSizeY() &&	
				pos.getZ() <= this.worker.referencePointDestination.getZ() &&  pos.getZ() >= this.worker.referencePointDestination.getZ()+EnumBuildingType.FARM.getSizeZ())
		{
			return true;
		}	
		return false;
	}
}
