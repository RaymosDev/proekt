package gameEngine; 

/*  
    Entity - это SceneObject, который имеет коллайдер для обработки столкновений с другими entity. 
    
    Он предоставляет методы для получения и установки позиции и размера, 
    а также абстрактные методы для обработки столкновений. 
    Вложенный класс EntityCollider расширяет функциональность коллайдера, 
    обеспечивая взаимодействие с другими коллайд
*/

public abstract class Entity extends SceneObject 
{
    private Collider collider; // Объект коллайдера, который будет использоваться для обнаружения столкновений.

    public Entity(Vector2 position, Vector2 size) { // Конструктор класса Entity, принимающий позицию и размер.
        collider = new EntityCollider(this, position, size); // Инициализация коллайдера для этой сущности с заданной позицией и размером.
    }

    @Override
    public void initialize() { // Переопределение метода инициализации.
        collider.setActive(true); // Активация коллайдера при инициализации сущности.
    }

    public final Vector2 getPosition() { // Метод для получения текущей позиции сущности.
        return collider.getCenter(); // Возврат центра коллайдера как позиции сущности.
    }

    public final void setPosition(Vector2 position) { // Метод для установки новой позиции сущности.
        collider.setCenter(position); // Установка центра коллайдера на новую позицию.
    }

    public Vector2 getSize() { // Метод для получения размера сущности.
        return collider.getDimensions().clone(); // Возврат размеров коллайдера как размера сущности.
    }

    public void setSize(Vector2 size) { // Метод для установки нового размера сущности.
        collider.setDimensions(size); // Установка новых размеров для коллайдера.
    }

    @Override
    public void dispose() { // Переопределение метода освобождения ресурсов.
        collider.setActive(false); // Деактивация коллайдера при освобождении сущности.
    }

    public abstract void onCollisionEnter(Entity other); // Абстрактный метод для обработки входа в столкновение с другой сущностью.

    public abstract void onCollisionExit(Entity other); // Абстрактный метод для обработки выхода из столкновения с другой сущностью.

    public static class EntityCollider extends Collider { // Вложенный класс EntityCollider, наследующий от Collider.
        private Entity entity; // Ссылка на сущность, к которой принадлежит этот коллайдер.

        private EntityCollider(Entity entity, Vector2 position, Vector2 size) { // Конструктор класса EntityCollider.
            super(position, size.getX(), size.getY()); // Вызов конструктора родительского класса Collider с позицией и размерами.
            this.entity = entity; // Сохранение ссылки на сущность.
        }

        public Entity getEntity() { // Метод для получения сущности, к которой принадлежит этот коллайдер.
            return entity; // Возврат ссылки на сущность.
        }

        @Override
        public void onCollisionEnter(Collider other) { // Переопределение метода обработки входа в столкновение.
            if (other instanceof EntityCollider) { // Проверка, является ли другой коллайдер коллайдером сущности.
                Entity otherEntity = ((EntityCollider) other).getEntity(); // Получение другой сущности.
                entity.onCollisionEnter(otherEntity); // Вызов метода обработки входа в столкновение для этой сущности.
            }
        }

        @Override
        public void onCollisionExit(Collider other) { // Переопределение метода обработки выхода из столкновения.
            if (other instanceof EntityCollider) { // Проверка, является ли другой коллайдер коллайдером сущности.
                Entity otherEntity = ((EntityCollider) other).getEntity(); // Получение другой сущности.
                entity.onCollisionExit(otherEntity); // Вызов метода обработки выхода из столкновения для этой сущности.
            }
        }
    }
}
