package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAIAccessChest extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	private int itemIdToAccess = 0;
	private int quantity;
	private boolean deposit;
	public boolean active;

    private int timeoutCounter;
    private int maxStayTicks;
	
    public EntityAIAccessChest(EntityWorker workerIn, double speedIn, int itemIdIn, int quantityIn, boolean depositIn)
	{
		super(workerIn, speedIn, 64);
		this.worker = workerIn;
		this.itemIdToAccess = itemIdIn;
		this.quantity = quantityIn;
		this.deposit = depositIn;
		this.active = false;
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
	
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
    	//Minecraft.getMinecraft().player.sendChatMessage("Accessing Chest");
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

        if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 8.0D)
        {
        	if (this.deposit)
        	{
        		this.worker.depositItemsToChest(this.destinationBlock.up(), itemIdToAccess, quantity);
        	}
        	else	
        	{
        		this.worker.takeItemsFromChest(this.destinationBlock.up(), itemIdToAccess, quantity);
        	}
        	this.active = false;
        	//this.worker.stage++;
        	this.worker.moveToNextStage();
        	this.runDelay = 0;
        }
    }
	
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

}
