package com.memegod.thestalker.client;

import com.memegod.thestalker.TheStalkerMod;
import com.memegod.thestalker.registry.ModEntities;
import com.memegod.thestalker.entity.StalkerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheStalkerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.STALKER.get(), StalkerRenderer::new);
    }

    public static final class StalkerRenderer extends MobRenderer<StalkerEntity, HumanoidModel<StalkerEntity>> {
        private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/zombie/zombie.png");

        public StalkerRenderer(EntityRendererProvider.Context context) {
            super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.6F);
        }

        @Override
        public ResourceLocation getTextureLocation(StalkerEntity entity) {
            return TEXTURE;
        }

        @Override
        protected void scale(StalkerEntity entity, com.mojang.blaze3d.vertex.PoseStack poseStack, float partialTick) {
            poseStack.scale(1.45F, 1.45F, 1.45F);
        }
    }
}
