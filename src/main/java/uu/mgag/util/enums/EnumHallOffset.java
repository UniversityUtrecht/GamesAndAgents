package uu.mgag.util.enums;

import net.minecraft.util.math.BlockPos;

public enum EnumHallOffset
{
	ENTRANCE (0, 1, -12),
	BUILDER_SPOT (-3, 0, -12);
	
	private BlockPos offset;
	
	private EnumHallOffset(int x, int y, int z)
	{
		this.offset = new BlockPos(x, y, z);
	}
	
	public BlockPos getOffset()
	{
		return this.offset;
	}

}
