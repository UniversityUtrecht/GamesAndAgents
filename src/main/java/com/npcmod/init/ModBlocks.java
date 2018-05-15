package com.npcmod.init;

import java.util.ArrayList;
import java.util.List;

import com.npcmod.blocks.RubyBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block>();

	public static final Block RUBY_BLOCK = new RubyBlock("ruby_block", Material.IRON);
}
