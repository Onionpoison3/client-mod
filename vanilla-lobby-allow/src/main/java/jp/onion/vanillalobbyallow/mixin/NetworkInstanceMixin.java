package jp.onion.vanillalobbyallow.mixin;

import net.minecraftforge.network.NetworkInstance;
import net.minecraftforge.network.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Always accept the remote protocol version on the client, including ACCEPTVANILLA / ABSENT.
 */
@Mixin(value = NetworkInstance.class, remap = false)
public abstract class NetworkInstanceMixin {

    @Inject(method = "tryServerVersionOnClient", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$acceptAnyServerVersion(String version, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
