package org.icarus.paperwhite.networking;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundDebugSubscriptionRequestPacket;
import net.minecraft.util.debug.DebugSubscription;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerboundDebugSubscriptionRequestPacket.class)
public abstract class MixinServerboundDebugSubscriptionRequestPacket implements Packet<ServerGamePacketListener> {
    @Mutable
    @Final
    @Shadow
    private static StreamCodec<RegistryFriendlyByteBuf, Set<DebugSubscription<?>>> SET_STREAM_CODEC;

    /**
     * Paperwhite - from <a href="https://github.com/PaperMC/Paper/pull/13943/">paper pr 13943</a>
     */
    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void replaceCodec(CallbackInfo ci){
        SET_STREAM_CODEC = ByteBufCodecs.collection(
                    ReferenceOpenHashSet::new,
                    ByteBufCodecs.registry(Registries.DEBUG_SUBSCRIPTION),
                    64);
    }
}
