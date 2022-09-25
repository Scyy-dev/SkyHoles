package me.scyphers.fruitservers.skyhole.util;

import static java.lang.Math.*;

public class MathUtil {

    public static int clamp(int value, int min, int max) {
        return max(min, min(value, max));
    }

}
