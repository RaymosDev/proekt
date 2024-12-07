package impl.entities; // Объявление пакета, в котором находится класс Projectile.

import gameEngine.Entity; // Импорт класса Entity, от которого наследуется Projectile.
import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.

/**
 * Абстрактный класс Projectile представляет собой снаряд, который может двигаться
 * в игровом мире. Он наследует от класса Entity и управляет своей скоростью.
 * Основные компоненты класса:
Поля:

velocity: Вектор, представляющий скорость снаряда. Он определяет, как быстро и в каком направлении снаряд будет двигаться.
Конструктор:

Projectile(Vector2 position, Vector2 size, Vector2 velocity): Конструктор принимает позицию, размер и скорость снаряда. Он вызывает конструктор родительского класса с позицией и размером, а также клонирует вектор скорости, чтобы избежать изменений оригинала.
Методы:

tick(): Этот метод обновляет состояние снаряда на каждом кадре. Он получает текущую позицию, добавляет к ней скорость, умноженную на время, прошедшее с последнего обновления, и устанавливает новую позицию.
 */
public abstract class Projectile extends Entity {
    private Vector2 velocity; // Вектор, представляющий скорость снаряда.

    // Конструктор класса Projectile, принимающий позицию, размер и скорость.
    public Projectile(Vector2 position, Vector2 size, Vector2 velocity) {
        super(position, size); // Вызов конструктора родительского класса с позицией и размером.
        this.velocity = velocity.clone(); // Клонирование вектора скорости для предотвращения изменений оригинала.
    }

    // Метод обновления состояния снаряда на каждом кадре.
    @Override
    public void tick() {
        Vector2 position = getPosition(); // Получение текущей позиции снаряда.
        position.add(velocity.clone().multiply(Game.getInstance().getDeltaTime())); // Обновление позиции в зависимости от скорости и времени.
        setPosition(position); // Установка обновленной позиции снаряда.
    }
}
