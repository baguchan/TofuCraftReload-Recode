package baguchan.tofucraft.data;

import baguchan.tofucraft.TofuCraftReload;
import baguchan.tofucraft.registry.TofuBlocks;
import baguchan.tofucraft.registry.TofuItems;
import baguchan.tofucraft.registry.TofuTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGenerator extends ItemTagsProvider {
	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper exFileHelper) {
		super(generator, provider, TofuCraftReload.MODID, exFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {
		tag(ItemTags.PLANKS).add(TofuBlocks.TOFU_STEM_PLANKS.get().asItem());

		tag(TofuTags.Items.DUST_SALT).add(TofuItems.SALT.get());
		tag(TofuTags.Items.SALT).add(TofuItems.SALT.get());
		tag(TofuTags.Items.SOYBEAN).add(TofuItems.SEEDS_SOYBEANS.get());
		tag(TofuTags.Items.SOYMILK).addTag(TofuTags.Items.MILK_SOYMILK);
		tag(TofuTags.Items.MILK_SOYMILK).add(TofuItems.BUCKET_SOYMILK.get(), TofuItems.SOYMILK.get());
		tag(TofuTags.Items.RICE).add(TofuItems.RICE.get());
		tag(ItemTags.LOGS_THAT_BURN).add(TofuBlocks.TOFU_STEM.get().asItem());
		tag(ItemTags.ARROWS).add(TofuItems.ZUNDA_ARROW.get());
		tag(Tags.Items.SEEDS).add(TofuItems.SEEDS_CHILI.get(), TofuItems.SEEDS_SOYBEANS.get(), TofuItems.SEEDS_SOYBEANS_NETHER.get(), TofuItems.SEEDS_SOYBEANS_SOUL.get(), TofuItems.SEEDS_RICE.get());
		tag(TofuTags.Items.RICE_BLOCK).add(TofuBlocks.RICE_BLOCK.get().asItem());
	}
}