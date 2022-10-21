package me.scyphers.fruitservers.skyhole.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class Messenger extends ConfigFile {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private String prefix;

    public Messenger(ConfigManager manager) {
        super(manager, "messages.yml", true);

        // Get the prefix
        String rawPrefix = config.getString("prefix");
        if (rawPrefix != null) this.prefix = rawPrefix;
        else this.prefix = "[COULD_NOT_LOAD_PREFIX]";

    }

    @Override
    public void reloadConfig() throws Exception {
        super.reloadConfig();
        // Get the prefix
        String rawPrefix = config.getString("prefix");
        if (rawPrefix != null) this.prefix = rawPrefix;
        else this.prefix = "[COULD_NOT_LOAD_PREFIX]";
    }

    public static String replace(String message, String... replacements) {

        if (replacements == null || replacements.length == 0) return message;

        if (replacements.length % 2 != 0) throw new IllegalArgumentException("Not all placeholders have a corresponding replacement");

        for (int i = 0; i < replacements.length; i += 2) {
            String placeholder = replacements[i];
            String replacement = replacements[i + 1];
            message = message.replaceAll(placeholder, replacement);
        }

        return message;

    }

    public void msg(CommandSender sender, String path) {
        this.msg(sender, path, new String[0]);
    }

    public void msg(CommandSender sender, String path, String... replacements) {
        Component message = this.getMsg(path, replacements);
        if (Component.EQUALS.test(message, Component.empty())) return;
        sender.sendMessage(message);
    }

    // Getting messages from messages.yml
    public Component getMsg(String path) {
        return this.getMsg(path, new String[0]);
    }

    public Component getMsg(String path, String... replacements) {

        String rawMessage = config.getString(path);
        if (rawMessage == null) return messageNotFound(path);

        if (rawMessage.equals("")) return Component.empty();

        String messagePrefix = "";
        if (!rawMessage.startsWith("[NO_PREFIX]")) {
            messagePrefix = prefix;
        } else {
            rawMessage = rawMessage.substring(11);
        }

        rawMessage = replace(rawMessage, replacements);

        return miniMessage.deserialize(messagePrefix + rawMessage);

    }

    // Get messages without formatting them to BaseComponents from messages.yml
    public String getRawMsg(String path) {
        return this.getRawMsg(path, new String[0]);
    }

    public String getRawMsg(String path, String... replacements) {

        String rawMessage = config.getString(path);

        if (rawMessage == null) return "Could not find message at " + path;

        if (rawMessage.equalsIgnoreCase("")) return "";

        String messagePrefix = "";

        if (!rawMessage.startsWith("[NO_PREFIX]")) {
            messagePrefix = prefix + " ";
        } else {
            rawMessage = rawMessage.substring(11);
        }

        rawMessage = replace(rawMessage, replacements);

        return messagePrefix + rawMessage;

    }

    // Generic message sent if a message cannot be found in messages.yml
    public static Component messageNotFound(String messagePath) {
        return Component.text("Could not find message at " + messagePath);
    }

}
