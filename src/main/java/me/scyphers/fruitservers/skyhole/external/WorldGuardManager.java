package me.scyphers.fruitservers.skyhole.external;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.SessionManager;
import me.scyphers.fruitservers.skyhole.SkyHole;
import me.scyphers.fruitservers.skyhole.SkyHoleEffect;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

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

    public boolean applySkyholeEffect(org.bukkit.World world, String regionName, Vector velocity, int slowFallDuration, int slowFallStrength) {
        if (!pluginLoaded || !flagsLoaded) return false;
        World wgWorld = BukkitAdapter.adapt(world);
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(wgWorld);
        if (manager == null) return false;
        ProtectedRegion region = manager.getRegion(regionName);
        if (region == null) return false;
        region.setFlag(SKYHOLE_FLIGHT, new SkyHoleEffect(velocity, slowFallDuration, slowFallStrength));
        return true;
    }

    public void registerFlags() {

        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            try {
                // register the flags with the registry
                registry.register(SKYHOLE_FLIGHT);
                plugin.getLogger().info("WorldGuard flags registered");
            } catch (FlagConflictException e) {
                e.printStackTrace();
                plugin.getLogger().warning("World Guard flags not registered!");
                this.flagsLoaded = false;
                return;
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().warning("World Guard instance not found!");
            this.flagsLoaded = false;
            return;
        }

        this.flagsLoaded = true;

    }

    public void loadSessionHandlers() {
        plugin.getLogger().info("Registering session handler for Skyhole regions");
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(SkyHoleSession.FACTORY, null);
    }

    public List<String> getRegionNames(org.bukkit.Location location) {
        if (!pluginLoaded) return Collections.emptyList();
        org.bukkit.World world = location.getWorld();
        if (world == null) return Collections.emptyList();
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (manager == null) return Collections.emptyList();
        return manager.getApplicableRegionsIDs(BlockVector3.at(location.getX(), location.getY(), location.getZ()));
    }
}
