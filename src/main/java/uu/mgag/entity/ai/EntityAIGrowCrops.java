package uu.mgag.entity.ai;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.enums.EnumBuildingType;

public class EntityAIGrowCrops extends EntityAIMoveToBlock {
	private final EntityWorker worker;
	public boolean active;
	BlockPos randomBlockPos = null;
	
	public EntityAIGrowCrops(EntityWorker workerIn, double speedIn) {
		super(workerIn, speedIn, 16);
        worker = workerIn;
		this.active = false;
		this.runDelay = 10;
		this.setMutexBits(7);
		
    }
	
	/**
	 * If task is currently not running, set task it to active and immediately start executing it.
	 */
	public void activateIfNotRunning()
	{
		if(this.active != true)
		{
			this.runDelay = 0;
			this.active = true;
			randomBlockPos = new BlockPos(this.worker.workPoint.getX() - new Random().nextInt(-EnumBuildingType.FARM.getSizeX()), 
										this.worker.workPoint.getY()+2, 
										this.worker.workPoint.getZ() - new Random().nextInt(-EnumBuildingType.FARM.getSizeZ()));
		}
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
            Block block = iblockstate.getBlock();
            if (block instanceof BlockCrops && !((BlockCrops)block).isMaxAge(iblockstate))
            {
            	this.worker.world.setBlockState(blockPos.down(), Blocks.FARMLAND.getDefaultState());
                this.worker.world.setBlockState(blockPos, ((BlockCrops)block).withAge(((BlockCrops)block).getMaxAge()), 3); // 7 is max age for wheat
            }
            
            this.active = false;
    		this.worker.moveToNextStage();
            this.runDelay = 10;
        }
    }

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {

		Block blockRandom = worldIn.getBlockState(randomBlockPos).getBlock();
		IBlockState iblockstateRandom = worldIn.getBlockState(randomBlockPos);
		if(blockRandom instanceof BlockCrops && !((BlockCrops)blockRandom).isMaxAge(iblockstateRandom) && isBlockInsideFarm(worldIn, randomBlockPos))
		{
			if(pos.getX() == randomBlockPos.getX() && pos.getY() == randomBlockPos.down().getY() && pos.getZ() == randomBlockPos.getZ())
				return true;
		}
		else
		{
			Block block = worldIn.getBlockState(pos).getBlock();
			if (block == Blocks.FARMLAND)
	        {
				
				pos = pos.up();
	            IBlockState iblockstate = worldIn.getBlockState(pos);
	            block = iblockstate.getBlock();
	            if(block instanceof BlockCrops && !((BlockCrops)block).isMaxAge(iblockstate) && isBlockInsideFarm(worldIn, pos))
	            {
	            	return true;
	            }
	        }
		}
		return false;
	}
	
	private boolean isBlockInsideFarm(World worldIn, BlockPos pos)
	{
		if(this.worker.workPoint != null && 
				pos.getX() < this.worker.workPoint.getX() &&  pos.getX() > this.worker.workPoint.getX()+EnumBuildingType.FARM.getSizeX() &&
				pos.getY() > this.worker.workPoint.getY() &&  pos.getY() < this.worker.workPoint.getY()+EnumBuildingType.FARM.getSizeY() &&	
				pos.getZ() < this.worker.workPoint.getZ() &&  pos.getZ() > this.worker.workPoint.getZ()+EnumBuildingType.FARM.getSizeZ())
		{
			return true;
		}	
		return false;
	}
}