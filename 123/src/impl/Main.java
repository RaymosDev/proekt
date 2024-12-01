package impl;

import gameEngine.Game;
import gameEngine.ResourceLoader;
import impl.scenes.MainMenuScene;
import java.awt.Image;

public class Main {
    public static  int WIDTH = 1270;
    public static  int HEIGHT = 640;

    public static Difficulty difficulty = Difficulty.MEDIUM;

    public static void main(String[] args) {
	Image icon = ResourceLoader.loadImage("res/images/Icon.png");
	Game game = new Game("Space", WIDTH, HEIGHT, icon);
	game.start();
	game.loadScene(new MainMenuScene());
    }
}