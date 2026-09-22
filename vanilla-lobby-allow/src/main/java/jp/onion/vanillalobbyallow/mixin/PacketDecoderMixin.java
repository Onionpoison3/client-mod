package jp.onion.vanillalobbyallow.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * BungeeCord/Paper recipe packets often have leftover bytes after a Forge client
 * reads a shorter vanilla payload. Skip leftovers instead of kicking.
 */
@Mixin(PacketDecoder.class)
public abstract class PacketDecoderMixin {

    @Redirect(
            method = "decode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/FriendlyByteBuf;readableBytes()I"
            ),
            require = 0
    )
    private int vanillalobbyallow$dropExtraBytes(FriendlyByteBuf buf) {
        int leftover = buf.readableBytes();
        if (leftover > 0) {
            buf.skipBytes(leftover);
        }
        return 0;
    }
}
