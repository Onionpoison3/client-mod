package jp.onion.vanillalobbyallow;

import com.mojang.logging.LogUtils;
import io.netty.handler.codec.DecoderException;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.util.Locale;

@Mod(VanillaLobbyAllow.MOD_ID)
public final class VanillaLobbyAllow {
    public static final String MOD_ID = "vanillalobbyallow";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation DUMMY_LOCATION = new ResourceLocation("minecraft", "air");

    public VanillaLobbyAllow() {
        if (FMLEnvironment.dist != Dist.CLIENT) {
            LOGGER.warn("Vanilla Lobby Allow is client-only; ignore on dedicated server.");
            return;
        }
        LOGGER.info(
                "Vanilla Lobby Allow enabled: Forge clients may join vanilla/Paper lobbies "
                        + "behind Velocity or BungeeCord (invalid ResourceLocations, recipe packets, "
                        + "and missing Create-addon datapack registries ignored)."
        );
    }

    public static boolean hasClientRegistry(String namespace, String path) {
        return hasClientRegistry(ResourceKey.createRegistryKey(new ResourceLocation(namespace, path)));
    }

    public static boolean hasClientRegistry(ResourceKey<? extends Registry<?>> key) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.level() == null) {
            return false;
        }
        return mc.player.level().registryAccess().registry(key).isPresent();
    }

    public static boolean creatingSpaceSynced() {
        return hasClientRegistry("creatingspace", "rocket_accessible_dimension");
    }

    public static ResourceLocation sanitizeLocation(String raw) {
        if (raw == null || raw.isEmpty()) {
            return DUMMY_LOCATION;
        }
        ResourceLocation parsed = ResourceLocation.tryParse(raw);
        if (parsed != null) {
            return parsed;
        }
        String lower = raw.toLowerCase(Locale.ROOT);
        int colon = lower.indexOf(':');
        String namespace;
        String path;
        if (colon >= 0) {
            namespace = lower.substring(0, colon);
            path = lower.substring(colon + 1);
        } else {
            namespace = "minecraft";
            path = lower;
        }
        namespace = namespace.replaceAll("[^a-z0-9._-]", "_");
        path = path.replaceAll("[^a-z0-9/._-]", "_");
        if (namespace.isEmpty()) {
            namespace = "minecraft";
        }
        if (path.isEmpty()) {
            path = "invalid";
        }
        parsed = ResourceLocation.tryParse(namespace + ":" + path);
        return parsed != null ? parsed : DUMMY_LOCATION;
    }

    public static boolean isIgnorableNetworkError(Throwable thrown) {
        for (Throwable t = thrown; t != null; t = t.getCause()) {
            if (t instanceof ResourceLocationException) {
                return true;
            }
            if (t instanceof IndexOutOfBoundsException) {
                return true;
            }
            if (t instanceof DecoderException) {
                String msg = t.getMessage();
                if (msg != null && (msg.contains("character in path of location")
                        || msg.contains("was larger than I expected")
                        || msg.contains("smaller than I expected"))) {
                    return true;
                }
            }
            String msg = t.getMessage();
            if (msg != null && msg.contains("character in path of location")) {
                return true;
            }
        }
        return false;
    }
}
