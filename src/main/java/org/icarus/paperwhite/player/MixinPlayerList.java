package org.icarus.paperwhite.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Paperwhite - fix <a href="https://github.com/PaperMC/Paper/pull/14288/">Paper 14288</a>
 */
@Mixin(PlayerList.class)
public abstract class MixinPlayerList {
    @WrapOperation(
        method = "sendLevelInfo",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;isRaining()Z"
        )
    )
    private boolean fixDesyncOnRespawnedPlayers(ServerLevel instance,
                                                Operation<Boolean> original,
                                                @Local(argsOnly = true) ServerPlayer player) {
        boolean isRainy = original.call(instance);

        if (!isRainy) {
            player.setPlayerWeather(org.bukkit.WeatherType.CLEAR, false);
            player.updateWeather(-1.0F, instance.rainLevel, -1.0F, instance.thunderLevel);
        }

        return isRainy;
    }
}
