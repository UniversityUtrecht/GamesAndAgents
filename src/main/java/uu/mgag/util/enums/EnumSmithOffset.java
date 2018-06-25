package uu.mgag.util.enums;

import net.minecraft.util.math.BlockPos;

public enum EnumSmithOffset
{
	ENTRANCE (0, 1, -6),
	FURNACE (-3, 1, -3);
	
	private BlockPos offset;
	
	private EnumSmithOffset(int x, int y, int z)
	{
		this.offset = new BlockPos(x, y, z);
	}
	
	public BlockPos getOffset()
	{
		return this.offset;
	}

}
