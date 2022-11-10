package baguchan.tofucraft.entity.goal;

import baguchan.tofucraft.entity.TofuGandlem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ChargeGoal extends Goal {

	private final TofuGandlem gandlem;
	private int attackTime;

	private int cooldown;
	private int maxCooldown;
	private final UniformInt timeBetweenCooldown;

	public ChargeGoal(TofuGandlem p_32247_, UniformInt rushCooldown) {
		this.gandlem = p_32247_;
		this.timeBetweenCooldown = rushCooldown;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.maxCooldown <= 0) {
			this.maxCooldown = timeBetweenCooldown.sample(this.gandlem.getRandom());
			return false;
		} else {
			if (cooldown > this.maxCooldown) {
				this.cooldown = 0;
				this.maxCooldown = timeBetweenCooldown.sample(this.gandlem.getRandom());
				return this.gandlem.getHealth() < this.gandlem.getMaxHealth() / 2 && !this.gandlem.isRush();
			} else {
				++this.cooldown;
				return false;
			}
		}

	}

	@Override
	public boolean canContinueToUse() {
		return this.attackTime < 80 && !this.gandlem.isChargeFailed();
	}

	@Override
	public void start() {
		super.start();
		this.attackTime = 0;
		this.gandlem.setCharging(true);
		this.gandlem.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 2.0F, 1.0F);
	}

	@Override
	public void tick() {
		super.tick();
		++this.attackTime;

		if (this.attackTime == 79) {
			this.gandlem.setCharging(false);
			this.gandlem.setFullCharge(true);
		}

	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}