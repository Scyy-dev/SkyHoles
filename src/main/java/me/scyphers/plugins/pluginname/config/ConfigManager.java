package me.scyphers.plugins.pluginname.config;

import me.scyphers.plugins.pluginname.Plugin;

/**
 * Manager for a collection of config files. Recommended to provide methods for getting each of the ConfigFiles it manages
 */
public interface ConfigManager {

    /**
     * Reloads all configs this manager is responsible for
     */
    void reloadConfigs() throws Exception;

    Plugin getPlugin();

}
