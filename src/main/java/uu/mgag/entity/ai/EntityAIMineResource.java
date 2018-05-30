package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import uu.mgag.entity.EntityWorker;

public class EntityAIMineResource extends EntityAIBase {
    private final EntityWorker worker;
    private final Block resourceType;
    private InventoryBasic inventory;

    public EntityAIMineResource(EntityWorker workerIn, double speedIn, Block resourceTypeIn) {
        worker = workerIn;
        resourceType = resourceTypeIn;
        inventory = worker.getWorkerInventory();
    }

    @Override
    public boolean shouldExecute() {
        return resourceInRange() && !hasBlock();
    }

    private boolean hasBlock()
    {
        inventory = worker.getWorkerInventory();
        // For now, done when worker has at least one log in inventory
        for(int i=0; i<inventory.getSizeInventory(); i++)
        {
            if (Block.getBlockFromItem(inventory.getStackInSlot(i).getItem()) instanceof BlockLog) {
                Minecraft.getMinecraft().player.sendChatMessage("NPC has " + resourceType.toString());
                return true;
            }
        }
        return false;
    }

    private boolean resourceInRange() {
        BlockPos pos = worker.getPos();
        inventory = worker.getWorkerInventory();

        for (int x = pos.getX()-1; x < pos.getX()+1; x++)
        {
            for (int y = pos.getY()-1; y < pos.getY() + 2; y++)
            {
                for (int z = pos.getZ() - 1; z < pos.getZ() + 1; z++)
                {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    if (worker.world.getBlockState(blockPos).getBlock() == resourceType) {
                        worker.world.destroyBlock(blockPos, false);
                        worker.getWorkerInventory().addItem(new ItemStack(resourceType));
                        Minecraft.getMinecraft().player.sendChatMessage("NPC acquired " + resourceType.toString());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
