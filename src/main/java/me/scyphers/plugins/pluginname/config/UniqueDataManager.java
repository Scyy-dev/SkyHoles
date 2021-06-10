package me.scyphers.plugins.pluginname.config;

import me.scyphers.plugins.pluginname.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UniqueDataManager implements ConfigManager {

    private final Map<UUID, UniqueDataFile> playerData;

    private final Plugin plugin;

    public UniqueDataManager(Plugin plugin) {
        this.plugin = plugin;
        this.playerData = new HashMap<>();
    }

    public void loadPlayerData(UUID uuid) {
        if (playerData.containsKey(uuid)) return;
        playerData.put(uuid, new UniqueDataFile(this, uuid));
    }

    public void unloadPlayerData(UUID uuid) {
        if (!playerData.containsKey(uuid)) return;
        playerData.remove(uuid);
    }

    public UniqueDataFile getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    @Override
    public void reloadConfigs() throws Exception {
        for (UUID uuid : playerData.keySet()) {
            playerData.get(uuid).reloadConfig();
        }
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
