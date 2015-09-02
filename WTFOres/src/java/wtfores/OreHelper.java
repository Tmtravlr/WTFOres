package wtfores;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import wtfcore.utilities.BlockInfo;
import wtfores.blocks.OverlayOre;

public class OreHelper {
	
	public static boolean enableDenseOres;
	public static boolean removeVanillaOres;

	public static HashSet<AddCustomOre> customOres = new HashSet<AddCustomOre>();
	
	public static HashSet<BlockInfo> stoneTypes = new HashSet<BlockInfo>();
	public static HashSet<BlockInfo> stoneTypesToAdd = new HashSet<BlockInfo>();
	
	public static class StoneOreType {
		public BlockInfo stone;
		public BlockInfo ore;
		
		public StoneOreType(BlockInfo stoneBlock, BlockInfo oreBlock) {
			this.stone = stoneBlock;
			this.ore = oreBlock;
		}
	}
	
	public static Comparator<StoneOreType> stoneOreComparator = new Comparator<StoneOreType>() {
		public int compare(StoneOreType type1, StoneOreType type2) {
			if(type1.stone.block != type2.stone.block) {
				return Block.getIdFromBlock(type2.stone.block) - Block.getIdFromBlock(type1.stone.block);
			}
			
			if(type1.stone.meta != type2.stone.meta) {
				return type2.stone.meta - type1.stone.meta;
			}
			
			if(type1.ore.block != type2.ore.block) {
				return Block.getIdFromBlock(type2.ore.block) - Block.getIdFromBlock(type1.ore.block);
			}
			
			if(type1.ore.meta != type2.ore.meta) {
				return type2.ore.meta - type1.ore.meta;
			}
			
			return 0;
		}
	};
	
	public static TreeMap<StoneOreType, OverlayOre> stoneToOre = new TreeMap<StoneOreType, OverlayOre>(stoneOreComparator);

	public static BlockInfo getOreForStone(World world, int x, int y, int z, Block oreBlock, int oreMeta, int density) {
		
		if(!enableDenseOres) {
			density = 0;
		}
		
		density = MathHelper.clamp_int(density, 0, 15);
	
		OverlayOre ore = stoneToOre.get(new StoneOreType(new BlockInfo(world.getBlock(x, y, z), density), new BlockInfo(oreBlock, oreMeta)));
		
		if(ore != null) {
			
			return new BlockInfo(ore, world.getBlockMetadata(x, y, z));
		}
		
		return null;
	}
	
}
