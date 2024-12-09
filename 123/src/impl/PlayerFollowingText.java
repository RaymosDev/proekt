package impl;

/*  
*  Рисуем текст рядом с игроком
*/

import gameEngine.Game; 
import gameEngine.ResourceLoader;
import gameEngine.SceneObject;
import gameEngine.Vector2;
import impl.entities.PlayerShip; 
import impl.scenes.GameScene;
import java.awt.Color;
import java.awt.Font; 
import java.awt.Graphics; 

public class PlayerFollowingText extends SceneObject { 
    private static final double DURATION = 1.25; // Длительность отображения текста (сек)
    private static final Font FONT = ResourceLoader.loadFont("res/Font.ttf", 30); 

    private String text; // Сам текст
    private double deathTime; // Игровое время, когда текст должен исчезнуть


    public PlayerFollowingText(String text) {
        this.text = text; 
        deathTime = Game.getInstance().getTime() + DURATION;
    }

    @Override
    public void initialize() { 
    }

    @Override
    public void tick() // Обновления состояния объекта
    {
        if (Game.getInstance().getTime() >= deathTime) 
        {
            Game.getInstance().getOpenScene().removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) // Отрисовка текста
    { 
        g.setFont(FONT);
        g.setColor(Color.WHITE);
        PlayerShip player = ((GameScene) Game.getInstance().getOpenScene()).getPlayer(); // Получили объект игрока в текущей сцене
        Vector2 textPosition = player.getPosition().add(75, -20); // Вычислили позицию текста
        g.drawString(text, (int) textPosition.getX(), (int) textPosition.getY()); // Отрисовка текста
    }

    @Override
    public void dispose() { // типа "Освобождение ресурсов"
    }
}
