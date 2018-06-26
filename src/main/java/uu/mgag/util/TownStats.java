package uu.mgag.util;

public class TownStats
{
	public static int res_food = 150;
	public static int res_wood = 100;
	public static int res_stone = 60;
	public static int res_iron = 0;
	
	public static int amount_ore = 2;
	
	public static int population_limit = 0;
	
	public static int count_supply = 0;
	public static int count_hall = 0;
	public static int count_farm = 0;
	public static int count_lumber = 0;
	public static int count_mine = 0;
	public static int count_smith = 0;
	
    public static int unit_count_farmer = 0;
    public static int unit_count_lumberjack = 0;
    public static int unit_count_miner = 0;
    public static int unit_count_blacksmith = 0;

    public static int getUnitCount()
    {
        return unit_count_farmer + unit_count_lumberjack + unit_count_miner + unit_count_blacksmith;
    }
}
