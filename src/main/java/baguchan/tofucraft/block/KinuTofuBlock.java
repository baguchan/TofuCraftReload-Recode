package baguchan.tofucraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class KinuTofuBlock extends Block {
	public static final IntegerProperty AGE = BlockStateProperties.field_208170_W;

	public KinuTofuBlock(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	public void func_180655_c(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.func_180655_c(stateIn, worldIn, pos, rand);
		if (isUnderWeight(worldIn, pos) &&
				rand.nextInt(4) == 0) {
			double d4 = rand.nextBoolean() ? 0.8D : -0.8D;
			double d0 = pos.getX() + 0.5D + rand.nextFloat() * d4;
			double d1 = (pos.getY() + rand.nextFloat());
			double d2 = pos.getZ() + 0.5D + rand.nextFloat() * d4;
			worldIn.func_195594_a((IParticleData) ParticleTypes.field_197618_k, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	public void func_180658_a(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		super.func_180658_a(worldIn, pos, entityIn, fallDistance * 0.75F);
		worldIn.func_175655_b(pos, true);
	}

	public void func_225534_a_(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		super.func_225534_a_(state, worldIn, pos, random);
		if (isUnderWeight(worldIn, pos))
			worldIn.func_175655_b(pos, true);
	}

	public boolean isUnderWeight(World world, BlockPos pos) {
		BlockState weightBlock = world.getBlockState(pos.above());
		BlockState baseBlock = world.getBlockState(pos.func_177977_b());
		boolean isWeightValid = (weightBlock != null && (weightBlock.func_185904_a() == Material.field_151576_e || weightBlock.func_185904_a() == Material.field_151573_f));
		float baseHardness = baseBlock.func_185887_b((IBlockReader) world, pos.func_177977_b());
		boolean isBaseValid = (baseBlock.func_200015_d((IBlockReader) world, pos) && (baseBlock.func_185904_a() == Material.field_151576_e || baseBlock.func_185904_a() == Material.field_151573_f || baseHardness >= 1.0F || baseHardness < 0.0F));
		return (isWeightValid && isBaseValid);
	}

	protected void func_206840_a(StateContainer.Builder<Block, BlockState> builder) {
		builder.func_206894_a(new Property[]{AGE});
	}
}
