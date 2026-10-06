package org.icarus.paperwhite.entity;

import org.bukkit.event.entity.EntityTargetEvent;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Paperwhite - fix bee's incorrect angry reason
 */
@Mixin(targets = "net.minecraft.world.entity.animal.bee.Bee$BeeHurtByOtherGoal")
public abstract class MixinBee {
    @ModifyArg(
        method = "alertOther",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;setTarget(Lnet/minecraft/world/entity/LivingEntity;Lorg/bukkit/event/entity/EntityTargetEvent$TargetReason;)Z"
        ),
        index = 1
    )
    private EntityTargetEvent.TargetReason modifyReason(EntityTargetEvent.@Nullable TargetReason reason){
        return EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY;
    }
}
