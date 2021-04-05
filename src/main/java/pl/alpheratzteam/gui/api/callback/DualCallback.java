package pl.alpheratzteam.gui.api.callback;

/**
 * @author Unix
 * @since 13.02.2021
 */

public interface DualCallback<A, B> {

    /**
     * Dual callback.
     * @param first first value in callback
     * @param second second value in callback
     */
    void onCallback(A first, B second);

}