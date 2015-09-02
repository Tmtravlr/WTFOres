package wtfores;

import java.io.File;
import java.util.HashSet;

import scala.actors.threadpool.Arrays;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import wtfcore.WTFCore;
import wtfcore.proxy.ClientProxy;
import wtfcore.utilities.BlockInfo;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;

public class WTFOresConfig {

	public static void customConfig() {

		Configuration config = new Configuration(new File("config/WTFOresConfig.cfg"));

		config.load();
		
		WTFOres.DEBUG = config.get("Debug", "Debug", false).getBoolean();
		
		String[] defaultStones = new String[] {"minecraft:stone",
											   "minecraft:netherrack",
											   "UndergroundBiomes:igneousStone-7",
											   "UndergroundBiomes:metamorphicStone-7",
											   "UndergroundBiomes:sedimentaryStone-7"
											   };
		
		Property prop = config.get("Ubified, Fracturing, Variable Density Ores", "Stones to generate ores in", defaultStones);
		prop.comment = "Add blocks here which you want ores to generate in.\n"
				+ "If the block has multiple metdata values, put a - and\n"
				+ "the maximum metadata value it should generate in. It has\n"
				+ "to generate in every metadata value smaller than that. That's\n"
				+ "just the way WTF set it up.";
		
		String[] stoneStrings = prop.getStringList();

		//Get the stone types from the list; They are <Block name>-<Metadata>
		for(String stoneString : stoneStrings) {
			String[] split = stoneString.split("-");
			
			if(split.length == 0) {
				continue;
			}
			
			Block block = GameData.getBlockRegistry().getObject(split[0]);
			
			if(block == null || block == Blocks.air) {
				continue;
			}
			
			int meta = 0;
			
			if(split.length > 1) {
				meta = Integer.parseInt(split[1]);
			}
			
			OreHelper.stoneTypesToAdd.add(new BlockInfo(block, meta));
			
			for(int loop = 0; loop < meta+1; loop++) {
				OreHelper.stoneTypes.add(new BlockInfo(block, loop));
			}
		}
		
		String[] defaultOres = new String[]{"oreBlock=minecraft:coal_ore,useTexture=coal_ore,genType=1,v1=6,v2=6,maxDensity=3,maxHeightPercent=110,minPerChunk=80,maxPerChunk=100,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:diamond_ore,useTexture=diamond_ore,genType=4,maxDensity=5,maxHeightPercent=25,minPerChunk=2,maxPerChunk=8,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:iron_ore,useTexture=iron_ore,genType=2,v1=8,maxDensity=3,maxHeightPercent=75,minPerChunk=40,maxPerChunk=80,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:redstone_ore,useTexture=redstone_ore,genType=5,v1=6,maxDensity=3,maxHeightPercent=25,minPerChunk=6,maxPerChunk=10,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:lapis_ore,useTexture=lapis_ore,genType=6,maxDensity=3,maxHeightPercent=33,minPerChunk=4,maxPerChunk=8,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:gold_ore,useTexture=gold_ore,genType=3,v1=8,maxDensity=3,maxHeightPercent=45,minPerChunk=12,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:emerald_ore,useTexture=emerald_ore,maxDensity=3,genType=7,dimensionBlacklist=1_-1",
											"oreBlock=minecraft:quartz_ore,useTexture=quartz_ore,maxDensity=3,genType=6,maxHeightPercent=110,minPerChunk=90,maxPerChunk=160,biomeTypeWhitelist=NETHER",
											"oreBlock=TConstruct:SearedBrick,metadata=5,useTexture=aluminum_ore,genType=2,maxDensity=3,maxHeightPercent=75,minPerChunk=50,v1=10,dimensionBlacklist=1_-1",
											"oreBlock=TConstruct:SearedBrick,metadata=3,useTexture=copper_ore,genType=3,maxDensity=3,maxHeightPerent=90,minPerChunk=65,v1=10,dimensionBlacklist=1_-1",
											"oreBlock=TConstruct:SearedBrick,metadata=4,useTexture=tin_ore,genType=1,maxDensity=3,maxHeightPercent=110,minPerChunk=60,maxPerChunk=90,v1=3,v2=3,dimensionBlacklist=1_-1",
											"oreBlock=WTFTweaks:sulfur_ore,metadata=0,useTexture=sulfur_ore,genType=1,maxDensity=3,maxHeightPercent=110,minPerChunk=20,v1=3,v2=3,dimensionBlacklist=1_-1",
											"oreBlock=WTFTweaks:nitre_ore,metadata=0,useTexture=nitre_ore,genType=0,maxDensity=3,maxHeightPercent=66,minPerChunk=60,v1=8,dimensionBlacklist=1_-1"
											};
		
		prop = config.get("Ubified, Fracturing, Variable Density Ores", "Added Ores", defaultOres);
		prop.comment =  "Add or modify existing ore gen here. The options are:\n"
				+ "\n"
				+ "      oreBlock - The ore block this represents.\n"
				+ "\n"
				+ "      useTexture - The base name of the overlay texture to use in your resource pack.\n"
				+ "                   There should be 3, called <name>0_16, <name>1_16 and <name>2_16, etc.\n"
				+ "                   where <name> is the name you provide, and 0 represents normal,\n"
				+ "                   1 represents double density, 2 represents triple density, etc.\n"
				+ "                   You can add as many as you want for the maxDensity you set.\n"
				+ "\n"
				+ "      genType,v1,v2 - The generation type. v1 and v2 are type-dependant variables:\n"
				+ "                      0: Vanilla style generation. v1 is the number of blocks per deposit, \n"
				+ "                         v2 is not used.\n"
				+ "                      1: Seam style generation. Generates a flat sheet of ore. V1 and v2 are\n"
				+ "                         the average width and depth. By default, coal uses this generation type.\n"
				+ "                      2: 2x2 vein, that isn’t solid. v1 is the average vein length, v2 is not\n"
				+ "                         used. By default, iron uses this deposit type.\n"
				+ "                      3: 3x3 vein, that is quite disperse. v1 is the length of the vein, v2 isn’t\n"
				+ "                         used. By default, gold uses this.\n"
				+ "                      4: Single block. This generates the ores as single, isolated blocks. By\n"
				+ "                         default, diamond uses this generation.\n"
				+ "                      5: Vertical veins: single block vertical veins. By default, redstone uses\n"
				+ "                         this type of generation. v1 and v2 are not used.\n"
				+ "                      6: Star shaped deposits. By default, lapis lazuli uses this type of\n"
				+ "                         generation. v1 and v2 are not used.\n"
				+ "                      7: Replacement. Attempts to replace a block that has generated with an ore\n"
				+ "                         from this mod with random density. By default, emerald uses this. v1 and\n"
				+ "                         v2 are not used.\n"
				+ "\n"
				+ "      maxDentity - The maximum dentiy that the ores can generate with (1-16, defaults to 3)."
				+ "\n"
				+ "      minHeightPercent/maxHeightPercent - Min/max height at which this ore will generate compared\n"
				+ "                                          to the surface level.\n"
				+ "\n"
				+ "      minPerChunk/maxPerChunk - The minimum and maximum ore groups that will attempt to generate.\n"
				+ "                                if the max isn't specified, it will be the same as the min.\n\n"
				+ "\n"
				+ "      biomeBlacklist/biomeWhiteList - Whitelist or blacklist biomes for this to generate in.\n"
				+ "                                      It must be in the form <biome name 1>-<biome name 2>-...\n"
				+ "\n"
				+ "      biomeTypeBlacklist/biomeTypeWhitelist - Whitelist or blacklist biome types for this to generate\n"
				+ "                                              in. It must be in the form <biome type 1>-<biome type 2>-...\n"
				+ "\n"
				+ "      dimensionBlacklist/dimensionWhitelist - Whitelist or blacklist dimensions for this to generate\n"
				+ "                                              in. It must be in the form <dimension id 1>_<dimension id 2>_...\n"
				+ "\n"
				+ "If you add an entry for an ore that may not exist (like the tinker's ores above), it will only load\n"
				+ "if that block is found in the game. If you are adding ore generation for a mod that generates ores,\n"
				+ "don't forget to disable their ore gen in their config files!";
		
		String[] oreStrings = prop.getStringList();
		
		for(String oreString : oreStrings) {
			parseConfig(oreString);
		}

		OreHelper.removeVanillaOres = config.get("Ore Generation", "Remove vanilla ores", true).getBoolean();
		OreHelper.enableDenseOres = config.get("Ore Generation", "Use dense ores during vein generation", true).getBoolean();

		config.save();

	}

