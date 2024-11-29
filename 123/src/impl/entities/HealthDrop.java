package impl.entities;

import java.awt.Graphics;
import java.awt.Image;

import gameEngine.Game;
import gameEngine.ResourceLoader;
import gameEngine.Vector2;
import impl.Main;
import impl.PlayerFollowingText;

public class HealthDrop extends Drop {
    private static final double HEAL_AMOUNT = 4;
    private static final double LIFETIME = 10.0;
    private static final int ORIGINAL_WIDTH = 30; // Оригинальная ширина
    private static final int ORIGINAL_HEIGHT = 48; // Оригинальная высота
    private static final int WIDTH = scaleSize(ORIGINAL_WIDTH); // Масштабированная ширина
    private static final int HEIGHT = scaleSize(ORIGINAL_HEIGHT); // Масштабированная высота
    private static final Image SPRITE = ResourceLoader.loadImage("res/images/entities/drops/HealthDrop.png")
	    .getScaledInstance(WIDTH, HEIGHT, 0);

    public HealthDrop(Vector2 position) {
	super(position, new Vector2(WIDTH, HEIGHT), LIFETIME);
    }

    private static int scaleSize(int originalSize) {
        double scaleX = (double) Main.WIDTH / 1800; // Используйте ваше целевое разрешение
        double scaleY = (double) Main.HEIGHT / 800; // Используйте ваше целевое разрешение
        return (int) (originalSize * Math.min(scaleX, scaleY)); // Применяем масштабирование
    }

    @Override
    public void onPickup(PlayerShip player) {
	player.heal(HEAL_AMOUNT);
	ResourceLoader.loadAudioClip("res/audio/RestoreHP.wav").start();
	PlayerFollowingText text = new PlayerFollowingText("+" + (int) HEAL_AMOUNT + " HP");
	Game.getInstance().getOpenScene().addObject(text);
    }

    @Override
    public void render(Graphics g) {
	Vector2 position = getPosition();
	g.drawImage(SPRITE, (int) (position.getX() - WIDTH / 2.0), (int) (position.getY() - HEIGHT / 2.0), null);
    }
}
