package org.icarus.paperwhite.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.sensing.SecondaryPoiSensor;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SecondaryPoiSensor.class)
public abstract class MixinSecondaryPoiSensor {
    @Inject(
        method = "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/villager/Villager;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void doTick(ServerLevel level, Villager entity, CallbackInfo ci) {
        if (entity.getVillagerData().profession().value().secondaryPoi().isEmpty()) ci.cancel();
    }
}
