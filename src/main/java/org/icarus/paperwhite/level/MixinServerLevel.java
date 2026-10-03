package org.icarus.paperwhite.level;

import ca.spottedleaf.moonrise.patches.chunk_system.level.ChunkSystemLevelReader;
import ca.spottedleaf.moonrise.patches.chunk_system.level.ChunkSystemServerLevel;
import ca.spottedleaf.moonrise.patches.chunk_tick_iteration.ChunkTickServerLevel;
import com.llamalad7.mixinextras.sugar.Local;
import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerEntityGetter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.jspecify.annotations.Nullable;
import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.concurrent.Executor;
import java.util.function.Function;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel extends Level implements ServerEntityGetter, WorldGenLevel, ChunkSystemServerLevel, ChunkSystemLevelReader, ChunkTickServerLevel {

    protected MixinServerLevel(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates, @Nullable ChunkGenerator generator, @Nullable BiomeProvider biomeProvider, World.Environment environment, Function<SpigotWorldConfig, WorldConfiguration> paperWorldConfigCreator, Executor executor) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates, generator, biomeProvider, environment, paperWorldConfigCreator, executor);
    }

    @ModifyArg(
        method = "moonrise$loadChunksAsync(IIIILnet/minecraft/world/level/chunk/status/ChunkStatus;Lca/spottedleaf/concurrentutil/util/Priority;Ljava/util/function/Consumer;Ljava/util/function/Consumer;)V",
        at = @At(
            value = "INVOKE",
            target = "Lca/spottedleaf/moonrise/common/PlatformHooks;scheduleChunkLoad(Lnet/minecraft/server/level/ServerLevel;IILnet/minecraft/world/level/chunk/status/ChunkStatus;ZLca/spottedleaf/concurrentutil/util/Priority;Ljava/util/function/Consumer;)V"
        ),
        index = 3
    )
    private ChunkStatus replaceFullChunkStatus(ChunkStatus originalStatus,
                                           @Local(argsOnly = true) ChunkStatus chunkStatus) {
        return chunkStatus;
    }
}
