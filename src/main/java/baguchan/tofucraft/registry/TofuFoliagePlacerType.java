package baguchan.tofucraft.registry;

import baguchan.tofucraft.TofuCraftReload;
import baguchan.tofucraft.world.gen.foliage.TofuFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = TofuCraftReload.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TofuFoliagePlacerType {
	public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(ForgeRegistries.FOLIAGE_PLACER_TYPES, TofuCraftReload.MODID);


	public static final RegistryObject<FoliagePlacerType<TofuFoliagePlacer>> TOFU_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("tofu_foliage_placer", () -> new FoliagePlacerType<>(TofuFoliagePlacer.CODEC));
}
