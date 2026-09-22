package jp.onion.vanillalobbyallow.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Skip Twilight Forest locked-biome toasts when {@code twilight:restrictions}
 * was not synced (Paper/vanilla lobby). Prefer the player level — {@code mc.level}
 * can still be null on the first ticks after join.
 */
@Pseudo
@Mixin(targets = "twilightforest.client.LockedBiomeListener", remap = false)
public abstract class LockedBiomeListenerMixin {

    private static final ResourceKey<? extends Registry<?>> RESTRICTIONS =
            ResourceKey.createRegistryKey(new ResourceLocation("twilight", "restrictions"));

    @Inject(
            method = "clientTick(Lnet/minecraftforge/event/TickEvent$ClientTickEvent;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void vanillalobbyallow$skipWithoutRegistry(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.level() == null) {
            ci.cancel();
            return;
        }
        if (mc.player.level().registryAccess().registry(RESTRICTIONS).isEmpty()) {
            ci.cancel();
        }
    }
}
