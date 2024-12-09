package gameEngine;

/*  
*   SceneObject - основа для создания различных объектов, которые могут быть добавлены в сцену (с) Умный индус
*/

import java.awt.Graphics;

public abstract class SceneObject { 

    public abstract void initialize(); // Инициализации объекта

    public abstract void tick(); // Обновления состояния объекта 

    public abstract void render(Graphics g); // Отрисовка объекта 

    public abstract void dispose(); // Освобождение ресурсов объекта 
}
