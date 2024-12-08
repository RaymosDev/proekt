package gameEngine; // Объявление пакета, в котором находится класс Collider.

/*  Этот класс Collider служит основой для создания коллайдеров в игре, позволяя 
    им отслеживать столкновения с 
    другими коллайдерами и управлять своими состояниями и координатами.
*/

import java.util.ArrayList; // Импорт класса ArrayList для использования динамического массива.
import java.util.List; // Импорт интерфейса List для работы с коллекциями.

public abstract class Collider { // Объявление абстрактного класса Collider, который будет служить основой для других классов.
    private static List<Collider> activeColliders = new ArrayList<>(); // Статический список активных коллайдеров.

    private double minX, minY, maxX, maxY; // Координаты ограничивающего прямоугольника коллайдера.
    private List<Collider> contacts; // Список коллайдеров, с которыми данный коллайдер контактирует.

    public Collider(double minX, double minY, double maxX, double maxY) { // Конструктор, принимающий координаты.
        this.minX = minX; // Инициализация минимальной координаты X.
        this.minY = minY; // Инициализация минимальной координаты Y.
        this.maxX = maxX; // Инициализация максимальной координаты X.
        this.maxY = maxY; // Инициализация максимальной координаты Y.
        contacts = new ArrayList<>(); // Инициализация списка контактов.
        activeColliders.add(this); // Добавление текущего коллайдера в список активных.
    }

    public Collider(Vector2 center, double width, double height) { // Конструктор, принимающий центр и размеры.
        this(center.getX() - width / 2.0, center.getY() - height / 2.0, center.getX() + width / 2.0,
             center.getY() + height / 2.0); // Вычисление координат на основе центра и размеров.
    }

    public void setActive(boolean active) { // Метод для активации или деактивации коллайдера.
        if (active) { // Если коллайдер становится активным.
            if (!activeColliders.contains(this)) { // Проверка, не добавлен ли коллайдер уже в список.
                activeColliders.add(this); // Добавление коллайдера в список активных.
                checkForCollision(); // Проверка на столкновения с другими коллайдерами.
            }
        } else { // Если коллайдер становится неактивным.
            activeColliders.remove(this); // Удаление коллайдера из списка активных.
            for (int i = 0; i < contacts.size(); i++) { // Проход по всем контактам.
                Collider contact = contacts.get(i); // Получение текущего контакта.
                handleCollisionExit(contact); // Обработка выхода из столкновения.
            }
        }
    }

    public double getMinX() { // Метод для получения минимальной координаты X.
        return minX; // Возврат значения minX.
    }

    public void setMinX(double minX) { // Метод для установки минимальной координаты X.
        this.minX = minX; // Установка нового значения minX.
        checkForCollision(); // Проверка на столкновения после изменения.
    }

    public double getMinY() { // Метод для получения минимальной координаты Y.
        return minY; // Возврат значения minY.
    }

    public void setMinY(double minY) { // Метод для установки минимальной координаты Y.
        this.minY = minY; // Установка нового значения minY.
        checkForCollision(); // Проверка на столкновения после изменения.
    }

    public double getMaxX() { // Метод для получения максимальной координаты X.
        return maxX; // Возврат значения maxX.
    }

    public void setMaxX(double maxX) { // Метод для установки максимальной координаты X.
        this.maxX = maxX; // Установка нового значения maxX.
        checkForCollision(); // Проверка на столкновения после изменения.
    }

    public double getMaxY() { // Метод для получения максимальной координаты Y.
        return maxY; // Возврат значения maxY.
    }

    public void setMaxY(double maxY) { // Метод для установки максимальной координаты Y.
        this.maxY = maxY; // Установка нового значения maxY.
        checkForCollision(); // Проверка на столкновения после изменения.
    }

    public Vector2 getMin() { // Метод для получения минимальной точки коллайдера.
        return new Vector2(minX, minY); // Возврат новой точки с координатами (minX, minY).
    }

    public Vector2 getMax() { // Метод для получения максимальной точки коллайдера.
        return new Vector2(maxX, maxY); // Возврат новой точки с координатами (maxX, maxY).
    }

