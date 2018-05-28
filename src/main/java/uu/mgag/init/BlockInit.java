package uu.mgag.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import uu.mgag.blocks.BlockBase;
import uu.mgag.blocks.BlockFoundation;
import uu.mgag.util.enums.EnumBuildingType;

public class BlockInit
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block FOUNDATION_BLOCK = new BlockFoundation("foundation_block", Material.ROCK);

}
