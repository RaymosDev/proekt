package impl.entities; // Объявление пакета, в котором находится класс Drop.

import gameEngine.Entity; // Импорт класса Entity из игрового движка.
import gameEngine.Game; // Импорт класса Game из игрового движка.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.

/**
 * Абстрактный класс Drop представляет собой объект, который может быть подбираем игроком.
 * Он наследует функциональность от класса Entity и содержит логику для управления временем жизни и поведением объекта.
 * Основные компоненты класса:
Поля:

deathTime: Время, когда объект должен исчезнуть.
startY: Начальная позиция по оси Y, используемая для анимации.
Конструктор:

Инициализирует объект с заданной позицией, размером и временем жизни.
Методы:

tick(): Обновляет состояние объекта, проверяет, истекло ли время жизни, и обновляет позицию с анимацией.
onCollisionEnter(Entity other): Обрабатывает столкновение с другим объектом. Если это корабль игрока, вызывается метод onPickup(), и объект удаляется из сцены.
onCollisionExit(Entity other): Метод для обработки выхода из столкновения (в данном случае не реализован).
onPickup(PlayerShip player): Абстрактный метод, который должен быть реализован подклассами для обработки подбора предмета игроком.
 */
public abstract class Drop extends Entity {
    private double deathTime; // Время, когда объект должен исчезнуть.
    private double startY; // Начальная позиция по оси Y, используется для анимации.

    // Конструктор класса Drop, принимающий позицию, размер и время жизни.
    public Drop(Vector2 position, Vector2 size, double lifetime) {
        super(position, size); // Вызов конструктора родительского класса с заданной позицией и размерами.
        deathTime = Game.getInstance().getTime() + lifetime; // Установка времени смерти объекта.
        startY = position.getY(); // Запоминание начальной позиции по оси Y.
    }

    @Override
    public void tick() { // Метод обновления состояния объекта.
        double time = Game.getInstance().getTime(); // Получение текущего времени.
        if (time >= deathTime) { // Проверка, истекло ли время жизни объекта.
            Game.getInstance().getOpenScene().removeObject(this); // Удаление объекта из сцены.
        } else {
            Vector2 position = getPosition(); // Получение текущей позиции объекта.
            // Обновление позиции по оси X (движение влево) и по оси Y (синусоидальное движение).
            position.setX(position.getX() - 150.0 * Game.getInstance().getDeltaTime());
            position.setY(startY + 15.0 * Math.sin(2.0 * (deathTime - time))); // Анимация по оси Y.
            setPosition(position); // Установка новой позиции объекта.
        }
    }

    @Override
    public void onCollisionEnter(Entity other) { // Метод обработки столкновения с другим объектом.
        if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
            onPickup((PlayerShip) other); // Вызов метода обработки подбора предмета.
            Game.getInstance().getOpenScene().removeObject(this); // Удаление объекта из сцены после подбора.
        }
    }

    @Override
    public void onCollisionExit(Entity other) { // Метод обработки выхода из столкновения.
    }

    // Абстрактный метод, который должен быть реализован подклассами для обработки подбора предмета игроком.
    public abstract void onPickup(PlayerShip player);
}