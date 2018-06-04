package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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

	@Override
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
    public void startExecuting()
    {
    	//this.runDelay = 10;
    	//this.active = true;
    	super.startExecuting();
    	//this.getResourceInRange();
    }

    private boolean hasBlock()
    {
        InventoryBasic inventory = worker.getWorkerInventory();
        // For now, done when worker has at least one log in inventory
        for(int i=0; i<inventory.getSizeInventory(); i++)
        {
            if (Block.getBlockFromItem(inventory.getStackInSlot(i).getItem()) instanceof BlockLog)
            {
                Minecraft.getMinecraft().player.sendChatMessage("NPC has " + resourceType.toString());
                return true;
            }
        }
        return false;
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
                this.worker.getWorkerInventory().addItem(new ItemStack(Blocks.LOG, 1)); // TODO: stack size based on drops
                worker.world.setBlockState(blockPos, ((BlockCrops)resourceType).getDefaultState(), 3); // Change this
                Minecraft.getMinecraft().player.sendChatMessage("NPC acquired " + resourceType.toString());
                this.active = false;
                this.worker.stage++;
                this.runDelay = 10;
                this.worker.printWorkersInventory();
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
