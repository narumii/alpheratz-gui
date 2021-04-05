package pl.alpheratzteam.gui.api.callback;

/**
 * @author Unix
 * @since 13.02.2021
 */

public interface Callback<T> {

    /**
     * Callback
     * @param value value in callback
     */
    void onCallback(T value);

}