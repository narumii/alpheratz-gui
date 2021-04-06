package pl.alpheratzteam.gui.api.gui;

import lombok.Setter;
import pl.alpheratzteam.gui.api.gui.button.AlpheratzButton;
import pl.alpheratzteam.gui.api.input.keyboard.Key;
import pl.alpheratzteam.gui.window.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Unix
 * @since 13.02.2021
 */

public class AlpheratzScreen extends ScreenHelper {

    protected final List<AlpheratzButton> buttons = new ArrayList<>();

    protected double width, height;

    @Setter protected Window window;

    public void init() {
        buttons.clear();
    }

    public void onGuiClosed() {
    }

    public void keyClicked(Key key) {
    }

    public void mouseClicked(int button, int action) {
        if (button != 0) {
            return;
        }

        buttons.stream()
                .filter(AlpheratzButton::isHovered)
                .findFirst()
                .ifPresent(AlpheratzButton::mouseClicked);
    }

    public void draw(Window window, double mouseX, double mouseY) {
        buttons.forEach(button -> button.draw(window, mouseX, mouseY));
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

}