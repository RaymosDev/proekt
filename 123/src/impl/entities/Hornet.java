package impl.entities;

import java.awt.Graphics;
import java.awt.Image;

import gameEngine.Entity;
import gameEngine.Game;
import gameEngine.ResourceLoader;
import gameEngine.Scene;
import gameEngine.SceneObject;
import gameEngine.Vector2;
import impl.Main;
import impl.scenes.GameScene;

public class Hornet extends Entity implements DamagableEntity {
    private static final int ORIGINAL_WIDTH = 132;
    private static final int ORIGINAL_HEIGHT = 63;
    private static final int MAX_HEALTH = 1;
    private static final double ORIGINAL_SPEED = 700.0; // Оригинальная скорость
    private static final int SCORE_VALUE = 75;
    private static final double HEALTH_DROP_CHANCE = 0.15;
    private static final double BASE_CONTACT_DAMAGE = 1.0;
    private static final double LASER_COOLDOWN = 0.5;
    private static final int ORIGINAL_LASER_WIDTH = 30;
    private static final int ORIGINAL_LASER_HEIGHT = 6;
    private static final double ORIGINAL_LASER_SPEED = 1000.0; // Оригинальная скорость лазера
    private static final double BASE_LASER_DAMAGE = 1.0;

    // Масштабированные размеры
    private static final int WIDTH = scaleSize(ORIGINAL_WIDTH);
    private static final int HEIGHT = scaleSize(ORIGINAL_HEIGHT);
    private static final int LASER_WIDTH = scaleSize(ORIGINAL_LASER_WIDTH);
    private static final int LASER_HEIGHT = scaleSize(ORIGINAL_LASER_HEIGHT);

    private static final Image SPRITE_1 = ResourceLoader.loadImage("res/images/entities/hornet/Hornet1.png")
	    .getScaledInstance(WIDTH, HEIGHT, 0);
    private static final Image SPRITE_2 = ResourceLoader.loadImage("res/images/entities/hornet/Hornet2.png")
	    .getScaledInstance(WIDTH, HEIGHT, 0);
    private static final Image LASER = ResourceLoader.loadImage("res/images/entities/hornet/HornetLaser.png")
	    .getScaledInstance(LASER_WIDTH, LASER_HEIGHT, 0);

    private double health;
    private double rand;
    private double nextFireTime;
    private double speed;

    public Hornet(Vector2 position) {
	super(position, new Vector2(WIDTH, HEIGHT));
	health = MAX_HEALTH;
	rand = 6.28318530718 * Math.random();
	speed = scaleSpeed(ORIGINAL_SPEED); // Масштабируем скорость
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
	position.setY(Main.HEIGHT * 0.42 * Math.sin(rand + 2.0 * Game.getInstance().getTime()) + Main.HEIGHT / 2.0);
	position.add(-speed * Game.getInstance().getDeltaTime(), 0); // Используем масштабированную скорость
	setPosition(position);
	double currentTime = Game.getInstance().getTime();
	if (currentTime > nextFireTime) {
	    fireLaser();
	    nextFireTime = currentTime + LASER_COOLDOWN;
	}
    }

    @Override
    public void render(Graphics g) {
	Vector2 position = getPosition();
	double time = Game.getInstance().getTime();
	Image sprite;
	if (time % 0.3 < 0.15) {
	    sprite = SPRITE_1;
	} else {
	    sprite = SPRITE_2;
	}
	g.drawImage(sprite, (int) (position.getX() - WIDTH / 2.0), (int) (position.getY() - HEIGHT / 2.0), null);
    }

    private void fireLaser() {
	Laser laser = new Laser(getPosition().add(-80, -10));
	Game.getInstance().getOpenScene().addObject(laser);
    }

    @Override
    public void onCollisionEnter(Entity other) {
	if (other instanceof PlayerShip) {
	    ((PlayerShip) other).damage(Main.difficulty.getModifier() * BASE_CONTACT_DAMAGE);
	    destroy();
	}
    }

    @Override
    public void onCollisionExit(Entity other) {
    }

    @Override
    public void damage(double amount) {
	health -= amount;
	if (health <= 0) {
	    destroy();
	}
    }

    private void destroy() {
	GameScene scene = (GameScene) Game.getInstance().getOpenScene();
	scene.removeObject(this);
	scene.addScore(SCORE_VALUE);
	Explosion explosion = new Explosion(getPosition(), 150, 0.3);
	scene.addObject(explosion);
	if (Math.random() < HEALTH_DROP_CHANCE) {
	    scene.addObject(new HealthDrop(getPosition()));
	}
    }

    private static class Laser extends Projectile {
	public Laser(Vector2 position) {
	    super(position, new Vector2(LASER_WIDTH, LASER_HEIGHT), new Vector2(-scaleSpeed(ORIGINAL_LASER_SPEED), 0)); // Масштабируем скорость лазера
	}

	@Override
	public void initialize() {
	    ResourceLoader.loadAudioClip("res/audio/HornetLaser.wav").start();
	}

	@Override
	public void render(Graphics g) {
	    Vector2 position = getPosition();
	    g.drawImage(LASER, (int) (position.getX() - LASER_WIDTH / 2.0),
		    (int) (position.getY() - LASER_HEIGHT / 2.0), null);
	}

	@Override
	public void onCollisionEnter(Entity other) {
	    if (other instanceof PlayerShip) {
		((PlayerShip) other).damage(BASE_LASER_DAMAGE * Main.difficulty.getModifier());
		Scene scene = Game.getInstance().getOpenScene();
		scene.removeObject(this);
		scene.addObject(new LaserSpark(getPosition().add(LASER_WIDTH / 2.0, 0)));
	    } else if (other instanceof Projectile) {
		Scene scene = Game.getInstance().getOpenScene();
		scene.removeObject(this);
		scene.addObject(new LaserSpark(getPosition().add(LASER_WIDTH / 2.0, 0)));
	    }
	}

	@Override
	public void onCollisionExit(Entity other) {
	}
    }

    private static class LaserSpark extends SceneObject {
	private static final double DURATION = 0.1;
	private static final int LASER_SPARK_WIDTH = 35;
	private static final int LASER_SPARK_HEIGHT = 35;
	private static final Image LASER_SPARK = ResourceLoader
		.loadImage("res/images/entities/hornet/HornetLaserSpark.png")
		.getScaledInstance(LASER_SPARK_WIDTH, LASER_SPARK_HEIGHT, 0);

	private Vector2 position;
	private double deathTime;

	private LaserSpark(Vector2 position) {
	    this.position = position;
	    deathTime = Game.getInstance().getTime() + DURATION;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void tick() {
	    if (Game.getInstance().getTime() > deathTime) {
		Game.getInstance().getOpenScene().removeObject(this);
	    }
	}

	@Override
	public void render(Graphics g) {
	    int x = (int) (position.getX() - LASER_SPARK_WIDTH / 2.0);
	    int y = (int) (position.getY() - LASER_SPARK_HEIGHT / 2.0);
	    g.drawImage(LASER_SPARK, x, y, null);
	}

	@Override
	public void dispose() {
	}
    }
}
