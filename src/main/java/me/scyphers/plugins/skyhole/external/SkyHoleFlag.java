package me.scyphers.plugins.skyhole.external;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import me.scyphers.plugins.skyhole.SkyHoleEffect;
import org.jetbrains.annotations.Nullable;

public class SkyHoleFlag extends Flag<SkyHoleEffect> {

    protected SkyHoleFlag(String name) {
        super(name);
    }

    @Override
    public SkyHoleEffect parseInput(FlagContext flagContext) throws InvalidFlagFormat {
        String userInput = flagContext.getUserInput();
        try {
            return deserialise(userInput);
        } catch (Exception e) {
            throw new InvalidFlagFormat("Format is boostSpeed,slowFallDuration,slowFallStrength");
        }

    }

    @Override
    public SkyHoleEffect unmarshal(@Nullable Object o) {
        return o instanceof String oString ? deserialise(oString) : SkyHoleEffect.invalidEffect();
    }

    @Override
    public Object marshal(SkyHoleEffect skyHoleEffect) {
        return serialise(skyHoleEffect);
    }

    private static Object serialise(SkyHoleEffect skyHoleEffect) {
        return "" + skyHoleEffect.getBoostSpeed() + ","
                + skyHoleEffect.getSlowFallDuration() + ","
                + skyHoleEffect.getSlowFallStrength();
    }

    private static SkyHoleEffect deserialise(String input) {
        String[] args = input.split(",");
        double boostSpeed = Double.parseDouble(args[0]);
        int slowFallDuration = Integer.parseInt(args[1]);
        int slowFallStrength = Integer.parseInt(args[2]);
        return new SkyHoleEffect(boostSpeed, slowFallDuration, slowFallStrength);
    }
}
