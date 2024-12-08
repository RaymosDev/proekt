package impl.entities; // Объявление пакета, в котором находится класс Explosion.

import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.

import gameEngine.Entity; // Импорт класса Entity из игрового движка.
import gameEngine.Game; // Импорт класса Game из игрового движка.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.

/**
 * Класс Explosion представляет собой анимацию взрыва в игре.
 * Он наследует функциональность от класса Entity и управляет отображением кадров анимации.
 * Основные компоненты класса:
Поля:

FRAMES: Массив изображений, представляющих кадры анимации взрыва.
lifetime: Время, в течение которого анимация будет отображаться.
startTime: Время начала анимации.
scaledFrames: Массив кадров, масштабированных до заданного размера.
Конструктор:

Инициализирует объект с заданной позицией, размером и временем жизни. Масштабирует кадры анимации до заданного размера.
Методы:

initialize(): Инициализирует объект и запускает звуковой эффект взрыва.
tick(): Обновляет состояние объекта (пока не реализован).
render(Graphics g): Отрисовывает текущий кадр анимации на экране, проверяя, не истекло ли время жизни анимации.
onCollisionEnter(Entity other): Метод для обработки столкновения (не реализован).
onCollisionExit(Entity other): Метод для обработки выхода из столкновения (не реализован).
 */
public class Explosion extends Entity {
    // Массив кадров анимации взрыва, загружаемых из ресурсов.
    private static final Image[] FRAMES = {
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion1.png"),
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion2.png"),
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion3.png"),
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion4.png"),
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion5.png"),
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion6.png"),
        ResourceLoader.loadImage("res/images/entities/explosion/Explosion7.png")
    };

    private double lifetime; // Время жизни анимации взрыва.
    private double startTime; // Время, когда анимация была запущена.
    private Image[] scaledFrames; // Массив кадров, масштабированных до заданного размера.

    // Конструктор класса Explosion, принимающий позицию, размер и время жизни.
    public Explosion(Vector2 position, int size, double lifetime) {
        super(position, new Vector2(size, size)); // Вызов конструктора родительского класса с заданной позицией и размерами.
        this.lifetime = lifetime; // Установка времени жизни анимации.
        startTime = Game.getInstance().getTime(); // Запоминание времени начала анимации.
        scaledFrames = new Image[FRAMES.length]; // Инициализация массива масштабированных кадров.
        for (int i = 0; i < FRAMES.length; i++) {
            // Масштабирование каждого кадра до заданного размера.
            scaledFrames[i] = FRAMES[i].getScaledInstance(size, size, Image.SCALE_FAST);
        }
    }

    @Override
    public void initialize() { // Метод инициализации объекта.
        super.initialize(); // Вызов метода инициализации родительского класса.
        ResourceLoader.loadAudioClip("res/audio/Explosion.wav").start(); // Запуск звукового эффекта взрыва.
    }

    @Override
    public void tick() { // Метод обновления состояния объекта (в данном случае не реализован).
    }

    @Override
    public void render(Graphics g) { // Метод отрисовки анимации на экране.
        double time = Game.getInstance().getTime(); // Получение текущего времени.
        double proportion = (time - startTime) / lifetime; // Вычисление пропорции времени жизни.
        int frameIndex = (int) (proportion * FRAMES.length); // Определение текущего кадра анимации.
        if (frameIndex >= FRAMES.length) { // Если время анимации истекло.
            Game.getInstance().getOpenScene().removeObject(this); // Удаление объекта из сцены.
            return; // Прекращение выполнения метода.
        }
        Image frame = scaledFrames[frameIndex]; // Получение текущего кадра для отображения.
        Vector2 position = getPosition(); // Получение текущей позиции объекта.
        Vector2 size = getSize(); // Получение размера объекта.
        // Вычисление координат для отрисовки кадра, чтобы центрировать его.
        int x = (int) (position.getX() - size.getX() / 2.0);
        int y = (int) (position.getY() - size.getY() / 2.0);
        g.drawImage(frame, x, y, null); // Отрисовка текущего кадра на графическом контексте.
    }

    @Override
    public void onCollisionEnter(Entity other) { // Метод обработки столкновения с другим объектом (не реализован).
    }

    @Override
    public void onCollisionExit(Entity other) { // Метод обработки выхода из столкновения (не реализован).
    }
}
