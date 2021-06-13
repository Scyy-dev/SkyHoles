package me.scyphers.plugins.skyhole.external;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Set;

public class WorldGuardSessionManager extends FlagValueChangeHandler<State> {

    public static final Factory FACTORY = new Factory();

    public static class Factory extends Handler.Factory<WorldGuardSessionManager> {

        @Override
        public WorldGuardSessionManager create(Session session) {
            return new WorldGuardSessionManager(session);
        }

    }

    protected WorldGuardSessionManager(Session session) {
        super(session, WorldGuardManager.SKYHOLE_FLIGHT);
    }

    @Override
    protected void onInitialValue(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet, State state) {

    }

    @Override
    protected boolean onSetValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, State state, State t1, MoveType moveType) {
        return false;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, State state, MoveType moveType) {
        return false;
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer localPlayer, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        if (!toSet.testState(null, WorldGuardManager.SKYHOLE_FLIGHT)) return true;

        // Get original player object as WorldGuard doesn't offer vectors
        Player player = Bukkit.getPlayer(localPlayer.getUniqueId());

        // This should never happen
        if (player == null) return true;

        Vector velocity = player.getVelocity();

        // TODO - change this value in config? base it off of current velocity? play around with it
        velocity.setY(20);

        player.setVelocity(velocity);

        // Give the slow fall effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 1));

        return true;

    }
}
