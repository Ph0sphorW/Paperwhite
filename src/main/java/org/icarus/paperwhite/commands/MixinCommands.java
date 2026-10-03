package org.icarus.paperwhite.commands;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.UnaryOperator;

import static net.minecraft.util.StringUtil.filterText;

@Mixin(Commands.class)
public class MixinCommands {
    @ModifyArg(
        method = "finishParsing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Ljava/util/function/UnaryOperator;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 0
        ),
        index = 0
    )
    private static UnaryOperator<Style> filterOutDisallowedChars(UnaryOperator<Style> modifyFunc, @Local(argsOnly = true) String command) {
        return style -> style.withClickEvent(new ClickEvent.SuggestCommand("/" + filterText(command)));
    }
}
