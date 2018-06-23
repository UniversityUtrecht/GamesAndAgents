package uu.mgag.entity;

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
import uu.mgag.entity.ai.EntityAIMoveToBuildSite;
import uu.mgag.entity.ai.EntityAIMoveToSupplyPoint;
import uu.mgag.util.Reference;
import uu.mgag.util.enums.EnumEntityStage;
import uu.mgag.util.enums.EnumSupplyOffset;


public class EntityBuilder extends EntityWorker implements INpc
{
    private EntityAIMoveToBuildSite moveToBuildSite = new EntityAIMoveToBuildSite(this, 0.6D);
    
    private boolean testflag = true;


	public EntityBuilder(World worldIn) {
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
        if (!this.areAdditionalTasksSet)
        {
            this.areAdditionalTasksSet = true; 
            
            this.tasks.addTask(2, moveToBuildSite);
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
            case MOVE_TO_BUILD_SITE:
                if (!this.moveToBuildSite.active) 
                {
                    this.moveToBuildSite.active = true;
                }
                break;
            case BUILD:
                if (testflag) this.loadBuilding("test");
                testflag = false;
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
            stage = EnumEntityStage.MOVE_TO_BUILD_SITE;
            break;
        case NONE:
            stage = EnumEntityStage.IDLE;
            break;
        case MOVE_TO_BUILD_SITE:
            stage = EnumEntityStage.BUILD;
            break;
        case BUILD:
            stage = EnumEntityStage.IDLE;
            break;
        default:
            stage = EnumEntityStage.NONE;
            break;
        }
    }
    
    public boolean loadBuilding(String name)
    {
        Minecraft.getMinecraft().player.sendChatMessage("Building " + name);
        
        BlockPos blockpos = this.getPosition().subtract(new Vec3i(1, 1, 1));
        
        WorldServer worldserver = (WorldServer)this.world;
        MinecraftServer minecraftserver = this.world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        
        ResourceLocation testloc = new ResourceLocation(Reference.MOD_ID, "mgag_test01");
        Template template = templatemanager.get(minecraftserver, testloc);
        //Template template = templatemanager.get(minecraftserver, new ResourceLocation(Reference.MOD_ID + ":structures/mgag_test01.nbt"));
        
        Minecraft.getMinecraft().player.sendChatMessage(testloc.getResourceDomain());
        
        if (template == null)
        {
            Minecraft.getMinecraft().player.sendChatMessage("Template null");
            return false;
        }
        else
        {
            BlockPos blockpos2 = template.getSize();
            Minecraft.getMinecraft().player.sendChatMessage(blockpos2.toString());
            
            PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_180).setIgnoreEntities(true).setChunk((ChunkPos)null).setReplacedBlock((Block)null).setIgnoreStructureBlock(false);

            template.addBlocksToWorldChunk(this.world, blockpos, placementsettings);
            return true;
        }
    }


}
