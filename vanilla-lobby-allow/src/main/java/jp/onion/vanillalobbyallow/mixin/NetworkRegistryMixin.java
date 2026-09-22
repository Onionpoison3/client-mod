package jp.onion.vanillalobbyallow.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkInstance;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.ServerStatusPing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Suppress Forge mandatory-channel rejection when the remote endpoint is vanilla/Paper.
 * Proxy-agnostic: Velocity (Ambassador) and BungeeCord (forge_support) both rely on this.
 */
@Mixin(value = NetworkRegistry.class, remap = false)
public abstract class NetworkRegistryMixin {

    @Inject(method = "canConnectToVanillaServer", at = @At("HEAD"), cancellable = true)
    private static void vanillalobbyallow$allowVanillaServer(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "listRejectedVanillaMods", at = @At("HEAD"), cancellable = true)
    private static void vanillalobbyallow$clearRejectedVanillaMods(
            BiFunction<NetworkInstance, String, Boolean> testFunction,
            CallbackInfoReturnable<List<String>> cir
    ) {
        cir.setReturnValue(Collections.emptyList());
    }

    @Inject(method = "validateClientChannels", at = @At("HEAD"), cancellable = true)
    private static void vanillalobbyallow$acceptMissingChannels(
            Map<ResourceLocation, String> channels,
            CallbackInfoReturnable<Map<ResourceLocation, String>> cir
    ) {
        cir.setReturnValue(Collections.emptyMap());
    }

    @Inject(method = "validateServerChannels", at = @At("HEAD"), cancellable = true)
    private static void vanillalobbyallow$acceptClientChannels(
            Map<ResourceLocation, String> channels,
            CallbackInfoReturnable<Map<ResourceLocation, String>> cir
    ) {
        cir.setReturnValue(Collections.emptyMap());
    }

    @Inject(method = "checkListPingCompatibilityForClient", at = @At("HEAD"), cancellable = true)
    private static void vanillalobbyallow$pingOk(
            Map<ResourceLocation, ServerStatusPing.ChannelData> channels,
            CallbackInfoReturnable<Boolean> cir
    ) {
        cir.setReturnValue(true);
    }

    @Inject(method = "getClientNonVanillaNetworkMods", at = @At("HEAD"), cancellable = true)
    private static void vanillalobbyallow$noClientNonVanilla(CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(Collections.emptyList());
    }
}
