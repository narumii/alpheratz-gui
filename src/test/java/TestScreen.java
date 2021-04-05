import pl.alpheratzteam.gui.api.gui.AlpheratzScreen;
import pl.alpheratzteam.gui.api.gui.button.AlpheratzButton;
import pl.alpheratzteam.gui.window.Window;

/**
 * @author Unix
 * @since 13.02.2021
 */

public class TestScreen extends AlpheratzScreen {

    @Override
    public void init() {
        super.init();
        buttons.add(new AlpheratzButton(width / 2.0F - 95, height / 2.0F, 200, 20, "click here!", button -> System.out.println("Hello world from alpheratz-gui!")));
    }

    @Override
    public void draw(Window window, double mouseX, double mouseY) {
        super.draw(window, mouseX, mouseY);
    }

}