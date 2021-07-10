package me.scyphers.plugins.skyhole;

import me.scyphers.plugins.skyhole.command.AdminCommand;
import me.scyphers.plugins.skyhole.config.SimpleConfigManager;
import me.scyphers.plugins.skyhole.config.Settings;
import me.scyphers.plugins.skyhole.external.WorldGuardManager;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static java.lang.Math.*;

import java.util.Arrays;
import java.util.List;

public class SkyHole extends JavaPlugin {

    private static SkyHole instance;

    // External plugins
    private WorldGuardManager worldGuardManager;

    // Config
    private SimpleConfigManager configManager;

    public static SkyHole getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        worldGuardManager.loadSessionHandlers();

        // Register the Config Manager
        configManager = new SimpleConfigManager(this);

        // Register the Admin Command
        AdminCommand adminCommand = new AdminCommand(this);
        getCommand("skyhole").setExecutor(adminCommand);
        getCommand("skyhole").setTabCompleter(adminCommand);

    }

    @Override
    public void onLoad() {
        // Load WorldGuard
        this.worldGuardManager = new WorldGuardManager(this);
        if (!worldGuardManager.isPluginLoaded()) {
            getLogger().severe("WorldGuard hooks failed! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }
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

    public void applyBoost(Player player, SkyHoleEffect skyHoleEffect) {
        double boostSpeed = skyHoleEffect.getBoostSpeed();
        int slowFallDuration = skyHoleEffect.getSlowFallDuration();
        int slowFallStrength = skyHoleEffect.getSlowFallStrength();

        // Speed boost
        Vector playerVelocity = player.getVelocity();
        playerVelocity.setY(boostSpeed);
        player.setVelocity(playerVelocity);

        // Slow Fall
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, slowFallDuration, slowFallStrength, false, false));

        // Sound Effect
        player.playSound(player.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_OUT, 10, 2);

        // Particles
        applyParticleEffect(player, boostSpeed);
    }

    public void applyParticleEffect(Player player, double velocity) {

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            Location spiral1, spiral2;
            private double angle = 0;

            @Override
            public void run() {
                angle += PI / 8;
                Location playerLoc = player.getLocation();
                spiral1 = playerLoc.clone().add(cos(angle), sin(angle) + 1, sin(angle));
                spiral2 = playerLoc.clone().add(cos(angle + PI), sin(angle) + 1, sin(angle + PI));

                World world = playerLoc.getWorld();
                if (world != null) {
                    world.spawnParticle(Particle.SNOWFLAKE, spiral1, 0);
                    world.spawnParticle(Particle.SNOWFLAKE, spiral2, 0);
                }

            }
        }, 0, 1);

        int particleDurationTicks = clamp((int) velocity * 5, 5, 20);

        Bukkit.getScheduler().runTaskLater(this, () -> Bukkit.getScheduler().cancelTask(taskID), particleDurationTicks);

    }

    public static int clamp(int value, int min, int max) {
        return max(min, min(value, max));
    }
}
