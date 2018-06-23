package uu.mgag.entity;

import net.minecraft.entity.INpc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityBlacksmith extends EntityWorker implements INpc
{

	public EntityBlacksmith(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

	protected void initEntityAI()
    {
		super.initEntityAI();
		// Add any AI that doesn't need to be instantiated separately
		// AI that needs an identifier goes in setAdditionalAItasks (See Lumberjack)
    }
	
	protected void setAdditionalAItasks()
    {
		
    }
	
	protected void updateAITasks()
	{
		super.updateAITasks();
		// This is called every tick and should have the AI switching code (See Lumberjack)
	}

	/**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setAdditionalAItasks();
    }

	@Override
	public void moveToNextStage() {
		// TODO Auto-generated method stub
		
	}
}
