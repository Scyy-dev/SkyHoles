package me.scyphers.plugins.skyhole.external;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import me.scyphers.plugins.skyhole.SkyHole;
import org.bukkit.plugin.Plugin;

public class WorldGuardManager {

    private final SkyHole plugin;

    private final boolean pluginLoaded;

    private boolean flagsLoaded;

    public static final StateFlag SKYHOLE_FLIGHT = new StateFlag("skyhole-flight", false);

    public WorldGuardManager(SkyHole plugin) {
        this.plugin = plugin;

        Plugin worldguard = plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        if (worldguard == null || !worldguard.isEnabled()) {
            plugin.getLogger().severe("Unable to find WorldGuard! No Flags will be loaded");
            this.pluginLoaded = false;
            return;
        }

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
                plugin.getLogger().info("Collection WG flags registered");
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

    public boolean canSkyholeFly() {
        return pluginLoaded && flagsLoaded;
    }

}
