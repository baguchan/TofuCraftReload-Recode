package baguchan.tofucraft.registry;

import baguchan.tofucraft.TofuCraftReload;
import baguchan.tofucraft.loot.LootInLootModifier;
import baguchan.tofucraft.loot.SeedDropModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TofuLootModifiers {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TofuCraftReload.MODID);

	public static final RegistryObject<Codec<SeedDropModifier>> ADD_ITEM = LOOT_MODIFIERS.register("seed_drops", SeedDropModifier.CODEC);
	public static final RegistryObject<Codec<LootInLootModifier>> LOOT_IN_LOOT_MODIFIER = LOOT_MODIFIERS.register("loot_in_loot", LootInLootModifier.CODEC);
}