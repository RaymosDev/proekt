package impl.scenes; // Объявление пакета, в котором находится класс EndingScene.

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Font; // Импорт класса Font для работы с шрифтами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.event.KeyEvent; // Импорт класса KeyEvent для обработки событий клавиатуры.
import java.io.File; // Импорт класса File для работы с файлами.
import java.io.FileNotFoundException; // Импорт класса FileNotFoundException для обработки ошибок при работе с файлами.
import java.io.FileWriter; // Импорт класса FileWriter для записи в файлы.
import java.io.IOException; // Импорт класса IOException для обработки ошибок ввода-вывода.
import java.nio.file.Files; // Импорт класса Files для работы с файлами.
import java.nio.file.Paths; // Импорт класса Paths для работы с путями к файлам.
import java.util.Scanner; // Импорт класса Scanner для чтения данных из файла.

import javax.sound.sampled.Clip; // Импорт класса Clip для работы с аудиофайлами.

import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.InputManager; // Импорт класса InputManager для обработки ввода от пользователя.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import impl.Main; // Импорт главного класса приложения.

/**
 * Класс EndingScene представляет собой сцену окончания игры, которая отображает
 * результаты игрока и предоставляет варианты для продолжения игры.
 * сновные компоненты класса:
Поля:

backgroundImage: Фоновое изображение для сцены.
backgroundMusic: Фоновая музыка, играющая во время сцены.
currentOption: Текущий выбранный вариант в меню.
score: Счет игрока.
wasNewHighScore: Флаг, указывающий, был ли установлен новый рекорд.
highScore: Высший счет.
os: Операционная система.
Шрифты:

DEATH_FONT, SCORE_FONT, UI_FONT: Шрифты для отображения текста на экране.
Варианты меню:

ENDING_OPTIONS: Массив строк, представляющий варианты меню окончания игры.
Конструктор:

Инициализирует счет игрока и загружает фоновое изображение.
Методы:

initialize(): Инициализирует сцену, загружает музыку, устанавливает новый рекорд, если это необходимо.
render(Graphics g): Отрисовывает сцену, включая фон, текст и меню.
endMenuEnter(InputManager inputManager): Обрабатывает нажатия клавиш для выбора варианта в меню.
getHighScore(): Получает высший счет из файла.
setHighScore(int highScore): Устанавливает новый высший счет в файл.
dispose(): Освобождает ресурсы при завершении сцены.
 */
public class EndingScene extends SceneWithKeys {
    private Image backgroundImage; // Фоновое изображение для сцены.
    private Clip backgroundMusic; // Фоновая музыка для сцены.
    private int currentOption; // Текущий выбранный вариант в меню.
    private int score; // Счет игрока.
    private boolean wasNewHighScore; // Флаг, указывающий, был ли установлен новый рекорд.
    private int highScore; // Высший счет.
    private String os; // Операционная система.

