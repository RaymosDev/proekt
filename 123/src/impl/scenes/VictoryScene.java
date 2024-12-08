package impl.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.sound.sampled.Clip;

import gameEngine.Game;
import gameEngine.InputManager;
import gameEngine.ResourceLoader;
import impl.Main;

public class VictoryScene extends SceneWithKeys {
    private Image backgroundImage;
    private Clip backgroundMusic;
    private int currentOption;
    private int score;
    private boolean wasNewHighScore;
    private int highScore;

    private static final Font VICTORY_FONT = ResourceLoader.loadFont("res/Font.ttf", 50);
    private static final Font SCORE_FONT = ResourceLoader.loadFont("res/Font.ttf", 50);
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36);

    private final String[] VICTORY_OPTIONS = { "Перезапуск", "Главное меню", "Выйти" };

    public VictoryScene(int score) {
        this.score = score;
        backgroundImage = ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png")
                .getScaledInstance(24750, 825, 0);
    }

    public void initialize() {
      backgroundMusic = ResourceLoader.loadAudioClip("res/audio/DeathScene.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        addObject(new FadeIn(5.0));

        try {
            highScore = getHighScore();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (score > highScore) {
            wasNewHighScore = true;
            try {
                setHighScore(score);
            } catch (IOException e) {
                e.printStackTrace();
            }
            highScore = score;
        } else {
            wasNewHighScore = false;
        }
    }

    public void render(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null);
        super.render(g);
        InputManager inputManager = Game.getInstance().getInputManager();
        g.setColor(Color.WHITE);
        g.setFont(VICTORY_FONT);

        String victoryMessage = "ВЫ ПОБЕДИЛИ!";
        int containerWidth = Main.WIDTH;
        int victoryMessageWidth = g.getFontMetrics().stringWidth(victoryMessage);
        int victoryMessageX = (containerWidth - victoryMessageWidth) / 2;
        g.drawString(victoryMessage, victoryMessageX, 60);

        g.setFont(SCORE_FONT);
        String scoreText = "Ваш счёт: " + score;
        int scoreTextWidth = g.getFontMetrics().stringWidth(scoreText);
        int scoreTextX = (containerWidth - scoreTextWidth) / 2;
        g.drawString(scoreText, scoreTextX, 130);

        if (wasNewHighScore) {
            String highScoreText = "Новый рекорд!";
            int highScoreTextWidth = g.getFontMetrics().stringWidth(highScoreText);
            int highScoreTextX = (containerWidth - highScoreTextWidth) / 2;
            g.drawString(highScoreText, highScoreTextX, 200);
        } else {
            String highScoreText = "Ваш рекорд: " + highScore;
            int highScoreTextWidth = g.getFontMetrics().stringWidth(highScoreText);
            int highScoreTextX = (containerWidth - highScoreTextWidth) / 2;
            g.drawString(highScoreText, highScoreTextX, 200);
        }

        g.setFont(UI_FONT);
        currentOption = upDown(inputManager, VICTORY_OPTIONS, currentOption);
        renderScrollingMenus(g, VICTORY_OPTIONS, currentOption);
        victoryMenuEnter(inputManager);
    }

    public void victoryMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            if (currentOption == 0) {
                Game.getInstance().loadScene(new GameScene());
            } else if (currentOption == 1) {
                Game.getInstance().loadScene(new MainMenuScene("Main"));
            } else if (currentOption == 2) {
                Game.getInstance().stop();
            }
        }
    }

    public int getHighScore() throws FileNotFoundException {
        File highScoreFile;
        int highScore = 0;

        highScoreFile = new File("highScore.txt");

        if (highScoreFile.exists()) {
            Scanner scanner = new Scanner(highScoreFile);
            if (scanner.hasNext()) {
                highScore = scanner.nextInt();
            }
            scanner.close();
        }
        return highScore;
    }

    public void setHighScore(int highScore) throws IOException {
        File highScoreFile = new File("highScore.txt");

        if (!highScoreFile.exists()) {
            highScoreFile.createNewFile();
        }

        FileWriter fw = new FileWriter(highScoreFile);
        String highScoreString = "" + highScore;
        fw.write(highScoreString);
        fw.close();
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundMusic.stop();
    }
}
