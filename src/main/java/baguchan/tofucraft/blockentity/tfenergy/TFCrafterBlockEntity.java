package baguchan.tofucraft.blockentity.tfenergy;

import baguchan.tofucraft.blockentity.tfenergy.base.WorkerBaseBlockEntity;
import baguchan.tofucraft.inventory.TFCrafterMenu;
import baguchan.tofucraft.registry.TofuBlockEntitys;
import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.CRAFTING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ORIENTATION;

public class TFCrafterBlockEntity extends WorkerBaseBlockEntity implements StackedContentsCompatible, Container, MenuProvider, CraftingContainer {

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	private int progress = 0;

	private int refreshTime = 0;
	public static final int MAX_CRAFT_TIME = 80;
	private final RecipeType<? extends CraftingRecipe> recipeType;

	private final RecipeManager.CachedCheck<CraftingContainer, ? extends Recipe> quickCheck;


	protected final ContainerData containerData = new ContainerData() {
		private final int[] slotStates = new int[9];
		private int triggered = 0;

		@Override
		public int get(int p_307671_) {
			return p_307671_ == 9 ? this.triggered : this.slotStates[p_307671_];
		}

		@Override
		public void set(int p_307241_, int p_307484_) {
			if (p_307241_ == 9) {
				this.triggered = p_307484_;
			} else {
				this.slotStates[p_307241_] = p_307484_;
			}
		}

		@Override
		public int getCount() {
			return 10;
		}
	};

