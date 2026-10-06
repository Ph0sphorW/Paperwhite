package org.icarus.paperwhite.networking;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kyori.adventure.text.Component;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Paperwhite - prevent removing player twice
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl extends ServerCommonPacketListenerImpl {
    @Shadow
    private boolean waitingForSwitchToConfig;

    public MixinServerGamePacketListenerImpl(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
        super(server, connection, cookie);
    }

    @WrapOperation(
        method = "onDisconnect",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;removePlayerFromWorld(Lnet/kyori/adventure/text/Component;)V"
        )
    )
    private void dontRemovePlayerTwice(ServerGamePacketListenerImpl instance,
                                       Component quitMessage,
                                       Operation<Void> original) {
        if (!this.waitingForSwitchToConfig) original.call(instance, quitMessage);
    }
}
