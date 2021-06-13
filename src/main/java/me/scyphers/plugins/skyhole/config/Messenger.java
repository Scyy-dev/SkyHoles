package me.scyphers.plugins.skyhole.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messenger extends ConfigFile {

    public static final Pattern hex = Pattern.compile("&#[a-zA-Z0-9]{6}");

    // If using this as a template, feel free to customise this exact char - it was chosen at random
    private static final char interactChar = 'φ';

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

    // Managing Spigots BaseComponents
    public static BaseComponent[] toComponent(String message) {
        return toComponent(message, null);
    }

    public static BaseComponent[] toComponent(String message, BaseComponent interactable) {

        ComponentBuilder builder = new ComponentBuilder();

        boolean validInteractable = interactable != null;

        StringBuilder sb = new StringBuilder();
        TextComponent component = new TextComponent();
        for (int i = 0; i < message.length(); i++) {

            char letter = message.charAt(i);

            if ( letter != '&') {
                sb.append(letter);
            } else {
                // Add text and formatting
                if (!sb.toString().equalsIgnoreCase("")) {
                    component.setText(sb.toString());
                    builder.append(component);
                    sb = new StringBuilder();
                    component = new TextComponent();
                }

                switch (message.charAt(i + 1)) {
                    case '#':
                        String hex = message.substring(i + 1, i + 8);
                        component.setColor(ChatColor.of(hex));
                        i += 7;
                        break;
                    case interactChar:
                        if (validInteractable) {
                            component.setText(sb.toString());
                            builder.append(component);
                            sb = new StringBuilder();
                            builder.append(interactable);
                        }
                        i += 1;
                        break;
                    default:
                        char colourCode = message.charAt(i + 1);
                        switch (colourCode) {
                            case 'k':
                                component.setObfuscated(true);
                                break;
                            case 'l':
                                component.setBold(true);
                                break;
                            case 'm':
                                component.setStrikethrough(true);
                                break;
                            case 'n':
                                component.setUnderlined(true);
                                break;
                            case 'o':
                                component.setItalic(true);
                                break;
                            case 'r':
                                component.setObfuscated(false);
                                component.setBold(false);
                                component.setStrikethrough(false);
                                component.setUnderlined(false);
                                component.setItalic(false);
                            default:
                                component.setColor(ChatColor.getByChar(colourCode));
                                break;
                        }
                        i += 1;
                }
            }
        }

        component.setText(sb.toString());
        builder.append(component);

        return builder.create();

    }

    // Managing formatting/replacements
    public static String markForInteractEvent(String text, String textToMark) {
        return text.replaceAll(textToMark, '&' + String.valueOf(interactChar));
    }

    public static String format(String message) {

        // Replace hex colour codes
        Matcher hexMatcher = hex.matcher(message);
        while (hexMatcher.find()) {
            String rawMatch = message.substring(hexMatcher.start(), hexMatcher.end());
            String hexCode = message.substring(hexMatcher.start() + 1, hexMatcher.end());
            message = message.replace(rawMatch, ChatColor.of(hexCode).toString());
        }

        // Translate normal colour codes and return the message
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String replace(String message, String... replacements) {
        if (replacements != null && replacements[0] != null) {

            if (replacements.length % 2 != 0) throw new IllegalArgumentException("Not all placeholders have a corresponding replacement");

            for (int i = 0; i < replacements.length; i += 2) {
                String placeholder = replacements[i];
                String replacement = replacements[i + 1];
                message = message.replaceAll(placeholder, replacement);
            }

        }
        return message;

    }

    // Sending messages that aren't from messages.yml
    public void send(CommandSender sender, String message) {
        BaseComponent[] components = toComponent(message);
        this.msg(sender, components);
    }

    // Sending messages from messages.yml
    public void msg(CommandSender sender, BaseComponent[] message) {
        sender.spigot().sendMessage(message);
    }

    public void msg(Player player, ChatMessageType type, BaseComponent[] message) {
        player.spigot().sendMessage(type, null, message);
    }

    public void msg(CommandSender sender, String path) {
        this.msg(sender, path, (String) null);
    }

    public void msg(CommandSender sender, String path, String... replacements) {
        BaseComponent[] message = this.getMsg(path, replacements);
        if (message.length == 0) return;
        this.msg(sender, message);
    }

    // Sending list messages from messages.yml
    public void msgList(CommandSender sender, String path) {
        this.msgList(sender, path, (String) null);
    }

    public void msgList(CommandSender sender, String path, String... replacements) {
        for (BaseComponent[] message : this.getListMsg(path, replacements)) {
            this.msg(sender, message);
        }
    }

    // Getting messages from messages.yml
    public BaseComponent[] getMsg(String path) {
        return this.getMsg(path, (String) null);
    }

    public BaseComponent[] getMsg(String path, String... replacements) {

        String rawMessage = config.getString(path);
        if (rawMessage == null) return messageNotFound(path);

        if (rawMessage.equals("")) return new BaseComponent[0];

        String messagePrefix = "";
        if (!rawMessage.startsWith("[NO_PREFIX]")) {
            messagePrefix = prefix;
        } else {
            rawMessage = rawMessage.substring(11);
        }

        rawMessage = replace(rawMessage, replacements);

        return toComponent(messagePrefix + rawMessage);

    }

    // Get messages without formatting them to BaseComponents from messages.yml
    public String getRawMsg(String path) {
        return this.getRawMsg(path, (String) null);
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

    // Get list messages from messages.yml
    public List<BaseComponent[]> getListMsg(String path) {
        return this.getListMsg(path, (String) null);
    }

    public List<BaseComponent[]> getListMsg(String path, String... replacements) {

        List<String> rawList = config.getStringList(path);
        List<BaseComponent[]> list = new LinkedList<>();

        if (rawList.size() == 0) return Collections.singletonList(messageNotFound(path));

        for (String item : rawList) {

            item = replace(item, replacements);

            list.add(toComponent(item));

        }

        return list;

    }

    // Get list messages without formatting them to BaseComponents from messages.yml
    public List<String> getRawListMsg(String path) {
        return this.getRawListMsg(path, (String) null);
    }

    public List<String> getRawListMsg(String path, String... replacements) {

        List<String> rawList = config.getStringList(path);
        List<String> list = new LinkedList<>();

        if (rawList.size() == 0) return Collections.singletonList("Could not find message at " + path);

        for (String item : rawList) {

            item = replace(item, replacements);
            list.add(item);

        }

        return list;

    }

    // Generic message sent if a message cannot be found in messages.yml
    public static BaseComponent[] messageNotFound(String messagePath) {
        return new BaseComponent[] {
                new TextComponent("Could not find message at " + messagePath)
        };
    }

}
