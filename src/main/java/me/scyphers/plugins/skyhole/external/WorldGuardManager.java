package me.scyphers.plugins.skyhole.external;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import me.scyphers.plugins.skyhole.SkyHole;
import org.bukkit.plugin.Plugin;

public class WorldGuardManager {

    private final SkyHole plugin;

    private final boolean pluginLoaded;

    private boolean flagsLoaded;

    public static final SkyHoleFlag SKYHOLE_FLIGHT = new SkyHoleFlag("skyhole-flight") {
    };

    public WorldGuardManager(SkyHole plugin) {
        this.plugin = plugin;

        Plugin worldguard = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        if (worldguard == null) {
            plugin.getLogger().severe("Unable to find WorldGuard! No Flags will be loaded");
            this.pluginLoaded = false;
            return;
        }

        // Register the flag
        this.registerFlags();

        this.pluginLoaded = true;

    }

    public boolean isPluginLoaded() {
        return pluginLoaded;
    }

    public void registerFlags() {

        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            try {
                // register the flags with the registry
                registry.register(SKYHOLE_FLIGHT);
                plugin.getLogger().info("WorldGuard flags registered");
                this.flagsLoaded = false;
            } catch (FlagConflictException e) {
                e.printStackTrace();
                plugin.getLogger().warning("World Guard flags not registered!");
                this.flagsLoaded = false;
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().warning("World Guard instance not found!");
            this.flagsLoaded = false;
        }

        this.flagsLoaded = true;

    }

    public void loadSessionHandlers() {
        plugin.getLogger().info("Registering session handler for Skyhole regions");
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(SkyHoleSession.FACTORY, null);
    }

}
