package impl.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

import gameEngine.InputManager;
import gameEngine.ResourceLoader;
import gameEngine.Scene;
import impl.Main;

public class SceneWithKeys extends Scene {
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36);
    public final Image buttonSelected = ResourceLoader.loadImage("res/images/ui/ButtonHover.png").getScaledInstance(336,
	    56, 0);
    public final Image button = ResourceLoader.loadImage("res/images/ui/Button.png").getScaledInstance(336, 56, 0);

    public void initialize() {
    }

  public void renderScrollingMenus(Graphics g, String[] options, int currentOption) {
    Image buttonState;
    g.setFont(UI_FONT);
    int containerWidth = Main.WIDTH;
    int offset = 120;
    int startY = (Main.HEIGHT - (options.length * 67)) / 2 + offset;

    for (int i = 0; i < options.length; i++) {
        if (currentOption == i) {
            g.setColor(Color.WHITE);
            buttonState = buttonSelected;
        } else {
            g.setColor(Color.WHITE);
            buttonState = button;
        }

        int buttonX = (containerWidth - buttonState.getWidth(null)) / 2;
        g.drawImage(buttonState, buttonX, startY + 67 * i, null);

        String option = options[i];
        int width = g.getFontMetrics().stringWidth(option);
        int textX = (containerWidth - width) / 2;
        g.drawString(option, textX, startY + 39 + 67 * i);
    }
}

    public int upDown(InputManager inputManager, String[] options, int currentOption) {

	if (inputManager.getKeyDown(KeyEvent.VK_DOWN)) {
	    onSound();
	    currentOption++;
	    if (currentOption >= options.length) {
		return 0;
	    }
	}

	if (inputManager.getKeyDown(KeyEvent.VK_UP)) {
	    onSound();
	    currentOption--;
	    if (currentOption < 0) {
		return options.length - 1;
	    }
	}
	return currentOption;
    }

    public void onSound() {
	ResourceLoader.loadAudioClip("res/audio/Button.wav").start();
    }
}
