package pl.alpheratzteam.gui.api.timer;

/**
 * @author Unix
 * @since 13.02.2021
 */

public final class Timer {

    /**
     * Returns the running time of the program in double.
     * @return {@code double} value of the running time.
     */
    public double getTime() {
        return (double) System.nanoTime() / (double) 1000000000L;
    }

}