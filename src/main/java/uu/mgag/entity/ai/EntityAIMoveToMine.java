package uu.mgag.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.Minecraft;
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
import uu.mgag.util.enums.EnumMineOffset;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityAIMoveToMine extends EntityAIMoveToBlock
{
	private final EntityWorker worker;
	private EnumMineOffset mineOffset;
	public boolean active;

	public EntityAIMoveToMine(EntityWorker workerIn, double speedIn, EnumMineOffset offs)
	{
		super(workerIn, speedIn, 64);
		this.worker = workerIn;
		this.mineOffset = offs;
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
		if (this.worker.getDistanceSqToCenter(this.destinationBlock) <= 3.0D) 
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
		//Minecraft.getMinecraft().player.sendChatMessage("Moving to Supply Point");
		super.startExecuting();
    }
	
    /**
     * Return true to set given position as destination
     */
	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos)
	{
		BlockPos foundationPos = pos.subtract(mineOffset.getOffset());		
		Block block = worldIn.getBlockState(foundationPos).getBlock();			
		int type = block.getMetaFromState(worldIn.getBlockState(foundationPos));
		
		if (block == Block.REGISTRY.getObject(new ResourceLocation("mm:foundation_block")) && type == EnumBuildingType.MINE.getMeta())
		{
			return true;
		}		
		
		return false;
	}

}
