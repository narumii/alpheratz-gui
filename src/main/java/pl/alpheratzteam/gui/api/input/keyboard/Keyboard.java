package pl.alpheratzteam.gui.api.input.keyboard;

/**
 * @author Unix
 * @since 13.02.2021
 */

public interface Keyboard {

    /**
     * Checking whether a key on the keyboard has been pressed.
     * @param window window id.
     * @param key key press.
     */
    void onKeyboard(long window, Key key);

}