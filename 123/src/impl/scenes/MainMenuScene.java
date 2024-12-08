package impl.scenes; // Объявление пакета, в котором находится класс MainMenuScene.

/*  Класс MainMenuScene представляет собой главное меню игры, 
    позволяя игроку выбирать различные опции, такие как старт игры, 
    изменение сложности, разрешения, просмотр титров или выход из игры. 
    Он управляет отображением меню, обработкой ввода пользователя и 
    переходами между различными подменю.
Основные компоненты класса:
Константы:

UI_FONT: Шрифт для отображения текста в пользовательском интерфейсе.
MAIN_MENU_OPTIONS, SETTINGS_OPTIONS, RESOLUTION_OPTIONS: Массивы строк, представляющие варианты меню.
Поля:

backgroundImage: Изображение фона меню.
title: Заголовок меню.
backgroundMusic: Фоновая музыка для главного меню.
currentOption: Текущая выбранная опция в меню.
sceneOption: Опция текущей сцены (например, "Main", "Difficulty").
Методы:

initialize(): Загружает ресурсы и настраивает сцену.
render(Graphics g): Отрисовывает элементы меню в зависимости от текущей опции сцены.
mainMenuEnter(InputManager inputManager): Обрабатывает ввод в главном меню.
settingsMenuEnter(InputManager inputManager): Обрабатывает ввод в меню настроек сложности.
resolutionMenuEnter(InputManager inputManager): Обрабатывает ввод в меню разрешения.
creditsScene(Graphics g, InputManager inputManager): Отрисовывает титры.
returnToMenuOption(Graphics g, InputManager inputManager): Обрабатывает возврат к главному меню.
*/

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Font; // Импорт класса Font для работы со шрифтами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.event.KeyEvent; // Импорт класса KeyEvent для обработки событий клавиатуры.
import java.awt.image.BufferedImage; // Импорт класса BufferedImage для работы с изображениями.

import javax.sound.sampled.Clip; // Импорт класса Clip для работы с аудио.

import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.InputManager; // Импорт класса InputManager для обработки ввода пользователя.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import impl.Difficulty; // Импорт класса Difficulty для работы с уровнями сложности.
import impl.Main; // Импорт главного класса приложения.
import impl.entities.AsteroidLarge; // Импорт класса AsteroidLarge, представляющего крупный астероид.
import impl.entities.AsteroidSmall; // Импорт класса AsteroidSmall, представляющего малый астероид.
import impl.entities.HealthDrop; // Импорт класса HealthDrop, представляющего аптечки.
import impl.entities.Hornet; // Импорт класса Hornet, представляющего врага Hornet.
import impl.entities.Javelin; // Импорт класса Javelin, представляющего врага Javelin.
import impl.entities.Marauder; // Импорт класса Marauder, представляющего врага Marauder.
import impl.entities.PlayerShip; // Импорт класса PlayerShip, представляющего игрока.

