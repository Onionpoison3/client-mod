package jp.onion.vanillalobbyallow.mixin;

import jp.onion.vanillalobbyallow.VanillaLobbyAllow;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.rae.creatingspace.content.event.CSClientEvent", remap = false)
public abstract class CSClientEventMixin {

    @Inject(
            method = "onTick",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void vanillalobbyallow$skipTick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.level() == null || !VanillaLobbyAllow.creatingSpaceSynced()) {
            ci.cancel();
        }
    }
}
