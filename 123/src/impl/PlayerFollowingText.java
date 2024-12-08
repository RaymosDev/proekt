package impl; // Объявление пакета, в котором находится класс PlayerFollowingText.

/*  Класс PlayerFollowingText представляет собой объект сцены, 
    который отображает текст, следящий за позицией игрока. Он имеет длительность
    отображения, после которой текст исчезает. В конструкторе устанавливается 
    текст и время его исчезновения. Метод tick проверяет, истекло ли время, 
    и удаляет текст, если это так. Метод render отвечает за отрисовку текста 
    на экране, устанавливая шрифт и цвет, 
    а также вычисляя позицию текста относительно игрока.
*/

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Font; // Импорт класса Font для работы с шрифтами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.

import gameEngine.Game; // Импорт класса Game из пакета gameEngine.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader из пакета gameEngine.
import gameEngine.SceneObject; // Импорт класса SceneObject из пакета gameEngine.
import gameEngine.Vector2; // Импорт класса Vector2 из пакета gameEngine.
import impl.entities.PlayerShip; // Импорт класса PlayerShip из пакета impl.entities.
import impl.scenes.GameScene; // Импорт класса GameScene из пакета impl.scenes.

public class PlayerFollowingText extends SceneObject { // Объявление класса PlayerFollowingText, наследующего от SceneObject.
    private static final double DURATION = 1.25; // Длительность отображения текста в секундах.
    private static final Font FONT = ResourceLoader.loadFont("res/Font.ttf", 30); // Загрузка шрифта для текста.

    private String text; // Строка, содержащая текст для отображения.
    private double deathTime; // Время, когда текст должен исчезнуть.

    // Конструктор класса, принимающий текст для отображения.
    public PlayerFollowingText(String text) {
        this.text = text; // Инициализация поля text.
        deathTime = Game.getInstance().getTime() + DURATION; // Установка времени исчезновения текста.
    }

    @Override
    public void initialize() { // Метод инициализации (пока не используется).
    }

    @Override
    public void tick() { // Метод обновления состояния объекта.
        // Проверка, истекло ли время отображения текста.
        if (Game.getInstance().getTime() >= deathTime) {
            Game.getInstance().getOpenScene().removeObject(this); // Удаление объекта из сцены.
        }
    }

    @Override
    public void render(Graphics g) { // Метод для отрисовки текста.
        g.setFont(FONT); // Установка шрифта для графического контекста.
        g.setColor(Color.WHITE); // Установка цвета текста (белый).
        PlayerShip player = ((GameScene) Game.getInstance().getOpenScene()).getPlayer(); // Получение объекта игрока из текущей сцены.
        Vector2 textPosition = player.getPosition().add(75, -20); // Вычисление позиции текста относительно игрока.
        g.drawString(text, (int) textPosition.getX(), (int) textPosition.getY()); // Отрисовка текста на экране.
    }

    @Override
    public void dispose() { // Метод освобождения ресурсов (пока не используется).
    }
}
