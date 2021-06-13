package me.scyphers.plugins.skyhole.config;

import me.scyphers.plugins.skyhole.SkyHole;

/**
 * Manager for a collection of config files. Recommended to provide methods for getting each of the ConfigFiles it manages
 */
public interface ConfigManager {

    /**
     * Reloads all configs this manager is responsible for
     */
    void reloadConfigs() throws Exception;

    SkyHole getPlugin();

}
