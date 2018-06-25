package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAIHarvestCrops extends EntityAIMoveToBlock {
	private final EntityWorker worker;
    private Block resourceType;
	public boolean active;
	
	public EntityAIHarvestCrops(EntityWorker workerIn, double speedIn, Block resourceTypeIn) {
		super(workerIn, speedIn, 16);
        worker = workerIn;
        resourceType = resourceTypeIn; // TODO: generalize this to all crops
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
			System.out.println("Could not find suitable resources, moving to next task.");
			return false;
		}
		return active && super.shouldExecute();
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
            if (worker.world.getBlockState(blockPos).getBlock() == resourceType &&
            		block instanceof BlockCrops && 
            		((BlockCrops)block).isMaxAge(iblockstate)) 
            {
                this.worker.getWorkerInventory().addItem(new ItemStack(Items.WHEAT, 1)); // TODO: stack size based on drops
                worker.world.setBlockState(blockPos, ((BlockCrops)resourceType).getDefaultState(), 3); // Change this
                //Minecraft.getMinecraft().player.sendChatMessage("NPC acquired " + resourceType.toString());
                this.active = false;
                this.worker.moveToNextStage();
                this.runDelay = 10;
            }
        }
    }

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                return true;
            }
        }
		return false;
	}
}
