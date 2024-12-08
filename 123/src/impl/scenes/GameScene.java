package impl.scenes; // Объявление пакета, в котором находится класс GameScene.

/*  Класс GameScene представляет собой игровую сцену, в которой происходит 
    основная игра. Он управляет игровым процессом, взаимодействиями игрока, 
    отображением пользовательского интерфейса и обработкой событий, 
    таких как пауза и обновление состояния игры.
Основные компоненты класса:
Константы:

FIRST_WAVE_WAIT_TIME и WAVE_REST_TIME: Определяют время ожидания перед первой волной врагов и время отдыха между волнами.
UI_FONT: Шрифт для отображения текста в пользовательском интерфейсе.
PLAYER_START: Начальная позиция игрока.
PAUSE_MENU_OPTIONS: Опции меню паузы.
Поля:

bounds: Коллайдер для границ сцены.
backgroundImage: Изображение фона сцены.
backgroundMusic: Фоновая музыка.
player: Игрок.
score: Очки игрока.
currentPauseOption: Текущая опция в меню паузы.
paused: Флаг, указывающий, находится ли игра на паузе.
inputManager: Менеджер ввода.
waveMessage и waveMessageDuration: Сообщение о волне и его длительность.
Методы:

initialize(): Инициализирует сцену, загружает ресурсы и добавляет объекты.
tick(): Обновляет состояние игры на каждом тике.
pauseTick(): Обрабатывает логику меню паузы.
pause(), unPause(): Устанавливают и снимают паузу.
render(Graphics g): Отрисовывает сцену, включая фон, пользовательский интерфейс и индикатор здоровья.
drawHealthBar(Graphics g): Отрисовывает индикатор здоровья игрока.
dispose(): Освобождает ресурсы при завершении сцены.
endGame(): Завершает игру и переходит на экран завершения.
setWaveMessage(String message): Устанавливает сообщение о волне.
*/

import gameEngine.Collider; // Импорт класса Collider для обработки коллизий.
import gameEngine.Entity; // Импорт класса Entity для работы с игровыми объектами.
import gameEngine.Entity.EntityCollider; // Импорт класса EntityCollider для обработки коллизий с сущностями.
import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.InputManager; // Импорт класса InputManager для обработки ввода пользователя.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.SceneObject; // Импорт класса SceneObject, от которого наследуется GameScene.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт главного класса приложения.
import impl.entities.PlayerShip; // Импорт класса PlayerShip, представляющего игрока.
import impl.waves.Wave1; // Импорт класса Wave1, представляющего первую волну врагов.

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Font; // Импорт класса Font для работы с шрифтами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.event.KeyEvent; // Импорт класса KeyEvent для обработки событий клавиатуры.
import java.awt.image.BufferedImage; // Импорт класса BufferedImage для работы с изображениями.
import java.util.ArrayList; // Импорт класса ArrayList для работы с динамическими массивами.
import java.util.List; // Импорт класса List для работы с коллекциями.
import javax.sound.sampled.Clip; // Импорт класса Clip для работы с аудио.

