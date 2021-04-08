package pl.alpheratzteam.gui.api.gui.font;

import lombok.Getter;
import pl.alpheratzteam.gui.util.OpenGLUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author superblaubeere27
 * Source: https://github.com/superblaubeere27/ClientBase/blob/master/src/main/java/net/superblaubeere27/clientbase/utils/fontRenderer/GlyphPageFontRenderer.java
 **/

public class CustomFontRenderer {

    @Getter
    private final FontPage regularGlyphPage, boldGlyphPage, italicGlyphPage, boldItalicGlyphPage;
    private final Locale locale = new Locale("PL", "pl");

    private final int[] colorCode = new int[32];

    private float posX, posY;
    private float red, green, blue, alpha;

    private boolean boldStyle, italicStyle, underlineStyle, strikethroughStyle;

    public CustomFontRenderer(FontPage regularGlyphPage, FontPage boldGlyphPage, FontPage italicGlyphPage, FontPage boldItalicGlyphPage) {
        this.regularGlyphPage = regularGlyphPage;
        this.boldGlyphPage = boldGlyphPage;
        this.italicGlyphPage = italicGlyphPage;
        this.boldItalicGlyphPage = boldItalicGlyphPage;

        for (int i = 0; i < 32; ++i) {
            var j = (i >> 3 & 1) * 85;
            var k = (i >> 2 & 1) * 170 + j;
            var l = (i >> 1 & 1) * 170 + j;
            var i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public static CustomFontRenderer create(String fontName, int size, boolean bold, boolean italic, boolean boldItalic, int additionalSpace) {
        var chars = new char[512];
        IntStream.range(0, chars.length).forEachOrdered(i -> chars[i] = (char) i);

        var regularPage = new FontPage(new Font(fontName, Font.PLAIN, size), true, true);

        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();

        var boldPage = regularPage;
        var italicPage = regularPage;
        var boldItalicPage = regularPage;

        if (bold) {
            boldPage = new FontPage(new Font(fontName, Font.BOLD, size), true, true);

            boldPage.generateGlyphPage(chars);
            boldPage.setupTexture();
        }

        if (italic) {
            italicPage = new FontPage(new Font(fontName, Font.ITALIC, size), true, true);

            italicPage.generateGlyphPage(chars);
            italicPage.setupTexture();
        }

        if (boldItalic) {
            boldItalicPage = new FontPage(new Font(fontName, Font.BOLD | Font.ITALIC, size), true, true);

            boldItalicPage.generateGlyphPage(chars);
            boldItalicPage.setupTexture();
        }

        regularPage.setAdditionalSpace(additionalSpace);
        return new CustomFontRenderer(regularPage, boldPage, italicPage, boldItalicPage);
    }

    public static CustomFontRenderer create(String fontName, int size, boolean bold, boolean italic, boolean boldItalic) {
        var chars = new char[512];
        IntStream.range(0, chars.length).forEachOrdered(i -> chars[i] = (char) i);

        var regularPage = new FontPage(new Font(fontName, Font.PLAIN, size), true, true);

        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();

        var boldPage = regularPage;
        var italicPage = regularPage;
        var boldItalicPage = regularPage;

        if (bold) {
            boldPage = new FontPage(new Font(fontName, Font.BOLD, size), true, true);

            boldPage.generateGlyphPage(chars);
            boldPage.setupTexture();
        }

        if (italic) {
            italicPage = new FontPage(new Font(fontName, Font.ITALIC, size), true, true);

            italicPage.generateGlyphPage(chars);
            italicPage.setupTexture();
        }

        if (boldItalic) {
            boldItalicPage = new FontPage(new Font(fontName, Font.BOLD | Font.ITALIC, size), true, true);

            boldItalicPage.generateGlyphPage(chars);
            boldItalicPage.setupTexture();
        }

        return new CustomFontRenderer(regularPage, boldPage, italicPage, boldItalicPage);
    }

    public static CustomFontRenderer create(Font font) {
        var chars = new char[512];
        IntStream.range(0, chars.length).forEachOrdered(i -> chars[i] = (char) i);
        
        var regularPage = new FontPage(font, true, true);
        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();

        return new CustomFontRenderer(regularPage, regularPage, regularPage, regularPage);
    }

    public static CustomFontRenderer create(Font font, int additionalSpace) {
        var chars = new char[512];
        IntStream.range(0, chars.length).forEachOrdered(i -> chars[i] = (char) i);

        var regularPage = new FontPage(font, true, true);
        regularPage.setAdditionalSpace(additionalSpace);
        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();

        return new CustomFontRenderer(regularPage, regularPage, regularPage, regularPage);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        OpenGLUtil.enableAlpha();
        resetStyles();
        int i;

        if (dropShadow) {
            i = renderString(text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, renderString(text, x, y, color, false));
        } else {
            i = renderString(text, x, y, color, false);
        }

        return i;
    }

    public int drawCenteredString(String text, float x, float y, int color) {
        OpenGLUtil.enableAlpha();
        resetStyles();
        return renderString(text, x - getStringWidth(text) / 2.0f, y, color, false);
    }

    public int drawCenteredString(String text, float x, float y, int color, boolean dropShadow) {
        OpenGLUtil.enableAlpha();
        resetStyles();
        return renderString(text, x - getStringWidth(text) / 2.0f, y, color, dropShadow);
    }

    public int drawCenteredString(String text, double x, double y, int color) {
        OpenGLUtil.enableAlpha();
        resetStyles();
        return this.renderString(text, (float) x - getStringWidth(text) / 2.0f, (float) y, color, false);
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (Objects.isNull(text)) {
            return 0;
        }

        if ((color & -67108864) == 0) {
            color |= -16777216;
        }

        if (dropShadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }

        red = (float) (color >> 16 & 255) / 255.0F;
        blue = (float) (color >> 8 & 255) / 255.0F;
        green = (float) (color & 255) / 255.0F;
        alpha = (float) (color >> 24 & 255) / 255.0F;
        OpenGLUtil.color(red, blue, green, alpha);
        posX = x * 2.0f;
        posY = y * 2.0f;

        renderStringAtPos(text, dropShadow);
        return (int) (posX / 4.0f);
    }

    private void renderStringAtPos(String text, boolean shadow) {
        var glyphPage = getCurrentGlyphPage();

        OpenGLUtil.pushMatrix();
        OpenGLUtil.scaled(0.5, 0.5, 0.5);

        OpenGLUtil.enableBlend();
        OpenGLUtil.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGLUtil.enableTexture2D();

        glyphPage.bindTexture();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        for (int i = 0; i < text.length(); ++i) {
            var c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length()) {
                var i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(locale).charAt(i + 1));

                if (i1 < 16) {
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    if (i1 < 0) {
                        i1 = 15;
                    }

                    if (shadow) {
                        i1 += 16;
                    }

                    var j1 = colorCode[i1];
                    OpenGLUtil.color((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, this.alpha);
                } else if (i1 == 17) {
                    boldStyle = true;
                } else if (i1 == 18) {
                    strikethroughStyle = true;
                } else if (i1 == 19) {
                    underlineStyle = true;
                } else if (i1 == 20) {
                    italicStyle = true;
                } else {
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    OpenGLUtil.color(red, blue, green, alpha);
                }

                ++i;
            } else {
                glyphPage = getCurrentGlyphPage();
                glyphPage.bindTexture();

                var f = glyphPage.drawChar(c0, posX, posY);
                doDraw(f, glyphPage);
            }
        }

        glyphPage.unbindTexture();
        GL11.glPopMatrix();
    }

    private void doDraw(float f, FontPage glyphPage) {
        if (strikethroughStyle) {
            OpenGLUtil.disableTexture2D();

            GL11.glBegin(7);

            GL11.glVertex2d(posX, (posY + (float) (glyphPage.getMaxFontHeight() / 2)));
            GL11.glVertex2d((posX + f), (posY + (float) (glyphPage.getMaxFontHeight() / 2)));
            GL11.glVertex2d((posX + f), (posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F));
            GL11.glVertex2d(posX, (posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F));

            GL11.glEnd();

            OpenGLUtil.enableTexture2D();
        }

        if (underlineStyle) {
            OpenGLUtil.disableTexture2D();

            GL11.glBegin(7);

            var l = this.underlineStyle ? -1 : 0;
            GL11.glVertex2d((posX + (float) l), (posY + (float) glyphPage.getMaxFontHeight()));
            GL11.glVertex2d((posX + f), (posY + (float) glyphPage.getMaxFontHeight()));
            GL11.glVertex2d((posX + f), (posY + (float) glyphPage.getMaxFontHeight() - 1.0F));
            GL11.glVertex2d((posX + (float) l), (posY + (float) glyphPage.getMaxFontHeight() - 1.0F));
            GL11.glEnd();

            OpenGLUtil.enableTexture2D();
        }

        posX += f;
    }

    private FontPage getCurrentGlyphPage() {
        if (boldStyle && italicStyle) {
            return boldItalicGlyphPage;
        } else if (boldStyle) {
            return boldGlyphPage;
        } else if (italicStyle) {
            return italicGlyphPage;
        }

        return regularGlyphPage;
    }

    private void resetStyles() {
        boldStyle = false;
        italicStyle = false;
        underlineStyle = false;
        strikethroughStyle = false;
    }

    public int getFontHeight() {
        return regularGlyphPage.getMaxFontHeight() / 2;
    }

    public int getStringWidth(String text) {
        if (Objects.isNull(text)) {
            return 0;
        }

        var width = 0;
        var size = text.length();
        var on = false;

        for (int i = 0; i < size; i++) {
            var character = text.charAt(i);

            if (character == '§') {
                on = true;
            } else if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17) {
                    boldStyle = true;
                } else if (colorIndex == 20) {
                    italicStyle = true;
                } else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }
                i++;
                on = false;
            } else {
                if (on) {
                    i--;
                }

                character = text.charAt(i);
                width += getCurrentGlyphPage().getWidth(character) - 8;
            }
        }

        return width / 2;
    }

    public String trimStringToWidth(String text, int width) {
        return trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int maxWidth, boolean reverse) {
        var stringBuilder = new StringBuilder();
        var on = false;
        var j = reverse ? text.length() - 1 : 0;
        var k = reverse ? -1 : 1;
        var width = 0;

        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            var character = text.charAt(i);
            if (character == '§') {
                on = true;
            } else if (on && character >= '0' && character <= 'r') {
                var colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17) {
                    boldStyle = true;
                } else if (colorIndex == 20) {
                    italicStyle = true;
                } else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }
                i++;
                on = false;
            } else {
                if (on) {
                    i--;
                }

                character = text.charAt(i);
                width += (getCurrentGlyphPage().getWidth(character) - 8) / 2;
            }

            if (i > width) {
                break;
            }

            if (reverse) {
                stringBuilder.insert(0, character);
            } else {
                stringBuilder.append(character);
            }
        }

        return stringBuilder.toString();
    }

}
