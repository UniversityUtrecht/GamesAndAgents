package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uu.mgag.entity.EntityWorker;
import uu.mgag.util.enums.EnumBuildingType;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityAIMoveToSupplyPoint extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	private EnumSupplyOffset supplyChest;
	public boolean complete;

	public EntityAIMoveToSupplyPoint(EntityWorker workerIn, double speedIn, EnumSupplyOffset side)
	{
		super(workerIn, speedIn, 64);
		this.worker = workerIn;
		this.supplyChest = side;
		this.complete = false;
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
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !complete;
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
	public void updateTask()
    {
		if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 1.0D) complete = true;
		super.updateTask();
    }
    
    /**
     * Return true to set given position as destination
     */
	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
		BlockPos foundationPos = pos.subtract(supplyChest.getOffset());		
		Block block = worldIn.getBlockState(foundationPos).getBlock();			
		int type = block.getMetaFromState(worldIn.getBlockState(foundationPos));
		
		if (block == Block.REGISTRY.getObject(new ResourceLocation("mm:foundation_block")) && type == EnumBuildingType.SUPPLY_POINT.getMeta())
		{
			return true;
		}		
		
		return false;
	}

}
