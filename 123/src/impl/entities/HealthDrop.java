package impl.entities;

import java.awt.Graphics;
import java.awt.Image;

import gameEngine.Game;
import gameEngine.ResourceLoader;
import gameEngine.Vector2;
import impl.PlayerFollowingText;
import impl.ResolutionConfig;

public class HealthDrop extends Drop {
    private static final double HEAL_AMOUNT = 4;
    private static final double LIFETIME = 10.0;

    private static int WIDTH;
    private static int HEIGHT;
    private static Image SPRITE;

    static {
        updateDimensions();
    }

    public HealthDrop(Vector2 position) {
        super(position, new Vector2(WIDTH, HEIGHT), LIFETIME);
    }

    public static void updateDimensions() {
        ResolutionConfig.Resolution currentResolution = ResolutionConfig.getHealthDropSize();
        WIDTH = currentResolution.width;
        HEIGHT = currentResolution.height;
        SPRITE = ResourceLoader.loadImage("res/images/entities/drops/HealthDrop.png")
                .getScaledInstance(WIDTH, HEIGHT, 0);
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
