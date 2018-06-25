package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityAIMoveToBuildSite extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	public boolean active;
	
	public BlockPos size = new BlockPos(0,0,0);
	
	public EntityAIMoveToBuildSite(EntityWorker workerIn, double speedIn)
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
		if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 1.5D) 
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
		//Minecraft.getMinecraft().player.sendChatMessage(this.destinationBlock.getX() + ", " + this.destinationBlock.getY() + ", " + this.destinationBlock.getZ());
		//Minecraft.getMinecraft().player.sendChatMessage("Moving to Build Site");
		super.startExecuting();
    }

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) 
	{
		for (int i = 0; i < size.getX(); i++) for (int j = 0; j < size.getZ(); j++)
		{
			Vec3i offset1 = new Vec3i(i, -1, j);
			Vec3i offset2 = new Vec3i(i, 0, j);
			
			Block block1 = worldIn.getBlockState(pos.add(offset1)).getBlock();
			Block block2 = worldIn.getBlockState(pos.add(offset2)).getBlock();
			
			if (block1 != Blocks.DIRT && block1 != Blocks.GRASS)
				return false;
			
			if (block2 != Blocks.AIR && block2 != Blocks.TALLGRASS && block2 != Blocks.YELLOW_FLOWER && block2 != Blocks.RED_FLOWER)
				return false;
		}
		return true;
	}
	
	public BlockPos getDestination()
	{
		return this.destinationBlock;
	}
}
