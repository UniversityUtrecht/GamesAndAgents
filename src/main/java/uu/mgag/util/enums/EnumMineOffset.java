package uu.mgag.util.enums;

import net.minecraft.util.math.BlockPos;

public enum EnumMineOffset
{
	ENTRANCE (0, 1, -2),
	MINE_SOUTHEAST (-1, 0, -1);
	
	private BlockPos offset;
	
	private EnumMineOffset(int x, int y, int z)
	{
		this.offset = new BlockPos(x, y, z);
	}
	
	public BlockPos getOffset()
	{
		return this.offset;
	}

}
