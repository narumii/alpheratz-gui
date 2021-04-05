package pl.alpheratzteam.gui.api.gui;

import pl.alpheratzteam.gui.util.OpenGLUtil;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * @author Unix
 * @since 13.02.2021
 */

public class ScreenHelper {

    public static void drawRect(double x, double y, double width, double height, int color) {
        if (x < width) {
            var newWidth = x;
            x = width;
            width = newWidth;
        }

        if (y < height) {
            var newHeight = y;
            y = height;
            height = newHeight;
        }

        OpenGLUtil.enableBlend();
        OpenGLUtil.disableTexture2D();
        OpenGLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
        OpenGLUtil.color(color);

        GL11.glBegin(7); {
            GL11.glVertex2d(x, height);
            GL11.glVertex2d(width, height);
            GL11.glVertex2d(width, y);
            GL11.glVertex2d(x, y);
        }
        GL11.glEnd();

        OpenGLUtil.enableTexture2D();
        OpenGLUtil.disableBlend();
    }

    public static void drawCircle(double x, double y, double radius, int color) {
        glPushMatrix();

        OpenGLUtil.color(color);
        glBegin(GL_POLYGON);

        for (int i = 0; i < 360; i++) {
            glVertex2d(x + Math.sin(Math.toRadians(i)) * radius, y + Math.cos(Math.toRadians(i)) * radius);
        }

        glEnd();
        glPopMatrix();
    }

    public static void drawLineLoop(double startX, double startY, double width, double height, float lineWidth, int color) {
        OpenGLUtil.color(color);
        glLineWidth(lineWidth);

        glBegin(GL_LINE_LOOP);

        glVertex2d(startX + width, startY + height);
        glVertex2d(startX + width, startY);
        glVertex2d(startX, startY);
        glVertex2d(startX, startY + height);

        glEnd();
    }

}