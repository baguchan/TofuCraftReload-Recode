package baguchan.tofucraft.data;

import baguchan.tofucraft.TofuCraftReload;
import baguchan.tofucraft.registry.TofuBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class BlockstateGenerator extends BlockStateProvider {
	public BlockstateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, TofuCraftReload.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(TofuBlocks.KINUTOFU);
		simpleBlock(TofuBlocks.MOMENTOFU);
		simpleBlock(TofuBlocks.ISHITOFU);
		simpleBlock(TofuBlocks.METALTOFU);
		simpleBlock(TofuBlocks.DIAMONDTOFU);

		simpleBlock(TofuBlocks.HELLTOFU);
		simpleBlock(TofuBlocks.HELLTOFU_BRICK);
		simpleBlock(TofuBlocks.HELLTOFU_SMOOTH_BRICK);
		simpleBlock(TofuBlocks.SOULTOFU);
		simpleBlock(TofuBlocks.SOULTOFU_BRICK);
		simpleBlock(TofuBlocks.SOULTOFU_SMOOTH_BRICK);
	}

	@Nonnull
	@Override
	public String getName() {
		return "TofuCraftReload blockstates and block models";
	}
}