package baguchan.tofucraft.entity.ai;

import baguchan.tofucraft.api.TofunianJobBlocks;
import baguchan.tofucraft.entity.TofunianEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class MakeFoodGoal extends MoveToBlockGoal {
	private final TofunianEntity creature;

	private int cookTick;

	public MakeFoodGoal(TofunianEntity creature, double speedIn, int length) {
		super(creature, speedIn, length);
		this.creature = creature;
	}

	public boolean canUse() {
		return (this.creature.getRole() == TofunianEntity.Roles.TOFUCOOK && !this.creature.hasExcessFood() && this.creature.hasFarmSeeds() && this.creature.getLevel().isDay() && super.canUse());
	}

	public boolean canContinueToUse() {
		return (super.canContinueToUse() && this.creature.getRole() == TofunianEntity.Roles.TOFUCOOK && !this.creature.hasExcessFood() && this.creature.hasFarmSeeds() && this.creature.getLevel().isDay() && this.mob != null);
	}

	public void start() {
		super.start();
		this.cookTick = 0;
	}

	public void tick() {
		super.tick();
		this.creature.getLookControl().setLookAt(this.mob.getX(), this.mob.getY(), this.mob.getZ());
		if (this.cookTick > 0)
			this.cookTick--;
		if (isReachedTarget() && this.cookTick <= 0) {
			this.creature.cookingFood();
			this.creature.swing(Hand.MAIN_HAND);
			this.creature.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 0.7F);
			this.cookTick = 20;
		}
	}

	protected boolean isValidTarget(IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos);
		Block block = blockstate.getBlock();
		return TofunianJobBlocks.getJobBlockList().entrySet().stream().findFirst().filter(entry ->
				(entry.getValue() == this.creature.getRole() && entry.getKey() == block))
				.isPresent();
	}

	protected boolean findNearestBlock() {
		if (this.creature.getTofunainJobBlock() != null &&
				isValidTarget(this.creature.getLevel(), this.creature.getTofunainJobBlock())) {
			this.blockPos = this.creature.getTofunainJobBlock();
			return true;
		}
		return super.findNearestBlock();
	}
}