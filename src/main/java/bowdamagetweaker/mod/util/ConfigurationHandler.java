package bowdamagetweaker.mod.util;

import java.io.File;
import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler
{
  public static Configuration config;
  
  public static boolean playArrowHitSound = true;
  
  private static String[] bowList =
  {
		  "minecraft:bow~10",
		  "spartanweaponry:crossbow_wood~0.5",
		  "spartanweaponry:longbow_wood~0.5"
  };
  
  public static ArrayList<SpartanBow> bows = new ArrayList<SpartanBow>();
  
	public static class SpartanBow
	{
		public String bow = "";
		public double damage = 0.0D;
	}


  public static void init(File configFile)
  {
    if (config == null)
    {
      config = new Configuration(configFile, Integer.toString(4));
      loadConfiguration();
    } 
  }
  
  private static void loadConfiguration()
  {
    playArrowHitSound = config.getBoolean("Arrow Impact Sound", "general", true, "Arrows will make an impact sound when they hit an entity, regardless of range");
    bowList = config.getStringList("Bow List", "general", bowList, "Add or subtract base damage from bows. Vanilla bow BASE damage is 2.0. The damage of a fully-charged (meaning a critical, the ones with the trail of particles) vanilla bow is: (~3 * X) + rand.nextInt(~3 * X / 2 + 2). The ~3 is based off arrow velocity and is usually around 3. The X is the arrow's base damage (vanilla arrow base damage is 2.0, and after the equation the final damage of a fully-charged arrow averages around 9 damage). If you change the vanilla arrow damage from 2.0 to 1.0, the average fully-charged arrow would average 4 damage. Format for this config setting example:   modID:itemName~number,spartanweaponry:longbow_wood~0.5");

    for ( String s : bowList )
	{
		try
		{
			String[] list = s.split("~");
			SpartanBow spartanBow = new SpartanBow();
			spartanBow.bow = list[0];
			spartanBow.damage = Double.parseDouble(list[1]);
			bows.add(spartanBow);
		}
		catch ( Exception e )
		{
			System.out.println( "WARNING, no bow found or incorrect format: " + s );
		}
	}
    
    if (config.hasChanged())
    {
      config.save();
    }
}
}