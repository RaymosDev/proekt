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
    private final String[] MAIN_MENU_OPTIONS = { "Старт", "Сложность", "Разрешение", "Титры", "Выйти" };
    private final String[] SETTINGS_OPTIONS = { "Простая", "Средняя", "Высокая", "Назад" };
    private final String[] RESOLUTION_OPTIONS = { "1230x710", "1800x800", "Назад" };

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
            // Центрируем изображение title
            int imageWidth = title.getWidth(null); // Получаем ширину изображения
            int xTitle = (Main.WIDTH - imageWidth) / 2; // Вычисляем координату X для центрирования
            g.drawImage(title, xTitle, 45, null); // Рисуем изображение по центру

            currentOption = upDown(inputManager, MAIN_MENU_OPTIONS, currentOption);
            renderScrollingMenus(g, MAIN_MENU_OPTIONS, currentOption);
            mainMenuEnter(inputManager);
        } else if (sceneOption.equals("Difficulty")) {
            // Центрируем изображение title
            int imageWidth = title.getWidth(null); // Получаем ширину изображения
            int xTitle = (Main.WIDTH - imageWidth) / 2; // Вычисляем координату X для центрирования
            g.drawImage(title, xTitle, 45, null); // Рисуем изображение по центру

            currentOption = upDown(inputManager, SETTINGS_OPTIONS, currentOption);
            renderScrollingMenus(g, SETTINGS_OPTIONS, currentOption);
            settingsMenuEnter(inputManager);
        } else if (sceneOption.equals("Resolution")) {
            // Центрируем изображение title
            int imageWidth = title.getWidth(null); // Получаем ширину изображения
            int xTitle = (Main.WIDTH - imageWidth) / 2; // Вычисляем координату X для центрирования
            g.drawImage(title, xTitle, 45, null); // Рисуем изображение по центру

            currentOption = upDown(inputManager, RESOLUTION_OPTIONS, currentOption);
            renderScrollingMenus(g, RESOLUTION_OPTIONS, currentOption);
            resolutionMenuEnter(inputManager);
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
                currentOption = Main.difficulty.ordinal(); // Установите текущую сложность
            } else if (currentOption == 2) {
                sceneOption = "Resolution";
                // Установите текущий выбор разрешения в зависимости от текущего разрешения
                currentOption = (Main.WIDTH == 1230) ? 0 : 1; // 0 для 1230x720, 1 для 1800x800
            } else if (currentOption == 3) {
                sceneOption = "Credits";
                addObject(new FadeIn(1.0));
            } else if (currentOption == 4) {
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
     * Handles resolution menu selection
     * 
     * @param inputManager
     */
    public void resolutionMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            onSound();
            
            // Проверяем, была ли выбрана опция "Назад"
            if (currentOption == 2) {
                // Возвращаемся в главное меню без изменения разрешения
                sceneOption = "Main"; // Вернуться в главное меню
                return; // Завершить выполнение метода
            }

            // Устанавливаем новое разрешение в зависимости от выбранной опции
            int newWidth = (currentOption == 0) ? 1230 : 1800;
            int newHeight = (currentOption == 0) ? 710 : 800;

            // Проверяем, изменяется ли разрешение
            if (Main.WIDTH != newWidth || Main.HEIGHT != newHeight) {
                Main.WIDTH = newWidth;
                Main.HEIGHT = newHeight;
                Game.getInstance().getDisplay().resize(Main.WIDTH, Main.HEIGHT);
            }
            
            // Сброс текущего выбора разрешения
            currentOption = 2; // Можно оставить это, если хотите сбросить выбор при возвращении
            sceneOption = "Main"; // Вернуться в главное меню
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
        g.setFont(UI_FONT); // Убедитесь, что шрифт установлен
        String title = "ТИТРЫ";
        int containerWidth = Main.WIDTH; // Получаем ширину контейнера

        // Центрируем заголовок "ТИТРЫ"
        int titleWidth = g.getFontMetrics().stringWidth(title);
        int titleX = (containerWidth - titleWidth) / 2; // Вычисляем координату X для заголовка
        g.drawString(title, titleX, 105);

        String[] lines = { "Разработчики:",
            "", "Мищиряков Р. А.", "Степанов М. Д.", "Евдокимов П. С.", "",
        };
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int width = g.getFontMetrics().stringWidth(line);
            // Центрируем текст по оси X
            int textX = (containerWidth - width) / 2; // Вычисляем координату X для текста
            g.drawString(line, textX, 190 + i * 50);
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
        g.setFont(UI_FONT); // Убедитесь, что шрифт установлен
        String prompt = "НАЖМИТЕ ENTER";
        int containerWidth = Main.WIDTH; // Получаем ширину контейнера

        if (Game.getInstance().getTime() % 1.5 < 0.9) {
            int textWidth = g.getFontMetrics().stringWidth(prompt);
            // Центрируем текст по оси X
            int textX = (containerWidth - textWidth) / 2; // Вычисляем координату X для текста
            g.drawString(prompt, textX, 700);
        }

        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            currentOption = 0;
            onSound();
            addObject(new FadeIn(1.0));
            sceneOption = "Main"; // Установите сцену на "Main" при возврате
        }
    }
}
