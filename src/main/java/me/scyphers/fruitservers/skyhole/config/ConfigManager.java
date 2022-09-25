package me.scyphers.fruitservers.skyhole.config;

import me.scyphers.fruitservers.skyhole.SkyHole;

public interface ConfigManager {

    /**
     * Reloads all configs this manager is responsible for
     */
    void reloadConfigs() throws Exception;

    SkyHole getPlugin();

}