    public Vector2 getCenter() { // Метод для получения центра коллайдера.
        return new Vector2((minX + maxX) / 2.0, (minY + maxY) / 2.0); // Возврат центра как новой точки.
    }

    public void setCenter(Vector2 center) { // Метод для установки центра коллайдера.
        double semiWidth = (maxX - minX) / 2.0; // Половина ширины.
        double semiHeight = (maxY - minY) / 2.0; // Половина высоты.
        minX = center.getX() - semiWidth; // Установка новой минимальной координаты X.
        minY = center.getY() - semiHeight; // Установка новой минимальной координаты Y.
        maxX = center.getX() + semiWidth; // Установка новой максимальной координаты X.
        maxY = center.getY() + semiHeight; // Установка новой максимальной координаты Y.
        checkForCollision(); // Проверка на столкновения после изменения.
    }

    public Vector2 getDimensions() { // Метод для получения размеров коллайдера.
        return new Vector2(maxX - minX, maxY - minY); // Возврат нового вектора с шириной и высотой.
    }

    public void setDimensions(Vector2 dimensions) { // Метод для установки размеров коллайдера.
        Vector2 center = getCenter(); // Получение текущего центра коллайдера.
        minX = center.getX() - dimensions.getX() / 2.0; // Установка новой минимальной координаты X.
        minY = center.getY() - dimensions.getY() / 2.0; // Установка новой минимальной координаты Y.
        maxX = center.getX() + dimensions.getX() / 2.0; // Установка новой максимальной координаты X.
        maxY = center.getY() + dimensions.getY() / 2.0; // Установка новой максимальной координаты Y.
        checkForCollision(); // Проверка на столкновения после изменения.
    }

    public boolean isContacting(Collider other) { // Метод для проверки столкновения с другим коллайдером.
        return this.minX <= other.maxX && this.maxX >= other.minX && this.minY <= other.maxY && this.maxY >= other.minY; // Возврат true, если коллайдеры пересекаются.
    }

    public List<Collider> getContacts() { // Метод для получения списка контактов.
        return new ArrayList<>(contacts); // Возврат копии списка контактов.
    }

    private void checkForCollision() { // Метод для проверки столкновений с другими коллайдерами.
        for (int i = 0; i < activeColliders.size(); i++) { // Проход по всем активным коллайдерам.
            Collider other = activeColliders.get(i); // Получение текущего коллайдера.
            if (other == this) { // Проверка, не является ли это текущий коллайдер.
                continue; // Пропуск итерации, если это так.
            }
            boolean collides = this.isContacting(other); // Проверка на столкновение с другим коллайдером.
            if (contacts.contains(other)) { // Если коллайдер уже в списке контактов.
                if (!collides) { // Если столкновения больше нет.
                    handleCollisionExit(other); // Обработка выхода из столкновения.
                }
            } else { // Если коллайдер не в списке контактов.
                if (collides) { // Если произошла коллизия.
                    handleCollisionEnter(other); // Обработка входа в столкновение.
                }
            }
        }
    }

    private void handleCollisionEnter(Collider other) { // Метод для обработки входа в столкновение.
        this.contacts.add(other); // Добавление другого коллайдера в список контактов.
        other.contacts.add(this); // Добавление текущего коллайдера в список контактов другого коллайдера.
        this.onCollisionEnter(other); // Вызов метода обработки входа в столкновение для текущего коллайдера.
        other.onCollisionEnter(this); // Вызов метода обработки входа в столкновение для другого коллайдера.
    }

    private void handleCollisionExit(Collider other) { // Метод для обработки выхода из столкновения.
        this.contacts.remove(other); // Удаление другого коллайдера из списка контактов.
        other.contacts.remove(this); // Удаление текущего коллайдера из списка контактов другого коллайдера.
        this.onCollisionExit(other); // Вызов метода обработки выхода из столкновения для текущего коллайдера.
        other.onCollisionExit(this); // Вызов метода обработки выхода из столкновения для другого коллайдера.
    }

    public abstract void onCollisionEnter(Collider other); // Абстрактный метод для обработки входа в столкновение, должен быть реализован в подклассах.

    public abstract void onCollisionExit(Collider other); // Абстрактный метод для обработки выхода из столкновения, должен быть реализован в подклассах.
}
