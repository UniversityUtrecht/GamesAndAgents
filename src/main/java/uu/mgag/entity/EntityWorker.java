package uu.mgag.entity;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import uu.mgag.util.enums.EnumEntityStage;

public class EntityWorker extends EntityCreature implements INpc
{
	protected static final Logger LOGGER = LogManager.getLogger();
	protected int randomTickDivider;
	
	protected final InventoryBasic workerInventory;

    protected boolean areAdditionalTasksSet;    
    
    // Stages will be unique to each role
   	public EnumEntityStage stage;

    public EntityWorker(World worldIn)
    {
        super(worldIn);
        this.workerInventory = new InventoryBasic("Items", false, 8);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(false);
        this.stage = EnumEntityStage.NONE;
    }
	
	protected void initEntityAI()
    {
		tasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));                
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F)); 
    }
	
	private void setAdditionalAItasks() {}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
    }
	
	protected void updateAITasks()
    {
		super.updateAITasks();
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
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setAdditionalAItasks();
    }
    
    public InventoryBasic getWorkerInventory()
    {
    	return workerInventory;
    }
    
    /**
     * Print a debug text of this workers inventory to standard output.
     */
    public void printWorkersInventory()
    {
        for(int i=0; i<workerInventory.getSizeInventory(); i++)
        {
        	ItemStack stack = workerInventory.getStackInSlot(i);
        	System.out.println(stack.getItem().getRegistryName().toString() + ": " + stack.getCount());
        }
    }
    
    /**
     * Take items from chest and put them in worker's inventory. If worker's inventory is smaller than required size, take only 
     * the amount of resources that can be carried.
     * @param chestPosition Position of the chest in the world.
     * @param item to take
     * @param quantity of items to take
     */
    public void takeItemsFromChest(BlockPos chestPosition, int itemId, int quantity)
    {
        IBlockState iblockstate = world.getBlockState(chestPosition);
        Block block = iblockstate.getBlock();
        if (block instanceof BlockChest)
        {
        	ILockableContainer chestInventory = ((BlockChest)block).getLockableContainer(world, chestPosition);
        	
        	for(int i=0; i<chestInventory.getSizeInventory(); i++)
        	{
        		if(Item.getIdFromItem(chestInventory.getStackInSlot(i).getItem()) == itemId)
        		{
        			InventoryBasic workerInventory = getWorkerInventory();
                    ItemStack takenItems = chestInventory.getStackInSlot(i).splitStack(quantity);
                    
                    ItemStack extraItems = workerInventory.addItem(takenItems);
                    if(extraItems.getCount() > 0) // Worker inventory full
                    {
                    	chestInventory.getStackInSlot(i).grow(extraItems.getCount());
                    	break;
                    }
                    else if(takenItems.getCount() < quantity) // Not enough items in current stack
                    {
                    	quantity -= takenItems.getCount();
                    }
                    else
                    {
                    	break;
                    }
        		}
        	}
        }
    }
    
    /**
     * Put items to chest from workers inventory. First fill in existing stacks, then fill in empty slots.
     * @param chestPosition Position of the chest in the world.
     * @param item to take
     * @param quantity of items to take
     */
    public void depositItemsToChest(BlockPos chestPosition, int itemId, int quantity)
    {
    	
    	IBlockState iblockstate = world.getBlockState(chestPosition);
        Block block = iblockstate.getBlock();
        if (block instanceof BlockChest)
        {
        	ILockableContainer chestInventory = ((BlockChest)block).getLockableContainer(world, chestPosition);
        	
        	for(int i=0; i<workerInventory.getSizeInventory(); i++)
        	{
        		if(Item.getIdFromItem(workerInventory.getStackInSlot(i).getItem()) == itemId)
        		{
                    ItemStack takenItems = workerInventory.getStackInSlot(i).splitStack(quantity);
                    quantity -= takenItems.getCount();
                    
                    // First fill existing inventory stacks
                    for(int j=0; j<chestInventory.getSizeInventory(); j++)
                    {
                    	ItemStack currentStack = chestInventory.getStackInSlot(j);
                    	int currentStackSize = currentStack.getCount();
                    	int maxStackSize = currentStack.getMaxStackSize();
                    	
                    	if(Item.getIdFromItem(currentStack.getItem()) == itemId &&
                    			maxStackSize != currentStackSize)
                    	{
                    		if(maxStackSize < currentStackSize + takenItems.getCount()) // Not all items will fit in current stack
                    		{
                    			currentStack.grow(maxStackSize - currentStackSize);
                    			int itemsToReturn = takenItems.getCount() - (maxStackSize - currentStackSize);
                                workerInventory.getStackInSlot(i).grow(itemsToReturn);
                                quantity += itemsToReturn;
                    			
                    		}
                    		else // All items will fit in current stack
                    		{
                    			currentStack.grow(takenItems.getCount());
                    			takenItems.setCount(0);
                    			break;
                    		}
                    	}
                    	
                    }
                    
                    if(takenItems.getCount() > 0) // Start filling empty slots
                    {
                    	for(int j=0; j<chestInventory.getSizeInventory(); j++)
                        {
                        	if(Item.getIdFromItem(chestInventory.getStackInSlot(j).getItem()) == Block.getIdFromBlock(Blocks.AIR))
                        	{
                        		chestInventory.setInventorySlotContents(j, takenItems);
                        		break;
                        	}
                        }
                    }
                    else if(quantity == 0) // No more items to be added
                    {
                    	break;
                    }
        		}
        	}
        }
    }
    
    /**
     * Move to the next appropriate stage in the state machine.
     */
    public void moveToNextStage()
    {
    	stage = EnumEntityStage.NONE;
    }

}
