package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAIMoveToResource extends EntityAIMoveToBlock {
    private final EntityWorker worker;
    private Block resourceType;
	public boolean active;

    public EntityAIMoveToResource(EntityWorker workerIn, double speedIn, Block resourceTypeIn)
    {
        super(workerIn, speedIn, 64);
        this.worker  = workerIn;
        this.resourceType = resourceTypeIn;
		this.active = false;
		this.setMutexBits(7);
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
	public void updateTask()
    {
		if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 3.0D) 
		{
			active = false;
			this.worker.stage++;
			this.runDelay = 0;
			return;
		}
		if (active) super.updateTask();
    }
	
	/**
     * Returns whether the EntityAIBase should begin execution.
     */
	public boolean shouldExecute()
    {
		return active && super.shouldExecute();
    }
	
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return active && super.shouldContinueExecuting();
    }
    
    public void startExecuting()
    {
		Minecraft.getMinecraft().player.sendChatMessage("Moving to Resource");
    	super.startExecuting();
    }
	
    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
    	if (!active) return false;
        Block block = worldIn.getBlockState(pos).getBlock();
        return block == resourceType;
    }
    
    public void setResourceType(Block resourceTypeIn)
    {
    	this.resourceType = resourceTypeIn;
    }
}
