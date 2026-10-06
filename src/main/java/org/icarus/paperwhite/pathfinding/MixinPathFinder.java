package org.icarus.paperwhite.pathfinding;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Comparator;

/**
 * Paperwhite - fix inverted pathfinding
 */
@Mixin(PathFinder.class)
public abstract class MixinPathFinder {
    @ModifyVariable(
        method = "findPath(Lnet/minecraft/world/level/pathfinder/Node;Ljava/util/List;FIF)Lnet/minecraft/world/level/pathfinder/Path;",
        at = @At("STORE"),
        name = "comparator")
    private Comparator<Path> modifyComparator(
        Comparator<Path> comparator,
        @Local(name = "entryListIsEmpty") boolean entryListIsEmpty
    ) {
        return entryListIsEmpty
            ? Comparator.comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount)
            : Comparator.comparingInt(Path::getNodeCount);
    }
}
