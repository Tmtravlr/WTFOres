package wtfores.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import wtfcore.blocks.IAlphaMaskedBlock;
import wtfores.OreHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RedstoneOverlayOre extends OverlayOre implements IAlphaMaskedBlock{

	private boolean isLit;
	RedstoneOverlayOre opposite;

	public RedstoneOverlayOre(Block oreBlock, int parentMeta, int oreLevel, Block stoneBlock, String oreType, String[] stoneNames, String domain, boolean lit) {
		super(oreBlock, parentMeta, oreLevel, stoneBlock, oreType, stoneNames, domain);
		if (lit){
			this.setLightLevel(0.67F);
			this.isLit = true;
			this.setTickRandomly(true);
			this.setCreativeTab(null);
		}


	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta){
		if (OreHelper.enableDenseOres && this.oreLevel > 0){
			//Block blockToSet = BlockSets.oreUbifier.get(new OreBlockInfo (Blocks.lit_redstone_ore, this.oreMeta, this.stoneBlock, this.oreLevel+1));
			if (this.next != null){
				if(!this.isLit) {
					world.setBlock(x, y, z, this.opposite.next, meta, 0);
				}
				else {
					world.setBlock(x, y, z, this.next, meta, 0);
				}
			}
		}
	}

    @Override
	public int tickRate(World p_149738_1_)
    {
        return 30;
    }
    @Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_)
    {
        this.setToLit(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_);
    }
    
    public void onEntityWalking(World p_149724_1_, int p_149724_2_, int p_149724_3_, int p_149724_4_, Entity p_149724_5_)
    {
        this.setToLit(p_149724_1_, p_149724_2_, p_149724_3_, p_149724_4_);
        //super.onEntityWalking(p_149724_1_, p_149724_2_, p_149724_3_, p_149724_4_, p_149724_5_);
    }
    
    @Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        this.setToLit(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
        return super.onBlockActivated(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, p_149727_5_, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    }

    private void setToLit(World world, int x, int y, int z)
    {
        this.createParticles(world, x, y, z);

        if (!this.isLit)
        {
            world.setBlock(x, y, z, this.opposite, world.getBlockMetadata(x,y,z), 3);
        }
    }
    @Override
	public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (this.isLit)
        {
        	world.setBlock(x, y, z, this.opposite, world.getBlockMetadata(x, y, z), 0);
        }
        super.updateTick(world, x, y, z, random);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
    {
        if (this.isLit)
        {
            this.createParticles(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_);
        }
    }

    private void createParticles(World world, int p_150186_2_, int p_150186_3_, int p_150186_4_)
    {
        Random random = world.rand;
        double d0 = 0.0625D;

        for (int l = 0; l < 6; ++l)
        {
            double d1 = p_150186_2_ + random.nextFloat();
            double d2 = p_150186_3_ + random.nextFloat();
            double d3 = p_150186_4_ + random.nextFloat();

            if (l == 0 && !world.getBlock(p_150186_2_, p_150186_3_ + 1, p_150186_4_).isOpaqueCube())
            {
                d2 = p_150186_3_ + 1 + d0;
            }

            if (l == 1 && !world.getBlock(p_150186_2_, p_150186_3_ - 1, p_150186_4_).isOpaqueCube())
            {
                d2 = p_150186_3_ + 0 - d0;
            }

            if (l == 2 && !world.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ + 1).isOpaqueCube())
            {
                d3 = p_150186_4_ + 1 + d0;
            }

            if (l == 3 && !world.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ - 1).isOpaqueCube())
            {
                d3 = p_150186_4_ + 0 - d0;
            }

            if (l == 4 && !world.getBlock(p_150186_2_ + 1, p_150186_3_, p_150186_4_).isOpaqueCube())
            {
                d1 = p_150186_2_ + 1 + d0;
            }

            if (l == 5 && !world.getBlock(p_150186_2_ - 1, p_150186_3_, p_150186_4_).isOpaqueCube())
            {
                d1 = p_150186_2_ + 0 - d0;
            }

            if (d1 < p_150186_2_ || d1 > p_150186_2_ + 1 || d2 < 0.0D || d2 > p_150186_3_ + 1 || d3 < p_150186_4_ || d3 > p_150186_4_ + 1)
            {
                world.spawnParticle("reddust", d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

}
