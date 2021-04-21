package baguchan.tofucraft.registry;

import baguchan.tofucraft.world.gen.structure.TofuVillageStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "tofucraft", bus = EventBusSubscriber.Bus.MOD)
public class TofuStructures {
	public static final Structure<VillageConfig> TOFUVILLAGE = (Structure<VillageConfig>) new TofuVillageStructure(VillageConfig.field_236533_a_);

	public static <F extends Structure<?>> void putStructureOnAList(String name, F structure) {
		Structure.field_236365_a_.put(name, structure);
	}

	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<Structure<?>> registry) {
		registry.getRegistry().register(TOFUVILLAGE.setRegistryName("tofucraft:tofu_village"));
		putStructureOnAList("tofucraft:tofu_village", TOFUVILLAGE);
	}
}
