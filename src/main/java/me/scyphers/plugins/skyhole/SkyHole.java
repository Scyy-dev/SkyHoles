package me.scyphers.plugins.skyhole;

import me.scyphers.plugins.skyhole.command.AdminCommand;
import me.scyphers.plugins.skyhole.config.SimpleConfigManager;
import me.scyphers.plugins.skyhole.config.Settings;
import me.scyphers.plugins.skyhole.external.WorldGuardManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class SkyHole extends JavaPlugin {

    // External plugins
    private WorldGuardManager worldGuardManager;

    // Config
    private SimpleConfigManager configManager;

    @Override
    public void onEnable() {

        // Load WorldGuard
        this.worldGuardManager = new WorldGuardManager(this);
        if (!worldGuardManager.isPluginLoaded()) {
            getLogger().severe("WorldGuard hooks failed! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Register the Config Manager
        configManager = new SimpleConfigManager(this);

        // Register the Admin Command
        AdminCommand adminCommand = new AdminCommand(this);
        getCommand("admin").setExecutor(adminCommand);
        getCommand("admin").setTabCompleter(adminCommand);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

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

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }

    public Settings getSettings() {
        return configManager.getSettings();
    }

    public SimpleConfigManager getConfigManager() {
        return configManager;
    }

    public List<String> getSplashText() {
        StringBuilder authors = new StringBuilder();
        for (String author : this.getDescription().getAuthors()) {
            authors.append(author).append(", ");
        }
        authors.delete(authors.length() - 1, authors.length());
        return Arrays.asList(
                "SkyHole v" + this.getDescription().getVersion(),
                "Built by" + authors
        );
    }
}
