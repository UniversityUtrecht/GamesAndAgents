package uu.mgag.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumEntityType implements IStringSerializable
{
	BUILDER (0, "builder"),
	FARMER (1, "farmer"),
	LUMBERJACK (2, "lumberjack"),
	MINER (3, "miner"),
	BLACKSMITH (4, "blacksmith"),
	SOLDIER (5, "soldier");
	
	private static final EnumEntityType[] META_LOOKUP = new EnumEntityType[values().length];
	private final int meta;
	private final String name, unlocalizedName;
	
	private EnumEntityType(int metaIn, String nameIn)
	{
		this(metaIn, nameIn, nameIn);
	}
	
	private EnumEntityType(int metaIn, String nameIn, String unlocalizedNameIn)
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
	
	public static EnumEntityType byMetadata(int meta)
	{
		return META_LOOKUP[meta];
	}
	
	static
	{
		for(EnumEntityType enumtype : values())
		{
			META_LOOKUP[enumtype.getMeta()] = enumtype;
		}
	}
}

