package uu.mgag.util.enums;

import net.minecraft.util.math.Vec3i;

public enum EnumSupplyOffset
{
	BUILDING_MATERIALS (-2, 1, -4),
	ORES_MINERALS (-4, 1, -1),
	FOOD_INGREDIENTS (-1, 1, 1),
	OTHER_SUPPLIES (1, 1, -2);
	
	private Vec3i offset;
	
	private EnumSupplyOffset(int x, int y, int z)
	{
		this.offset = new Vec3i(x, y, z);
	}
	
	public Vec3i getOffset()
	{
		return this.offset;
	}

}
