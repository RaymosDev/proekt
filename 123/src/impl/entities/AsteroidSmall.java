package impl.entities;

import java.awt.Graphics;
import java.awt.Image;

import gameEngine.Entity;
import gameEngine.Game;
import gameEngine.ResourceLoader;
import gameEngine.Vector2;
import impl.Main;
import impl.scenes.GameScene;

public class AsteroidSmall extends Entity implements DamagableEntity {
    private static final double BASE_DAMAGE_AMOUNT = 1;
    private static final double MAX_HEALTH = 3;
    private static final double ORIGINAL_BASE_SPEED = 500; // Оригинальная скорость
    private static final int BASE_SCORE_VALUE = 20;

    // Оригинальные размеры
    private static final int ORIGINAL_WIDTH = 100;
    private static final int ORIGINAL_HEIGHT = 100;

    // Масштабированные размеры
    private static final int WIDTH = scaleSize(ORIGINAL_WIDTH);
    private static final int HEIGHT = scaleSize(ORIGINAL_HEIGHT);
    
    public static final Image SPRITE = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidSmall.png")
	    .getScaledInstance(WIDTH, HEIGHT, 0);

    private Vector2 velocity;
    private double currentHealth;

    public AsteroidSmall(Vector2 position, Vector2 direction) {
	super(position, new Vector2(WIDTH, HEIGHT));
	double difficultyModifier = Main.difficulty.getModifier();
	this.velocity = direction.clone().normalize().multiply(scaleSpeed(ORIGINAL_BASE_SPEED) * difficultyModifier);
	currentHealth = MAX_HEALTH * difficultyModifier;
    }

    private static int scaleSize(int originalSize) {
        double scaleX = (double) Main.WIDTH / 1800; // Используйте ваше целевое разрешение
        double scaleY = (double) Main.HEIGHT / 800; // Используйте ваше целевое разрешение
        return (int) (originalSize * Math.min(scaleX, scaleY)); // Применяем масштабирование
    }

    private static double scaleSpeed(double originalSpeed) {
        double scaleX = (double) Main.WIDTH / 1800; // Используйте ваше целевое разрешение
        double scaleY = (double) Main.HEIGHT / 800; // Используйте ваше целевое разрешение
        return originalSpeed * Math.min(scaleX, scaleY); // Применяем масштабирование
    }

    @Override
    public void tick() {
	Vector2 position = getPosition();
	position.add(velocity.clone().multiply(Game.getInstance().getDeltaTime()));
	setPosition(position);
    }

    @Override
    public void render(Graphics g) {
	Vector2 position = getPosition();
	g.drawImage(SPRITE, (int) position.getX() - WIDTH / 2, (int) position.getY() - HEIGHT / 2, null);
    }

    @Override
    public void onCollisionEnter(Entity other) {
	if (other instanceof PlayerShip) {
	    ((PlayerShip) other).damage(BASE_DAMAGE_AMOUNT * Main.difficulty.getModifier());
	    destroy();
	}
    }

    @Override
    public void onCollisionExit(Entity other) {
    }

    @Override
    public void damage(double amount) {
	currentHealth -= amount;
	if (currentHealth <= 0) {
	    destroy();
	}
    }

    private void destroy() {
	GameScene scene = (GameScene) Game.getInstance().getOpenScene();
	scene.removeObject(this);
	scene.addScore((int) (BASE_SCORE_VALUE * Main.difficulty.getModifier()));
	Explosion explosion = new Explosion(getPosition(), 100, 0.2);
	scene.addObject(explosion);
    }
}
