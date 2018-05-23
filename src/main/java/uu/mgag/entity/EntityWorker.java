package uu.mgag.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

public class EntityWorker extends EntityLiving implements INpc
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final InventoryBasic workerInventory;
	
	public EntityWorker(World worldIn)
    {
        this(worldIn, 0);
    }

    public EntityWorker(World worldIn, int professionId)
    {
        super(worldIn);
        this.workerInventory = new InventoryBasic("Items", false, 8);
        //this.setProfession(professionId);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }
	
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
    }
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

}
