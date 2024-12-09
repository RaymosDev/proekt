package gameEngine;

/*  
*   Vector2 класс представляет собой двумерный вектор и всевозможные операции с ним
*/

public class Vector2 
{ 
    // Координаты
    public double x; 
    public double y;


    public Vector2(double x, double y) 
    {
        this.x = x; 
        this.y = y;
    }


    public static Vector2 fromAngle(double angle, double magnitude)  // Создания вектора из угла и величины (Полярная СК)
    {
        double x = Math.cos(angle) * magnitude; 
        double y = Math.sin(angle) * magnitude; 
        return new Vector2(x, y); 
    }


    public double getX() 
    {
        return x; 
    }


    public Vector2 setX(double x) 
    {
        this.x = x; // Установка нового значения координаты X.
        return this; // возвращаем этот же объект для цепного вызова (см. method chaining)
    }


    public double getY() 
    {
        return y;
    }


    public Vector2 setY(double y) 
    {
        this.y = y; 
        return this; 
    }

   
    public Vector2 add(Vector2 other) // Сложение вектора с вектором
    {
        return add(other.x, other.y); 
    }


    public Vector2 add(double x, double y) // Сложение вектора с заданными координатами
    {
        this.x += x; 
        this.y += y; 
        return this; 
    }


    public Vector2 subtract(Vector2 other) // Разность вектора и вектора
    {
        return subtract(other.x, other.y); 
    }


    public Vector2 subtract(double x, double y) // Разность вектора и заданных координат
    {
        this.x -= x; 
        this.y -= y; 
        return this; 
    }

  
    public Vector2 multiply(double scalar)   // Умножения вектора на скаляр
    {
        x *= scalar;
        y *= scalar; 
        return this;
    }


    public double magnitude()  // Вычисление модуля (длины) вектора 
    {
        return Math.sqrt(x * x + y * y); 
    }

   
    public Vector2 normalize()  // Нормализация вектора (кто забыл: Нормализация вектора - это уменьшение длины вектора до единичной при сохранении направления, для этого каждую координату делим на длину вектора)
    {
        double magnitude = magnitude();
        x /= magnitude; 
        y /= magnitude; 
        return this; 
    }

  
    public double dotProduct(Vector2 other)  // Вчисления скалярного произведения с другим вектором
    {
        return this.x * other.x + this.y * other.y; 
    }


    public double angleBetween(Vector2 other) // вычисления угла между текущим вектором и другим вектором
    {
        return Math.acos(this.dotProduct(other) / (this.magnitude() * other.magnitude())); // Вычисление угла с использованием обратного косинуса.
    }

    
    public Vector2 rotate(double angle) // Поворот вектора на заданный угол (https://ru.stackoverflow.com/questions/1409394/%D0%9A%D0%B0%D0%BA-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0%D0%B5%D1%82-%D1%84%D0%BE%D1%80%D0%BC%D1%83%D0%BB%D0%B0-%D0%BF%D0%BE%D0%B2%D0%BE%D1%80%D0%BE%D1%82%D0%B0-%D0%B2%D0%B5%D0%BA%D1%82%D0%BE%D1%80%D0%B0-%D0%BD%D0%B0-%D1%83%D0%B3%D0%BE%D0%BB)
    {
        double sin = Math.sin(angle); 
        double cos = Math.cos(angle); 
        double newX = x * cos - y * sin; 
        double newY = x * sin + y * cos; 
        x = newX;
        y = newY; 
        return this; 
    }


    public Vector2 clone()  // Создать копию текущего вектора
    {
        return new Vector2(x, y);
    }



    // для удобного отображения вектора
    @Override
    public String toString() 
    {
        return String.format("[%f, %f]", x, y);
    }
}