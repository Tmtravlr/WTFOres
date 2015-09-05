package wtfores.blocks;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import wtfcore.WTFCore;
import wtfcore.blocks.IAlphaMaskedBlock;
import wtfcore.proxy.ClientProxy;
import wtfcore.tweaksmethods.FracMethods;
import wtfcore.utilities.BlockInfo;
import wtfcore.utilities.BlockSets;
import wtfcore.utilities.OreBlockInfo;
import wtfcore.utilities.UBCblocks;
import wtfores.AddCustomOre;
import wtfores.OreHelper;
import wtfores.OreHelper.StoneOreType;
import wtfores.WTFOres;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OverlayOre extends OreChildBlock implements IAlphaMaskedBlock
{
	private IIcon[] textures;
	public static String[] vanillaStone = {"stone"};
	protected static String[] vanillaObsidian = {"obsidian"};
	protected static String[] vanillaSand = {"sand"};
	protected static String[] vanillaSandstone = {"sandstone"};
	protected static String[] vanillaGravel = {"gravel"};


	public  OverlayOre(Block oreBlock, int parentMeta, int oreLevel, Block stoneBlock, String oreType, String[] stoneNames, String domain){
		super(oreBlock, parentMeta, stoneBlock);

		loadTextureStrings(oreType, stoneNames, domain);
		
		this.oreLevel = oreLevel;
		//if (this.parentBlock != Blocks.lit_redstone_ore){this.setCreativeTab(WTFOres.oreTab);}
	}

	public void loadTextureStrings(String oreType, String[] stoneNames, String domain){
		this.textureNames = new String[stoneNames.length];
		this.parentLocations = new String[stoneNames.length];

		for (int loop = 0; loop < stoneNames.length; loop++){
			textureNames[loop] = oreType+"_"+stoneNames[loop];
			parentLocations[loop] = domain+":"+stoneNames[loop];
		}
		this.oreType = oreType;
	}

	public static void registerOverlaidOre(Block oreBlock, int oreMeta, String oreType, Block stoneBlock, String stoneGeoType, String[] stoneNames, String domain, int densities){

		OverlayOre blockToRegister = null, blockToRegisterLit = null;
		OverlayOre prevBlock = null, prevBlockLit = null;

		for (int loop = 0; loop < densities; loop++){
			String name = oreType+loop+"_"+stoneGeoType;

			
			if(oreBlock == Blocks.redstone_ore) {
				String litName = name + "_lit";
				
				blockToRegister = (RedstoneOverlayOre)new RedstoneOverlayOre(oreBlock, oreMeta, loop, stoneBlock, "redstone_ore"+loop, stoneNames, domain, false).setBlockName(name);
				blockToRegisterLit = (RedstoneOverlayOre)new RedstoneOverlayOre(oreBlock, oreMeta, loop, stoneBlock, "redstone_ore"+loop, stoneNames, domain, true).setBlockName(litName);
				
				//I hate typecasting... but it can't be avoided without making extra variables.
				((RedstoneOverlayOre)blockToRegister).opposite = (RedstoneOverlayOre)blockToRegisterLit;
				((RedstoneOverlayOre)blockToRegister).next = prevBlock;
				((RedstoneOverlayOre)blockToRegisterLit).opposite = (RedstoneOverlayOre)blockToRegister;
				((RedstoneOverlayOre)blockToRegisterLit).next = prevBlockLit;
				
				GameRegistry.registerBlock(blockToRegisterLit, ItemBlockOverlayOre.class, litName);
				BlockSets.addOreBlock(blockToRegisterLit, FracMethods.wtforesfrac);
				prevBlockLit = blockToRegisterLit;
			}
			else {
				blockToRegister = (OverlayOre)new OverlayOre(oreBlock, oreMeta, loop, stoneBlock, oreType+loop, stoneNames, domain).setBlockName(name);
				blockToRegister.next = prevBlock;
			}
			
			GameRegistry.registerBlock(blockToRegister, ItemBlockOverlayOre.class, name);

//			BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, oreMeta, stoneBlock, loop), blockToRegister);
			OreHelper.stoneToOre.put(new StoneOreType(new BlockInfo(stoneBlock, loop), new BlockInfo(oreBlock, oreMeta)), blockToRegister);
			if(WTFOres.DEBUG) WTFCore.log.info("Added ore block '" + new ItemStack(blockToRegister).getDisplayName() + "' for ore '" + new ItemStack(oreBlock, 1, oreMeta).getDisplayName() + "', stone type '" + new ItemStack(stoneBlock).getDisplayName() + "', and density " + loop);
			BlockSets.addOreBlock(blockToRegister, FracMethods.wtforesfrac);
			prevBlock = blockToRegister;
		}
		//These are used to set ores of high density- doing this means I can call a negative density from the ubifier map and still get a block
//		BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, oreMeta, stoneBlock, -1), blockToRegister);
//		BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, oreMeta, stoneBlock, -2), blockToRegister);
//		BlockSets.oreUbifier.put(new OreBlockInfo(oreBlock, oreMeta, stoneBlock, -3), blockToRegister);
		
	}

	public static void registerOreSets(AddCustomOre newOre){
		
		for(BlockInfo stoneInfo : OreHelper.stoneTypesToAdd) {
			
			String stoneName = Block.blockRegistry.getNameForObject(stoneInfo.block);
			String domain = "minecraft";
			String name = stoneName.substring(stoneName.indexOf(":")+1);
			
			String[] stoneNameList = new String[stoneInfo.meta+1];
			
			for(int loop = 0; loop < stoneInfo.meta+1; loop++) {
				String iconName = stoneInfo.block.getIcon(0, loop).getIconName();
				if(iconName.indexOf(":") > 0) {
					domain = iconName.substring(0, iconName.indexOf(":"));
					stoneNameList[loop] = iconName.substring(iconName.indexOf(":")+1);
				}
				else {
					stoneNameList[loop] = iconName;
				}
				//System.out.println(stoneNameList[loop]);
			}
			
			registerOverlaidOre(newOre.oreBlock, newOre.oreMeta, newOre.textureName, stoneInfo.block, name, stoneNameList, domain, newOre.densityLevels);
		}
//		if (Loader.isModLoaded("UndergroundBiomes"))
//		{
//			registerOverlaidOre(oreBlock, parentMeta, oreType, UBCblocks.IgneousStone, "igneous", UBCblocks.IgneousStoneList, "undergroundbiomes", densities);
//			registerOverlaidOre(oreBlock, parentMeta, oreType, UBCblocks.MetamorphicStone, "metamorphic", UBCblocks.MetamorphicStoneList, "undergroundbiomes", densities);
//			registerOverlaidOre(oreBlock, parentMeta, oreType, UBCblocks.SedimentaryStone, "sedimentary", UBCblocks.SedimentaryStoneList, "undergroundbiomes", densities);
//		}
//		if (Loader.isModLoaded("TConstruct") && (oreType == "iron_ore" ||oreType == "gold_ore" ||oreType == "copper_ore" ||oreType == "aluminum_ore" ||oreType == "tin_ore" )){
//			registerOverlaidOre(oreBlock, parentMeta, oreType, Blocks.gravel, "gravel", vanillaGravel, "minecraft", densities);
//		}
	}

	public static void register(){
		//Extra sets with non-stone backgrounds
		//registerOverlaidOre(Blocks.diamond_ore, 0, "diamond_ore", Blocks.obsidian, "obsidian", vanillaObsidian, "minecraft");
		//registerOverlaidOre(Blocks.redstone_ore, 0, "redstone_ore", Blocks.sand, "sand", vanillaSand, "minecraft");
		//PROBLEM: sandstone is a sided block, and my texturer cannot currently handle sided blocks
		//registerOverlaidOre(Blocks.redstone_ore, 0, "redstone_ore", Blocks.sandstone, "sandstone", vanillaSandstone, "minecraft");

		//Iterates through all the config set ores
		Iterator<AddCustomOre> iterator = OreHelper.customOres.iterator();
		while (iterator.hasNext()){
			AddCustomOre newOre = iterator.next();
			registerOreSets(newOre);
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister){
		textures = new IIcon[16];
		for (int loop = 0; loop < textureNames.length; loop++){
			textures[loop] = iconRegister.registerIcon(WTFOres.modid+":"+textureNames[loop]);
			ClientProxy.registerBlockOverlay(textureNames[loop], parentLocations[loop], oreType, WTFOres.overlayDomain, false);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return textures[meta];
	}



	
}
