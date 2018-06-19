package uu.mgag.entity.ai;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uu.mgag.entity.EntityFarmer;
import uu.mgag.entity.EntityWorker;

public class EntityAISpawnEntity extends EntityAIBase {

	public boolean active;
	private final EntityWorker worker;
	private Entity entityToSpawn;
	private BlockPos position;
	
	public EntityAISpawnEntity(EntityWorker workerIn) {
		this.worker = workerIn;
		this.entityToSpawn = null;
		this.position = null;
		this.active = false;
		this.setMutexBits(7);
    }
	
	public void activate(BlockPos position)
    {
		this.entityToSpawn = entityToSpawn;
		this.position = position;
		this.active = true;
    }
	
	@Override
	public boolean shouldExecute() {
		return active;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return active && super.shouldContinueExecuting();
	}

	@Override
	public void startExecuting()
    {
		System.out.println("Spawning entity");
		Entity entityToSpawn = new EntityFarmer(this.worker.world);
		entityToSpawn.setPosition(position.getX()+1, position.getY(), position.getZ()+1);
		entityToSpawn.world.spawnEntity(entityToSpawn);
		
		
		
		/*
		EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this.horse.world);
        entityskeletonhorse.onInitialSpawn(p_188515_1_, (IEntityLivingData)null);
        entityskeletonhorse.setPosition(this.horse.posX, this.horse.posY, this.horse.posZ);
        entityskeletonhorse.hurtResistantTime = 60;
        entityskeletonhorse.enablePersistence();
        entityskeletonhorse.setHorseTamed(true);
        entityskeletonhorse.setGrowingAge(0);
        entityskeletonhorse.world.spawnEntity(entityskeletonhorse);
        return entityskeletonhorse;
		*/
		
		
		System.out.println("Spawned entity");
		this.active = false;
        this.worker.moveToNextStage();
    }
}
