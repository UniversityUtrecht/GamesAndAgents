package uu.mgag.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumBuildingType implements IStringSerializable
{
	SUPPLY_POINT (0, "supply", 0, 0, 0),
	TOWN_HALL (1, "hall", 0, 0, 0),
	FARM (2, "farm", -8, 2, -8), // TODO: changes in negative logics reflect AI classes too
	BLACKSMITH (3, "smith", 0, 0, 0);
	
	private static final EnumBuildingType[] META_LOOKUP = new EnumBuildingType[values().length];
	private final int meta;
	private final String name, unlocalizedName;
	private final int sizeX, sizeY, sizeZ;
	
	private EnumBuildingType(int metaIn, String nameIn, int sizeX, int sizeY, int sizeZ)
	{
		this(metaIn, nameIn, nameIn, sizeX, sizeY, sizeZ);
	}
	
	private EnumBuildingType(int metaIn, String nameIn, String unlocalizedNameIn, int sizeX, int sizeY, int sizeZ)
	{
		this.meta = metaIn;
		this.name = nameIn;
		this.unlocalizedName = unlocalizedNameIn;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}
	
	public int getMeta()
	{
		return this.meta;
	}
	
	public String getUnlocalizedName()
	{
		return this.unlocalizedName;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
	
	public static EnumBuildingType byMetadata(int meta)
	{
		return META_LOOKUP[meta];
	}
	
	static
	{
		for(EnumBuildingType enumtype : values())
		{
			META_LOOKUP[enumtype.getMeta()] = enumtype;
		}
	}
	
	public int getSizeX()
	{
		return sizeX;
	}
	public int getSizeY()
	{
		return sizeY;
	}
	public int getSizeZ()
	{
		return sizeZ;
	}
}
