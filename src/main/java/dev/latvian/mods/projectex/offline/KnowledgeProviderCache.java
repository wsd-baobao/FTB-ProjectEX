package dev.latvian.mods.projectex.offline;

import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum KnowledgeProviderCache {

    INSTANCE;

    private final Map<UUID, OneTickProvider> providerMap = new HashMap<>();

    public static KnowledgeProviderCache getInstance() {
        return INSTANCE;
    }

    public IKnowledgeProvider getCachedProvider(Level level, UUID id) {
        OneTickProvider otp = providerMap.get(id);
        if (otp == null || otp.timestamp < level.getGameTime()) {
            OneTickProvider newOtp = new OneTickProvider(level.getGameTime(), new WeakReference<>(OfflineKnowledgeManager.getInstance().getKnowledgeProviderFor(level, id)));
            providerMap.put(id, newOtp);
            return newOtp.getProvider().get();
        } else {
            return otp.getProvider().get();
        }
    }

    public static class OneTickProvider {
        private final long timestamp;
        private final WeakReference<IKnowledgeProvider> provider;

        public OneTickProvider(long timestamp, WeakReference<IKnowledgeProvider> provider) {
            this.timestamp = timestamp;
            this.provider = provider;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public WeakReference<IKnowledgeProvider> getProvider() {
            return provider;
        }


    }
}
