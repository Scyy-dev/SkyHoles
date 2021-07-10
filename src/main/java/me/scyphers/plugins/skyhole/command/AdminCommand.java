package me.scyphers.plugins.skyhole.command;

import me.scyphers.plugins.skyhole.config.Messenger;
import me.scyphers.plugins.skyhole.SkyHole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

        // IDE flags this with a warning for not enough case statements - ignored due to more commands expected when plugin is created
        if (args.length == 1) {
            if (sender.hasPermission("skyhole.help")) list.add("help");
            if (sender.hasPermission("skyhole.reload")) list.add("reload");
            return list;
        }
        return list;
    }
}
