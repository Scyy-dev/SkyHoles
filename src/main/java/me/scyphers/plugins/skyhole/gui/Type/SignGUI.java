package me.scyphers.plugins.skyhole.gui.type;

import me.scyphers.plugins.skyhole.SkyHole;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SignGUI implements GUI<SignChangeEvent> {

    protected final GUI<?> lastGUI;

    protected final SkyHole plugin;

    protected final Player player;

    protected final Sign sign;

    public SignGUI(GUI<?> lastGUI, SkyHole plugin, Player player, Sign sign) {
        this.lastGUI = lastGUI;
        this.plugin = plugin;
        this.player = player;
        this.sign = sign;
        sign.setEditable(true);
    }

    /**
     * The outcome of this method should never be another SignGUI
     * @param event the event to handle the GUI interaction
     * @return the new GUI
     */
    @Override
    public abstract @NotNull GUI<?> handleInteraction(SignChangeEvent event);

    @Override
    public @Nullable InventoryView open(Player player) {
        if (sign == null) {
            plugin.getLogger().warning("Unable to interact with sign");
        } else {
            sign.setEditable(true);
            player.openSign(sign);
        }
        return null;
    }

    public static Listener getListener(SkyHole plugin) {
        return new SignListener(plugin);
    }

    @Override
    public @Nullable GUI<?> getLastGUI() {
        return lastGUI;
    }

    @Override
    public @NotNull SkyHole getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public boolean shouldClose() {
        return false;
    }

    private static class SignListener implements Listener {
        public SignListener(SkyHole plugin) {
        }
        @EventHandler
        public void onSignChangeEvent(SignChangeEvent event) {
            // You have to fill this out as implementations will differ between plugins
        }

    }

}
