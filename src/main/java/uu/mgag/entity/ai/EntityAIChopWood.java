package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAIChopWood extends EntityAIMoveToBlock {
    private final EntityWorker worker;
    private final Block resourceType;
    private InventoryBasic inventory;
    public boolean active;
    private final int maxResourceCount;

    public EntityAIChopWood(EntityWorker workerIn, double speedIn, Block resourceTypeIn, int maxResourceCount) {
        super(workerIn, speedIn, 32);
        worker = workerIn;
        resourceType = resourceTypeIn;
        inventory = worker.getWorkerInventory();
        this.active = false;
        this.maxResourceCount = maxResourceCount;
        this.setMutexBits(7);
    }

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

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {

        return active &&super.shouldContinueExecuting();
    }

    public void updateTask()
    {
        super.updateTask();

        this.worker.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.worker.getVerticalFaceSpeed());
        if (worker.getPosition().distanceSq(destinationBlock) < 20)
        {
            IBlockState blockState = this.worker.world.getBlockState(destinationBlock);

            Block block = blockState.getBlock();
            if (block == resourceType && block instanceof BlockLog)
            {
                this.worker.addItemToInventory(Item.getItemFromBlock(resourceType), 1);
                this.worker.world.destroyBlock(destinationBlock, false);
                Minecraft.getMinecraft().player.sendChatMessage("NPC acquired " + resourceType.toString());
                this.runDelay = 10;
            }
        }

        if (this.worker.getItemAmountInInventory(Item.getItemFromBlock(resourceType)) >= maxResourceCount)
        {
            this.active = false;
            this.worker.moveToNextStage();
            this.runDelay = 10;
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        for (int i = -1; i < 5; i++) {
            destinationBlock = new BlockPos(pos.getX(), pos.getY() + i, pos.getZ());
            Block block = worldIn.getBlockState(destinationBlock).getBlock();
            if (block == Blocks.LOG) {
                return true;
            }
        }
        return false;
    }
}
