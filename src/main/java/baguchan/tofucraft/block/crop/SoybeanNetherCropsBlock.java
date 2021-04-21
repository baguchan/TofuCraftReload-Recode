package baguchan.tofucraft.block.crop;

import baguchan.tofucraft.registry.TofuItems;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class SoybeanNetherCropsBlock extends CropsBlock {
	private static final VoxelShape[] SHAPES = new VoxelShape[]{Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.func_208617_a(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D)};

	public SoybeanNetherCropsBlock(Properties builder) {
		super(builder);
	}

	protected boolean func_200014_a_(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return (state.func_203425_a(Blocks.field_150425_aM) || state.func_203425_a(Blocks.field_150424_aL) || state.func_203425_a(Blocks.field_235381_mu_));
	}

	public void func_225542_b_(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isAreaLoaded(pos, 1))
			return;
		int i = func_185527_x(state);
		if (i < func_185526_g()) {
			float f = getGrowthChance((Block) this, (IBlockReader) worldIn, pos);
			if (ForgeHooks.onCropsGrowPre((World) worldIn, pos, state, (random.nextInt((int) (25.0F / f) + 1) == 0))) {
				worldIn.func_180501_a(pos, func_185528_e(i + 1), 2);
				ForgeHooks.onCropsGrowPost((World) worldIn, pos, state);
			}
		}
	}

	protected static float getGrowthChance(Block blockIn, IBlockReader worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos blockpos = pos.func_177977_b();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				float f1 = 0.0F;
				BlockState blockstate = worldIn.getBlockState(blockpos.func_177982_a(i, 0, j));
				if (blockstate.canSustainPlant(worldIn, blockpos.func_177982_a(i, 0, j), Direction.UP, (IPlantable) blockIn))
					f1 = 4.0F;
				if (i != 0 || j != 0)
					f1 /= 4.0F;
				f += f1;
			}
		}
		BlockPos blockpos1 = pos.func_177978_c();
		BlockPos blockpos2 = pos.func_177968_d();
		BlockPos blockpos3 = pos.func_177976_e();
		BlockPos blockpos4 = pos.func_177974_f();
		boolean flag = (blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock());
		boolean flag1 = (blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock());
		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = (blockIn == worldIn.getBlockState(blockpos3.func_177978_c()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.func_177978_c()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.func_177968_d()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.func_177968_d()).getBlock());
			if (flag2)
				f /= 2.0F;
		}
		return f;
	}

	public boolean func_196260_a(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.func_177977_b();
		if (state.getBlock() == this)
			return worldIn.getBlockState(blockpos).canSustainPlant((IBlockReader) worldIn, blockpos, Direction.UP, (IPlantable) this);
		return func_200014_a_(worldIn.getBlockState(blockpos), (IBlockReader) worldIn, blockpos);
	}

	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return PlantType.NETHER;
	}

	protected IItemProvider getSeedsItem() {
		return (IItemProvider) TofuItems.SEEDS_SOYBEANS_NETHER;
	}

	public VoxelShape func_220053_a(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[((Integer) state.func_177229_b((Property) func_185524_e())).intValue()];
	}
}
