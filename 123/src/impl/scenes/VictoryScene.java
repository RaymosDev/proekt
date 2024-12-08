package impl.scenes; // Объявление пакета, в котором находится класс VictoryScene.

/*  Класс VictoryScene представляет собой сцену, которая отображается, 
    когда игрок выигрывает игру. Он позволяет отображать сообщение о победе, 
    текущий счет, новый рекорд, а также предоставляет меню с опциями, 
    такими как перезапуск игры, возврат в главное меню и выход из игры.
Основные компоненты класса:
Переменные:

backgroundImage: Изображение фона для сцены.
backgroundMusic: Фоновая музыка, которая играет во время сцены.
currentOption: Текущая выбранная опция в меню.
score: Счет игрока.
wasNewHighScore: Флаг, указывающий, был ли установлен новый рекорд.
highScore: Значение рекорда.
Константы:

VICTORY_FONT, SCORE_FONT, UI_FONT: Шрифты для отображения текста.
Методы:

initialize(): Метод для инициализации сцены, загрузки музыки и обработки рекорда.
render(Graphics g): Метод для отрисовки сцены, включая фон, текст и меню.
victoryMenuEnter(InputManager inputManager): Метод для обработки выбора опций меню.
getHighScore(): Метод для получения рекорда из файла.
setHighScore(int highScore): Метод для записи нового рекорда в файл.
dispose(): Метод для освобождения ресурсов, таких как остановка музыки.
*/

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Font; // Импорт класса Font для работы со шрифтами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.event.KeyEvent; // Импорт класса KeyEvent для обработки событий клавиатуры.
import java.io.File; // Импорт класса File для работы с файлами.
import java.io.FileNotFoundException; // Импорт класса FileNotFoundException для обработки исключений при работе с файлами.
import java.io.FileWriter; // Импорт класса FileWriter для записи в файлы.
import java.io.IOException; // Импорт класса IOException для обработки ошибок ввода-вывода.
import java.nio.file.Files; // Импорт класса Files для работы с файловой системой.
import java.nio.file.Paths; // Импорт класса Paths для работы с путями к файлам.
import java.util.Scanner; // Импорт класса Scanner для чтения данных из файлов.

import javax.sound.sampled.Clip; // Импорт класса Clip для работы со звуковыми эффектами.

import gameEngine.Game; // Импорт класса Game для управления игровым процессом.
import gameEngine.InputManager; // Импорт класса InputManager для обработки ввода пользователя.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import impl.Main; // Импорт главного класса приложения.

public class VictoryScene extends SceneWithKeys { // Определение класса VictoryScene, который наследует SceneWithKeys.
    private Image backgroundImage; // Переменная для хранения изображения фона.
    private Clip backgroundMusic; // Переменная для хранения фона музыки.
    private int currentOption; // Переменная для хранения текущей выбранной опции в меню.
    private int score; // Переменная для хранения счета игрока.
    private boolean wasNewHighScore; // Переменная для проверки, был ли установлен новый рекорд.
    private int highScore; // Переменная для хранения рекорда.

