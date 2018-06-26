package uu.mgag.entity;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.INpc;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import uu.mgag.entity.ai.*;
import uu.mgag.util.Reference;
import uu.mgag.util.TownStats;
import uu.mgag.util.enums.EnumBuildingType;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumHallOffset;
import uu.mgag.util.enums.EnumSupplyOffset;

public class EntityLumberjack extends EntityWorker implements INpc
{
    private int maxResourceCount = 5;
    private int replantCount = 0;

    private BlockPos padding = new BlockPos(4,0,4);

    private EntityAIMoveToSupplyPoint moveToSupplyPoint = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.BUILDING_MATERIALS);
	private EntityAIAccessChest accessChest = new EntityAIAccessChest(this, 0.6D, Item.getIdFromItem(Item.getItemFromBlock(Blocks.LOG)), maxResourceCount, true);
	private EntityAIMineResource mineResource = new EntityAIMineResource(this, 0.6D, Blocks.LOG);
    private EntityAIChopWood chopWood = new EntityAIChopWood(this, 0.6D, Blocks.LOG, maxResourceCount);
    private EntityAIMoveToBuildSite moveToBuildSite = new EntityAIMoveToBuildSite(this, 0.6D);


    public EntityLumberjack(World worldIn) {
		super(worldIn);
	}

	protected void initEntityAI()
    {
		super.initEntityAI();
		// Add any AI that doesn't need to be instantiated separately
		// AI that needs an identifier goes in setAdditionalAItasks (See Lumberjack)
    }
	
	protected void setAdditionalAItasks()
    {
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, moveToSupplyPoint);
            this.tasks.addTask(2, accessChest);
            this.tasks.addTask(2, chopWood);
            this.tasks.addTask(2, moveToBuildSite);
        }		
    }
	
	protected void updateAITasks()
	{
	    switch(stage) {
            case MOVE_TO_SUPPLY_POINT_BUILD:
                if (!this.moveToSupplyPoint.active)
                {
                    this.moveToSupplyPoint.active = true;
                    //Minecraft.getMinecraft().player.sendChatMessage("Lumberjack: new stage: MOVE_TO_SUPPLY_POINT");
                }
                break;
            case DEPOSIT_RESOURCES:
                if (!this.accessChest.active)
                {
                    this.accessChest.active = true;
                    //Minecraft.getMinecraft().player.sendChatMessage("Lumberjack: new stage: DEPOSIT_RESOURCES");
                }
                break;
            case GATHER_RESOURCES:
                if (!this.chopWood.active)
                {
                    this.chopWood.active = true;
                    //Minecraft.getMinecraft().player.sendChatMessage("Lumberjack: new stage: GATHER_RESOURCES");
                }
                break;
            case MOVE_TO_BUILD_SITE:
                if (!this.moveToBuildSite.active)
                {
                    BlockPos size = new BlockPos(5,5,5);

                    this.moveToBuildSite.size = size.add(padding);
                    this.moveToBuildSite.activateIfNotRunning();
                }
                break;
            case REPLANT_RESOURCES:
                this.loadTree();
                moveToNextStage();
                break;
            case NONE:
                moveToNextStage();
                break;
            case IDLE:
                moveToNextStage();
                break;
            default:
                moveToNextStage();
                break;
        }
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
        switch(stage) {
            case DEPOSIT_RESOURCES:
                stage = EnumEntityStage.GATHER_RESOURCES;
                break;
            case GATHER_RESOURCES:
                // TODO: Check if it has enough resources before depositing
                stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_BUILD;
                break;
            case MOVE_TO_SUPPLY_POINT_BUILD:
                if (this.workerInventory.isEmpty())
                {
                    stage = EnumEntityStage.MOVE_TO_BUILD_SITE;
                }
                else stage = EnumEntityStage.DEPOSIT_RESOURCES;
                break;
            case MOVE_TO_BUILD_SITE:
                stage = EnumEntityStage.REPLANT_RESOURCES;
                break;
            case REPLANT_RESOURCES:
                if (replantCount == 5) stage = EnumEntityStage.GATHER_RESOURCES;
                else stage = EnumEntityStage.MOVE_TO_BUILD_SITE;
                break;
            case IDLE:
                stage = EnumEntityStage.GATHER_RESOURCES;
                break;
            case NONE:
                stage = EnumEntityStage.IDLE;
                break;
            default:
                stage = EnumEntityStage.NONE;
                break;
        }
    }
    
    public boolean loadTree()
    {
    	int randomNum = ThreadLocalRandom.current().nextInt(1/*min*/, 10/*max*/ + 1);
    	
        BlockPos blockpos = this.moveToBuildSite.getDestination().add(new BlockPos(padding.getX() / 2, 0, padding.getZ() / 2));
        
        WorldServer worldserver = (WorldServer)this.world;
        MinecraftServer minecraftserver = this.world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        
        ResourceLocation testloc = new ResourceLocation(Reference.MOD_ID, "mgag_tree_" + randomNum);
        Template template = templatemanager.get(minecraftserver, testloc);
                
        if (template == null)
        {
            Minecraft.getMinecraft().player.sendChatMessage("Template null");
            return false;
        }
        else
        {
            BlockPos blockpos2 = template.getSize();
            
            //Minecraft.getMinecraft().player.sendChatMessage(blockpos2.toString());
            
            PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true).setChunk((ChunkPos)null).setReplacedBlock((Block)null).setIgnoreStructureBlock(false);

            template.addBlocksToWorldChunk(this.world, blockpos, placementsettings);
            
            replantCount++;
            return true;
        }
    }
}
