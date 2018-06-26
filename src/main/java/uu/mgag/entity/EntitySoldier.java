package uu.mgag.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntitySoldier extends EntityWorker implements INpc
{
	private int attackTimer;
	private double attackDamage = 10.0D;
	
	private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.OTHER_SUPPLIES); // TODO: change this to home location
	
	public EntitySoldier(World worldIn) {
		super(worldIn);

		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
		this.setHealth(30);

		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD)); // TODO: this does not work...
        this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
	}
	
	protected void initEntityAI()
    {
		tasks.taskEntries.clear();
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 0.7D, true)); // A little bit faster than normal walk
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.6D, 30.0F));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityZombie>(this, EntityZombie.class, true));
    }
	
	@Override
	protected void setAdditionalAItasks() {
		if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            this.tasks.addTask(4, moveToSupplyPoint);
        }
	}
	
	
	/**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }
        
        // Move back home if there are no more threats
    	if(this.getAttackTarget() == null && this.getRevengeTarget() == null) // TODO: add distance from home condition - if close to home then dont execute
        	moveToSupplyPoint.activateIfNotRunning();
        else
        	moveToSupplyPoint.active = false;
        	
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(attackDamage + this.rand.nextInt(15)));

        if (flag)
        {
            entityIn.motionY += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }
    
    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 4)
        {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
    
	@Override
	public void moveToNextStage() {
		// TODO: after coming back home tell other NPCs that it is safe
	}

	
	
	
}