    // Константы для шрифтов.
    private static final Font VICTORY_FONT = ResourceLoader.loadFont("res/Font.ttf", 50);
    private static final Font SCORE_FONT = ResourceLoader.loadFont("res/Font.ttf", 50);
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36);

    // Опции меню на экране победы.
    private final String[] VICTORY_OPTIONS = { "Перезапуск", "Главное меню", "Выйти" };

    // Конструктор класса, принимает счет игрока как параметр.
    public VictoryScene(int score) {
        this.score = score; // Установка счета игрока.
        // Загрузка изображения фона и его масштабирование.
        backgroundImage = ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png")
                .getScaledInstance(24750, 825, 0);
    }

    // Метод инициализации сцены.
    public void initialize() {
        // Загрузка и воспроизведение фоновой музыки.
        backgroundMusic = ResourceLoader.loadAudioClip("res/audio/DeathScene.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Зацикливание музыки.
        addObject(new FadeIn(5.0)); // Добавление эффекта затухания.

        // Попытка получить высокий счет из файла.
        try {
            highScore = getHighScore();
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
            highScore = score; // Обновление значения рекорда.
        } else {
            wasNewHighScore = false; // Рекорд не установлен.
        }
    }

    // Метод для отрисовки сцены.
    public void render(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null); // Отрисовка фона.
        super.render(g); // Вызов метода отрисовки родительского класса.
        InputManager inputManager = Game.getInstance().getInputManager(); // Получение менеджера ввода.
        g.setColor(Color.WHITE); // Установка цвета текста.
        g.setFont(VICTORY_FONT); // Установка шрифта для сообщения о победе.

        String victoryMessage = "ВЫ ПОБЕДИЛИ!"; // Сообщение о победе.
        int containerWidth = Main.WIDTH; // Получение ширины контейнера.
        int victoryMessageWidth = g.getFontMetrics().stringWidth(victoryMessage); // Получение ширины сообщения.
        int victoryMessageX = (containerWidth - victoryMessageWidth) / 2; // Центрирование сообщения по X.
        g.drawString(victoryMessage, victoryMessageX, 60); // Отрисовка сообщения о победе.

        // Отрисовка счета игрока.
        g.setFont(SCORE_FONT); // Установка шрифта для счета.
        String scoreText = "Ваш счёт: " + score; // Формирование текста со счетом.
        int scoreTextWidth = g.getFontMetrics().stringWidth(scoreText); // Получение ширины текста со счетом.
        int scoreTextX = (containerWidth - scoreTextWidth) / 2; // Центрирование текста по X.
        g.drawString(scoreText, scoreTextX, 130); // Отрисовка текста со счетом.

        // Проверка, был ли установлен новый рекорд.
        if (wasNewHighScore) {
            String highScoreText = "Новый рекорд!"; // Сообщение о новом рекорде.
            int highScoreTextWidth = g.getFontMetrics().stringWidth(highScoreText); // Получение ширины текста с рекордом.
            int highScoreTextX = (containerWidth - highScoreTextWidth) / 2; // Центрирование текста по X.
            g.drawString(highScoreText, highScoreTextX, 200); // Отрисовка текста с рекордом.
        } else {
            String highScoreText = "Ваш рекорд: " + highScore; // Формирование текста с рекордом.
            int highScoreTextWidth = g.getFontMetrics().stringWidth(highScoreText); // Получение ширины текста с рекордом.
            int highScoreTextX = (containerWidth - highScoreTextWidth) / 2; // Центрирование текста по X.
            g.drawString(highScoreText, highScoreTextX, 200); // Отрисовка текста с рекордом.
        }

        g.setFont(UI_FONT); // Установка шрифта для пользовательского интерфейса.
        currentOption = upDown(inputManager, VICTORY_OPTIONS, currentOption); // Обработка навигации по опциям меню.
        renderScrollingMenus(g, VICTORY_OPTIONS, currentOption); // Отрисовка меню с опциями.
        victoryMenuEnter(inputManager); // Обработка нажатия клавиши для выбора опции.
    }

    // Метод для обработки нажатия клавиши Enter для выбора опции меню.
    public void victoryMenuEnter(InputManager inputManager) {
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) { // Если нажата клавиша Enter.
            if (currentOption == 0) { // Если выбрана опция "Перезапуск".
                Game.getInstance().loadScene(new GameScene()); // Загрузка новой игровой сцены.
            } else if (currentOption == 1) { // Если выбрана опция "Главное меню".
                Game.getInstance().loadScene(new MainMenuScene("Main")); // Загрузка главного меню.
            } else if (currentOption == 2) { // Если выбрана опция "Выйти".
                Game.getInstance().stop(); // Остановка игры.
            }
        }
    }

    // Метод для получения высокого счета из файла.
    public int getHighScore() throws FileNotFoundException {
        File highScoreFile; // Переменная для хранения файла с рекордом.
        int highScore = 0; // Переменная для хранения значения рекорда.

        highScoreFile = new File("highScore.txt"); // Создание объекта файла.

        if (highScoreFile.exists()) { // Проверка, существует ли файл.
            Scanner scanner = new Scanner(highScoreFile); // Создание сканера для чтения файла.
            if (scanner.hasNext()) { // Если в файле есть данные.
                highScore = scanner.nextInt(); // Чтение рекорда.
            }
            scanner.close(); // Закрытие сканера.
        }
        return highScore; // Возврат значения рекорда.
    }

    // Метод для установки высокого счета в файл.
    public void setHighScore(int highScore) throws IOException {
        File highScoreFile = new File("highScore.txt"); // Создание объекта файла.

        if (!highScoreFile.exists()) { // Если файл не существует.
            highScoreFile.createNewFile(); // Создание нового файла.
        }

        FileWriter fw = new FileWriter(highScoreFile); // Создание объекта для записи в файл.
        String highScoreString = "" + highScore; // Преобразование рекорда в строку.
        fw.write(highScoreString); // Запись рекорда в файл.
        fw.close(); // Закрытие объекта записи.
    }

    // Переопределение метода dispose для освобождения ресурсов.
    @Override
    public void dispose() {
        super.dispose(); // Вызов метода dispose родительского класса.
        backgroundMusic.stop(); // Остановка фоновой музыки.
    }
}
