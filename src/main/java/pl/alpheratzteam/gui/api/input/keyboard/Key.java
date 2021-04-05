package pl.alpheratzteam.gui.api.input.keyboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Unix
 * @since 13.02.2021
 */

@Getter
@RequiredArgsConstructor
public class Key {

    private final int key, scanNode, action, mods;

    /**
     * Checking if a button has been released.
     * @return the {@code boolean} value depending on whether the button has been released.
     */
    public boolean isReleased() {
        return action == 0;
    }

    /**
     * Checking if a button has been press.
     * @return the {@code boolean} value depending on whether the button has been press.
     */
    public boolean isPress() {
        return action == 1;
    }

    /**
     * Checking if a button has been pressed.
     * @return the {@code boolean} value depending on whether the button has been pressed.
     */
    public boolean isPressed() {
        return action == 2;
    }

}