package org.icarus.paperwhite.logger;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * L.C.B.
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Unique
    private final org.slf4j.Logger paperwhite$logger = org.slf4j.LoggerFactory.getLogger("Paperwhite");

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void paperwhite$init(final CallbackInfo ci) {
        paperwhite$logger.info("Sisyphe System initialized.");
    }

    @Inject(
        method = "stopServer",
        at = @At("HEAD")
    )
    private void paperwhite$stopServer(final CallbackInfo ci) {
        paperwhite$logger.info("Sisyphe System terminated.");
    }
}
