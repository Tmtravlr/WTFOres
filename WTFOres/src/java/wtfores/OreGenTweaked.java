package wtfores;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import wtfcore.WTFCore;
import wtfcore.utilities.BlockInfo;
import wtfcore.utilities.BlockSets;
import wtfcore.utilities.OreBlockInfo;
import wtfores.gencores.GenOreProvider;
import wtfores.gencores.VOreGen;
import cpw.mods.fml.common.IWorldGenerator;

public class OreGenTweaked implements IWorldGenerator{

	private World world;
	private int surface;
	private int chunkX;
	private int chunkZ;
	private Random random;
	private float surfaceMod;
	private VOreGen gen;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
		
		int x = chunkX * 16;
		int z = chunkZ * 16;
		
		this.world = world;
		this.surface = world.getHeightValue(x + 8, z + 8);
		if(surface < 64) surface = 64;
		this.chunkX =x;
		this.chunkZ = z;
		this.random = random;
		this.surfaceMod = (float)surface/64F;
		if (gen == null){
			gen = GenOreProvider.getGenCore();
		}


		Iterator<AddCustomOre> iterator = OreHelper.customOres.iterator();
		while (iterator.hasNext()){
			AddCustomOre newOre = (AddCustomOre)iterator.next();
			
			//First check biome/biome type/dimension conditions
			
			int dimension = world.provider.dimensionId;
			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
			BiomeDictionary.Type[] biomeTypes = BiomeDictionary.getTypesForBiome(biome);
			
			if(newOre.dimensionBlacklist.contains(dimension)) {
				continue;
			}
			if(newOre.dimensionWhitelist != null && !newOre.dimensionWhitelist.contains(dimension)) {
				continue;
			}
			
			if(newOre.biomeBlacklist.contains(biome.biomeName.toLowerCase())) {
				continue;
			}
			if(newOre.biomeWhitelist != null && !newOre.biomeWhitelist.contains(biome.biomeName.toLowerCase())) {
				continue;
			}
			
			boolean hasWhite = false;
			
			for(BiomeDictionary.Type biomeType : biomeTypes) {
				if(newOre.biomeTypeBlacklist.contains(biomeType.name().toLowerCase())) {
					continue;
				}
				if(newOre.biomeTypeWhitelist == null || newOre.biomeTypeWhitelist.contains(biomeType.name().toLowerCase())) {
					hasWhite = true;
				}
			}
			
			if(!hasWhite) {
				continue;
			}
			
			//If those check out, generate the ore.
			if(WTFOres.DEBUG) {
				WTFCore.log.info(" -- Surface is " + surface + ". Generating '" + new ItemStack(newOre.oreBlock, 1, newOre.oreMeta).getDisplayName() + "' with type " + newOre.genType);
			}

			switch (newOre.genType){
			
			case 0: // (Block oreBlock, int metadata, int counter, int numberOfBlocks, int maxHeight)
				genDefault(newOre);
				break;
			case 1: // (Block oreBlock, int metadata, int counter, int maxHeight, int widthX, int widthZ)
				genSheet(newOre);
				break;
			case 2: //  genthickVein(Block oreBlock, int metadata, int counter, int maxHeight, int baseLength)
				genthickVein(newOre);
				break;
			case 3: // genDisperseVein(Block oreBlock, int metadata, int counter, int maxHeight, int baseLength)
				genDisperseVein(newOre);
				break;
			case 4: // (Block oreBlock, int metadata, int counter, int height)
				genSingle(newOre);
				break;
			case 5: // genVertical(Block oreBlock, int metadata, int counter, int maxHeight, int length)
				genVertical(newOre);
				break;
			case 6: // genStar(Block oreBlock, int metadata, int counter, int height)
				genStar (newOre);
				break;
			case 7: 
				genReplace(newOre);
				break;

			}
		}
	}

	public void genDefault(AddCustomOre newOre){
		Block oreBlock = newOre.oreBlock;
		//int metadata = newOre.metadata;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);
		int numberOfBlocks = newOre.var1;

		for (int fail = 0; counter > 0 && fail < 1000; fail++){

			float f = random.nextFloat() * (float)Math.PI;
			double d0 = chunkX + 8 + MathHelper.sin(f) * numberOfBlocks / 8.0F;
			double d1 = chunkX + 8 - MathHelper.sin(f) * numberOfBlocks / 8.0F;
			double d2 = chunkZ + 8 + MathHelper.cos(f) * numberOfBlocks / 8.0F;
			double d3 = chunkZ + 8 - MathHelper.cos(f) * numberOfBlocks / 8.0F;
			int height = newOre.getHeight(surface);
			double d4 = height + random.nextInt(3) - 2;
			double d5 = height + random.nextInt(3) - 2;

			for (int l = 0; l <= numberOfBlocks; ++l)
			{
				double d6 = d0 + (d1 - d0) * l / numberOfBlocks;
				double d7 = d4 + (d5 - d4) * l / numberOfBlocks;
				double d8 = d2 + (d3 - d2) * l / numberOfBlocks;
				double d9 = random.nextDouble() * numberOfBlocks / 16.0D;
				double d10 = (MathHelper.sin(l * (float)Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
				double d11 = (MathHelper.sin(l * (float)Math.PI / numberOfBlocks) + 1.0F) * d9 + 1.0D;
				int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
				int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
				int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
				int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
				int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
				int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

				for (int k2 = i1; k2 <= l1; ++k2){
					double d12 = (k2 + 0.5D - d6) / (d10 / 2.0D);
					if (d12 * d12 < 1.0D){
						for (int l2 = j1; l2 <= i2; ++l2){
							double d13 = (l2 + 0.5D - d7) / (d11 / 2.0D);
							if (d12 * d12 + d13 * d13 < 1.0D){
								for (int i3 = k1; i3 <= j2; ++i3){

									if (genOre(world, oreBlock, newOre.oreMeta, k2, l2, i3, random.nextInt(newOre.densityLevels))){
										counter--;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void genSheet(AddCustomOre newOre){
		Block oreBlock = newOre.oreBlock;
		//int metadata = newOre.metadata;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);

		int widthX=newOre.var1;
		int widthZ=newOre.var2;

		for (int fail = 0; counter> 0 && fail < 1000; fail++){
			int height = newOre.getHeight(surface);
			float slopeX = random.nextFloat()/2;
			float slopeZ = random.nextFloat()/2;
			int startX = random.nextInt(8);
			int startZ = random.nextInt(8);
			widthX = widthX + random.nextInt(2) + random.nextInt(2);
			widthZ = widthZ + random.nextInt(2) + random.nextInt(2);

			for (int loopX = 0; loopX < widthX; loopX++){
				int x = chunkX+startX+loopX;
				for (int loopZ = 0; loopZ < widthZ; loopZ++){
					int z = chunkZ+startZ+loopZ;
					int y = height + MathHelper.floor_double((loopX * slopeX) + (loopZ * slopeZ));
					int densityToSet = 1 + random.nextInt(newOre.densityLevels) - (2*(height/(surface+10)));

					if (genOre(world, oreBlock, newOre.oreMeta, x, y, z, densityToSet)){
						counter--;
					}
				}
			}
		}
	}


	private void genthickVein(AddCustomOre newOre){
		Block oreBlock = newOre.oreBlock;
		//int metadata = newOre.metadata;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);
		int baseLength = newOre.var1;


		for (int fail = 0; counter > 0 && fail < 1000; fail++){
			float slopeXZ = random.nextFloat()-0.5F;
			float slopeXY = random.nextFloat()-0.5F;
			int startX = random.nextInt(8)+8;
			int startZ = random.nextInt(8)+8;
			int height = newOre.getHeight(surface);
			int length = baseLength -3 + random.nextInt(3)+random.nextInt(3)+random.nextInt(3);

			for (int loopX = 0; loopX < length; loopX++){

				int x = MathHelper.floor_double(chunkX+startX + loopX);
				int z = MathHelper.floor_double(chunkZ+startZ + slopeXZ*loopX);
				int y = height + MathHelper.floor_double(loopX * slopeXY);
				if (y > 0 && y < 256){
					int zmod = 0;
					int ymod = 0;

					for (int loop = 0; loop < 4; loop ++){
						if (loop == 1){ymod = 0; zmod = 1;}
						if (loop == 2){ymod = 1; zmod = 0;}
						if (loop == 3){ ymod = 1; zmod = 1;}

						if (random.nextBoolean()){
							int densityToSet = 1 + random.nextInt(newOre.densityLevels) - (2*(height/(surface+10)));

							if (genOre(world, oreBlock, newOre.oreMeta, x, y+ymod, z+zmod, densityToSet)){
								counter--;
							}
						}
					}
				}
			}
		}
	}


	private void genDisperseVein(AddCustomOre newOre){

		Block oreBlock = newOre.oreBlock;
		//int metadata = newOre.metadata;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);
		int baseLength = newOre.var1;


		for (int fail = 0; counter > 0 && fail < 1000; fail++){
			int height = newOre.getHeight(surface);
			float slopeXZ = random.nextFloat()-0.5F;
			float slopeXY = random.nextFloat()-0.5F;
			int startX = random.nextInt(8);
			int startZ = random.nextInt(4)+8;
			//int density = 2*(height/(surface+10));
			int length = baseLength+random.nextInt(5)-2;

			//if (BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(chunkX, chunkZ), Type.SNOWY)){
			//	density=density-1;
			//}

			for (int loopX = 0; loopX < length; loopX++){

				int x = MathHelper.floor_double(chunkX+startX + loopX);
				int z = MathHelper.floor_double(chunkZ+startZ + slopeXZ*loopX);
				int y = height + MathHelper.floor_double(loopX * slopeXY);

				int rand = random.nextInt(6);
				if (rand == 0){z=z+1;}
				else if (rand ==2){z=z-1;}
				else if (rand ==3){x=x+1;}
				else if (rand ==4){x=x-1;}

				int densityToSet = 1 + random.nextInt(newOre.densityLevels) - (2*(height/(surface+10)));

				if (genOre(world, oreBlock, newOre.oreMeta, x, y, z, densityToSet)){
					counter--;
				}
			}
		}
	}

	private void genSingle(AddCustomOre newOre){

		Block oreBlock = newOre.oreBlock;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);

		for (int fail = 0; fail < 1000 && counter > 0; fail++){
			int x=chunkX+random.nextInt(16);
			int y = newOre.getHeight(surface);
			int z = chunkZ+random.nextInt(16);

			if (genOre(world, oreBlock, newOre.oreMeta, x, y, z, random.nextInt(newOre.densityLevels+1))){
				counter--;
			}
		}
	}

	private void genVertical(AddCustomOre newOre){

		if (newOre.oreBlock == Blocks.redstone_ore){
			genVerticalRedstone(newOre);
		}
		else{



			Block oreBlock = newOre.oreBlock;
			int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);

			int length = newOre.var1;

			for (int fail = 0; counter > 0 && fail < 1000; fail ++){

				int height = newOre.getHeight(surface);

				float slopeXY = (random.nextFloat()-0.5F) /3;
				float slopeZY = (random.nextFloat()-0.5F) /3;
				int startX = random.nextInt(6)+8;
				int startZ = random.nextInt(6)+8;
				length = length + random.nextInt(5)+random.nextInt(5)-4;
				for (int loopY = 0; loopY < length; loopY++){

					int x = MathHelper.floor_double(chunkX+startX + slopeXY*loopY);
					int z = MathHelper.floor_double(chunkZ+startZ + slopeZY*loopY);
					int y = height - loopY;

					if (genOre(world, oreBlock, newOre.oreMeta, x, y, z, random.nextInt(newOre.densityLevels+1))){
						counter--;
					}
				}
			}
		}
	}

	private void genVerticalRedstone(AddCustomOre newOre){
		Block oreBlock = newOre.oreBlock;
		//int metadata = newOre.metadata;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);

		int length = newOre.var1;

		for (int fail = 0; counter > 0 && fail < 1000; fail ++){

			int height = newOre.getHeight(surface);

			float slopeXY = (random.nextFloat()-0.5F) /3;
			float slopeZY = (random.nextFloat()-0.5F) /3;
			int startX = random.nextInt(6)+8;
			int startZ = random.nextInt(6)+8;
			length = length + random.nextInt(5)+random.nextInt(5)-4;
			int y = height;
			int x = MathHelper.floor_double(chunkX+startX);
			int z = MathHelper.floor_double(chunkZ+startZ);

			for (int loopY = 0; loopY < length; loopY++){

				x = MathHelper.floor_double(chunkX+startX + slopeXY*loopY);
				z = MathHelper.floor_double(chunkZ+startZ + slopeZY*loopY);
				y = height - loopY;
				
				if (genOre(world, oreBlock, newOre.oreMeta, x, y, z, random.nextInt(newOre.densityLevels+1))){
					counter--;
				}
			}
		}
	}

	private void genStar(AddCustomOre newOre){

		Block oreBlock = newOre.oreBlock;
		int metadata = newOre.oreMeta;
		int counter = MathHelper.floor_float(newOre.getPerChunk()*surfaceMod);


		for (int fail = 0; fail < 1000 && counter > 0; fail ++){
			int x=chunkX+random.nextInt(12)+2;
			int y = newOre.getHeight(surface);
			if (y > 1 && y < 255){
				int z = chunkZ+random.nextInt(16);
				Block targetBlock = world.getBlock(x, y, z);
				BlockInfo blockToSet = OreHelper.getOreForStone(world, x, y, z, oreBlock, world.getBlockMetadata(x, y, z), 1);
				if (blockToSet != null){
					if (genOre(world, oreBlock, metadata, x,y,z,random.nextInt(newOre.densityLevels+1))){counter--;}
					if (genOre(world, oreBlock, metadata, x+1,y,z,random.nextInt(newOre.densityLevels+1))){counter--;}
					if (genOre(world, oreBlock, metadata, x-1,y,z,random.nextInt(newOre.densityLevels+1))){counter--;}
					if (genOre(world, oreBlock, metadata, x,y+1,z,random.nextInt(newOre.densityLevels+1))){counter--;}
					if (genOre(world, oreBlock, metadata, x,y-1,z,random.nextInt(newOre.densityLevels+1))){counter--;}
					if (genOre(world, oreBlock, metadata, x,y,z+1,random.nextInt(newOre.densityLevels+1))){counter--;}
					if (genOre(world, oreBlock, metadata, x,y,z-1,random.nextInt(newOre.densityLevels+1))){counter--;}
				}
			}
		}
	}
	
	//Added by Tmtravlr... perhaps this could be done more efficiently, but I'm not sure how.
	private void genReplace(AddCustomOre newOre) {
		//Loop through the chunk and replace all the existing ore blocks that this overrides
		for(int x = chunkX; x < chunkX+16; x++) {
			for(int z = chunkZ; z < chunkZ+16; z++) {
				for(int y = 0; y < world.getHeight(); y++) {
					if(world.getBlock(x, y, z) == newOre.oreBlock && world.getBlockMetadata(x, y, z) == newOre.oreMeta) {
						BlockInfo surroundType = getSurroundingBlockType(x, y, z);
						gen.setBlockWithoutNotify(world, x, y, z, surroundType.block, surroundType.meta);
						genOre(world, newOre.oreBlock, newOre.oreMeta, x,y,z,random.nextInt(newOre.densityLevels+1));
					}
				}
			}
		}
	}

	private boolean genOre(World world, Block oreBlock, int oreMeta, int x, int y, int z, int density){
		if (y > 0 && y < 256){
			density--;
			
//			Block blockToSet = BlockSets.oreUbifier.get(new OreBlockInfo(oreBlock, oreMeta, blockandmeta.block, density));
//			
//			if (blockToSet != null){
//				//gen.setBlockWithoutNotify(world, x, y, z, blockToSet, blockandmeta.meta);
//				world.setBlock(x, y, z, blockToSet, blockandmeta.meta, 2);
//				return true;
//			}
			
			BlockInfo blockandmeta = new BlockInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
			
			if(WTFOres.DEBUG && blockandmeta != null && Item.getItemFromBlock(blockandmeta.block) != null) WTFCore.log.info("Trying to generate ore in block '" + new ItemStack(Item.getItemFromBlock(blockandmeta.block), 1, blockandmeta.meta).getDisplayName() + "'");
			
			if(!OreHelper.stoneTypes.contains(blockandmeta)) {
				//Can't generate here, but it tried, so count it (so it doesn't try a zillion times and lag out).
				return true;
			}
			
			BlockInfo blockToSet = OreHelper.getOreForStone(world, x, y, z, oreBlock, oreMeta, density);
			if(WTFOres.DEBUG) WTFCore.log.info("Creating block '" + (blockToSet == null ? "null" : new ItemStack(blockToSet.block, 1, blockToSet.meta).getDisplayName()) + "' at "+x+" "+y+" "+z);
			
			if (blockToSet != null){
				gen.setBlockWithoutNotify(world, x, y, z, blockToSet.block, blockToSet.meta);
				return true;
			}
		}
		return false;
	}

	private BlockInfo getSurroundingBlockType(int x, int y, int z) {
		BlockInfo block;
		
		block = new BlockInfo(world.getBlock(x+1, y, z),world.getBlockMetadata(x+1, y, z));
		if(OreHelper.stoneTypes.contains(block)) {
			return block;
		}
		
		block = new BlockInfo(world.getBlock(x-1, y, z),world.getBlockMetadata(x-1, y, z));
		if(OreHelper.stoneTypes.contains(block)) {
			return block;
		}
		
		block = new BlockInfo(world.getBlock(x, y, z+1),world.getBlockMetadata(x, y, z+1));
		if(OreHelper.stoneTypes.contains(block)) {
			return block;
		}
		
		block = new BlockInfo(world.getBlock(x, y, z-1),world.getBlockMetadata(x, y, z-1));
		if(OreHelper.stoneTypes.contains(block)) {
			return block;
		}
		
		block = new BlockInfo(world.getBlock(x, y+1, z),world.getBlockMetadata(x, y+1, z));
		if(OreHelper.stoneTypes.contains(block)) {
			return block;
		}
		
		block = new BlockInfo(world.getBlock(x, y-1, z),world.getBlockMetadata(x, y-1, z));
		if(OreHelper.stoneTypes.contains(block)) {
			return block;
		}
		
		return new BlockInfo(Blocks.stone, 0);
	}

}
