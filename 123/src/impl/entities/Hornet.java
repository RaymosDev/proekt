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
import impl.ResolutionConfig;
import impl.scenes.GameScene;

public class Hornet extends Entity implements DamagableEntity {
    private static int WIDTH;
    private static int HEIGHT;
    private static final int MAX_HEALTH = 2;
    private static final double SPEED = 700.0;
    private static final int SCORE_VALUE = 75;
    private static final double HEALTH_DROP_CHANCE = 0.15;
    private static final double BASE_CONTACT_DAMAGE = 1.0;
    private static final double LASER_COOLDOWN = 0.5;
    private static int LASER_WIDTH; 
    private static int LASER_HEIGHT;
    private static final double LASER_SPEED = 1000.0;
    private static final double BASE_LASER_DAMAGE = 1.0;
    private static Image SPRITE_1;
    private static Image SPRITE_2;
    private static Image LASER;

    private double health;
    private double rand;
    private double nextFireTime;

    static {
        updateDimensions();
    }

    public Hornet(Vector2 position) {
        super(position, new Vector2(WIDTH, HEIGHT));
        health = MAX_HEALTH;
        rand = 6.28318530718 * Math.random();
    }

    public static void updateDimensions() {
        ResolutionConfig.Resolution hornetSize = ResolutionConfig.getHornetSize();
        WIDTH = hornetSize.width;
        HEIGHT = hornetSize.height;

        ResolutionConfig.Resolution laserSize = ResolutionConfig.getLaserHornetSize();
        LASER_WIDTH = laserSize.width;
        LASER_HEIGHT = laserSize.height;

        SPRITE_1 = ResourceLoader.loadImage("res/images/entities/hornet/Hornet1.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        SPRITE_2 = ResourceLoader.loadImage("res/images/entities/hornet/Hornet2.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        LASER = ResourceLoader.loadImage("res/images/entities/hornet/HornetLaser.png")
                .getScaledInstance(LASER_WIDTH, LASER_HEIGHT, Image.SCALE_SMOOTH);
    }

    @Override
    public void tick() {
        Vector2 position = getPosition();
        position.setY(Main.HEIGHT * 0.42 * Math.sin(rand + 2.0 * Game.getInstance().getTime()) + Main.HEIGHT / 2.0);
        position.add(-SPEED * Game.getInstance().getDeltaTime(), 0);
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
        Image sprite = (time % 0.3 < 0.15) ? SPRITE_1 : SPRITE_2;
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

    public static class Laser extends Projectile {
        public Laser(Vector2 position) {
            super(position, new Vector2(LASER_WIDTH, LASER_HEIGHT), new Vector2(-LASER_SPEED, 0));
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
                .getScaledInstance(LASER_SPARK_WIDTH, LASER_SPARK_HEIGHT, Image.SCALE_SMOOTH);

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