	public static void parseConfig(String fullOreString){
		//split it at each ; into an array
		fullOreString = fullOreString.replaceAll("\\s","");
		String[] oreStringArray = fullOreString.split(";");

		for (int loop = 0; loop < oreStringArray.length; loop ++){
			String[] currentString = oreStringArray[loop].split(",");

			AddCustomOre customOre = new AddCustomOre();
			String overrideTexture = null;

			WTFCore.log.info ("WTF-ores: loading from "+  oreStringArray[loop]);

			for (int stringLoop = 0; stringLoop < currentString.length; stringLoop++){

				if (currentString[stringLoop].startsWith("oreBlock=")){
					customOre.oreBlock = GameData.getBlockRegistry().getObject(currentString[stringLoop].substring(9));
				}
				else if (currentString[stringLoop].startsWith("metadata=")){
					customOre.oreMeta = Integer.parseInt(currentString[stringLoop].substring(9));
				}
				else if (currentString[stringLoop].startsWith("useTexture=")){
					customOre.textureName = currentString[stringLoop].substring(11);
				}
				else if (currentString[stringLoop].startsWith("genType=")){
					customOre.genType = Integer.parseInt(currentString[stringLoop].substring(8));
				}
				else if (currentString[stringLoop].startsWith("maxHeightPercent=")){
					customOre.setMaxHeightPercent(Integer.parseInt(currentString[stringLoop].substring(17)));
				}
				else if (currentString[stringLoop].startsWith("minHeightPercent=")){
					customOre.setMinHeightPercent(Integer.parseInt(currentString[stringLoop].substring(17)));
				}
				else if (currentString[stringLoop].startsWith("maxPerChunk=")){
					customOre.setMaxPerChunk(Integer.parseInt(currentString[stringLoop].substring(12)));
				}
				else if (currentString[stringLoop].startsWith("minPerChunk=")){
					customOre.setMinPerChunk(Integer.parseInt(currentString[stringLoop].substring(12)));
				}
				else if (currentString[stringLoop].startsWith("v1=")){
					customOre.var1 = Integer.parseInt(currentString[stringLoop].substring(3));
				}
				else if (currentString[stringLoop].startsWith("v2=")){
					customOre.var2 = Integer.parseInt(currentString[stringLoop].substring(3));
				}
				else if (currentString[stringLoop].startsWith("maxDensity=")){
					customOre.densityLevels = Integer.parseInt(currentString[stringLoop].substring(11));
				}
				else if(currentString[stringLoop].startsWith("biomeBlacklist=")) {
					customOre.biomeBlacklist = parseStringSet(currentString[stringLoop].substring(15));
				}
				else if(currentString[stringLoop].startsWith("biomeWhitelist=")) {
					customOre.biomeWhitelist = parseStringSet(currentString[stringLoop].substring(15));
				}
				else if(currentString[stringLoop].startsWith("biomeTypeBlacklist=")) {
					customOre.biomeTypeBlacklist = parseStringSet(currentString[stringLoop].substring(19));
				}
				else if(currentString[stringLoop].startsWith("biomeTypeWhitelist=")) {
					customOre.biomeTypeWhitelist = parseStringSet(currentString[stringLoop].substring(19));
				}
				else if(currentString[stringLoop].startsWith("dimensionBlacklist=")) {
					customOre.dimensionBlacklist = parseIntegerSet(currentString[stringLoop].substring(19));
				}
				else if(currentString[stringLoop].startsWith("dimensionWhitelist=")) {
					customOre.dimensionBlacklist = parseIntegerSet(currentString[stringLoop].substring(19));
				}
				else {
					WTFCore.log.error("WTFOres CustomOre Config: Cannot parse " + currentString[stringLoop]);
				}


			}
			
			if(customOre.densityLevels <= 0) {
				customOre.densityLevels = 3;
			}
			
			if (customOre.oreBlock == null || customOre.oreBlock == Blocks.air){
				if(WTFOres.DEBUG) WTFCore.log.error("Adding custom ores, block not found for " +  oreStringArray[loop]);
			}
			else {
				//the default required to gen the AddCustomOre is oreBlock, metadata, useTexture, and genType
				if(WTFOres.DEBUG) WTFCore.log.info("Adding custom ores, block loaded: " +  customOre.oreBlock.getUnlocalizedName());
				OreHelper.customOres.add(customOre);
				if (overrideTexture==null){overrideTexture=customOre.textureName;}
				ClientProxy.registerBlockOverlayOverride(customOre.oreBlock, overrideTexture, "minecraft:stone", customOre.textureName+0, WTFOres.overlayDomain, false);
			}
		}
	}
	
	private static HashSet<String> parseStringSet(String entry) {
		HashSet<String> stringSet = new HashSet<String>();
				
		String[] split = entry.split("-");
		
		for(String string : split) {
			stringSet.add(string.toLowerCase());
		}
		
		return stringSet;
	}
	
	private static HashSet<Integer> parseIntegerSet(String entry) {
		HashSet<Integer> intSet = new HashSet<Integer>();
				
		String[] split = entry.split("_");
		
		for(String intString : split) {
			intSet.add(Integer.decode(intString));
		}
		
		return intSet;
	}
}
