package org.icarus.paperwhite.networking;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerConnectionListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

/**
 * Paperwhite - from <a href="https://github.com/PaperMC/Paper/pull/13943/">paper pr 13943</a>
 */
@Mixin(ServerConnectionListener.class)
public abstract class MixinServerConnectionListener {
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
