package jp.onion.vanillalobbyallow.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mods that add RecipeBookType entries make Forge read extra booleans from
 * ClientboundLogin / recipe-book packets. Vanilla/Paper only send the four
 * base types, so the client DecoderException-kicks itself. Skip unread bytes.
 */
@Mixin(RecipeBookSettings.class)
public abstract class RecipeBookSettingsMixin {

    @Inject(
            method = "read(Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/stats/RecipeBookSettings;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void vanillalobbyallow$safeRead(
            FriendlyByteBuf buf,
            CallbackInfoReturnable<RecipeBookSettings> cir
    ) {
        RecipeBookSettings settings = new RecipeBookSettings();
        for (RecipeBookType type : RecipeBookType.values()) {
            if (buf.readableBytes() < 2) {
                break;
            }
            settings.setOpen(type, buf.readBoolean());
            settings.setFiltering(type, buf.readBoolean());
        }
        cir.setReturnValue(settings);
    }
}
