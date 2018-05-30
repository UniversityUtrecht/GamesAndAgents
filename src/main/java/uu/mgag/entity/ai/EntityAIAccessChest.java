package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAIAccessChest extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	private Block itemToAccess;
	private int quantity;
	private boolean deposit;
	public boolean complete;

    private int timeoutCounter;
    private int maxStayTicks;
	
	public EntityAIAccessChest(EntityWorker workerIn, double speedIn, Block itemIn, int quantityIn, boolean depositIn)
	{
		super(workerIn, speedIn, 64);
		this.worker = workerIn;
		this.itemToAccess = itemIn;
		this.quantity = quantityIn;
		this.deposit = depositIn;
		this.complete = false;
	}
	
	@Override
	public boolean shouldExecute()
	{
        if (this.runDelay <= 0)
        {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.worker.world, this.worker))
            {
                return false;
            }
        }

        return super.shouldExecute();
	}

	/**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !complete;
    }
	
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.timeoutCounter = 0;
        this.maxStayTicks = this.worker.getRNG().nextInt(this.worker.getRNG().nextInt(1200) + 1200) + 1200;
    }

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();

        pos = pos.up();
        IBlockState iblockstate = worldIn.getBlockState(pos);
        block = iblockstate.getBlock();

        if (block instanceof BlockChest)
        {
            return true;
        }
		return false;
	}
	
	/**
     * Keep ticking a continuous task that has already been started
     */
	public void updateTask()
    {
        //super.updateTask();
        this.worker.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.worker.getVerticalFaceSpeed());

        if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 1.5D)
        {
        	if (this.deposit)
        		this.worker.depositItemsToChest(this.destinationBlock.up(), itemToAccess, 1);
        	else	
            	this.worker.takeItemsFromChest(this.destinationBlock.up(), itemToAccess, quantity);

    		this.worker.printWorkersInventory();
        	this.complete = true;    
        }
    }

}