	public TFCrafterBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		super(TofuBlockEntitys.TF_CRAFTER.get(), p_155229_, p_155230_, 5000);
		this.recipeType = RecipeType.CRAFTING;
		this.quickCheck = RecipeManager.createCheck(RecipeType.CRAFTING);
	}

	public static void tick(Level level, BlockPos blockPos, BlockState blockState, TFCrafterBlockEntity tfcrafter) {
		if (level.isClientSide()) return;
		boolean worked = false;
		if (tfcrafter.getEnergyStored() > 0) {
			if (tfcrafter.refreshTime <= 0) {

				Optional<? extends RecipeHolder<? extends Recipe>> optional = tfcrafter.quickCheck.getRecipeFor(tfcrafter, level);

				if (optional.isPresent() || tfcrafter.progress > 0) {
					++tfcrafter.progress;
					if (tfcrafter.progress == MAX_CRAFT_TIME) {
						tfcrafter.progress = 0;
						if (level instanceof ServerLevel serverLevel) {
							dispenseFrom(tfcrafter, blockState, serverLevel, blockPos);
						}
					}
					worked = true;

					tfcrafter.drain(2, false);

				} else {
					tfcrafter.refreshTime = 30 + tfcrafter.level.random.nextInt(30);
				}
			} else {
				tfcrafter.progress = 0;
				tfcrafter.refreshTime--;
			}
		}


		if (blockState.getValue(CRAFTING) != worked) {
			level.setBlock(blockPos, blockState.setValue(CRAFTING, true), 2);
		}
	}

	protected static void dispenseFrom(TFCrafterBlockEntity blockEntity, BlockState p_307495_, ServerLevel p_307310_, BlockPos p_307672_) {
		BlockEntity $$5 = p_307310_.getBlockEntity(p_307672_);
		if ($$5 instanceof TFCrafterBlockEntity crafterblockentity) {
			Optional<? extends RecipeHolder<? extends Recipe>> optional = blockEntity.quickCheck.getRecipeFor(blockEntity, p_307310_);
			if (optional.isEmpty()) {
				p_307310_.levelEvent(1050, p_307672_, 0);
			} else {
				p_307310_.setBlock(p_307672_, (BlockState) p_307495_.setValue(CRAFTING, true), 2);
				CraftingRecipe craftingrecipe = (CraftingRecipe) optional.get().value();
				ItemStack itemstack = craftingrecipe.assemble(crafterblockentity, p_307310_.registryAccess());
				itemstack.onCraftedBySystem(p_307310_);
				dispenseItem(p_307310_, p_307672_, crafterblockentity, itemstack, p_307495_);
				craftingrecipe.getRemainingItems(crafterblockentity).forEach((p_307328_) -> {
					dispenseItem(p_307310_, p_307672_, crafterblockentity, p_307328_, p_307495_);
				});
				crafterblockentity.getItems().forEach((p_307295_) -> {
					if (!p_307295_.isEmpty()) {
						p_307295_.shrink(1);
					}

				});
				crafterblockentity.setChanged();
			}
		}

	}

	private static void dispenseItem(Level p_307361_, BlockPos p_307620_, TFCrafterBlockEntity p_307387_, ItemStack p_307296_, BlockState p_307501_) {
		Direction direction = (p_307501_.getValue(ORIENTATION)).front();
		Container container = HopperBlockEntity.getContainerAt(p_307361_, p_307620_.relative(direction));
		ItemStack itemstack = p_307296_.copy();
		if (container == null || !(container instanceof CrafterBlockEntity) && p_307296_.getCount() <= container.getMaxStackSize()) {
			if (container != null) {
				while (!itemstack.isEmpty()) {
					int i = itemstack.getCount();
					itemstack = HopperBlockEntity.addItem(p_307387_, container, itemstack, direction.getOpposite());
					if (i == itemstack.getCount()) {
						break;
					}
				}
			}
		} else {
			while (!itemstack.isEmpty()) {
				ItemStack itemstack2 = itemstack.copyWithCount(1);
				ItemStack itemstack1 = HopperBlockEntity.addItem(p_307387_, container, itemstack2, direction.getOpposite());
				if (!itemstack1.isEmpty()) {
					break;
				}

				itemstack.shrink(1);
			}
		}

		if (!itemstack.isEmpty()) {
			Vec3 vec3 = Vec3.atCenterOf(p_307620_).relative(direction, 0.7);
			DefaultDispenseItemBehavior.spawnItem(p_307361_, itemstack, 6, direction, vec3);
			p_307361_.levelEvent(1049, p_307620_, 0);
			p_307361_.levelEvent(2010, p_307620_, direction.get3DDataValue());
		}

	}

	@Override
	public int getContainerSize() {
		return 10;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.inventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int index) {
		return inventory.get(index);
	}

	public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
		return ContainerHelper.removeItem(this.inventory, p_70298_1_, p_70298_2_);
	}

	public ItemStack removeItemNoUpdate(int p_70304_1_) {
		return ContainerHelper.takeItem(this.inventory, p_70304_1_);
	}


	@Override
	public void setItem(int index, ItemStack stack) {
		inventory.set(index, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public boolean stillValid(Player p_18946_) {
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return p_18946_.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	public NonNullList<ItemStack> getInventory() {
		return inventory;
	}

	public void saveAdditional(CompoundTag cmp) {
		super.saveAdditional(cmp);
		ContainerHelper.saveAllItems(cmp, this.inventory);
		cmp.putInt("progress", this.progress);
		cmp.putInt("RefreshTime", this.refreshTime);

		this.addDisabledSlots(cmp);
		this.addTriggered(cmp);
	}

	public void load(CompoundTag cmp) {
		super.load(cmp);
		ContainerHelper.loadAllItems(cmp, this.inventory);

		this.progress = cmp.getInt("progress");
		this.refreshTime = cmp.getInt("RefreshTime");
		int[] aint = cmp.getIntArray("disabled_slots");

		for (int i = 0; i < 9; ++i) {
			this.containerData.set(i, 0);
		}

		for (int j : aint) {
			if (this.slotCanBeDisabled(j)) {
				this.containerData.set(j, 1);
			}
		}

		this.containerData.set(9, cmp.getInt("triggered"));
	}

	@Override
	public boolean canPlaceItem(int p_307543_, ItemStack p_307267_) {
		if (this.containerData.get(p_307543_) == 1) {
			return false;
		} else {
			ItemStack itemstack = this.inventory.get(p_307543_);
			int i = itemstack.getCount();
			if (i >= itemstack.getMaxStackSize()) {
				return false;
			} else if (itemstack.isEmpty()) {
				return true;
			} else {
				return !this.smallerStackExist(i, itemstack, p_307543_);
			}
		}
	}

	private boolean smallerStackExist(int p_307396_, ItemStack p_307520_, int p_307348_) {
		for (int i = p_307348_ + 1; i < 9; ++i) {
			ItemStack itemstack = this.getItem(i);
			if (itemstack.isEmpty() || itemstack.getCount() < p_307396_ && ItemStack.isSameItemSameTags(itemstack, p_307520_)) {
				return true;
			}

		}

		return false;
	}


	private void addDisabledSlots(CompoundTag p_307523_) {
		IntList intlist = new IntArrayList();

		for (int i = 0; i < 9; ++i) {
			if (this.isSlotDisabled(i)) {
				intlist.add(i);
			}
		}

		p_307523_.putIntArray("disabled_slots", intlist);
	}

	public void setSlotState(int p_307571_, boolean p_307624_) {
		if (this.slotCanBeDisabled(p_307571_)) {
			this.containerData.set(p_307571_, p_307624_ ? 0 : 1);
			this.setChanged();
		}
	}

	public boolean isSlotDisabled(int p_307461_) {
		if (p_307461_ >= 0 && p_307461_ < 9) {
			return this.containerData.get(p_307461_) == 1;
		} else {
			return false;
		}
	}

	private boolean slotCanBeDisabled(int p_307658_) {
		return p_307658_ > -1 && p_307658_ < 9 && this.inventory.get(p_307658_).isEmpty();
	}

	private void addTriggered(CompoundTag p_307675_) {
		p_307675_.putInt("triggered", this.containerData.get(9));
	}

	public void setTriggered(boolean p_307366_) {
		this.containerData.set(9, p_307366_ ? 1 : 0);
	}

	@VisibleForTesting
	public boolean isTriggered() {
		return this.containerData.get(9) == 1;
	}

	@Override
	public void clearContent() {
		this.inventory.clear();
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public void fillStackedContents(StackedContents p_40281_) {
		for (ItemStack itemstack : this.inventory) {
			p_40281_.accountStack(itemstack);
		}
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.tofucraft.tf_crafter");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
		return new TFCrafterMenu(p_39954_, p_39955_);
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.inventory;
	}


	public int getRedstoneSignal() {
		int i = 0;

		for (int j = 0; j < this.getContainerSize(); ++j) {
			ItemStack itemstack = this.getItem(j);
			if (!itemstack.isEmpty() || this.isSlotDisabled(j)) {
				++i;
			}
		}

		return i;
	}
}
