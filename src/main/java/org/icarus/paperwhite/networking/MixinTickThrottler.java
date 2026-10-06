package org.icarus.paperwhite.networking;

import net.minecraft.util.TickThrottler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Paperwhite - fix tick throttler worked incorrectly
 */
@Mixin(TickThrottler.class)
public abstract class MixinTickThrottler {
    @Inject(
        method = "isIncrementAndUnderThreshold(II)Z",
        at = @At("TAIL"),
        cancellable = true
    )
    private void isIncrementAndUnderThreshold(int incrementStep,
                                              int threshold,
                                              CallbackInfoReturnable<Boolean> cir) {
        if (threshold <= 0) cir.setReturnValue(true);
    }
}
