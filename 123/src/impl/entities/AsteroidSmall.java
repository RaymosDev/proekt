package impl.entities; // Объявление пакета, в котором находится класс AsteroidSmall.

/*  Класс AsteroidSmall представляет собой маленький астероид в игре, 
    который наследует функциональность от класса Entity и реализует интерфейс 
    DamagableEntity. Он содержит статические переменные для размеров и спрайтов, 
    а также динамические переменные для управления состоянием астероида, 
    такими как скорость и здоровье.
    Методы класса обеспечивают обновление позиции астероида, его отрисовку, 
    обработку столкновений, получение урона и уничтожение. Маленький астероид 
    может взаимодействовать с кораблем игрока, нанося ему урон, 
    и производить эффект взрыва при уничтожении.
*/

import java.awt.Graphics; // Импорт класса Graphics для отрисовки графики.
import java.awt.Image; // Импорт класса Image для работы с изображениями.

import gameEngine.Entity; // Импорт класса Entity из игрового движка.
import gameEngine.Game; // Импорт класса Game из игрового движка.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт основного класса приложения.
import impl.ResolutionConfig; // Импорт класса ResolutionConfig для работы с разрешениями.
import impl.scenes.GameScene; // Импорт класса GameScene, представляющего игровую сцену.

public class AsteroidSmall extends Entity implements DamagableEntity { // Объявление класса AsteroidSmall, который наследует Entity и реализует интерфейс DamagableEntity.
    private static final double BASE_DAMAGE_AMOUNT = 1; // Базовое количество урона, наносимого маленьким астероидом.
    private static final double MAX_HEALTH = 3; // Максимальное здоровье маленького астероида.
    private static final double BASE_SPEED = 500; // Базовая скорость маленького астероида.
    private static final int BASE_SCORE_VALUE = 20; // Базовое количество очков, получаемых за уничтожение маленького астероида.

    private static int WIDTH; // Ширина маленького астероида.
    private static int HEIGHT; // Высота маленького астероида.
    private static Image SPRITE; // Изображение спрайта маленького астероида.

    private Vector2 velocity; // Вектор скорости маленького астероида.
    private double currentHealth; // Текущее здоровье маленького астероида.

    static { // Статический блок инициализации.
        updateDimensions(); // Обновление размеров маленького астероида в зависимости от разрешения.
    }

    // Конструктор класса AsteroidSmall, принимающий позицию и направление.
    public AsteroidSmall(Vector2 position, Vector2 direction) {
        super(position, new Vector2(WIDTH, HEIGHT)); // Вызов конструктора родительского класса с заданной позицией и размерами.
        double difficultyModifier = Main.difficulty.getModifier(); // Получение модификатора сложности.
        this.velocity = direction.clone().normalize().multiply(BASE_SPEED * difficultyModifier); // Инициализация скорости маленького астероида.
        currentHealth = MAX_HEALTH * difficultyModifier; // Установка текущего здоровья с учетом модификатора сложности.
    }

    // Метод для обновления размеров маленького астероида в зависимости от разрешения.
    public static void updateDimensions() {
        ResolutionConfig.Resolution currentResolution = ResolutionConfig.getAsteroidSmallSize(); // Получение текущих размеров маленького астероида.
        WIDTH = currentResolution.width; // Установка ширины.
        HEIGHT = currentResolution.height; // Установка высоты.

        // Загрузка и масштабирование спрайта маленького астероида.
        SPRITE = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidSmall.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }

    @Override
    public void tick() { // Метод обновления состояния маленького астероида.
        Vector2 position = getPosition(); // Получение текущей позиции маленького астероида.
        position.add(velocity.clone().multiply(Game.getInstance().getDeltaTime())); // Обновление позиции с учетом скорости и времени.
        setPosition(position); // Установка новой позиции.
    }

    @Override
    public void render(Graphics g) { // Метод отрисовки маленького астероида.
        Vector2 position = getPosition(); // Получение текущей позиции маленького астероида.
        // Отрисовка спрайта на графическом контексте.
        g.drawImage(SPRITE, (int) position.getX() - WIDTH / 2, (int) position.getY() - HEIGHT / 2, null);
    }

    @Override
    public void onCollisionEnter(Entity other) { // Метод обработки столкновения с другим объектом.
        if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
            ((PlayerShip) other).damage(BASE_DAMAGE_AMOUNT * Main.difficulty.getModifier()); // Наносим урон кораблю игрока.
            destroy(); // Уничтожаем маленький астероид.
        }
    }

    @Override
    public void onCollisionExit(Entity other) { // Метод обработки выхода из столкновения.
    }

    @Override
    public void damage(double amount) { // Метод для получения урона.
        currentHealth -= amount; // Уменьшаем текущее здоровье на полученное количество урона.
        if (currentHealth <= 0) {
            destroy(); // Если здоровье меньше или равно нулю, уничтожаем маленький астероид.
        }
    }

    private void destroy() { // Метод для уничтожения маленького астероида.
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        scene.removeObject(this); // Удаление маленького астероида из сцены.
        scene.addScore((int) (BASE_SCORE_VALUE * Main.difficulty.getModifier())); // Добавление очков за уничтожение маленького астероида.
        Explosion explosion = new Explosion(getPosition(), 100, 0.2); // Создание эффекта взрыва.
        scene.addObject(explosion); // Добавление эффекта взрыва на сцену.
    }
}
