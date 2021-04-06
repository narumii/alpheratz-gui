package pl.alpheratzteam.gui.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import lombok.Getter;
import lombok.Setter;
import pl.alpheratzteam.gui.api.gui.AlpheratzScreen;
import pl.alpheratzteam.gui.api.input.keyboard.Key;
import pl.alpheratzteam.gui.api.input.keyboard.Keyboard;
import pl.alpheratzteam.gui.api.input.mouse.Mouse;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import pl.alpheratzteam.gui.api.gui.ScaledResolution;
import pl.alpheratzteam.gui.api.gui.font.CustomFontRenderer;
import pl.alpheratzteam.gui.api.timer.Timer;
import pl.alpheratzteam.gui.util.OpenGLUtil;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author Unix
 * @since 13.02.2021
 */

@Getter
public class Window {

    private final Logger logger = Logger.getLogger(Window.class.getSimpleName());
    private final Timer timer = new Timer();

    private String title;

    private int lastWidth, lastHeight, width, height;
    private boolean fullscreen;

    private long windowId;

    @Setter private Keyboard keyboard;
    @Setter private Mouse mouse;

    private CustomFontRenderer fontRenderer;
    private ScaledResolution scaledResolution;

    private AlpheratzScreen currentScreen;

    private int fpsCounter;

    /**
     * Creates a program window.
     * @param title window title.
     */
    private void createWindow(String title) {
        windowId = glfwCreateWindow(width, height, this.title = title, (fullscreen) ? glfwGetPrimaryMonitor() : 0, 0);
        if (windowId == 0) {
            throw new IllegalStateException("Failed to create window!");
        }

        if (!fullscreen) {
            var glfwVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(windowId, (glfwVidMode.width() - width) / 2, (glfwVidMode.height() - height) / 2);
        }

        glfwShowWindow(windowId);
        glfwMakeContextCurrent(windowId);
    }

    /**
     * Turns the program on/off full screen.
     * @param fullscreen on/off.
     */
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        // TODO: 13.02.2021 set full screen
//        setCallbacks();
//        createWindow(title);
//        setLocalCallbacks();
//        initializeCallbacks();
//        windowId = glfwCreateWindow(width, height, title, (this.fullscreen = fullscreen) ? glfwGetPrimaryMonitor() : 0, 0);
    }

    /**
     * Opens the program window.
     * @param title window title.
     * @param width window width.
     * @param height window height.
     * @param fullscreen window fullscreen.
     */
    public void openWindow(String title, int width, int height, boolean fullscreen) {
        setCallbacks();
        if (!glfwInit()) {
            throw new IllegalStateException("GLFW Failed to initialize!");
        }

        this.fullscreen = fullscreen;
        setSize(width, height);
        createWindow(title);

        OpenGLUtil.load(GL.createCapabilities());
        initializeCallbacks();
        fontRenderer = CustomFontRenderer.create(new Font("Sans", Font.PLAIN, 16));
        createWindowLoop();
    }
    
    public void setResizable(boolean resizable) {
        // TODO: 06.04.2021
    }

    /**
     * This method updates lwjgl 3.0 events.
     */
    public void update() {
        glfwPollEvents();
    }

    /**
     * Creating a basic window loop.
     */
    private void createWindowLoop() {
        var frameCap = 1.0 / 60.0;
        var frameTime = 0.0;
        var startTime = timer.getTime();
        var unprocessed = 0.0;

        initGL(true);
        while (!shouldClose()) {
            var canRender = false;
            var secondTime = timer.getTime();
            var passed = secondTime - startTime;

            unprocessed += passed;
            frameTime += passed;
            startTime = secondTime;

            while (unprocessed >= frameCap) {
                initGL(hasResized());

                unprocessed -= frameCap;
                canRender = true;
                update();

                if (frameTime >= 1.0) {
                    frameTime = 0;
//                    System.out.println("FPS: " + frames);
                    fpsCounter = 0;
                }
            }

            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);
                if (Objects.nonNull(currentScreen)) {
                    currentScreen.draw(this, mouse.getX(), mouse.getY());
                }

                glfwSwapBuffers(windowId);
                fpsCounter++;
            }
        }

        glfwTerminate();
    }

    /**
     * Renames the window.
     * @param title window title.
     */
    public void setTitle(String title) {
        glfwSetWindowTitle(windowId, this.title = title);
    }

    public void setIcon(String path) {
//        glfwSetWindowIcon(windowId, buffer);
    }

    /**
     * Resizes the window.
     * @param width window width.
     * @param height window height
     */
    public void setSize(int width, int height) {
        this.lastWidth = (this.width == 0) ? width : this.width;
        this.lastHeight = (this.height == 0) ? height : this.height;
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if the window has been resized.
     * @return the {@code boolean} value if the window has been increased.
     */
    public boolean hasResized() {
        return (lastWidth != width || lastHeight != height);
    }

    /**
     * Checks chether the window should be disabled.
     * @return the {@code boolean} value if the window tries to be turned off.
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(windowId);
    }

    /**
     * Set LWJGL 3.0 error callback
     */
    public void setCallbacks() {
        glfwSetErrorCallback((error, description) -> {
            throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
        });
    }

    /**
     * Removing the remaining callbacks from memory.
     */
    public void cleanCallbacks() {
        glfwFreeCallbacks(windowId);
    }

    /**
     * Setting a callback that checks if the screen has been resized.
     */
    public void setLocalCallbacks() {
        glfwSetWindowSizeCallback(windowId, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                setSize(width, height);
            }
        });
    }

    /**
     * Setting up basic callbacks.
     */
    public void initializeCallbacks() {
        cleanCallbacks();
        setLocalCallbacks();

        if (Objects.nonNull(keyboard)) {
            glfwSetKeyCallback(windowId, (window, key, scanNode, action, mods) -> {
                var keyboardKey = new Key(key, scanNode, action, mods);
                keyboard.onKeyboard(window, keyboardKey);
                if (Objects.nonNull(currentScreen)) {
                    currentScreen.keyClicked(keyboardKey);
                }
            });
        }

        if (Objects.nonNull(mouse)) {
            glfwSetCursorPosCallback(windowId, (windowId, x, y) -> mouse.set(x / scaledResolution.getScaleFactor(), y / scaledResolution.getScaleFactor()));
            glfwSetMouseButtonCallback(windowId, (windowId, button, action, mods) -> {
                mouse.setAction(action);
                mouse.click(button);

                if (Objects.nonNull(currentScreen) && action == 1) {
                    currentScreen.mouseClicked(button, action);
                }
            });
        }
    }

    public void displayGuiScreen(AlpheratzScreen gui) {
        if (Objects.nonNull(currentScreen)) {
            currentScreen.onGuiClosed();
        }

        if (Objects.nonNull(currentScreen = gui)) {
            currentScreen.setWindow(this);
            currentScreen.setSize(width, height);
            currentScreen.init();
        }
    }

    private void initGL(boolean hasResized) {
        if (hasResized) {
            setSize(width, height);
            scaledResolution = new ScaledResolution(this, 2);

            if (Objects.nonNull(currentScreen)) {
                currentScreen.setSize(scaledResolution.getScaledWidthD(), scaledResolution.getScaledHeightD());
                currentScreen.init();
            }
        }

        glViewport(0, 0, width, height);

        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledResolution.getScaledWidthD(), scaledResolution.getScaledHeightD(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();

        OpenGLUtil.translate(0.0F, 0.0F, -2000.0F);
    }

}