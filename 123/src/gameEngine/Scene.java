package gameEngine;

/*  
*   Класс для создания сцен. Тут функционал добавления, удаления, получения объектов в сцене. 
*/

import java.awt.Graphics;
import java.util.ArrayList; 
import java.util.List;

public abstract class Scene 
{ 
    public List<SceneObject> objects; // Список объектов сцены

    public Scene() 
    {
        objects = new ArrayList<>();
    }

    public final void addObject(SceneObject object)  // Добавление объекта в сцену
    {
        objects.add(object);
        object.initialize(); 
    }

    public final void removeObject(SceneObject object) // Удаление объекта из сцены
    { 
        objects.remove(object);
        object.dispose();
    }

    public final List<SceneObject> getObjects()  // Получение списка объектов сцены
    {
        return new ArrayList<>(objects);
    }

    public abstract void initialize(); // Инициализации сцены

    public void tick() // Пробегаемся по всем объектам в сцене и обновляем их состояния
    { 
        for (int i = 0; i < objects.size(); i++) 
        { 
            SceneObject object = objects.get(i); 
            object.tick();
        }
    }

    public void render(Graphics g) // Пробегаемся по всем объектам в сцене и отрисовываем каждый
    { 
        for (int i = 0; i < objects.size(); i++) 
        { 
            SceneObject object = objects.get(i); 
            object.render(g);
        }
    }

    public void dispose() // Освобождения ресурсов всех объектов в сцене
    { 
        for (int i = 0; i < objects.size(); i++) 
        {
            objects.get(i).dispose();
        }
    }
}