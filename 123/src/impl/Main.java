package impl;

import gameEngine.Game;
import gameEngine.ResourceLoader;
import impl.scenes.MainMenuScene;
import java.awt.Image;

public class Main {
    public static final int WIDTH = 1800;
    public static final int HEIGHT = 800;

    public static Difficulty difficulty = Difficulty.MEDIUM;

    public static void main(String[] args) {
	Image icon = ResourceLoader.loadImage("res/images/Icon.png");
	Game game = new Game("Space", WIDTH, HEIGHT, icon);
	game.start();
	game.loadScene(new MainMenuScene());
    }
}
