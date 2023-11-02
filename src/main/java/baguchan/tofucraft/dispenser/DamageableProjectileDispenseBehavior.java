package baguchan.tofucraft.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class DamageableProjectileDispenseBehavior extends AbstractProjectileDispenseBehavior {
	public ItemStack execute(BlockSource p_123366_, ItemStack p_123367_) {
		Level level = p_123366_.level();
		Position position = DispenserBlock.getDispensePosition(p_123366_);
		Direction direction = p_123366_.state().getValue(DispenserBlock.FACING);
		for (int i = 0; i < shootCount(); i++) {
			Projectile projectile = this.getProjectile(level, position, p_123367_);
			projectile.shoot((double) direction.getStepX(), (double) ((float) direction.getStepY() + 0.1F), (double) direction.getStepZ(), this.getPower(), this.getUncertainty());
			level.addFreshEntity(projectile);
		}
		if (p_123367_.hurt(1, p_123366_.level().getRandom(), null)) {
			p_123367_.setCount(0);
		}
		return p_123367_;
	}

	protected int shootCount() {
		return 1;
	}
}
