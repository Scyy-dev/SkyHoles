package me.scyphers.fruitservers.skyhole.config;

import me.scyphers.fruitservers.skyhole.SkyHole;

public class SimpleConfigManager implements ConfigManager {

    private final SkyHole plugin;

    private final Messenger messenger;

    /**
     * Load all configs in
     * @param plugin the plugin to get Plugin data folder references
     */
    public SimpleConfigManager(SkyHole plugin) {
        this.plugin = plugin;
        this.messenger = new Messenger(this);
    }

    /**
     * Reloads all ConfigFiles registered to this handler
     */
    @Override
    public void reloadConfigs() throws Exception {
        messenger.reloadConfig();
    }

    /**
     * Get the Player Messenger ConfigFile
     * @return the Player Messenger
     */
    public Messenger getPlayerMessenger() {
        return messenger;
    }

    @Override
    public SkyHole getPlugin() {
        return plugin;
    }
}
