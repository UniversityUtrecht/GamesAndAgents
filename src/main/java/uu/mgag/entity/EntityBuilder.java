package uu.mgag.entity;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import uu.mgag.entity.ai.EntityAIMoveToBlockPos;
import uu.mgag.entity.ai.EntityAIMoveToBuildSite;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.Reference;
import uu.mgag.util.TownStats;
import uu.mgag.util.enums.EnumBuildingType;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumEntityType;
import uu.mgag.util.enums.EnumSupplyOffset;


public class EntityBuilder extends EntityWorker implements INpc
{    
    private boolean testflag = true;
    private BlockPos padding = new BlockPos(8,0,8);
    
    private EnumBuildingType nextBuilding = null;
    
    private boolean needBuild = false;
    private boolean needSpawn = false;

    private EntityAIMoveToBuildSite moveToBuildSite = new EntityAIMoveToBuildSite(this, 0.6D);
    private EntityAIMoveToBlockPos moveToHome = new EntityAIMoveToBlockPos(this, 0.6D);
    private EntityAIMoveToSupplyPoint moveToSupplyPointFood = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.FOOD_INGREDIENTS);
    private EntityAIMoveToSupplyPoint moveToSupplyPointBuild = new EntityAIMoveToSupplyPoint(this, 0.6D, EnumSupplyOffset.BUILDING_MATERIALS);

	public EntityBuilder(World worldIn)
	{
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
		homePoint = new BlockPos(this.posX, this.posY, this.posZ);
		this.moveToHome.setDestination(homePoint);		
		
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, moveToBuildSite);
            this.tasks.addTask(2, moveToHome);
            this.tasks.addTask(2, moveToSupplyPointFood);
            this.tasks.addTask(2, moveToSupplyPointBuild);
        }
    }
	
	protected void updateAITasks()
	{
        switch (stage)
        {
            case IDLE:
                moveToNextStage();
                break;
            case NONE:
                moveToNextStage();
                break;
            case DECIDE_NEXT_BUILD:
            	this.nextBuilding = this.decideNext();
            	if (this.nextBuilding != null) moveToNextStage();
            	break;
            case RETURN_HOME:
            	this.moveToHome.activateIfNotRunning();
            	break;
            case MOVE_TO_SUPPLY_POINT_BUILD:
            	this.moveToSupplyPointBuild.activateIfNotRunning();
            	break;
            case MOVE_TO_SUPPLY_POINT_FOOD:
            	this.moveToSupplyPointFood.activateIfNotRunning();
            	break;
            case MOVE_TO_BUILD_SITE:
                if (!this.moveToBuildSite.active) 
                {
                	BlockPos size = this.loadBuildingSize("mgag_" + this.nextBuilding.getName());
                	
                	if (size == null) break;
                	
                	this.moveToBuildSite.size = size;                	
                    this.moveToBuildSite.activateIfNotRunning();
                }
                break;
            case BUILD:
            	this.loadBuilding("mgag_" + this.nextBuilding.getName());
            	moveToNextStage();
                break;
            case SPAWN:
        		this.spawnNecessary();            		
            	this.updateTownStatsResources();
            	this.updateTownStatsBuildings();            	
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
    public void moveToNextStage()
    {
        switch (stage)
        {
        case IDLE:
            stage = EnumEntityStage.DECIDE_NEXT_BUILD;
            break;
        case NONE:
            stage = EnumEntityStage.IDLE;
            break;
        case RETURN_HOME:
        	stage = EnumEntityStage.IDLE;
        	break;
        case MOVE_TO_SUPPLY_POINT_BUILD:
        	stage = EnumEntityStage.MOVE_TO_BUILD_SITE;
        	break;
        case MOVE_TO_SUPPLY_POINT_FOOD:
        	stage = EnumEntityStage.SPAWN;
        	break;
        case DECIDE_NEXT_BUILD:
        	if (TownStats.count_supply == 0)
        		stage = EnumEntityStage.MOVE_TO_BUILD_SITE;
        	else if (needBuild)
        		stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_BUILD;
        	else if (needSpawn)
        		stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_FOOD;
        	break;
        case MOVE_TO_BUILD_SITE:
            stage = EnumEntityStage.BUILD;
            break;
        case BUILD:
        	stage = EnumEntityStage.MOVE_TO_SUPPLY_POINT_FOOD;
            break;
        case SPAWN:
            stage = EnumEntityStage.RETURN_HOME;
            break;        	
        default:
            stage = EnumEntityStage.NONE;
            break;
        }
    }
    
    public EnumBuildingType decideNext()
    {
    	if (TownStats.count_supply == 0) if (TownStats.res_stone >= 10 && TownStats.res_wood >= 10)
    	{
    		needBuild = true;
    		needSpawn = false;
    		return EnumBuildingType.SUPPLY_POINT;
    	}
    	
    	/*if (TownStats.count_hall == 0) if (TownStats.res_stone >= 50 && TownStats.res_wood >= 50)
    	{
    		needBuild = true;
    		needSpawn = false;
    		return EnumBuildingType.TOWN_HALL;
    	}
    	
    	if (TownStats.count_hall == 1) if (TownStats.res_wood >= 20 && TownStats.res_food >= 50)
    	{
    		needBuild = true;
    		needSpawn = true;
    		return EnumBuildingType.FARM;
    	}
    	
    	if (TownStats.count_hall == 1) if (TownStats.res_wood < 20 && TownStats.res_food >= 50)
    	{
    		needBuild = false;
    		needSpawn = true;
    		return EnumBuildingType.NO_BUILDING;
    	}
    	
    	if (TownStats.count_hall == 1) if (TownStats.res_stone < 20 && TownStats.res_food >= 50)
    	{
    		needBuild = true;
    		needSpawn = true;
    		return EnumBuildingType.MINE;
    	}*/
    	
    	if (TownStats.count_supply == 1) if (TownStats.count_smith < 1)
    	{
    		needBuild = true;
    		needSpawn = true;
    		return EnumBuildingType.BLACKSMITH;
    	}
    	
    	needBuild = false;
    	needSpawn = false;
    	return null;
    }
    
    private void spawnNecessary()
    {
    	switch (nextBuilding)
    	{
    	case FARM:
    		this.world.spawnEntity(this.spawnNewEntity(this.world, this.getPosition(), EnumEntityType.FARMER));
    		break;
    	case NO_BUILDING:
			this.world.spawnEntity(this.spawnNewEntity(this.world, this.getPosition(), EnumEntityType.LUMBERJACK));
			break;
    	case MINE:
			this.world.spawnEntity(this.spawnNewEntity(this.world, this.getPosition(), EnumEntityType.MINER));
			break;    	
    	case BLACKSMITH:
			this.world.spawnEntity(this.spawnNewEntity(this.world, this.getPosition(), EnumEntityType.BLACKSMITH));
			break;      		
    	default:
    		break;
    	}
    }
    
    public BlockPos loadBuildingSize(String name)
    {
    	WorldServer worldserver = (WorldServer)this.world;
        MinecraftServer minecraftserver = this.world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        
        ResourceLocation testloc = new ResourceLocation(Reference.MOD_ID, name);
        Template template = templatemanager.get(minecraftserver, testloc);
                
        if (template == null)
        {
			Minecraft.getMinecraft().player.sendChatMessage("Template null");
            return null;
        }
        else return template.getSize().add(padding);
    }
    
    public boolean loadBuilding(String name)
    {
        Minecraft.getMinecraft().player.sendChatMessage("Building " + name);
        
        BlockPos blockpos = this.moveToBuildSite.getDestination().add(new BlockPos(padding.getX() / 2, -1, padding.getZ() / 2));
        
        WorldServer worldserver = (WorldServer)this.world;
        MinecraftServer minecraftserver = this.world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        
        ResourceLocation testloc = new ResourceLocation(Reference.MOD_ID, name);
        Template template = templatemanager.get(minecraftserver, testloc);
        //Template template = templatemanager.get(minecraftserver, new ResourceLocation(Reference.MOD_ID + ":structures/mgag_test01.nbt"));
                
        if (template == null)
        {
            Minecraft.getMinecraft().player.sendChatMessage("Template null");
            return false;
        }
        else
        {
            BlockPos blockpos2 = template.getSize();            

            this.workPoint = blockpos.add(new BlockPos(blockpos2.getX(), 0, blockpos2.getZ()));        	
        	Minecraft.getMinecraft().player.sendChatMessage("SpawnWork: " + this.workPoint.getX() + "," + this.workPoint.getY() + "," + this.workPoint.getZ());
            //Minecraft.getMinecraft().player.sendChatMessage(blockpos2.toString());
            
            PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true).setChunk((ChunkPos)null).setReplacedBlock((Block)null).setIgnoreStructureBlock(false);

            template.addBlocksToWorldChunk(this.world, blockpos, placementsettings);
            return true;
        }
    }
    
    private void updateTownStatsBuildings()
    {
    	switch (nextBuilding)
        {
    	case SUPPLY_POINT:
    		TownStats.count_supply++;
    		break;
    	case TOWN_HALL:
    		TownStats.count_hall++;
    		break;
    	case FARM:
    		TownStats.count_farm++;
    		break;
    	case BLACKSMITH:
    		TownStats.count_smith++;
    		break;
    	case MINE:
    		TownStats.count_mine++;
    		break;
    	case NO_BUILDING:
    		TownStats.count_lumber++;
    		break;
		default:
			break;    		
        }
    	
    	this.nextBuilding = null;
    }
    
    private void updateTownStatsResources()
    {
    	switch (nextBuilding)
        {
    	case SUPPLY_POINT:
    		TownStats.res_stone -= 10;
    		TownStats.res_wood -= 10;
    		break;
    	case TOWN_HALL:
    		TownStats.res_stone -= 50;
    		TownStats.res_wood -= 50;
    		break;
    	case FARM:
    		TownStats.res_wood -= 20;
    		TownStats.res_food -= 50;
    		break;
    	case BLACKSMITH:
    		TownStats.res_wood -= 30;
    		TownStats.res_stone -= 30;
    		TownStats.res_food -= 50;
    		break;
    	case MINE:
    		TownStats.res_food -= 50;
    		break;
    	case NO_BUILDING:
    		TownStats.res_food -= 50;
    		break;
		default:
			break;    		
        }
    }
    
    //what you need to do is call this.worker.world.spawnEntity(EntityFarmer.spawnNewEntity(this.worker.world, this.worker.getPosition()    
    public EntityWorker spawnNewEntity(World world, BlockPos position, EnumEntityType type)
    {
    	EntityWorker newEntity;
    	
    	switch (type)
    	{
    	case BUILDER:
    		newEntity = new EntityBuilder(world);
    		break;
    	case FARMER:
    		newEntity = new EntityFarmer(world);
    		break;
    	case LUMBERJACK:
    		newEntity = new EntityLumberjack(world);
    		break;
    	case MINER:
    		newEntity = new EntityMiner(world);
    		break;
    	case BLACKSMITH:
    		newEntity = new EntityBlacksmith(world);
    		break;
		default:
			newEntity = new EntityBuilder(world);
			break;
    	}
    	
    	newEntity.setPosition(position.getX(), position.getY(), position.getZ());
    	newEntity.workPoint = this.workPoint;
    	newEntity.setAdditionalAItasks();
    	return newEntity;
    }

}
