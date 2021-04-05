package pl.alpheratzteam.gui.util;

import de.matthiasmann.twl.utils.PNGDecoder;
import pl.alpheratzteam.gui.api.texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * @author Unix
 * @since 14.02.2021
 */

public final class TextureUtil {

    private TextureUtil() {
    }

    /**
     * Loads the texture via allocations into OpenGL.
     * @param bufferedImage texture to load.
     * @return {@link Texture}.
     */
    public static Texture loadTexture(BufferedImage bufferedImage) {
        try {
            var byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            var decoder = new PNGDecoder(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            var width = decoder.getWidth();
            var height = decoder.getHeight();
            var buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();

            var textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            return new Texture(textureId);
        } catch (Exception ex) {
            return new Texture(-1);
        }
    }

}