package impl.entities; // Объявление пакета, в котором находится класс HealthDrop.

import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.

import gameEngine.Game; // Импорт класса Game из игрового движка.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.PlayerFollowingText; // Импорт класса, отображающего текст, следящий за игроком.
import impl.ResolutionConfig; // Импорт класса, конфигурирующего разрешение и размеры объектов.

/**
 * Класс HealthDrop представляет собой объект, восстанавливающий здоровье игрока.
 * Он наследует функциональность от класса Drop.
 * Основные компоненты класса:
Поля:

HEAL_AMOUNT: Количество здоровья, восстанавливаемого при подборе.
LIFETIME: Время, в течение которого объект будет существовать в игре.
WIDTH и HEIGHT: Размеры объекта, которые будут обновляться в зависимости от разрешения экрана.
SPRITE: Изображение, представляющее объект.
Статический блок:

Вызывается при загрузке класса для обновления размеров и загрузки изображения.
Конструктор:

Инициализирует объект с заданной позицией, размерами и временем жизни.
Методы:

updateDimensions(): Обновляет размеры объекта в зависимости от текущего разрешения и загружает изображение.
onPickup(PlayerShip player): Обрабатывает событие подбора игроком, восстанавливая здоровье и воспроизводя звуковой эффект.
render(Graphics g): Отрисовывает объект на экране, центрируя его по текущей позиции.
 */
public class HealthDrop extends Drop {
    private static final double HEAL_AMOUNT = 4; // Количество здоровья, восстанавливаемого при подборе.
    private static final double LIFETIME = 10.0; // Время жизни объекта HealthDrop.

    private static int WIDTH; // Ширина объекта.
    private static int HEIGHT; // Высота объекта.
    private static Image SPRITE; // Изображение объекта.

    // Статический блок для обновления размеров и загрузки изображения при загрузке класса.
    static {
        updateDimensions();
    }

    // Конструктор класса HealthDrop, принимающий позицию.
    public HealthDrop(Vector2 position) {
        super(position, new Vector2(WIDTH, HEIGHT), LIFETIME); // Вызов конструктора родительского класса с заданной позицией и размерами.
    }

    // Метод для обновления размеров объекта в зависимости от текущего разрешения.
    public static void updateDimensions() {
        ResolutionConfig.Resolution currentResolution = ResolutionConfig.getHealthDropSize(); // Получение текущего разрешения для HealthDrop.
        WIDTH = currentResolution.width; // Установка ширины объекта.
        HEIGHT = currentResolution.height; // Установка высоты объекта.
        // Загрузка и масштабирование изображения для HealthDrop.
        SPRITE = ResourceLoader.loadImage("res/images/entities/drops/HealthDrop.png")
                .getScaledInstance(WIDTH, HEIGHT, 0);
    }

    // Метод обработки подбора объекта игроком.
    @Override
    public void onPickup(PlayerShip player) {
        player.heal(HEAL_AMOUNT); // Восстановление здоровья игрока.
        ResourceLoader.loadAudioClip("res/audio/RestoreHP.wav").start(); // Запуск звукового эффекта восстановления здоровья.
        // Создание текста, следящего за игроком, для отображения количества восстановленного здоровья.
        PlayerFollowingText text = new PlayerFollowingText("+" + (int) HEAL_AMOUNT + " HP");
        Game.getInstance().getOpenScene().addObject(text); // Добавление текста в текущую сцену.
    }

    // Метод отрисовки объекта на экране.
    @Override
    public void render(Graphics g) {
        Vector2 position = getPosition(); // Получение текущей позиции объекта.
        // Отрисовка изображения объекта, центрируя его по координатам.
        g.drawImage(SPRITE, (int) (position.getX() - WIDTH / 2.0), (int) (position.getY() - HEIGHT / 2.0), null);
    }
}
