package me.scyphers.plugins.skyhole.config;

import me.scyphers.plugins.skyhole.SkyHole;

public class SimpleConfigManager implements ConfigManager {

    private final SkyHole plugin;

    private final Messenger messenger;
    private final Settings settings;

    /**
     * Load all configs in
     * @param plugin the plugin to get Plugin data folder references
     */
    public SimpleConfigManager(SkyHole plugin) {
        this.plugin = plugin;
        this.messenger = new Messenger(this);
        this.settings = new Settings(this);
    }

    /**
     * Reloads all ConfigFiles registered to this handler
     */
    @Override
    public void reloadConfigs() throws Exception {
        messenger.reloadConfig();
        settings.reloadConfig();
    }

    /**
     * Get the Player Messenger ConfigFile
     * @return the Player Messenger
     */
    public Messenger getPlayerMessenger() {
        return messenger;
    }

    /**
     * Get the default Settings ConfigFile
     * @return the Settings
     */
    public Settings getSettings() {
        return settings;
    }

    @Override
    public SkyHole getPlugin() {
        return plugin;
    }
}
