package impl.entities;

import java.awt.Graphics;
import java.awt.Image;

import gameEngine.Entity;
import gameEngine.Game;
import gameEngine.ResourceLoader;
import gameEngine.Vector2;
import impl.Main;
import impl.ResolutionConfig;
import impl.scenes.GameScene;

public class AsteroidSmall extends Entity implements DamagableEntity {
    private static final double BASE_DAMAGE_AMOUNT = 1;
    private static final double MAX_HEALTH = 3;
    private static final double BASE_SPEED = 500;
    private static final int BASE_SCORE_VALUE = 20;

    private static int WIDTH;
    private static int HEIGHT;
    private static Image SPRITE;

    private Vector2 velocity;
    private double currentHealth;

    static {
        updateDimensions();
    }

    public AsteroidSmall(Vector2 position, Vector2 direction) {
        super(position, new Vector2(WIDTH, HEIGHT));
        double difficultyModifier = Main.difficulty.getModifier();
        this.velocity = direction.clone().normalize().multiply(BASE_SPEED * difficultyModifier);
        currentHealth = MAX_HEALTH * difficultyModifier;
    }

    public static void updateDimensions() {
        ResolutionConfig.Resolution currentResolution = ResolutionConfig.getAsteroidSmallSize();
        WIDTH = currentResolution.width;
        HEIGHT = currentResolution.height;

        SPRITE = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidSmall.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
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
