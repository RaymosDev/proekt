package impl.waves; // Объявление пакета, в котором находится класс Wave.

/*  Класс Wave является абстрактным классом, который служит основой 
    для создания различных волн врагов в игре. Он предоставляет методы 
    для создания различных типов врагов, таких как астероиды, Javelin, Hornet 
    и Marauder, а также определяет методы для инициализации волны 
    и установки сообщения о текущей волне.
Основные компоненты класса:
Методы для создания врагов:

spawnAsteroid(): Создает и добавляет большой астероид в текущую игровую сцену.
spawnJavelin(): Создает и добавляет врага Javelin в текущую игровую сцену.
spawnHornet(): Создает и добавляет врага Hornet в текущую игровую сцену.
spawnMarauder(): Создает и добавляет врага Marauder в текущую игровую сцену.
Методы:

initialize(): Метод, который вызывается при инициализации волны, устанавливает сообщение о текущей волне.
getWaveMessage(): Абстрактный метод, который должен быть реализован в подклассах для предоставления сообщения о волне.
render(Graphics g): Переопределенный метод для отрисовки волны; в данном случае он не выполняет никаких действий.
dispose(): Переопределенный метод для освобождения ресурсов; в данном случае он не выполняет никаких действий.
*/

import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import gameEngine.Game; // Импорт класса Game для управления игровым процессом.
import gameEngine.SceneObject; // Импорт класса SceneObject, от которого наследуется Wave.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт главного класса приложения.
import impl.entities.AsteroidLarge; // Импорт класса AsteroidLarge, представляющего большой астероид.
import impl.entities.Hornet; // Импорт класса Hornet, представляющего врага Hornet.
import impl.entities.Javelin; // Импорт класса Javelin, представляющего врага Javelin.
import impl.entities.Marauder; // Импорт класса Marauder, представляющего врага Marauder.
import impl.scenes.GameScene; // Импорт класса GameScene, представляющего игровую сцену.

public abstract class Wave extends SceneObject { // Определение абстрактного класса Wave, который наследует SceneObject.
    
    // Метод для создания большого астероида.
    protected void spawnAsteroid() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        Vector2 position = new Vector2(Main.WIDTH + 100, Main.HEIGHT * Math.random()); // Установка случайной позиции для астероида.
        Vector2 direction = new Vector2(-1, Math.random() - 0.5); // Установка направления движения астероида.
        AsteroidLarge enemy = new AsteroidLarge(position, direction); // Создание нового астероида.
        scene.addObject(enemy); // Добавление астероида в сцену.
    }

    // Метод для создания врага Javelin.
    protected void spawnJavelin() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        Vector2 position = new Vector2(Main.WIDTH + 50, Math.random() * Main.HEIGHT * 0.85 + 0.075 * Main.HEIGHT); // Установка случайной позиции для Javelin.
        Javelin enemy = new Javelin(position); // Создание нового врага Javelin.
        scene.addObject(enemy); // Добавление Javelin в сцену.
    }

    // Метод для создания врага Hornet.
    protected void spawnHornet() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        Vector2 position = new Vector2(Main.WIDTH + 50, Math.random() * Main.HEIGHT); // Установка случайной позиции для Hornet.
        Hornet enemy = new Hornet(position); // Создание нового врага Hornet.
        scene.addObject(enemy); // Добавление Hornet в сцену.
    }

    // Метод для создания врага Marauder.
    protected void spawnMarauder() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        Vector2 position = new Vector2(Main.WIDTH + 100, Math.random() * Main.HEIGHT * 0.85 + 0.075 * Main.HEIGHT); // Установка случайной позиции для Marauder.
        Marauder enemy = new Marauder(position); // Создание нового врага Marauder.
        scene.addObject(enemy); // Добавление Marauder в сцену.
    }

    // Метод инициализации волны врагов.
    @Override
    public void initialize() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        scene.setWaveMessage(getWaveMessage()); // Установка сообщения о текущей волне.
    }

    // Абстрактный метод для получения сообщения о волне, который должен быть реализован в подклассах.
    protected abstract String getWaveMessage();

    // Переопределение метода render для отрисовки волны (в данном случае ничего не отрисовывается).
    @Override
    public void render(Graphics g) {
    }

    // Переопределение метода dispose для освобождения ресурсов (в данном случае ничего не освобождается).
    @Override
    public void dispose() {
    }
}
