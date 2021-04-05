package pl.alpheratzteam.gui.api.input.mouse;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Unix
 * @since 13.02.2021
 */

@Getter
public abstract class Mouse {

    private double x, y;

    @Setter private int action;

    /**
     * Sets the x and y mouse positions.
     * @param x mouse x position.
     * @param y mouse y position.
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public abstract void click(int button);

    /**
     * Checking if a mouse button has been released.
     * @return the {@code boolean} value depending on whether the button has been released.
     */
    public boolean isReleased() {
        return action == 0;
    }

    /**
     * Checking if a mouse button has been press.
     * @return the {@code boolean} value depending on whether the button has been press.
     */
    public boolean isPress() {
        return action == 1;
    }

    /**
     * Checking if a mouse button has been pressed.
     * @return the {@code boolean} value depending on whether the button has been pressed.
     */
    public boolean isPressed() {
        return action == 2;
    }

}