package jp.onion.vanillalobbyallow.mixin;

import jp.onion.vanillalobbyallow.VanillaLobbyAllow;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Gravity mixins call into Creating Space for every entity tick. On a Paper
 * lobby the planet map was never filled — treat gravity as vanilla.
 */
@Pseudo
@Mixin(targets = "com.rae.creatingspace.content.planets.CSDimensionUtil", remap = false)
public abstract class CSDimensionUtilMixin {

    @Inject(
            method = "shouldHandleGravity(Lnet/minecraft/resources/ResourceLocation;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void vanillalobbyallow$vanillaGravity(
            ResourceLocation dimension,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!VanillaLobbyAllow.creatingSpaceSynced()) {
            cir.setReturnValue(false);
        }
    }
}
