package org.rhm.milkable_blazes.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void milkable_blazes$saveAdditionalHandler(CompoundTag compoundTag, CallbackInfo ci) {

    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void milkable_blazes$readAdditionalHandler(CompoundTag compoundTag, CallbackInfo ci) {

    }
}
