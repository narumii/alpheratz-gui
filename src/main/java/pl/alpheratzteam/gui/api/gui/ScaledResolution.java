package pl.alpheratzteam.gui.api.gui;

import lombok.Getter;
import pl.alpheratzteam.gui.util.MathUtil;
import pl.alpheratzteam.gui.window.Window;

/**
 * @author Unix
 * @since 13.02.2021
 */

@Getter
public final class ScaledResolution {

    private final double scaledWidthD, scaledHeightD;

    private int scaledWidth, scaledHeight, scaleFactor;

    public ScaledResolution(Window window, int scale) {
        this.scaledWidth = window.getWidth();
        this.scaledHeight = window.getHeight();
        this.scaleFactor = scale;
        this.scaledWidthD = (double) scaledWidth / (double) scaleFactor;
        this.scaledHeightD = (double) scaledHeight / (double) scaleFactor;
        this.scaledWidth = MathUtil.ceiling_double_int(scaledWidthD);
        this.scaledHeight = MathUtil.ceiling_double_int(scaledHeightD);
    }

}