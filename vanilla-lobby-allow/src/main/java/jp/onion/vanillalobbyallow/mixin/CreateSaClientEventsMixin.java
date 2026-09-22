package jp.onion.vanillalobbyallow.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Create Stuff Additions (MCreator) first-person jetpack/exoskeleton ticks.
 * Skip until a player/level exists so Paper lobby join cannot NPE.
 */
@Pseudo
@Mixin(targets = "net.mcreator.createstuffadditions.events.ClientEvents", remap = false)
public abstract class CreateSaClientEventsMixin {

    @Inject(
            method = "onTick",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void vanillalobbyallow$skipUntilWorld(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            ci.cancel();
        }
    }
}
