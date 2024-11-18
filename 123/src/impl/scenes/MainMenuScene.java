package impl.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import gameEngine.Game;
import gameEngine.InputManager;
import gameEngine.ResourceLoader;
import impl.Difficulty;
import impl.Main;

/**
 * @author Marko Milovanovic
 */
public class MainMenuScene extends SceneWithKeys {
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36);
    // list of options
    private final String[] MAIN_MENU_OPTIONS = { "Старт", "Сложность", "Титры", "Выйти" };
    private final String[] SETTINGS_OPTIONS = { "Простоя", "Средняя", "Высокая", "Назад" };

    private BufferedImage backgroundImage;
    private Image title;
    private Clip backgroundMusic;

    // Stores the current option the user has highlighted.
    private int currentOption;
    // The current scene
    private String sceneOption;

    public MainMenuScene(String sceneOption) {
        this.sceneOption = sceneOption;
    }

    public MainMenuScene() {
        this.sceneOption = "Main"; // Установите начальную сцену на "Main"
    }

    @Override
    public void initialize() {
        backgroundImage = ResourceLoader.toBufferedImage(
            ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png").getScaledInstance(24750, 825, 0));
        title = ResourceLoader.loadImage("res/images/ui/Title.png").getScaledInstance(889, 322, 0);

        backgroundMusic = ResourceLoader.loadAudioClip("res/audio/MainMenuMusic.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        addObject(new FadeIn(1.0));
    }

    @Override
    public void render(Graphics g) {
        InputManager inputManager = Game.getInstance().getInputManager();
        g.setFont(UI_FONT);
        g.setColor(Color.WHITE);

        double time = Game.getInstance().getTime();
        int x = (int) (time * 50 % 22195);
        Image backgroundSubImage = backgroundImage.getSubimage(x, 0, Main.WIDTH, Main.HEIGHT);
        g.drawImage(backgroundSubImage, 0, 0, null);

        // render background and text
        if (sceneOption.equals("Main")) {
            g.drawImage(title, 455, 45, null);
            currentOption = upDown(inputManager, MAIN_MENU_OPTIONS, currentOption);
            renderScrollingMenus(g, MAIN_MENU_OPTIONS, currentOption);
            mainMenuEnter(inputManager);
        } else if (sceneOption.equals("Difficulty")) {
            g.drawImage(title, 455, 45, null);
            currentOption = upDown(inputManager, SETTINGS_OPTIONS, currentOption);
            renderScrollingMenus(g, SETTINGS_OPTIONS, currentOption);
            settingsMenuEnter(inputManager);
        } else if (sceneOption.equals("Credits")) {
            creditsScene(g, inputManager);
        }
        super.render(g);
    }

    /**
     * Decides what enter will do depending on the which option is highlighted,
     * picks the option selected
     * 
     * @param inputManager
     */
    public void mainMenuEnter(InputManager inputManager) {
        // depending on the option selected, enter will do something else
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            onSound();
            if (currentOption == 0) {
                backgroundMusic.stop();
                Game.getInstance().loadScene(new GameScene());
            } else if (currentOption == 1) {
                sceneOption = "Difficulty";
                currentOption = Main.difficulty.ordinal();
            } else if (currentOption == 2) {
                sceneOption = "Credits";
                addObject(new FadeIn(1.0));
            } else if (currentOption == 3) {
                Game.getInstance().stop();
            }
        }
    }

    /**
     * Decides what enter will do depending on the which option is highlighted, sets
     * a difficulty or returns the user to the main menu
     * 
     * @param inputManager
     */
    public void settingsMenuEnter(InputManager inputManager) {
        // depending on the option selected, enter will do something else
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            onSound();
            if (currentOption == 0) {
                Main.difficulty = Difficulty.EASY;
            } else if (currentOption == 1) {
                Main.difficulty = Difficulty.MEDIUM;
            } else if (currentOption == 2) {
                Main.difficulty = Difficulty.HARD;
            }
            currentOption = 1;
            sceneOption = "Main";
        }
    }

    /**
     * The credits scene: shows us who made the game as well as a button to return
     * us to the main menu
     * 
     * @param g
     * @param inputManager
     */
    public void creditsScene(Graphics g, InputManager inputManager) {
        g.drawString("ТИТРЫ", 825, 105);
        String[] lines = { "Разработчики:",
            "", "Мищиряков Р. А.", "Степанов М. Д.", "Евдокимов П. С.", "",
           };
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int width = g.getFontMetrics().stringWidth(line);
            g.drawString(lines[i], 900 - width / 2, 190 + i * 50);
        }
        returnToMenuOption(g, inputManager);
    }

    /**
     * Creates a big red RETURN TO MENU button that returns the user to the main
     * menu
     * 
     * @param g
     * @param inputManager
     */
    public void returnToMenuOption(Graphics g, InputManager inputManager) {
        if (Game.getInstance().getTime() % 1.5 < 0.9) {
            g.drawString("НАЖМИТЕ ENTER", 735, 735);
        }
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            currentOption = 0;
            onSound();
            addObject(new FadeIn(1.0));
            sceneOption = "Main"; // Установите сцену на "Main" при возврате
        }
    }
}
