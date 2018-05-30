package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityWorker;

public class EntityAIMoveToResource extends EntityAIMoveToBlock {
    private final EntityWorker worker;
    private final Block resourceType;

    public EntityAIMoveToResource(EntityWorker workerIn, double speedIn, Block resourceTypeIn)
    {
        super(workerIn, speedIn, 64);
        this.worker  = workerIn;
        resourceType = resourceTypeIn;
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        return block == resourceType;
    }
}
