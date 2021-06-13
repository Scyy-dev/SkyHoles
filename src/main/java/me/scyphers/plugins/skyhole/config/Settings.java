package me.scyphers.plugins.skyhole.config;

public class Settings extends ConfigFile {

    /**
     * Create a ConfigFile for the default 'config.yml' file
     * Intended to be used as a read-only file, it is highly recommended that
     *  {@link org.bukkit.configuration.file.YamlConfiguration#set(String, Object)} is not used on this file as comments will be overwritten
     */
    public Settings(ConfigManager manager) {
        super(manager,"config.yml", true);
    }
}
