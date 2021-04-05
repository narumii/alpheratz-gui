package pl.alpheratzteam.gui.api.gui.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.alpheratzteam.gui.api.gui.ScreenHelper;
import pl.alpheratzteam.gui.util.OpenGLUtil;
import pl.alpheratzteam.gui.window.Window;

/**
 * @author Unix
 * @since 13.02.2021
 */

@Getter
@AllArgsConstructor
public class AlpheratzButton extends ScreenHelper {

    private double x, y, width, height;
    private boolean enabled = true, visible = true, hovered;

    private String text;
    private ButtonCallback callback;

    public AlpheratzButton(double x, double y, double width, double height, String text, ButtonCallback callback) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.callback = callback;
    }

    public interface ButtonCallback {

        void onPress(AlpheratzButton button);

    }

    public void draw(Window window, double mouseX, double mouseY) {
        hovered = enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        if (!visible) {
            return;
        }

        OpenGLUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawRect(x, y, x + width, y + height, (hovered) ? -16742593 : -16727358);
        drawLineLoop(x, y, width, height, 2.5f, -1);
        window.getFontRenderer().drawCenteredString(text, x + width / 2, y + height - 16, -7792102);
    }

    public void mouseClicked() {
        callback.onPress(this);
    }
}