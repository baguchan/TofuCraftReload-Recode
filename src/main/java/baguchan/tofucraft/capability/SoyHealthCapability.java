package baguchan.tofucraft.capability;

import baguchan.tofucraft.TofuCraftReload;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoyHealthCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
	private int soyHealthLevel;
	private long lastTick = -12000L;

	//when can update is true. update last tick
	public void setSoyHealth(Player player, int level, boolean canUpdate) {
		if (canUpdate) {
			this.lastTick = player.getLevel().getDayTime();
			player.displayClientMessage(new TranslatableComponent("item.tofucraft.soymilk.drink_day", this.soyHealthLevel), true);
		}
//		if (!player.getLevel().isClientSide()) {
//			SoyMilkDrinkedMessage message = new SoyMilkDrinkedMessage(player, level, canUpdate);
//			TofuCraftReload.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
//		}
		this.soyHealthLevel = Mth.clamp(level, 1, 20);
	}

	public void removeAllSoyHealth(Player player) {
		this.soyHealthLevel = 0;
//		if (!player.getLevel().isClientSide()) {
//			SoyMilkDrinkedMessage message = new SoyMilkDrinkedMessage(player, this.soyHealthLevel, true);
//			TofuCraftReload.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
//		}
	}

	public long getRemainTick() {
		return this.lastTick;
	}

	public int getSoyHealthLevel() {
		return this.soyHealthLevel;
	}

	public void tick(Player player) {
		if (!player.getLevel().isClientSide()) {
			if (player.getLevel().getDayTime() > this.lastTick + 24000L) {
				if (this.soyHealthLevel > 0) {
					this.soyHealthLevel = Mth.clamp(this.soyHealthLevel - 2, 1, 20);
					this.lastTick = player.getLevel().getDayTime();
				}
			}
		}
	}

	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return (capability == TofuCraftReload.SOY_HEALTH_CAPABILITY) ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putLong("RemainTick", this.lastTick);
		nbt.putInt("SoyHealthLevel", this.soyHealthLevel);
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.lastTick = nbt.getLong("RemainTick");
		this.soyHealthLevel = nbt.getInt("SoyHealthLevel");
	}
}
