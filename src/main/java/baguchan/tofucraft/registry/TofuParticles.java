package baguchan.tofucraft.registry;

import baguchan.tofucraft.TofuCraftReload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = TofuCraftReload.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TofuParticles {
	public static final BasicParticleType TOFU_PORTAL = new BasicParticleType(false);

	public static final BasicParticleType ZUNDA_SPORE = new BasicParticleType(false);

	@SubscribeEvent
	public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
		event.getRegistry().register(TOFU_PORTAL.setRegistryName("tofu_portal"));
		event.getRegistry().register(ZUNDA_SPORE.setRegistryName("zunda_spore"));
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
		ParticleManager particleManager = (Minecraft.getInstance()).particleEngine;
		particleManager.register(TOFU_PORTAL, baguchan.tofucraft.client.particle.TofuPortalParticle.Factory::new);
		particleManager.register(ZUNDA_SPORE, baguchan.tofucraft.client.particle.ZundaSporeParticle.ZundaSporeFactory::new);
	}
}