    // Шрифты для отображения текста на экране.
    private static final Font DEATH_FONT = ResourceLoader.loadFont("res/Font.ttf", 50);
    private static final Font SCORE_FONT = ResourceLoader.loadFont("res/Font.ttf", 50);
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36);

    // Варианты меню окончания игры.
    private final String[] ENDING_OPTIONS = { "Перезапуск", "Главное меню", "Выйти" };

    // Конструктор класса EndingScene, принимающий счет игрока.
    public EndingScene(int score) {
        this.score = score; // Инициализация счета игрока.
        // Загрузка фонового изображения и масштабирование.
        backgroundImage = ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png")
            .getScaledInstance(24750, 825, 0);
    }

    // Метод инициализации сцены.
    public void initialize() {
        // Загрузка фоновой музыки и ее зацикливание.
        backgroundMusic = ResourceLoader.loadAudioClip("res/audio/DeathScene.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        addObject(new FadeIn(5.0)); // Добавление эффекта затухания.
        os = System.getProperty("os.name"); // Получение имени операционной системы.

        try {
            highScore = getHighScore(); // Получение высшего счета.
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Обработка исключения, если файл не найден.
        }
        // Проверка, установлен ли новый рекорд.
        if (score > highScore) {
            wasNewHighScore = true; // Установка флага нового рекорда.
            try {
                setHighScore(score); // Установка нового рекорда в файл.
            } catch (IOException e) {
                e.printStackTrace(); // Обработка исключения при записи в файл.
            }
            highScore = score; // Обновление высшего счета.
        } else {
            wasNewHighScore = false; // Новый рекорд не установлен.
        }
    }

    // Метод отрисовки сцены на экране.
    public void render(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null); // Отрисовка фонового изображения.
        super.render(g); // Вызов метода отрисовки родительского класса.
        InputManager inputManager = Game.getInstance().getInputManager(); // Получение менеджера ввода.
        g.setColor(Color.WHITE); // Установка цвета текста в белый.
        g.setFont(DEATH_FONT); // Установка шрифта для сообщения о смерти.

        // Сообщение о смерти игрока.
        String deathMessage = "ВЫ ПОГИБЛИ";
        int containerWidth = Main.WIDTH; // Ширина контейнера.
        int deathMessageWidth = g.getFontMetrics().stringWidth(deathMessage); // Ширина сообщения.
        int deathMessageX = (containerWidth - deathMessageWidth) / 2; // Вычисление координаты X для центрирования.
        g.drawString(deathMessage, deathMessageX, 60); // Отрисовка сообщения.

        g.setFont(SCORE_FONT); // Установка шрифта для отображения счета.
        String scoreText = "Ваш счёт: " + score; // Формирование текста с текущим счетом.
        int scoreTextWidth = g.getFontMetrics().stringWidth(scoreText); // Ширина текста счета.
        int scoreTextX = (containerWidth - scoreTextWidth) / 2; // Вычисление координаты X для центрирования.
        g.drawString(scoreText, scoreTextX, 130); // Отрисовка текста счета.

        // Проверка, был ли установлен новый рекорд.
        if (wasNewHighScore) {
            String highScoreText = "Новый рекорд!"; // Сообщение о новом рекорде.
            int highScoreTextWidth = g.getFontMetrics().stringWidth(highScoreText); // Ширина текста рекорда.
            int highScoreTextX = (containerWidth - highScoreTextWidth) / 2; // Вычисление координаты X для центрирования.
            g.drawString(highScoreText, highScoreTextX, 200); // Отрисовка текста рекорда.
        } else {
            String highScoreText = "Ваш рекорд: " + highScore; // Сообщение о текущем рекорде.
            int highScoreTextWidth = g.getFontMetrics().stringWidth(highScoreText); // Ширина текста рекорда.
            int highScoreTextX = (containerWidth - highScoreTextWidth) / 2; // Вычисление координаты X для центрирования.
            g.drawString(highScoreText, highScoreTextX, 200); // Отрисовка текста рекорда.
        }

        g.setFont(UI_FONT); // Установка шрифта для пользовательского интерфейса.
        currentOption = upDown(inputManager, ENDING_OPTIONS, currentOption); // Обработка перемещения по меню.
        renderScrollingMenus(g, ENDING_OPTIONS, currentOption); // Отрисовка меню.
        endMenuEnter(inputManager); // Обработка нажатия клавиш для выбора варианта.
    }

    // Метод для обработки нажатия клавиши Enter в меню.
    public void endMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) { // Если нажата клавиша Enter.
            if (currentOption == 0) {
                Game.getInstance().loadScene(new GameScene()); // Перезапуск игры.
            } else if (currentOption == 1) {
                Game.getInstance().loadScene(new MainMenuScene("Main")); // Переход в главное меню.
            } else if (currentOption == 2) {
                Game.getInstance().stop(); // Выход из игры.
            }
        }
    }

    // Метод для получения высшего счета из файла.
    public int getHighScore() throws FileNotFoundException {
        File highScoreFile; // Файл для хранения высшего счета.
        int highScore = 0; // Переменная для хранения высшего счета.

        highScoreFile = new File("highScore.txt"); // Определение файла.

        if (highScoreFile.exists()) { // Если файл существует.
            Scanner scanner = new Scanner(highScoreFile); // Создание сканера для чтения файла.
            if (scanner.hasNext()) {
                highScore = scanner.nextInt(); // Чтение высшего счета.
            }
            scanner.close(); // Закрытие сканера.
        }
        return highScore; // Возврат высшего счета.
    }

    // Метод для установки нового высшего счета в файл.
    public void setHighScore(int highScore) throws IOException {
        File highScoreFile = new File("highScore.txt"); // Определение файла для высшего счета.

        if (!highScoreFile.exists()) { // Если файл не существует.
            highScoreFile.createNewFile(); // Создание нового файла.
        }

        FileWriter fw = new FileWriter(highScoreFile); // Создание FileWriter для записи в файл.
        String highScoreString = "" + highScore; // Преобразование высшего счета в строку.
        fw.write(highScoreString); // Запись высшего счета в файл.
        fw.close(); // Закрытие FileWriter.
    }

    // Метод для освобождения ресурсов при завершении сцены.
    @Override
    public void dispose() {
        super.dispose(); // Вызов метода освобождения ресурсов родительского класса.
        backgroundMusic.stop(); // Остановка фоновой музыки.
    }
}
