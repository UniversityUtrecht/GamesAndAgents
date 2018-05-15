package com.npcmod.blocks;

import java.util.Random;

import com.npcmod.init.ModItems;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class RubyBlock extends BlockBase {

	public RubyBlock(String name, Material material) {
		super(name, material);
		
		setSoundType(SoundType.METAL);
		setHardness(2.0f);
		setResistance(15.0f);
		setHarvestLevel("pickaxe", 2);
		setLightLevel(1.0f);
		setLightOpacity(0);
		//setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ModItems.RUBY;
    }

	@Override
	public int quantityDropped(Random rand)
	{
		return 1 + rand.nextInt(2);
	}

}
