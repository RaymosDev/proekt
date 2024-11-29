package impl.entities;

import java.awt.Graphics;
import java.awt.Image;

import gameEngine.Entity;
import gameEngine.Game;
import gameEngine.ResourceLoader;
import gameEngine.Vector2;
import impl.Main;
import impl.scenes.GameScene;

public class AsteroidLarge extends Entity implements DamagableEntity {
    private static final double BASE_DAMAGE_AMOUNT = 3;
    private static final double MAX_HEALTH = 5;
    private static final double ORIGINAL_SPEED = 300; // Оригинальная скорость
    private static final int BASE_SCORE_VALUE = 50;

    // Оригинальные размеры
    private static final int ORIGINAL_WIDTH = 200;
    private static final int ORIGINAL_HEIGHT = 200;

    // Масштабированные размеры
    private static final int WIDTH = scaleSize(ORIGINAL_WIDTH);
    private static final int HEIGHT = scaleSize(ORIGINAL_HEIGHT);

    public static final Image SPRITE_1 = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidLarge1.png")
        .getScaledInstance(WIDTH, HEIGHT, 0);
    public static final Image SPRITE_2 = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidLarge2.png")
        .getScaledInstance(WIDTH, HEIGHT, 0);
    public static final Image SPRITE_3 = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidLarge3.png")
        .getScaledInstance(WIDTH, HEIGHT, 0);

    private Vector2 velocity;
    private double currentHealth;

    public AsteroidLarge(Vector2 position, Vector2 direction) {
        super(position, new Vector2(WIDTH, HEIGHT));
        double difficultyModifier = Main.difficulty.getModifier();
        this.velocity = direction.clone().normalize().multiply(scaleSpeed(ORIGINAL_SPEED) * difficultyModifier);
        currentHealth = MAX_HEALTH;
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
        Image sprite;
        if (currentHealth < 3) {
            sprite = SPRITE_3;
        } else if (currentHealth < 5) {
            sprite = SPRITE_2;
        } else {
            sprite = SPRITE_1;
        }
        g.drawImage(sprite, (int) position.getX() - WIDTH / 2, (int) position.getY() - HEIGHT / 2, null);
    }

    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof PlayerShip) {
            ((PlayerShip) other).damage(BASE_DAMAGE_AMOUNT * Main.difficulty.getModifier());
            destroy(false);
        }
    }

    @Override
    public void onCollisionExit(Entity other) {
    }

    @Override
    public void damage(double amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            destroy(true);
        }
    }

    private void destroy(boolean split) {
        ResourceLoader.loadAudioClip("res/audio/AsteroidHit.wav").start();
        GameScene scene = (GameScene) Game.getInstance().getOpenScene();
        scene.addScore((int) (BASE_SCORE_VALUE * Main.difficulty.getModifier()));
        Explosion explosion = new Explosion(getPosition(), 200, 0.4);
        scene.addObject(explosion);
        scene.removeObject(this);
        if (split) {
            Vector2 position = getPosition();
            scene.addObject(new AsteroidSmall(position, velocity));
            velocity.rotate(-Math.toRadians(30));
            scene.addObject(new AsteroidSmall(position, velocity));
            velocity.rotate(Math.toRadians(60));
            scene.addObject(new AsteroidSmall(position, velocity));
        }
    }
}
