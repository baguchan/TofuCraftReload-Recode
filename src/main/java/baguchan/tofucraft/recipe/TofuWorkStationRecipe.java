package baguchan.tofucraft.recipe;

import baguchan.tofucraft.registry.TofuRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class TofuWorkStationRecipe implements IWorkRecipe {

	protected Ingredient baseIngredient;
	protected Ingredient ingredient;
	protected Ingredient subIngredient;

	final ItemStack result;

	public TofuWorkStationRecipe(Ingredient baseIngredient, Ingredient ingredient, Ingredient subIngredient, ItemStack results) {
		this.baseIngredient = baseIngredient;
		this.ingredient = ingredient;
		this.subIngredient = subIngredient;
		this.result = results;
	}

	@Override
	public boolean matches(Container p_266855_, Level p_266781_) {
		return this.hasAnyMatching(p_266855_);
	}

	private boolean hasAnyMatching(Container container) {
		boolean flag = false;
		boolean flag2 = false;
		boolean flag3 = false;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack itemstack = container.getItem(i);
			if (this.baseIngredient.test(itemstack)) {
				flag = true;
			}
			if (this.ingredient.test(itemstack)) {
				flag2 = true;
			}
			if (this.subIngredient.test(itemstack)) {
				flag3 = true;
			}
		}

		return flag && flag2 && flag3;
	}

	@Override
	public ItemStack assemble(Container p_267036_, RegistryAccess p_266699_) {
		ItemStack itemstack = this.result.copy();

		return itemstack;
	}

	@Override
	public boolean isIngredient(ItemStack p_266982_) {
		return this.ingredient.test(p_266982_);
	}

	@Override
	public boolean isBaseIngredient(ItemStack p_266962_) {
		return this.baseIngredient.test(p_266962_);
	}

	@Override
	public boolean isSubIngredient(ItemStack p_267132_) {
		return this.subIngredient.test(p_267132_);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Ingredient getSubIngredient() {
		return subIngredient;
	}

	public Ingredient getBaseIngredient() {
		return baseIngredient;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	public ItemStack getResult() {
		return result;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {

		return TofuRecipes.RECIPE_TOFU_WORK_STATION.get();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.baseIngredient);
		nonnulllist.add(this.ingredient);
		nonnulllist.add(this.subIngredient);
		return nonnulllist;
	}


	@Override
	public RecipeType<?> getType() {

		return TofuRecipes.RECIPETYPE_TOFU_WORK_STATION.get();
	}

	public static class Serializer implements RecipeSerializer<TofuWorkStationRecipe> {
		private static final Codec<TofuWorkStationRecipe> CODEC = RecordCodecBuilder.create((p_301330_) -> {
			return p_301330_.group(Ingredient.CODEC.fieldOf("base").forGetter((p_298250_) -> {
				return p_298250_.baseIngredient;
			}), Ingredient.CODEC.fieldOf("ingredient").forGetter((p_297231_) -> {
				return p_297231_.ingredient;
			}), Ingredient.CODEC.fieldOf("sub").forGetter((p_299654_) -> {
				return p_299654_.subIngredient;
			}), ItemStack.RESULT_CODEC.fieldOf("result").forGetter((p_297480_) -> {
				return p_297480_.result;
			})).apply(p_301330_, TofuWorkStationRecipe::new);
		});

		public Codec<TofuWorkStationRecipe> codec() {
			return CODEC;
		}

		public TofuWorkStationRecipe fromNetwork(FriendlyByteBuf p_267316_) {
			Ingredient ingredient = Ingredient.fromNetwork(p_267316_);
			Ingredient ingredient1 = Ingredient.fromNetwork(p_267316_);
			Ingredient ingredient2 = Ingredient.fromNetwork(p_267316_);
			ItemStack itemstack = p_267316_.readItem();
			return new TofuWorkStationRecipe(ingredient, ingredient1, ingredient2, itemstack);
		}

		public void toNetwork(FriendlyByteBuf p_266746_, TofuWorkStationRecipe p_266927_) {
			p_266927_.baseIngredient.toNetwork(p_266746_);
			p_266927_.ingredient.toNetwork(p_266746_);
			p_266927_.subIngredient.toNetwork(p_266746_);
			p_266746_.writeItem(p_266927_.result);
		}
	}
}