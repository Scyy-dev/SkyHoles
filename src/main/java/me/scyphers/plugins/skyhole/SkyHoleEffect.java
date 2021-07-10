package me.scyphers.plugins.skyhole;

public record SkyHoleEffect(double boostSpeed, int slowFallDuration, int slowFallStrength) {

    public double getBoostSpeed() {
        return boostSpeed;
    }

    public int getSlowFallDuration() {
        return slowFallDuration;
    }

    public int getSlowFallStrength() {
        return slowFallStrength;
    }

    public static SkyHoleEffect invalidEffect() {
        return new SkyHoleEffect(0, 0, 0);
    }

    @Override
    public String toString() {
        return "SkyHoleEffect[" +
                "boost=" + boostSpeed +
                ", duration=" + slowFallDuration +
                ", strength=" + slowFallStrength +
                ']';
    }
}
