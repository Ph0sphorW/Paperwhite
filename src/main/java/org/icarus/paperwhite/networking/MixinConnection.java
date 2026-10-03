package org.icarus.paperwhite.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.SkipPacketDecoderException;
import net.minecraft.network.protocol.Packet;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class MixinConnection extends SimpleChannelInboundHandler<Packet<?>> {
    @Shadow
    private boolean stopReadingPackets;

    @Shadow
    private int receivedPackets;

    @Shadow
    @Final
    protected io.papermc.paper.util.@Nullable IntervalledCounter allPacketCounts;

    @Shadow
    @Final
    protected Object PACKET_LIMIT_LOCK;

    @Shadow
    private void killForPacketSpam() {
        throw new AssertionError();
    }

    @Inject(
        method = "exceptionCaught",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;debug(Ljava/lang/String;Ljava/lang/Throwable;)V",
            ordinal = 1
        )
    )
    private void exceptionCaught(ChannelHandlerContext ctx,
                                 Throwable exception,
                                 CallbackInfo ci) {
        if (exception instanceof SkipPacketDecoderException && !this.stopReadingPackets) {
            this.receivedPackets++;
            if (this.allPacketCounts != null) {
                synchronized (this.PACKET_LIMIT_LOCK) {
                    this.allPacketCounts.updateAndAdd(1, System.nanoTime());
                    if (this.allPacketCounts.getRate() >=
                        io.papermc.paper.configuration.GlobalConfiguration.get().packetLimiter.allPackets.maxPacketRate()) {
                        this.killForPacketSpam();
                    }
                }
            }
        }
    }
}
