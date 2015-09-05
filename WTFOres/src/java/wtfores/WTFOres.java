package wtfores;

import java.util.HashSet;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import wtfcore.WTFCore;
import wtfcore.WorldGenListener;
import wtfcore.utilities.LangWriter;
import wtfores.blocks.OverlayOre;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = WTFOres.modid, name = "WhiskyTangoFox's Ores", version = "1.1T", dependencies = "after:UndergroundBiomes;after:TConstructs;required-after:WTFCore;after:WTFTweaks")
public class WTFOres {

		public static  final String modid = WTFCore.WTFOres;

		@Instance(modid)
		public static WTFOres instance;

		//@SidedProxy(clientSide="cavebiomes.proxy.CBClientProxy", serverSide="cavebiomes.proxy.CommonProxy")
		//public static CommonProxy proxy;
		
		public static boolean DEBUG = false;

		public static String alphaMaskDomain = "wtfores:textures/blocks/alphamasks/";
		public static String overlayDomain =   "wtfores:textures/blocks/overlays/";

		public static HashSet<String> orenames = new HashSet<String>();

		public static CreativeTabs oreTab = new CreativeTabs("wtf_ores")
		{

			@Override
			@SideOnly(Side.CLIENT)
			public Item getTabIconItem()
			{
				return Item.getItemFromBlock(Blocks.gold_ore);
			}

		};

		@EventHandler
		public void PreInit(FMLPreInitializationEvent preEvent)
		{
			

		}

		@EventHandler public void load(FMLInitializationEvent event)
		{
			MinecraftForge.ORE_GEN_BUS.register(new VanillOreGenCatcher());

		}
		
		@EventHandler
		public void PostInit(FMLPostInitializationEvent postEvent){
			
			//WorldGenListener.generator = new OreGenTweaked();
			WTFOresConfig.customConfig();
			OverlayOre.register();
			GameRegistry.registerWorldGenerator(new OreGenTweaked(), Integer.MAX_VALUE);

		}

}
