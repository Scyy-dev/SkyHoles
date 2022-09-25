package me.scyphers.fruitservers.skyhole.external;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import me.scyphers.fruitservers.skyhole.SkyHoleEffect;
import org.bukkit.util.Vector;
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

    private static Object serialise(SkyHoleEffect effect) {
        Vector velocity = effect.velocity();
        return "" + velocity.getX() + ", " + velocity.getY() + ", " + velocity.getZ() + ", "
                + effect.slowFallStrength() + ", "
                + effect.slowFallDuration();
    }

    private static SkyHoleEffect deserialise(String input) {
        if (input.split(",").length == 3) return legacyDeserialise(input);
        String[] args = input.split(", ");
        try {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            int slowFallStrength = Integer.parseInt(args[3]);
            int slowFallDuration = Integer.parseInt(args[4]);
            Vector velocity = new Vector(x, y, z);
            return new SkyHoleEffect(velocity, slowFallStrength, slowFallDuration);
        } catch (Exception e) {
            return SkyHoleEffect.invalidEffect();
        }
    }

    private static SkyHoleEffect legacyDeserialise(String input) {
        String[] args = input.split(",");
        double boostSpeed = Double.parseDouble(args[0]);
        int slowFallDuration = Integer.parseInt(args[1]);
        int slowFallStrength = Integer.parseInt(args[2]);
        return new SkyHoleEffect(new Vector(0, boostSpeed, 0), slowFallDuration, slowFallStrength);
    }

}
