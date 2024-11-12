package org.rhm.milkable_blazes.mixin;

import org.rhm.milkable_blazes.BlazeAccess;
import org.rhm.milkable_blazes.MilkableBlazesModCommon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blaze.class)
public abstract class BlazeMixin extends MobMixin implements Shearable, BlazeAccess {
    @Unique
    private static final EntityDataAccessor<Boolean> milkable_blazes$SHORN = SynchedEntityData.defineId(Blaze.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Integer> milkable_blazes$MISSING_RODS = SynchedEntityData.defineId(Blaze.class, EntityDataSerializers.INT);


    protected BlazeMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.entityData.set(milkable_blazes$SHORN, false);
        this.entityData.set(milkable_blazes$MISSING_RODS, 0);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    //? if <=1.20.1 {
    /*protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(milkable_blazes$SHORN, false);
        this.entityData.define(milkable_blazes$MISSING_RODS, 0);
    }
    *///?} else {
    protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(milkable_blazes$SHORN, false);
        builder.define(milkable_blazes$MISSING_RODS, 0);
    }
    //?}

    @Override
    public void shear(SoundSource soundSource) {
        level().playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        this.entityData.set(milkable_blazes$SHORN, true);
        milkable_blazes$removeAttackGoal();
    }

    @Override
    public boolean readyForShearing() {
        return !milkable_blazes$isShorn();
    }

    @Override
    protected void milkable_blazes$saveAdditionalHandler(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("Shorn", this.entityData.get(milkable_blazes$SHORN));
        compoundTag.putInt("MissingRods", this.entityData.get(milkable_blazes$MISSING_RODS));
    }

    @Override
    protected void milkable_blazes$readAdditionalHandler(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("Shorn")) {
            boolean shorn = compoundTag.getBoolean("Shorn");
            this.entityData.set(milkable_blazes$SHORN, shorn);
            if (shorn)
                milkable_blazes$removeAttackGoal();
        }
        if (compoundTag.contains("MissingRods")) {
            this.entityData.set(milkable_blazes$MISSING_RODS, compoundTag.getInt("MissingRods"));
        }
    }

    @Override
    protected void milkable_blazes$interactHandler(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack item = player.getItemInHand(interactionHand);
        if ((item.is(MilkableBlazesModCommon.SHEAR_TAG) || item.is(MilkableBlazesModCommon.SHEARS_TAG) || item.is(Items.SHEARS)) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.level().gameEvent(player, GameEvent.SHEAR, position());

            if (!this.level().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) this.level();
                if (item.isDamageableItem()) {
                    //? if <=1.20.1 {
					/*item.hurt(
							1,
							serverWorld.getRandom(),
							(ServerPlayer) player
					);
					*///?} else {
                    item.hurtAndBreak(
                            1,
                            //? if >1.21 {
                            /*serverWorld,
                             *///?} elif >1.20.1
                            serverWorld.getRandom(),
                            (ServerPlayer) player,
                            (/*? if >=1.21 {*//*plr*//*?}*/) -> {}
                    );
                    //?}
                }
                int rodsLost = serverWorld.getRandom().nextInt(1,4);
                int bits = 0;
                int total = 0;
                for (int i = 0; i < 12; i++) {
                    if (level().getRandom().nextFloat() > 0.5) {
                        bits |= (1 << i);
                        total++;
                    }
                    if (total > rodsLost) break;
                }
                this.entityData.set(milkable_blazes$MISSING_RODS, bits);
                //TODO: maybe no hardcoding the item
                for (int i = 0; i < rodsLost; i++) {
                    Containers.dropItemStack(
                            serverWorld,
                            blockPosition().getX(),
                            blockPosition().getY(),
                            blockPosition().getZ(),
                            Items.BLAZE_ROD.getDefaultInstance()
                    );
                }
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
        } else if (item.is(MilkableBlazesModCommon.EMPTY_BUCKET_TAG) || item.is(Items.BUCKET)) {
            ItemStack itemStack2 = ItemUtils.createFilledResult(item, player, Items.LAVA_BUCKET.getDefaultInstance());
            player.setItemInHand(interactionHand, itemStack2);
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
        }
    }

    @Override
    public boolean milkable_blazes$isShorn() {
        return this.entityData.get(milkable_blazes$SHORN);
    }

    @Override
    public int milkable_blazes$getMissingRods() {
        return this.entityData.get(milkable_blazes$MISSING_RODS);
    }

    @Unique
    private void milkable_blazes$removeAttackGoal() {
        for (WrappedGoal availableGoal : this.goalSelector.getAvailableGoals()) {
            if (availableGoal.getGoal() instanceof Blaze.BlazeAttackGoal) {
                this.goalSelector.removeGoal(availableGoal.getGoal());
                break;
            }
        }
    }
}