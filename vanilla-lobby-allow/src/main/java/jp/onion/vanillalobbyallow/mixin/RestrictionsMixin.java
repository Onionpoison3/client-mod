package jp.onion.vanillalobbyallow.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Paper/vanilla lobbies do not sync Twilight Forest's datapack registry
 * {@code twilight:restrictions}. TF's {@code getRestrictionForBiome} calls
 * {@code registryOrThrow} and crashes; return empty instead when absent.
 */
@Pseudo
@Mixin(targets = "twilightforest.init.custom.Restrictions", remap = false)
public abstract class RestrictionsMixin {

    private static final ResourceKey<? extends Registry<?>> RESTRICTIONS =
            ResourceKey.createRegistryKey(new ResourceLocation("twilight", "restrictions"));

    @Inject(
            method = "getRestrictionForBiome(Lnet/minecraft/world/level/biome/Biome;Lnet/minecraft/world/entity/Entity;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void vanillalobbyallow$noRegistry(
            Biome biome,
            Entity entity,
            CallbackInfoReturnable<Optional<?>> cir
    ) {
        if (entity == null || entity.level() == null) {
            cir.setReturnValue(Optional.empty());
            return;
        }
        if (entity.level().registryAccess().registry(RESTRICTIONS).isEmpty()) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
