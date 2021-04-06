package pl.alpheratzteam.gui.util;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;

/**
 * @author Unix
 * @since 13.02.2021
 */

public final class OpenGLUtil {

    private static boolean openGL14;

    private OpenGLUtil() {
    }

    public static void load(GLCapabilities capabilities) {
        openGL14 = capabilities.OpenGL14 || capabilities.GL_EXT_blend_func_separate;
//        try (var memoryStack = stackPush()) {
//            dataBuffer = memoryStack.mallocInt(1);
//        }
    }

    public static void color(float red, float green, float blue, float alpha) {
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void color(int color) {
        var red = (float) (color >> 16 & 255) / 255.0F;
        var green = (float) (color >> 8 & 255) / 255.0F;
        var blue = (float) (color & 255) / 255.0F;
        var alpha = (float) (color >> 24 & 255) / 255.0F;

        color(red, green, blue, alpha);
    }

    public static void enableBlend() {
        GL11.glEnable(GL11.GL_BLEND);
    }

    public static void blendFunc(int src, int dest) {
        GL11.glBlendFunc(src, dest);
    }

    public static void tryBlendFuncSeparate(int src, int dest, int srcAlpha, int destAlpha) {
        if (openGL14) {
            GL14.glBlendFuncSeparate(src, dest, srcAlpha, destAlpha);
            return;
        }

        GL11.glBlendFunc(src, dest);
    }

    public static void disableBlend() {
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void enableAlpha() {
        GL11.glEnable(GL11.GL_ALPHA);
    }

    public static void disableAlpha() {
        GL11.glDisable(GL11.GL_ALPHA);
    }

    public static void enableTexture2D() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void disableTexture2D() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void translate(float x, float y, float z) {
        GL11.glTranslatef(x, y, z);
    }

    public static void bindTexture(int textureId) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }

    public static int glGenTextures() {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int texture) {
        if (texture != 0) {
            GL11.glDeleteTextures(texture);
        }
    }

    public static void scaled(double x, double y, double z) {
        GL11.glScaled(x, y, z);
    }

}