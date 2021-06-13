package me.scyphers.plugins.skyhole;

import me.scyphers.plugins.skyhole.command.AdminCommand;
import me.scyphers.plugins.skyhole.config.SimpleConfigManager;
import me.scyphers.plugins.skyhole.config.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class SkyHole extends JavaPlugin {

    private SimpleConfigManager configManager;

    @Override
    public void onEnable() {

        // Register the Config Manager
        this.configManager = new SimpleConfigManager(this);

        // Register the Admin Command
        AdminCommand adminCommand = new AdminCommand(this);
        this.getCommand("admin").setExecutor(adminCommand);
        this.getCommand("admin").setTabCompleter(adminCommand);



    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /**
     * Reload all configs registered by the {@link SimpleConfigManager} for this plugin
     * @param sender Output for messages
     */
    public void reload(CommandSender sender) {
        try {
            sender.sendMessage("Reloading...");
            configManager.reloadConfigs();
            sender.sendMessage("Successfully reloaded!");
        } catch (Exception e) {
            sender.sendMessage("Error reloading! Check console for logs!");
            e.printStackTrace();
        }
    }

    /**
     * Get the Settings for this plugin, each defined in config.yml
     * @return the Settings
     */
    public Settings getSettings() {
        return configManager.getSettings();
    }

    /**
     * Provides a bit of information about the plugin
     * @return the splash text
     */
    public List<String> getSplashText() {
        StringBuilder authors = new StringBuilder();
        for (String author : this.getDescription().getAuthors()) {
            authors.append(author).append(", ");
        }
        authors.delete(authors.length() - 1, authors.length());
        return Arrays.asList(
                "PLUGIN_NAME v" + this.getDescription().getVersion(),
                "Built by" + authors.toString()
        );
    }

    /**
     * @return the Config Manager
     */
    public SimpleConfigManager getConfigManager() {
        return configManager;
    }
}
