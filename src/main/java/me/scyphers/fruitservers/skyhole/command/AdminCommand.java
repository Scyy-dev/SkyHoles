package me.scyphers.fruitservers.skyhole.command;

import me.scyphers.fruitservers.skyhole.SkyHole;
import me.scyphers.fruitservers.skyhole.config.Messenger;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdminCommand implements TabExecutor {

    private final SkyHole plugin;

    private final Messenger pm;

    public AdminCommand(SkyHole plugin) {
        this.plugin = plugin;
        this.pm = plugin.getConfigManager().getPlayerMessenger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0) {
            for (String message : plugin.getSplashText()) {
                sender.sendMessage(message);
                return true;
            }
        }

        switch (args[0]) {
            case "reload" -> {
                if (!sender.hasPermission("skyhole.reload")) {
                    pm.msg(sender, "errorMessages.noPermission");
                    return true;
                }
                plugin.reload(sender);
                return true;
            }
            case "help" -> {
                if (!sender.hasPermission("skyhole.help")) {
                    pm.msg(sender, "errorMessages.noPermission");
                    return true;
                }
                sender.sendMessage("Use the 'skyhole-flight' worldguard flag to add an effect!");
                sender.sendMessage("Format is as following: boost,duration,strength");
                sender.sendMessage("boost is the velocity applied to the player, duration the duration of slow fall, and strength the strength of slow fall");
                sender.sendMessage("e.g. 2,100,2");
                return true;
            }
            case "place" -> {

                if (!sender.hasPermission("skyhole.place")) {
                    pm.msg(sender, "errorMessages.noPermission");
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    pm.msg(sender, "errorMessages.mustBePlayer");
                    return true;
                }

                if (args.length < 4) {
                    pm.msg(sender, "errorMessages.invalidCommandLength", "%length%", "" + 5);
                    return true;
                }

                double velocity;
                int strength, duration;
                try {
                    velocity = Double.parseDouble(args[1]);
                    strength = Integer.parseInt(args[2]);
                    duration = Integer.parseInt(args[3]);
                } catch (Exception e) {
                    pm.msg(sender, "errorMessages.notANumber", "%input%", args[1]);
                    return true;
                }

                String regionName = args[4].toLowerCase(Locale.ROOT);

                // Calculate the velocity to apply
                Vector direction = player.getEyeLocation().getDirection();
                direction.clone().normalize();
                direction.multiply(velocity);

                World world = player.getWorld();
                if (plugin.getWorldGuardManager().applySkyholeEffect(world, regionName, direction, strength, duration)) {
                    pm.msg(sender, "appliedSkyholeEffect", "%region%", regionName);
                } else {
                    pm.msg(sender, "errorMessages.regionNotFound", "%region%", regionName);
                }
                return true;

            }
            default -> {
                pm.msg(sender, "errorMessages.invalidCommand");
                return true;
            }
        }

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("skyhole.help")) list.add("help");
            if (sender.hasPermission("skyhole.reload")) list.add("reload");
            if (sender.hasPermission("skyhole.place")) list.add("place");
            return list;
        }

        if (sender.hasPermission("skyhole.place") && sender instanceof Player player) {
            return switch (args.length) {
                case 2 -> Arrays.asList("0.5", "1", "2", "3", "4", "5");
                case 3 -> Arrays.asList("0", "1", "2", "3", "4", "5");
                case 4 -> Arrays.asList("10", "20", "40", "60", "80", "100");
                case 5 -> plugin.getWorldGuardManager().getRegionNames(player.getLocation());
                default -> list;
            };
        }
        
        return list;
        
    }
}
