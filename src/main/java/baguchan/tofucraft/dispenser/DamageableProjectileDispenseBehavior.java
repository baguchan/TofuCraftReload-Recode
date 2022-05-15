package baguchan.tofucraft.dispenser;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class DamageableProjectileDispenseBehavior extends AbstractProjectileDispenseBehavior {
	public ItemStack execute(BlockSource p_123366_, ItemStack p_123367_) {
		Level level = p_123366_.getLevel();
		Position position = DispenserBlock.getDispensePosition(p_123366_);
		Direction direction = p_123366_.getBlockState().getValue(DispenserBlock.FACING);
		Projectile projectile = this.getProjectile(level, position, p_123367_);
		projectile.shoot((double) direction.getStepX(), (double) ((float) direction.getStepY() + 0.1F), (double) direction.getStepZ(), this.getPower(), this.getUncertainty());
		level.addFreshEntity(projectile);
		p_123367_.hurt(1, level.random, null);
		return p_123367_;
	}
}
