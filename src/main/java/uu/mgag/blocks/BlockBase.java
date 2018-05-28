package uu.mgag.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import uu.mgag.Main;
import uu.mgag.init.BlockInit;
import uu.mgag.init.ItemInit;
import uu.mgag.util.interfaces.IHasModel;

public class BlockBase extends Block implements IHasModel
{
	public BlockBase(String name, Material materialIn)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}	
}
