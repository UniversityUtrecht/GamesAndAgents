package uu.mgag.entity;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityBetterZombie extends EntityZombie {

	public EntityBetterZombie(World worldIn) {
		super(worldIn);
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityWorker>(this, EntityWorker.class, true));
	}
	
	@Override
	protected boolean shouldBurnInDay()
    {
        return false;
    }

}
