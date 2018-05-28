package uu.mgag.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumBuildingType implements IStringSerializable
{
	SUPPLY_POINT (0, "supply"),
	TOWN_HALL (1, "hall"),
	FARM (2, "farm");
	
	private static final EnumBuildingType[] META_LOOKUP = new EnumBuildingType[values().length];
	private final int meta;
	private final String name, unlocalizedName;
	
	private EnumBuildingType(int metaIn, String nameIn)
	{
		this(metaIn, nameIn, nameIn);
	}
	
	private EnumBuildingType(int metaIn, String nameIn, String unlocalizedNameIn)
	{
		this.meta = metaIn;
		this.name = nameIn;
		this.unlocalizedName = unlocalizedNameIn;
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
}
