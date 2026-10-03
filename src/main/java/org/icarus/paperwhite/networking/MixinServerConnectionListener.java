package org.icarus.paperwhite.networking;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerConnectionListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ServerConnectionListener.class)
public class MixinServerConnectionListener {
    @Shadow
    @Final
    List<Connection> connections;

    @Unique
    public void paperwhite$handleAllDisconnections() {
        synchronized (this.connections) {
            for (Connection connection : this.connections) {
                connection.channel.close().awaitUninterruptibly();
                connection.handleDisconnection();
            }
        }
    }
}
