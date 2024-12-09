package gameEngine; 

/*  
*   Collider - обработчких/отслеживатель столкновений с другими коллайдерами (управление своими состояниями и координатами)
*/

import java.util.ArrayList; 
import java.util.List; 

public abstract class Collider 
{ 
    private static List<Collider> activeColliders = new ArrayList<>(); //  список активных коллайдеров

    private double minX, minY, maxX, maxY; // координаты прямоугольника-коллайдера
    private List<Collider> contacts; // список других коллайдеров, с которыми данный коллайдер контактирует

    public Collider(double minX, double minY, double maxX, double maxY) // конструктор, принимающий координаты
    { 
        this.minX = minX; 
        this.minY = minY; 
        this.maxX = maxX; 
        this.maxY = maxY; 
        contacts = new ArrayList<>(); 
        activeColliders.add(this); // добавление текущего коллайдера в список активных (так надо, trust me)
    }

    public Collider(Vector2 center, double width, double height)  // конструктор, принимающий центр и размеры (мб можно было бы обойтись и одним конструктором, но так удобнее)
    {
        this(center.getX() - width / 2.0, center.getY() - height / 2.0, center.getX() + width / 2.0,
             center.getY() + height / 2.0); // вычисление координат на основе центра и размеров.
    }

    public void setActive(boolean active)  // активация/деактивация коллайдера
    {
        if (active) 
        { 
            if (!activeColliders.contains(this)) 
            {
                activeColliders.add(this); 
                checkForCollision(); 
            }
        } 
        else 
        { 
            activeColliders.remove(this); 
            for (int i = 0; i < contacts.size(); i++)  // проход по всем контактам.
            {
                Collider contact = contacts.get(i);
                handleCollisionExit(contact); // обработка выхода из столкновения.
            }
        }
    }


    public double getMinX() {
        return minX; 
    }

    public void setMinX(double minX) { 
        this.minX = minX; 
        checkForCollision(); // проверка на столкновения после изменения
    }

    public double getMinY() { 
        return minY; 
    }

    public void setMinY(double minY) {
        this.minY = minY; 
        checkForCollision(); 
    }

    public double getMaxX() { 
        return maxX; 
    }

    public void setMaxX(double maxX) { 
        this.maxX = maxX; 
        checkForCollision(); 
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY; 
        checkForCollision();
    }

    public Vector2 getMin() { // Минимальная точка коллайдера
        return new Vector2(minX, minY);
    }

    public Vector2 getMax() { // Максимальная точка коллайдера
        return new Vector2(maxX, maxY);
    }

    public Vector2 getCenter() {
        return new Vector2((minX + maxX) / 2.0, (minY + maxY) / 2.0);
    }



    public void setCenter(Vector2 center) // Установка центра коллайдера
    { 
        double semiWidth = (maxX - minX) / 2.0;
        double semiHeight = (maxY - minY) / 2.0;
        minX = center.getX() - semiWidth;
        minY = center.getY() - semiHeight; 
        maxX = center.getX() + semiWidth;
        maxY = center.getY() + semiHeight; 
        checkForCollision(); // проверка на столкновения после изменения
    }

    public Vector2 getDimensions() // Получить размера коллайдера
    {
        return new Vector2(maxX - minX, maxY - minY);
    }

    public void setDimensions(Vector2 dimensions) { // Установить размеры коллайдера
        Vector2 center = getCenter(); 
        minX = center.getX() - dimensions.getX() / 2.0; 
        minY = center.getY() - dimensions.getY() / 2.0; 
        maxX = center.getX() + dimensions.getX() / 2.0;
        maxY = center.getY() + dimensions.getY() / 2.0; 
        checkForCollision(); // Проверка на столкновения после изменения
    }

    public boolean isContacting(Collider other) { // метод для проверки столкновения с другим коллайдером
        return this.minX <= other.maxX && this.maxX >= other.minX && this.minY <= other.maxY && this.maxY >= other.minY; // Возврат true, если коллайдеры пересекаются
    }

    public List<Collider> getContacts() { // получить список контактов
        return new ArrayList<>(contacts);
    }


    // !!! Проверка столкновений с другими коллайдерами
    private void checkForCollision() { 
        for (int i = 0; i < activeColliders.size(); i++) // пробегаемся по всем активным коллайдерам
        {
            Collider other = activeColliders.get(i); 

            if (other == this) // если этот "другой" коллайдер на самом деле ЭТОТ коллайдер
            { 
                continue; // пропуск итерации
            }

            boolean collides = this.isContacting(other); // Проверка на столкновение с другим коллайдером
            
            if (contacts.contains(other)) // Если коллайдер уже в списке контактов
            { 
                if (!collides) // Если столкновения больше нет
                { 
                    handleCollisionExit(other); // Обработка выхода из столкновения
                }
            } 
            else // Если коллайдер не в списке контактов
            { 
                if (collides) // Если произошла коллизия
                { 
                    handleCollisionEnter(other); // Обработка входа в столкновение
                }
            }
        }
    }

    private void handleCollisionEnter(Collider other) // Обработка входа в столкновение для обоих коллайдеров
    { 
        this.contacts.add(other); 
        other.contacts.add(this); 
        this.onCollisionEnter(other); 
        other.onCollisionEnter(this); 
    }

    private void handleCollisionExit(Collider other) // Обработка выхода из столкновения для обоих коллайдеров
    { 
        this.contacts.remove(other); 
        other.contacts.remove(this); 
        this.onCollisionExit(other); 
        other.onCollisionExit(this); 
    }

    public abstract void onCollisionEnter(Collider other); // Обработка входа в столкновение

    public abstract void onCollisionExit(Collider other); // Обработка выхода из столкновения
}
