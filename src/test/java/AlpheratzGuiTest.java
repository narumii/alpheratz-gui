import org.lwjgl.glfw.GLFW;
import pl.alpheratzteam.gui.api.input.mouse.Mouse;
import pl.alpheratzteam.gui.window.Window;

/**
 * @author Unix
 * @since 13.02.2021
 */

public class AlpheratzGuiTest {

    public static void main(String[] args) {
        var window = new Window();
        window.setKeyboard((windowId, key) -> {
            if (!key.isPress()) {
                return;
            }

//            if (key.getKey() == GLFW.GLFW_KEY_V) {
//                window.setFullscreen(!window.isFullscreen());
//            }

            System.out.println(GLFW.glfwGetKeyName(key.getKey(), key.getScanNode()));
        });
        window.setMouse(new Mouse() {
            @Override
            public void click(int button) {
            }
        });

        window.displayGuiScreen(new TestScreen());
        window.openWindow("Test", 854, 480, false);
    }

}