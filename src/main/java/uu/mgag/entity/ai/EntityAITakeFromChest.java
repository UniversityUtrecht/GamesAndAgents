package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAITakeFromChest extends EntityAIMoveToBlock {
	
	private final EntityWorker worker;
	private Block itemToTake;
	private int quantity;
	
	public EntityAITakeFromChest(EntityWorker workerIn, double speedIn, Block itemToTake, int quantity) {
		super(workerIn, speedIn, 16);
		this.worker = workerIn;
		this.itemToTake = itemToTake;
		this.quantity = quantity;
		
		
	}
	
	/**
     * Returns whether the EntityAIBase should begin execution.
     */
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
	
	
	public void updateTask()
    {
        super.updateTask();
        this.worker.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.worker.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
        	// Reached chest, open and take resources.
        	
        	// Deposit items
        	//this.worker.takeItemsFromChest(this.destinationBlock.up(), itemToTake, quantity);
        	
        	// Take items
        	//this.worker.getWorkerInventory().addItem(new ItemStack(Blocks.LOG));
        	//this.worker.depositItemsToChest(this.destinationBlock.up(), itemToTake, 1);
        }
    }
	

}
