package jp.onion.vanillalobbyallow.mixin;

import jp.onion.vanillalobbyallow.VanillaLobbyAllow;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Paper/vanilla packets are often shorter than what a heavily-modded Forge client expects
 * (extra RecipeBookType flags, forge attributes, etc.). When the buffer is exhausted,
 * return safe defaults instead of DecoderException / IndexOutOfBoundsException.
 */
@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {

    private static boolean exhausted(Object self) {
        return ((FriendlyByteBuf) self).readableBytes() <= 0;
    }

    @Inject(method = "readBoolean", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$bool(CallbackInfoReturnable<Boolean> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "readByte", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$byte(CallbackInfoReturnable<Byte> cir) {
        if (exhausted(this)) {
            cir.setReturnValue((byte) 0);
        }
    }

    @Inject(method = "readUnsignedByte", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$ubyte(CallbackInfoReturnable<Short> cir) {
        if (exhausted(this)) {
            cir.setReturnValue((short) 0);
        }
    }

    @Inject(method = "readShort", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$short(CallbackInfoReturnable<Short> cir) {
        if (exhausted(this)) {
            cir.setReturnValue((short) 0);
        }
    }

    @Inject(method = "readInt", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$int(CallbackInfoReturnable<Integer> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "readLong", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$long(CallbackInfoReturnable<Long> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(0L);
        }
    }

    @Inject(method = "readFloat", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$float(CallbackInfoReturnable<Float> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(0f);
        }
    }

    @Inject(method = "readDouble", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$double(CallbackInfoReturnable<Double> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(0d);
        }
    }

    @Inject(method = "readVarInt", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$varInt(CallbackInfoReturnable<Integer> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "readVarLong", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$varLong(CallbackInfoReturnable<Long> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(0L);
        }
    }

    @Inject(method = "readNbt()Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$nbt(CallbackInfoReturnable<CompoundTag> cir) {
        if (exhausted(this)) {
            cir.setReturnValue(null);
        }
    }

    /**
     * Paper/BungeeCord recipe and plugin-channel packets can contain identifiers with
     * control characters (e.g. {@code 01234\u0006@01235}). Vanilla throws
     * ResourceLocationException and Netty disconnects. Sanitize instead.
     */
    @Inject(method = "readResourceLocation", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$resourceLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        FriendlyByteBuf self = (FriendlyByteBuf) (Object) this;
        if (exhausted(self)) {
            cir.setReturnValue(VanillaLobbyAllow.DUMMY_LOCATION);
            return;
        }
        String raw;
        try {
            raw = self.readUtf(32767);
        } catch (RuntimeException ex) {
            cir.setReturnValue(VanillaLobbyAllow.DUMMY_LOCATION);
            return;
        }
        cir.setReturnValue(VanillaLobbyAllow.sanitizeLocation(raw));
    }
}
