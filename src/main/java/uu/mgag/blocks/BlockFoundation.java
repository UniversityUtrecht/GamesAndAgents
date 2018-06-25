package uu.mgag.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import uu.mgag.Main;
import uu.mgag.init.BlockInit;
import uu.mgag.init.ItemInit;
import uu.mgag.util.ItemBlockVariants;
import uu.mgag.util.enums.EnumBuildingType;
import uu.mgag.util.interfaces.IHasModel;
import uu.mgag.util.interfaces.IMetaName;

public class BlockFoundation extends Block implements IHasModel, IMetaName
{
	public static final PropertyEnum<EnumBuildingType> VARIANT = PropertyEnum.<EnumBuildingType>create("variant", EnumBuildingType.class);
	private final EnumBuildingType type;
		
	public BlockFoundation(String name, Material materialIn)
	{
		this(name, materialIn, EnumBuildingType.NO_BUILDING);
	}
	
	public BlockFoundation(String name, Material materialIn, EnumBuildingType typeIn)
	{
		super(Material.ROCK);
		setUnlocalizedName(name);
		setRegistryName(name);		
		setBlockUnbreakable();
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumBuildingType.NO_BUILDING));
		
		this.type = typeIn;
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public int damageDropped(IBlockState state) 
	{
		return ((EnumBuildingType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) 
	{
		for(EnumBuildingType customblockfoundation$enumtype : EnumBuildingType.values())
		{
			items.add(new ItemStack(this, 1, customblockfoundation$enumtype.getMeta()));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(VARIANT, EnumBuildingType.byMetadata(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) 
	{
		return ((EnumBuildingType)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) 
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}

	@Override
	public String getSpecialName(ItemStack stack) 
	{
		return EnumBuildingType.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void registerModels() 
	{
		for(int i = 0; i < EnumBuildingType.values().length; i++)
		{
			Main.proxy.registerVariantRenderer(Item.getItemFromBlock(this), i, "foundation_" + EnumBuildingType.values()[i].getName(), "inventory");
		}
	}
}
