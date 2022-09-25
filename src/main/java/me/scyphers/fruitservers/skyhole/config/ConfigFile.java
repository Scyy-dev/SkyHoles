package me.scyphers.fruitservers.skyhole.config;

import com.google.common.base.Charsets;
import me.scyphers.fruitservers.skyhole.SkyHole;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ConfigFile {

    /**
     * The main plugin instance
     */
    protected final SkyHole plugin;

    /**
     * The interactable configuration for getting and setting of objects
     */
    protected YamlConfiguration config;

    /**
     * The file for the configuration
     */
    protected final File configFile;

    /**
     * File path of the config file from the Plugins data folder
     */
    protected final String configFilePath;

    /**
     * The manager for this config file
     */
    protected final ConfigManager manager;

    /**
     * Attaches the configuration getter/setter to the File specified at {@code configFilePath} or if the file is not found
     * Loads one from the plugin files
     * @param configFilePath path to the file from this plugins Data Folder
     */
    public ConfigFile(ConfigManager manager, String configFilePath, boolean fromResourceFile) {

        // Save the manager reference
        this.manager = manager;

        this.plugin = manager.getPlugin();

        // Save the message file path
        this.configFilePath = configFilePath;

        // Save the messages file
        this.configFile = new File(plugin.getDataFolder(), configFilePath);

        // Check if the file exists
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            if (fromResourceFile) {
                plugin.saveResource(configFilePath, false);
            } else {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Create the yml reference
        this.config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Reloads config by reading updating the reference to the file
     */
    public void reloadConfig() throws Exception {
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream defIMessagesStream = plugin.getResource(configFilePath);
        if (defIMessagesStream != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defIMessagesStream, Charsets.UTF_8)));
        }
    }

    /**
     * Gets the configuration getter/setter
     * @return the configuration getter/setter
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Gets the File for this ConfigFile
     * @return the File
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * Gets the path to the File (including the file) from the plugins data folder
     * @return the path
     */
    public String getConfigFilePath() {
        return configFilePath;
    }

    /**
     * Sets the configuration getter/setter. Required so that the file can be reloaded
     * @param config the configuration getter/setter
     */
    public void setConfig(YamlConfiguration config) {
        this.config = config;
    }
}
