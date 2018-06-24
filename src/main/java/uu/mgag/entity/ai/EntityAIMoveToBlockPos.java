package uu.mgag.entity.ai;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.TownStats;

public class EntityAIMoveToBlockPos extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	public boolean active;
	
	public EntityAIMoveToBlockPos(EntityWorker workerIn, double speedIn)
	{
		super(workerIn, speedIn, 64);
		this.worker = workerIn;
		this.active = false;
		this.setMutexBits(7);
	}
	
	/**
	 * If task is currently not running, set task it to active and immediately start executing it.
	 */
	public void activateIfNotRunning()
	{
		if(!this.active)
		{
			this.runDelay = 0;
			this.active = true;
		}
	}
	
	/**
     * Returns whether the EntityAIBase should begin execution.
     */
	public boolean shouldExecute()
    {
		return active;
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
		//Minecraft.getMinecraft().player.sendChatMessage(this.destinationBlock.getX() + ", " + this.destinationBlock.getY() + ", " + this.destinationBlock.getZ());
		Minecraft.getMinecraft().player.sendChatMessage("Moving to BlockPos");
		super.startExecuting();
    }
	
	/**
     * Keep ticking a continuous task that has already been started
     */
	public void updateTask()
    {
		if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 3.0D) 
		{
			active = false;
            this.worker.moveToNextStage();
			this.runDelay = 0;
			return;
		}
		if (active) super.updateTask();
    }

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos)
	{
		return true;
	}
	
	public void setDestination(BlockPos pos)
	{
		this.destinationBlock = pos;
	}
}
