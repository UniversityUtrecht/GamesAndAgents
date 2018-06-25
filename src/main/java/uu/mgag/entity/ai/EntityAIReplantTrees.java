package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import uu.mgag.entity.EntityWorker;

public class EntityAIReplantTrees extends EntityAIBase {
    private EntityWorker worker;
    public boolean active;

    public EntityAIReplantTrees(EntityWorker workerIn, double speedIn)
    {
        worker = workerIn;
        this.active = false;
        this.setMutexBits(7);
    }

    @Override
    public boolean shouldExecute() {
        return active;
    }

    @Override
    public void updateTask() {
        if (active)
        {
            plantTrees();
            this.worker.moveToNextStage();
            active = false;
        }
    }

    private void plantTrees() {
        BlockPos origin = worker.getPosition();
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                // Don't place on agent
                if (x == 0 && z == 0)
                    continue;

                BlockPos treePos = new BlockPos(origin.getX() + 3 * x, origin.getY(), origin.getZ() + 3 * z);
                Block block = worker.getEntityWorld().getBlockState(treePos).getBlock();
                if (block == Blocks.AIR  || block == Blocks.GRASS)
                {
                    Minecraft.getMinecraft().player.sendChatMessage("Planting tree at: " + treePos.toString());
                    //worker.getEntityWorld().setBlockState(treePos, Blocks.SAPLING.getDefaultState());
                    //((BlockSapling)Blocks.SAPLING).generateTree(this.worker.world, treePos, this.worker.world.getBlockState(treePos), this.worker.world.rand);
                    for (int y = 0; y < 4; y++)
                    {
                        BlockPos logPos = new BlockPos(treePos.getX(), treePos.getY() + y, treePos.getZ());
                        this.worker.world.setBlockState(logPos, Blocks.LOG.getDefaultState());
                    }
                }
            }
        }
    }
}
