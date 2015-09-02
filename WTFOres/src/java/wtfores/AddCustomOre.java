package wtfores;

import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class AddCustomOre {

	//block,meta,texturename,gentype,maxheight,perChunk,v1, v2

	public static Random random = new Random();
	public Block oreBlock;
	public int oreMeta = 0;
	public String textureName;
	public int genType = 0;
	public int densityLevels = 3;
	

	private float maxHeightPercent=100F;
	public void setMaxHeightPercent(int var){
		this.maxHeightPercent = (float)var/100F;
	}

	private float minHeightPercent=0F;
	public void setMinHeightPercent(int var){
		this.minHeightPercent = (float)var/100F;
	}

	private int maxPerChunk=-1;
	public void setMaxPerChunk(int var){
		this.maxPerChunk=var;
	}

	private int minPerChunk=-1;
	public void setMinPerChunk(int var){
		this.minPerChunk=var;
	}

	public int getPerChunk(){
		if (maxPerChunk==-1){return minPerChunk;}
		else if (minPerChunk==-1){return maxPerChunk;}
		else {
			return random.nextInt(maxPerChunk-minPerChunk)+minPerChunk;
		}
	}

	//Blacklists and whitelists for where this can generate (Added by Tmtravlr)
	
	public HashSet<String> biomeBlacklist = new HashSet<String>();
	public HashSet<String> biomeWhitelist;
	
	public HashSet<String> biomeTypeBlacklist = new HashSet<String>();
	public HashSet<String> biomeTypeWhitelist;
	
	public HashSet<Integer> dimensionBlacklist = new HashSet<Integer>();
	public HashSet<Integer> dimensionWhitelist;


	public int getHeight(int surface) {
		int maxHeight = MathHelper.floor_float(maxHeightPercent*surface);
		int minHeight = MathHelper.floor_float(minHeightPercent*surface);
		return random.nextInt(maxHeight-minHeight)+minHeight;
	}

	public int var1=5;
	public void setVar1(int var){
		this.var1=var;
	}
	public int var2=5;
	public void setVar2(int var){
		this.var2=var;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((biomeBlacklist == null) ? 0 : biomeBlacklist.hashCode());
		result = prime * result + ((biomeWhitelist == null) ? 0 : biomeWhitelist.hashCode());
		result = prime * result + ((biomeTypeBlacklist == null) ? 0 : biomeTypeBlacklist.hashCode());
		result = prime * result + ((biomeTypeWhitelist == null) ? 0 : biomeTypeWhitelist.hashCode());
		result = prime * result + ((dimensionBlacklist == null) ? 0 : dimensionBlacklist.hashCode());
		result = prime * result + ((dimensionWhitelist == null) ? 0 : dimensionWhitelist.hashCode());
		result = prime * result + genType;
		result = prime * result + Float.floatToIntBits(maxHeightPercent);
		result = prime * result + maxPerChunk;
		result = prime * result + oreMeta;
		result = prime * result + Float.floatToIntBits(minHeightPercent);
		result = prime * result + minPerChunk;
		result = prime * result
				+ ((oreBlock == null) ? 0 : oreBlock.hashCode());
		result = prime * result
				+ ((textureName == null) ? 0 : textureName.hashCode());
		result = prime * result + var1;
		result = prime * result + var2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddCustomOre other = (AddCustomOre) obj;
		if (biomeBlacklist == null) {
			if (other.biomeBlacklist != null)
				return false;
		} else if (!biomeBlacklist.equals(other.biomeBlacklist))
			return false;
		if (biomeWhitelist == null) {
			if (other.biomeWhitelist != null)
				return false;
		} else if (!biomeWhitelist.equals(other.biomeWhitelist))
			return false;
		if (biomeTypeBlacklist == null) {
			if (other.biomeTypeBlacklist != null)
				return false;
		} else if (!biomeTypeBlacklist.equals(other.biomeTypeBlacklist))
			return false;
		if (biomeTypeWhitelist == null) {
			if (other.biomeTypeWhitelist != null)
				return false;
		} else if (!biomeTypeWhitelist.equals(other.biomeTypeWhitelist))
			return false;
		if (dimensionBlacklist == null) {
			if (other.dimensionBlacklist != null)
				return false;
		} else if (!dimensionBlacklist.equals(other.dimensionBlacklist))
			return false;
		if (dimensionWhitelist == null) {
			if (other.dimensionWhitelist != null)
				return false;
		} else if (!dimensionWhitelist.equals(other.dimensionWhitelist))
			return false;
		if (genType != other.genType)
			return false;
		if (Float.floatToIntBits(maxHeightPercent) != Float
				.floatToIntBits(other.maxHeightPercent))
			return false;
		if (maxPerChunk != other.maxPerChunk)
			return false;
		if (oreMeta != other.oreMeta)
			return false;
		if (Float.floatToIntBits(minHeightPercent) != Float
				.floatToIntBits(other.minHeightPercent))
			return false;
		if (minPerChunk != other.minPerChunk)
			return false;
		if (oreBlock == null) {
			if (other.oreBlock != null)
				return false;
		} else if (!oreBlock.equals(other.oreBlock))
			return false;
		if (textureName == null) {
			if (other.textureName != null)
				return false;
		} else if (!textureName.equals(other.textureName))
			return false;
		if (var1 != other.var1)
			return false;
		if (var2 != other.var2)
			return false;
		return true;
	}





}
