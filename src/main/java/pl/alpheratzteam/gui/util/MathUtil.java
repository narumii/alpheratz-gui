package pl.alpheratzteam.gui.util;

/**
 * @author Mojang
 * Source: net.minecraft.util.MathHelper
 */

public final class MathUtil {

    private MathUtil() {
    }

    /**
     * This method is used to find the smallest integer, which is greater than or equal to the passed argument.
     * @param value value.
     * @return {@code int}.
     */
    public static int ceiling_double_int(double value) {
        var i = (int) value;
        return value > (double) i ? i + 1 : i;
    }

}
