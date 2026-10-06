package org.icarus.paperwhite.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ExperienceOrb;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Paperwhite - fix mending works incorrectly on items have "max_damage" DataComponent
 */
@Mixin(CraftPlayer.class)
public abstract class MixinCraftPlayer {
    @ModifyExpressionValue(
        method = "applyMending",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/component/DataComponentMap;has(Lnet/minecraft/core/component/DataComponentType;)Z"
        )
    )
    private boolean removeDataComponentCheck(boolean original){
        return true;
    }

    @Inject(
        method = "applyMending",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ExperienceOrb;setValue(I)V"
        ),
        cancellable = true
    )
    private void checkExperienceOrb(int amount,
                                    CallbackInfoReturnable<Integer> cir,
                                    @Local(name = "orb") ExperienceOrb orb){
        if (orb == null) cir.setReturnValue(amount);
    }
}
