package jp.onion.vanillalobbyallow.mixin;

import io.netty.channel.ChannelHandlerContext;
import jp.onion.vanillalobbyallow.VanillaLobbyAllow;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Last resort: if a decode still throws ResourceLocationException (recipe book,
 * plugin channels from BungeeCord), keep the connection instead of showing
 * "接続を維持できません".
 */
@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void vanillalobbyallow$keepConnected(
            ChannelHandlerContext ctx,
            Throwable thrown,
            CallbackInfo ci
    ) {
        if (VanillaLobbyAllow.isIgnorableNetworkError(thrown)) {
            VanillaLobbyAllow.LOGGER.warn(
                    "Vanilla Lobby Allow ignored a network decode error instead of disconnecting: {}",
                    thrown.toString()
            );
            ci.cancel();
        }
    }
}
