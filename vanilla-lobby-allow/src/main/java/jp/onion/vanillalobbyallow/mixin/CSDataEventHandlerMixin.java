package jp.onion.vanillalobbyallow.mixin;

import jp.onion.vanillalobbyallow.VanillaLobbyAllow;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Creating Space looks up {@code creatingspace:rocket_accessible_dimension}
 * on world load. Paper/vanilla lobbies never sync that datapack registry, so
 * {@code orElseThrow} kicks/crashes the client. Skip until the MOD world has it.
 */
@Pseudo
@Mixin(targets = "com.rae.creatingspace.content.event.DataEventHandler", remap = false)
public abstract class CSDataEventHandlerMixin {

    @Inject(method = "onLoadWorld", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void vanillalobbyallow$skipLoad(CallbackInfo ci) {
        if (!VanillaLobbyAllow.creatingSpaceSynced()) {
            ci.cancel();
        }
    }

    @Inject(method = "onPlayerJoin", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void vanillalobbyallow$skipJoin(CallbackInfo ci) {
        if (!VanillaLobbyAllow.creatingSpaceSynced()) {
            ci.cancel();
        }
    }

    @Inject(method = "onServerStarted", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void vanillalobbyallow$skipStarted(CallbackInfo ci) {
        if (!VanillaLobbyAllow.creatingSpaceSynced()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "getSideAwareRegistry",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void vanillalobbyallow$noMissingRegistry(
            ResourceKey<?> key,
            CallbackInfoReturnable<Registry<?>> cir
    ) {
        if (key != null && !VanillaLobbyAllow.hasClientRegistry(key.location().getNamespace(), key.location().getPath())) {
            cir.setReturnValue(null);
        }
    }
}
