package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.enums.EnumBuildingType;

public class EntityAIMoveToReferencePoint extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	private EnumBuildingType referencePoint;
	public boolean active;

	public EntityAIMoveToReferencePoint(EntityWorker workerIn, double speedIn, EnumBuildingType referencePoint) // TODO: refactor EnumBuildingType this
	{
		super(workerIn, speedIn, 64);
		this.worker = workerIn;
		this.referencePoint = referencePoint;
		this.active = false;
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
		//Minecraft.getMinecraft().player.sendChatMessage("Moving to Reference Point");
		super.startExecuting();
    }
	
    /**
     * Return true to set given position as destination
     */
	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos)
	{
		Block block = worldIn.getBlockState(pos).getBlock();		
		int type = block.getMetaFromState(worldIn.getBlockState(pos));
		if (block == Block.REGISTRY.getObject(new ResourceLocation("mm:foundation_block")) && type == referencePoint.getMeta())
		{
			return true;
		}		
		
		return false;
	}

}