public class GameScene extends SceneWithKeys {
    public static final double FIRST_WAVE_WAIT_TIME = 2.5; // Время ожидания перед первой волной.
    public static final double WAVE_REST_TIME = 5.0; // Время отдыха между волнами.
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 50); // Шрифт для пользовательского интерфейса.
    private static final Vector2 PLAYER_START = new Vector2(250, Main.HEIGHT / 2); // Начальная позиция игрока.
    private final String[] PAUSE_MENU_OPTIONS = { "Продолжить", "Главное меню" }; // Опции меню паузы.

    // Загрузка изображений для индикаторов здоровья.
    private static final Image GREEN_HP = ResourceLoader.loadImage("res/images/ui/GreenHp.png")
            .getScaledInstance((int) (10 * 2.5), (int) (14 * 2.5), 0);
    private static final Image BLACK_HP = ResourceLoader.loadImage("res/images/ui/Black.png")
            .getScaledInstance((int) (10 * 2.5), (int) (14 * 2.5), 0);
    private static final Image END_HP = ResourceLoader.loadImage("res/images/ui/EndHp.png")
            .getScaledInstance((int) (10 * 2.5), (int) (14 * 2.5), 0);
    private static final Image YELLOW_HP = ResourceLoader.loadImage("res/images/ui/YellowHp.png")
            .getScaledInstance((int) (10 * 2.5), (int) (14 * 2.5), 0);
    private static final Image RED_HP = ResourceLoader.loadImage("res/images/ui/RedHp.png")
            .getScaledInstance((int) (10 * 2.5), (int) (14 * 2.5), 0);

    private Collider bounds; // Коллайдер для границ сцены.
    private BufferedImage backgroundImage; // Изображение фона.
    private Clip backgroundMusic; // Музыкальный фон.
    private PlayerShip player; // Игрок.
    private int score; // Очки игрока.
    private int currentPauseOption; // Текущая опция в меню паузы.
    private boolean paused; // Флаг, указывающий, находится ли игра на паузе.
    private InputManager inputManager; // Менеджер ввода.

    private String waveMessage = ""; // Сообщение о волне.
    private double waveMessageDuration = 0; // Длительность отображения сообщения о волне.

    // Метод инициализации сцены.
    @Override
    public void initialize() {
        // Инициализация границ коллайдера.
        bounds = new Collider(-500, -500, Main.WIDTH + 500, Main.HEIGHT + 500) {
            @Override
            public void onCollisionEnter(Collider other) {
                // Логика при входе в коллайдер (не реализована).
            }

            @Override
            public void onCollisionExit(Collider other) {
                // Логика при выходе из коллайдера.
                if (other instanceof EntityCollider) {
                    Entity entity = ((EntityCollider) other).getEntity(); // Получение сущности.
                    Game.getInstance().getOpenScene().removeObject(entity); // Удаление сущности из сцены.
                }
            }
        };
        bounds.setActive(true); // Активация коллайдера.
        
        // Загрузка и масштабирование изображения фона.
        backgroundImage = ResourceLoader.toBufferedImage(
                ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png").getScaledInstance(24750, 825, 0));
        
        // Загрузка фоновой музыки и установка бесконечного воспроизведения.
        backgroundMusic = ResourceLoader.loadAudioClip("res/audio/GameMusic.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        
        currentPauseOption = 0; // Инициализация текущей опции меню паузы.
        inputManager = Game.getInstance().getInputManager(); // Получение менеджера ввода.
        player = new PlayerShip(PLAYER_START); // Создание игрока на стартовой позиции.
        addObject(player); // Добавление игрока в сцену.
        addObject(new FadeIn(1.5)); // Добавление эффекта затухания при входе в сцену.
        addObject(new Wave1(this)); // Добавление первой волны врагов.
    }

    // Метод, который вызывается на каждом тике (обновлении) игры.
    @Override
    public void tick() {
        super.tick(); // Вызов метода tick() родительского класса.
        
        // Обработка сообщения о волне.
        if (waveMessageDuration > 0) {
            waveMessageDuration -= Game.getInstance().getDeltaTime(); // Уменьшение времени отображения сообщения.
            if (waveMessageDuration <= 0) {
                waveMessage = ""; // Сброс сообщения, если время истекло.
            }
        }

        // Обработка паузы.
        if (paused) {
            if (Game.getInstance().getInputManager().getKeyDown(KeyEvent.VK_ESCAPE)) {
                unPause(); // Снятие паузы при нажатии клавиши ESC.
            }
            pauseTick(); // Обработка логики паузы.
        } else {
            if (Game.getInstance().getInputManager().getKeyDown(KeyEvent.VK_ESCAPE)) {
                pause(); // Установка паузы при нажатии клавиши ESC.
            }
        }
    }

    // Метод для обработки логики паузы.
    private void pauseTick() {
        currentPauseOption = upDown(inputManager, PAUSE_MENU_OPTIONS, currentPauseOption); // Обработка выбора в меню паузы.
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            // Логика при нажатии клавиши ENTER в меню паузы.
            if (currentPauseOption == 0) {
                unPause(); // Возобновление игры.
            } else if (currentPauseOption == 1) {
                unPause(); // Возобновление игры.
                backgroundMusic.stop(); // Остановка фоновой музыки.
                Game.getInstance().loadScene(new MainMenuScene("Main")); // Переход в главное меню.
            }
        }
    }

    // Метод для проверки, находится ли игра на паузе.
    public boolean isPaused() {
        return paused; // Возвращает состояние паузы.
    }

    // Метод для установки паузы.
    private void pause() {
        Game.getInstance().setTimeScale(0.0); // Остановка времени в игре.
        ResourceLoader.loadAudioClip("res/audio/Pause.wav").start(); // Воспроизведение звука паузы.
        paused = true; // Установка флага паузы.
    }

    // Метод для снятия паузы.
    private void unPause() {
        Game.getInstance().setTimeScale(1.0); // Возобновление времени в игре.
        ResourceLoader.loadAudioClip("res/audio/Unpause.wav").start(); // Воспроизведение звука снятия паузы.
        paused = false; // Сброс флага паузы.
    }

    // Метод отрисовки сцены.
    @Override
    public void render(Graphics g) {
        double time = Game.getInstance().getTime(); // Получение текущего времени игры.
        int x = (int) (time * 150 % 22195); // Вычисление смещения для фона.
        Image backgroundSubImage = backgroundImage.getSubimage(x, 0, Main.WIDTH, Main.HEIGHT); // Получение подизображения фона.
        g.drawImage(backgroundSubImage, 0, 0, null); // Отрисовка фона.
        super.render(g); // Вызов метода render() родительского класса.
        
        g.setColor(Color.WHITE); // Установка цвета текста.
        g.setFont(UI_FONT); // Установка шрифта.
        g.drawString("Счёт: " + score, 50, 70); // Отрисовка счёта.

        // Отрисовка сообщения о волне, если оно не пустое.
        if (!waveMessage.isEmpty()) {
            g.drawString(waveMessage, Main.WIDTH / 2 - g.getFontMetrics().stringWidth(waveMessage) / 2, 100);
        }
        
        drawHealthBar(g); // Вызов метода отрисовки индикатора здоровья.
        
        // Если игра на паузе, затемнённый экран и меню паузы.
        if (paused) {
            Color filter = new Color(0, 0, 0, 150); // Полупрозрачный черный цвет для затемнения.
            g.setColor(filter);
            g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT); // Заполнение экрана затемнением.
            
            g.setColor(Color.WHITE); // Установка цвета текста.
            String pauseMessage = "ПАУЗА"; // Сообщение о паузе.
            int containerWidth = Main.WIDTH; // Ширина контейнера.
            int pauseMessageWidth = g.getFontMetrics().stringWidth(pauseMessage); // Ширина сообщения о паузе.
            int pauseMessageX = (containerWidth - pauseMessageWidth) / 2; // Вычисление позиции по центру.
            g.drawString(pauseMessage, pauseMessageX, 230); // Отрисовка сообщения о паузе.
            
            renderScrollingMenus(g, PAUSE_MENU_OPTIONS, currentPauseOption); // Отрисовка меню паузы.
        }
    }

    // Метод для отрисовки индикатора здоровья игрока.
    private void drawHealthBar(Graphics g) {
        double healthProportion = player.getCurrentHealth() / player.getMaxHealth(); // Пропорция текущего здоровья.
        int totalBlocks = (int) player.getMaxHealth(); // Общее количество блоков здоровья.
        int numBars = (int) Math.ceil(healthProportion * totalBlocks); // Количество заполненных блоков.
        int blockWidth = 23; // Ширина блока здоровья.
        int padding = 10; // Отступ между блоками.
        int xOffset = Main.WIDTH - (totalBlocks * blockWidth) - padding; // Смещение по оси X для индикатора здоровья.

        g.drawImage(END_HP, xOffset - END_HP.getWidth(null), 35, null); // Отрисовка индикатора конца здоровья.

        // Отрисовка блоков здоровья.
        for (int i = 0; i < totalBlocks; i++) {
            Image hpBlock; // Переменная для хранения изображения блока здоровья.
            if (i < numBars) { // Если блок заполнен.
                // Определение цвета блока в зависимости от уровня здоровья.
                if (healthProportion < 0.33) {
                    hpBlock = RED_HP; // Красный блок.
                } else if (healthProportion < 0.66) {
                    hpBlock = YELLOW_HP; // Желтый блок.
                } else {
                    hpBlock = GREEN_HP; // Зеленый блок.
                }
                g.drawImage(hpBlock, xOffset + i * blockWidth, 35, null); // Отрисовка заполненного блока.
            } else {
                g.drawImage(BLACK_HP, xOffset + i * blockWidth, 35, null); // Отрисовка незаполненного блока.
            }
        }
    }

    // Метод для получения игрока.
    public PlayerShip getPlayer() {
        return player; // Возвращает объект игрока.
    }

    // Метод для добавления очков.
    public void addScore(int score) {
        this.score += score; // Увеличение счёта на переданное значение.
    }

    // Метод для освобождения ресурсов при завершении сцены.
    @Override
    public void dispose() {
        super.dispose(); // Вызов метода dispose() родительского класса.
        backgroundMusic.stop(); // Остановка фоновой музыки.

        List<SceneObject> objectsCopy = new ArrayList<>(objects); // Копирование объектов сцены.

        // Освобождение ресурсов для каждого объекта в сцене.
        for (SceneObject object : objectsCopy) {
            object.dispose();
        }

        objects.clear(); // Очистка списка объектов.
        bounds.setActive(false); // Деактивация коллайдера.
    }

    // Метод для завершения игры.
    public void endGame() {
        backgroundMusic.stop(); // Остановка фоновой музыки.
        Game.getInstance().loadScene(new EndingScene(score)); // Переход к сцене завершения с передачей счёта.
    }

    // Метод для получения текущего счёта.
    public int getScore() {
        return score; // Возвращает текущий счёт.
    }

    // Метод для установки сообщения о волне.
    public void setWaveMessage(String message) {
        this.waveMessage = message; // Установка сообщения о волне.
        this.waveMessageDuration = 3.0; // Установка длительности отображения сообщения.
    }
}
