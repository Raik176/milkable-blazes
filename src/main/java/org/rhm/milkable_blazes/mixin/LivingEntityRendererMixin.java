package org.rhm.milkable_blazes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import org.rhm.milkable_blazes.BlazeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    @Shadow public abstract M getModel();

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void beforeRender(T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (livingEntity instanceof Blaze blaze) {
            BlazeAccess access = (BlazeAccess) blaze;
            BlazeModelAccessor modelAccessor = (BlazeModelAccessor) this.getModel();
            if (access.milkable_blazes$isShorn()) {
                int i1 = 0;
                for (ModelPart upperBodyPart : modelAccessor.getUpperBodyParts()) {
                    upperBodyPart.visible = (access.milkable_blazes$getMissingRods() & (1 << i1)) != 0;;
                    i1++;
                }
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    private void afterRender(T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (livingEntity instanceof Blaze blaze) {
            BlazeAccess access = (BlazeAccess) blaze;
            BlazeModelAccessor modelAccessor = (BlazeModelAccessor) this.getModel();
            if (access.milkable_blazes$isShorn()) {
                for (ModelPart upperBodyPart : modelAccessor.getUpperBodyParts())
                    upperBodyPart.visible = true;
            }
        }
    }
}
