package baguchan.tofucraft.registry;

import baguchan.tofucraft.world.carver.TofuCanyonCarver;
import baguchan.tofucraft.world.carver.TofuCaveCarver;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "tofucraft", bus = EventBusSubscriber.Bus.MOD)
public class TofuCarvers {
	public static final WorldCarver<ProbabilityConfig> TOFU_CAVE = new TofuCaveCarver(ProbabilityConfig.field_236576_b_, 256);

	public static final WorldCarver<ProbabilityConfig> TOFU_CANYON = new TofuCanyonCarver(ProbabilityConfig.field_236576_b_);

	@SubscribeEvent
	public static void registerCarvers(RegistryEvent.Register<WorldCarver<?>> registry) {
		registry.getRegistry().register(TOFU_CAVE.setRegistryName("tofu_cave"));
		registry.getRegistry().register(TOFU_CANYON.setRegistryName("tofu_canyon"));
	}
}