public class MainMenuScene extends SceneWithKeys {
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36); // Шрифт для пользовательского интерфейса.
    
    // Опции главного меню.
    private final String[] MAIN_MENU_OPTIONS = { "Старт", "Сложность", "Разрешение", "Титры", "Выйти" };
    private final String[] SETTINGS_OPTIONS = { "Простая", "Средняя", "Высокая", "Назад" }; // Опции настроек сложности.
    private final String[] RESOLUTION_OPTIONS = { "1270x610", "1800x800", "Назад" }; // Опции разрешения.

    private BufferedImage backgroundImage; // Изображение фона.
    private Image title; // Изображение заголовка.
    private Clip backgroundMusic; // Фоновая музыка.

    private int currentOption; // Текущая выбранная опция в меню.

    private String sceneOption; // Опция текущей сцены.

    // Конструктор класса с параметром для установки опции сцены.
    public MainMenuScene(String sceneOption) {
        this.sceneOption = sceneOption;
    }

    // Конструктор класса по умолчанию, устанавливающий опцию сцены на "Main".
    public MainMenuScene() {
        this.sceneOption = "Main";
    }

    // Метод инициализации сцены.
    @Override
    public void initialize() {
        // Загрузка и масштабирование изображения фона.
        backgroundImage = ResourceLoader.toBufferedImage(
            ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png").getScaledInstance(24750, 825, 0));
        // Загрузка и масштабирование изображения заголовка.
        title = ResourceLoader.loadImage("res/images/ui/Title.png").getScaledInstance(889, 322, 0);

        // Загрузка фоновой музыки и установка бесконечного воспроизведения.
        backgroundMusic = ResourceLoader.loadAudioClip("res/audio/MainMenuMusic.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        addObject(new FadeIn(1.0)); // Добавление эффекта затухания при входе в сцену.
    }

    // Метод отрисовки сцены.
    @Override
    public void render(Graphics g) {
        InputManager inputManager = Game.getInstance().getInputManager(); // Получение менеджера ввода.
        g.setFont(UI_FONT); // Установка шрифта.
        g.setColor(Color.WHITE); // Установка цвета текста.

        double time = Game.getInstance().getTime(); // Получение текущего времени игры.
        int x = (int) (time * 50 % 22195); // Вычисление смещения для фона.
        Image backgroundSubImage = backgroundImage.getSubimage(x, 0, Main.WIDTH, Main.HEIGHT); // Получение подизображения фона.
        g.drawImage(backgroundSubImage, 0, 0, null); // Отрисовка фона.

        // В зависимости от опции сцены, отрисовываются разные элементы.
        if (sceneOption.equals("Main")) {
            int imageWidth = title.getWidth(null); // Получение ширины заголовка.
            int xTitle = (Main.WIDTH - imageWidth) / 2; // Вычисление позиции по центру.
            g.drawImage(title, xTitle, 0, null); // Отрисовка заголовка.

            currentOption = upDown(inputManager, MAIN_MENU_OPTIONS, currentOption); // Обработка выбора в главном меню.
            renderScrollingMenus(g, MAIN_MENU_OPTIONS, currentOption); // Отрисовка меню.
            mainMenuEnter(inputManager); // Обработка ввода в главном меню.
        } else if (sceneOption.equals("Difficulty")) {
            int imageWidth = title.getWidth(null);
            int xTitle = (Main.WIDTH - imageWidth) / 2;
            g.drawImage(title, xTitle, 0, null); // Отрисовка заголовка.

            currentOption = upDown(inputManager, SETTINGS_OPTIONS, currentOption); // Обработка выбора в меню сложности.
            renderScrollingMenus(g, SETTINGS_OPTIONS, currentOption); // Отрисовка меню настроек.
            settingsMenuEnter(inputManager); // Обработка ввода в меню сложности.
        } else if (sceneOption.equals("Resolution")) {
            int imageWidth = title.getWidth(null);
            int xTitle = (Main.WIDTH - imageWidth) / 2;
            g.drawImage(title, xTitle, 0, null); // Отрисовка заголовка.

            currentOption = upDown(inputManager, RESOLUTION_OPTIONS, currentOption); // Обработка выбора в меню разрешения.
            renderScrollingMenus(g, RESOLUTION_OPTIONS, currentOption); // Отрисовка меню разрешений.
            resolutionMenuEnter(inputManager); // Обработка ввода в меню разрешения.
        } else if (sceneOption.equals("Credits")) {
            creditsScene(g, inputManager); // Отрисовка сцены титров.
        }
        super.render(g); // Вызов метода render() родительского класса.
    }

    // Метод для обработки ввода в главном меню.
    public void mainMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) { // Если нажата клавиша ENTER.
            onSound(); // Воспроизведение звука.
            if (currentOption == 0) { // Если выбрана опция "Старт".
                backgroundMusic.stop(); // Остановка фоновой музыки.
                Game.getInstance().loadScene(new GameScene()); // Переход к игровому сценарию.
            } else if (currentOption == 1) { // Если выбрана опция "Сложность".
                sceneOption = "Difficulty"; // Установка опции сцены на "Difficulty".
                currentOption = Main.difficulty.ordinal(); // Установка текущей опции на основе уровня сложности.
            } else if (currentOption == 2) { // Если выбрана опция "Разрешение".
                sceneOption = "Resolution"; // Установка опции сцены на "Resolution".
                currentOption = (Main.WIDTH == 1270) ? 0 : 1; // Установка текущей опции на основе ширины экрана.
            } else if (currentOption == 3) { // Если выбрана опция "Титры".
                sceneOption = "Credits"; // Установка опции сцены на "Credits".
                addObject(new FadeIn(1.0)); // Добавление эффекта затухания.
            } else if (currentOption == 4) { // Если выбрана опция "Выйти".
                Game.getInstance().stop(); // Остановка игры.
            }
        }
    }

    // Метод для обработки ввода в меню настроек.
    public void settingsMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) { // Если нажата клавиша ENTER.
            onSound(); // Воспроизведение звука.
            if (currentOption == 0) { // Если выбрана опция "Простая".
                Main.difficulty = Difficulty.EASY; // Установка уровня сложности на "Простая".
            } else if (currentOption == 1) { // Если выбрана опция "Средняя".
                Main.difficulty = Difficulty.MEDIUM; // Установка уровня сложности на "Средняя".
            } else if (currentOption == 2) { // Если выбрана опция "Высокая".
                Main.difficulty = Difficulty.HARD; // Установка уровня сложности на "Высокая".
            }
            currentOption = 1; // Сброс текущей опции на "Средняя".
            sceneOption = "Main"; // Возврат к главному меню.
        }
    }

    // Метод для обработки ввода в меню разрешения.
    public void resolutionMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) { // Если нажата клавиша ENTER.
            onSound(); // Воспроизведение звука.
            if (currentOption == 2) { // Если выбрана опция "Назад".
                sceneOption = "Main"; // Возврат к главному меню.
                return; // Выход из метода.
            }
            // Установка нового разрешения в зависимости от текущей опции.
            int newWidth = (currentOption == 0) ? 1270 : 1800;
            int newHeight = (currentOption == 0) ? 640 : 800;

            // Если новое разрешение отличается от текущего, обновление разрешения.
            if (Main.WIDTH != newWidth || Main.HEIGHT != newHeight) {
                Main.WIDTH = newWidth; // Установка новой ширины.
                Main.HEIGHT = newHeight; // Установка новой высоты.
                Game.getInstance().getDisplay().resize(Main.WIDTH, Main.HEIGHT); // Изменение размера окна отображения.
                // Обновление размеров всех сущностей.
                HealthDrop.updateDimensions();
                AsteroidLarge.updateDimensions();
                PlayerShip.updateDimensions();
                AsteroidSmall.updateDimensions();
                Hornet.updateDimensions();
                Javelin.updateDimensions();
                Marauder.updateDimensions();
            }

            currentOption = 2; // Сброс текущей опции на "Назад".
            sceneOption = "Main"; // Возврат к главному меню.
        }
    }

    // Метод для отрисовки сцены титров.
    public void creditsScene(Graphics g, InputManager inputManager) {
        g.setFont(UI_FONT); // Установка шрифта.
        String title = "ТИТРЫ"; // Заголовок титров.
        int containerWidth = Main.WIDTH; // Ширина контейнера.

        int titleWidth = g.getFontMetrics().stringWidth(title); // Получение ширины заголовка.
        int titleX = (containerWidth - titleWidth) / 2; // Вычисление позиции по центру.
        g.drawString(title, titleX, 105); // Отрисовка заголовка.

        // Массив строк с именами разработчиков.
        String[] lines = { "Разработчики:",
            "", "Мищиряков Р. А.", "Степанов М. Д.", "Евдокимов П. С.", "",
        };
        
        // Отрисовка каждой строки с именем разработчика.
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int width = g.getFontMetrics().stringWidth(line); // Получение ширины строки.
            int textX = (containerWidth - width) / 2; // Вычисление позиции по центру.
            g.drawString(line, textX, 190 + i * 50); // Отрисовка строки.
        }
        
        returnToMenuOption(g, inputManager); // Обработка возврата к меню.
    }

    // Метод для обработки возврата к меню.
    public void returnToMenuOption(Graphics g, InputManager inputManager) {
        g.setFont(UI_FONT); // Установка шрифта.
        String prompt = "НАЖМИТЕ ENTER"; // Сообщение о возврате.
        int containerWidth = Main.WIDTH; // Ширина контейнера.

        // Мигающее сообщение, если текущее время меньше 0.9.
        if (Game.getInstance().getTime() % 1.5 < 0.9) {
            int textWidth = g.getFontMetrics().stringWidth(prompt); // Получение ширины сообщения.
            int textX = (containerWidth - textWidth) / 2; // Вычисление позиции по центру.
            g.drawString(prompt, textX, 600); // Отрисовка сообщения.
        }

        // Если нажата клавиша ENTER, возврат к главному меню.
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            currentOption = 0; // Сброс текущей опции.
            onSound(); // Воспроизведение звука.
            addObject(new FadeIn(1.0)); // Добавление эффекта затухания.
            sceneOption = "Main"; // Установка опции сцены на "Main".
        }
    }
}
