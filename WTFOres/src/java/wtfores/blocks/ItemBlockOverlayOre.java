package wtfores.blocks;

import wtfcore.items.ItemMetadataSubblock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOverlayOre extends ItemMetadataSubblock {

	public ItemBlockOverlayOre(Block block) {
		super(block);
	}
	
	public String getItemStackDisplayName(ItemStack stack) {
		if(this.field_150939_a instanceof OverlayOre) {
			OverlayOre ore = (OverlayOre) this.field_150939_a;
			String name = "Custom Ore";
			
			name = new ItemStack(ore.stoneBlock, 1, stack.getItemDamage()).getDisplayName() + " " + new ItemStack(ore.oreBlock, 1, ore.oreMeta).getDisplayName() + (ore.oreLevel <= 0 ? "" : " x" + (ore.oreLevel+1));
			
			return name;
		}
		
		return super.getItemStackDisplayName(stack);
	}

}
