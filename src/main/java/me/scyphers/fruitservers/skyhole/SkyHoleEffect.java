package me.scyphers.fruitservers.skyhole;

import org.bukkit.util.Vector;

public record SkyHoleEffect(Vector velocity, int slowFallDuration, int slowFallStrength) {

    public Vector velocity() {
        return velocity.clone();
    }

    public static SkyHoleEffect invalidEffect() {
        return new SkyHoleEffect(new Vector(0, 0, 0), 0, 0);
    }

    @Override
    public String toString() {
        return "SkyHoleEffect[" +
                "velocity=" + velocity +
                ", duration=" + slowFallDuration +
                ", strength=" + slowFallStrength +
                ']';
    }
}
