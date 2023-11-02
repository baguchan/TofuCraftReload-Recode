package baguchan.tofucraft.client.render.entity;

import baguchan.tofucraft.entity.projectile.FallingTofuEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

/**
 * <p>Revamped Falling Block Renderer.</p>
 * <p>Structure based on <a href=https://github.com/TeamTwilight/twilightforest/blob/1.19.x/src/main/java/twilightforest/client/renderer/entity/ThrownBlockRenderer.java>ThrownBlockRenderer</a></p>
 *
 * @author bagu_chan
 */

public class FallingTofuRenderer extends EntityRenderer<FallingTofuEntity> {

	public FallingTofuRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.5F;
	}

	@Override
	public void render(FallingTofuEntity entity, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
		BlockState blockstate = entity.getBlockState();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level world = entity.level();
			if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				ms.pushPose();
				BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				ms.translate(-0.5D, 0.0D, -0.5D);
				BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
				var model = dispatcher.getBlockModel(blockstate);
				for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entity.blockPosition())), ModelData.EMPTY))
					dispatcher.getModelRenderer().tesselateBlock(world, model, blockstate, blockpos, ms, buffers.getBuffer(renderType), false, RandomSource.create(), 0L, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
				ms.popPose();
				super.render(entity, yaw, partialTicks, ms, buffers, light);
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(FallingTofuEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}