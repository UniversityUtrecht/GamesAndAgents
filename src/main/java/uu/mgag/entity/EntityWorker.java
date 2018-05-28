package uu.mgag.entity;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityWorker extends EntityCreature implements INpc
{
	protected static final Logger LOGGER = LogManager.getLogger();
	protected int randomTickDivider;
	
	protected final InventoryBasic workerInventory;

    private boolean areAdditionalTasksSet;
	
	public EntityWorker(World worldIn)
    {
        this(worldIn, 0);
    }

    public EntityWorker(World worldIn, int professionId)
    {
        super(worldIn);
        this.workerInventory = new InventoryBasic("Items", false, 8);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }
	
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(2, new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.FOOD_INGREDIENTS));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    }
	
	/**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }
    
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_VILLAGER;
    }
    
    public World getWorld()
    {
        return this.world;
    }

    public BlockPos getPos()
    {
        return new BlockPos(this);
    }

}
