package baguchan.tofucraft.block;

import baguchan.tofucraft.registry.TofuTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class TofuMushroomBlock extends BushBlock implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 11.0D, 12.0D);

	private final Supplier<Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>>> featureSupplier;

	public TofuMushroomBlock(Properties properties, Supplier<Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>>> p_153984_) {
		super(properties);
		this.featureSupplier = p_153984_;
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
		return p_51042_.is(TofuTags.Blocks.TOFU_TERRAIN);
	}

	public boolean growMushroom(ServerLevel p_54860_, BlockPos p_54861_, BlockState p_54862_, RandomSource p_54863_) {
		p_54860_.removeBlock(p_54861_, false);
		if (this.featureSupplier.get().value().place(p_54860_, p_54860_.getChunkSource().getGenerator(), p_54863_, p_54861_)) {
			return true;
		} else {
			p_54860_.setBlock(p_54861_, p_54862_, 3);
			return false;
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter p_50897_, BlockPos p_50898_, BlockState p_50899_, boolean p_50900_) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level p_50901_, RandomSource p_50902_, BlockPos p_50903_, BlockState p_50904_) {
		return (double) p_50902_.nextFloat() < 0.4D;
	}

	@Override
	public void performBonemeal(ServerLevel p_50893_, RandomSource p_50894_, BlockPos p_50895_, BlockState p_50896_) {
		growMushroom(p_50893_, p_50895_, p_50896_, p_50894_);
	}
}
