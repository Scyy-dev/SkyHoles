package me.scyphers.fruitservers.skyhole.external;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import me.scyphers.fruitservers.skyhole.SkyHole;
import me.scyphers.fruitservers.skyhole.SkyHoleEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkyHoleSession extends FlagValueChangeHandler<SkyHoleEffect> {

    public static final Factory FACTORY = new Factory();
    public static class Factory extends Handler.Factory<SkyHoleSession> {
        @Override
        public SkyHoleSession create(Session session) {
            return new SkyHoleSession(session);
        }
    }

    public SkyHoleSession(Session session) {
        super(session, WorldGuardManager.SKYHOLE_FLIGHT);
    }

    @Override
    protected void onInitialValue(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet, SkyHoleEffect skyHoleEffect) {

    }

    @Override
    protected boolean onSetValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, SkyHoleEffect current, SkyHoleEffect old, MoveType moveType) {
        Player player = Bukkit.getPlayer(localPlayer.getUniqueId());
        if (player == null) return true;
        SkyHole.getInstance().applyBoost(player, current);
        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, SkyHoleEffect old, MoveType moveType) {
        return true;
    }

}
