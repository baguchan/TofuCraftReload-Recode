package baguchan.tofucraft.registry;

import baguchan.tofucraft.TofuCraftReload;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class TofuTrimMaterials {

	public static final ResourceKey<TrimMaterial> TOFU_METAL = registerKey("tofu_metal");
	public static final ResourceKey<TrimMaterial> TOFU_DIAMOND = registerKey("tofu_diamond");
	public static final ResourceKey<TrimMaterial> ZUNDA_RUBY = registerKey("zunda_ruby");

	private static ResourceKey<TrimMaterial> registerKey(String name) {
		return ResourceKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(TofuCraftReload.MODID, name));
	}

	public static void bootstrap(BootstapContext<TrimMaterial> context) {
		register(context, TOFU_METAL, Holder.direct(TofuItems.TOFUMETAL.get()), Style.EMPTY.withColor(0xAAB9C2), 0.2F);
		register(context, TOFU_DIAMOND, Holder.direct(TofuItems.TOFUDIAMOND.get()), Style.EMPTY.withColor(0x6CBEEB), 0.8F);
		register(context, ZUNDA_RUBY, Holder.direct(TofuItems.ZUNDARUBY.get()), Style.EMPTY.withColor(0x39650D), 0.7F);

	}

	private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Holder<Item> trimItem, Style color, float itemModelIndex) {
		TrimMaterial material = new TrimMaterial(trimKey.location().getPath(), trimItem, itemModelIndex, Map.of(), Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(color));
		context.register(trimKey, material);
	}

}