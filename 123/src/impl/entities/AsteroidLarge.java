package impl.entities; // Объявление пакета, в котором находится класс AsteroidLarge.

/*  Класс AsteroidLarge представляет собой крупный астероид в игре, 
который наследует функциональность от класса Entity и реализует интерфейс 
DamagableEntity. Он содержит статические переменные для хранения размеров и 
спрайтов, а также динамические переменные
для управления состоянием астероида, такими как скорость и здоровье.
Методы класса обеспечивают обновление позиции астероида, его отрисовку, 
обработку столкновений, получение урона и уничтожение с возможностью 
деления на меньшие астероиды. Астероид может взаимодействовать
с кораблем игрока, нанося ему урон, и производить эффект взрыва при уничтожении.
*/

import gameEngine.Entity; // Импорт класса Entity из игрового движка.
import gameEngine.Game; // Импорт класса Game из игрового движка.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт основного класса приложения.
import impl.ResolutionConfig; // Импорт класса ResolutionConfig для работы с разрешениями.
import impl.scenes.GameScene; // Импорт класса GameScene, представляющего игровую сцену.
import java.awt.Graphics; // Импорт класса Graphics для отрисовки графики.
import java.awt.Image; // Импорт класса Image для работы с изображениями.

public class AsteroidLarge extends Entity implements DamagableEntity { // Объявление класса AsteroidLarge, который наследует Entity и реализует интерфейс DamagableEntity.
    private static final double BASE_DAMAGE_AMOUNT = 3; // Базовое количество урона, наносимого астероидом.
    private static final double MAX_HEALTH = 5; // Максимальное здоровье астероида.
    private static final double BASE_SPEED = 300; // Базовая скорость астероида.
    private static final int BASE_SCORE_VALUE = 50; // Базовое количество очков, получаемых за уничтожение астероида.

    private static int WIDTH; // Ширина астероида.
    private static int HEIGHT; // Высота астероида.
    private static Image SPRITE_1; // Изображение первого спрайта астероида.
    private static Image SPRITE_2; // Изображение второго спрайта астероида.
    private static Image SPRITE_3; // Изображение третьего спрайта астероида.

    private Vector2 velocity; // Вектор скорости астероида.
    private double currentHealth; // Текущее здоровье астероида.

    static { // Статический блок инициализации.
        updateDimensions(); // Обновление размеров астероида в зависимости от разрешения.
    }

    // Конструктор класса AsteroidLarge, принимающий позицию и направление.
    public AsteroidLarge(Vector2 position, Vector2 direction) {
        super(position, new Vector2(WIDTH, HEIGHT)); // Вызов конструктора родительского класса с заданной позицией и размерами.
        double difficultyModifier = Main.difficulty.getModifier(); // Получение модификатора сложности.
        this.velocity = direction.clone().normalize().multiply(BASE_SPEED * difficultyModifier); // Инициализация скорости астероида.
        currentHealth = MAX_HEALTH; // Установка текущего здоровья на максимальное значение.
    }

    // Метод для обновления размеров астероида в зависимости от разрешения.
    public static void updateDimensions() {
        ResolutionConfig.Resolution currentResolution = ResolutionConfig.getAsteroidSize(); // Получение текущих размеров астероида.
        WIDTH = currentResolution.width; // Установка ширины.
        HEIGHT = currentResolution.height; // Установка высоты.

        // Загрузка и масштабирование спрайтов астероида.
        SPRITE_1 = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidLarge1.png")
                .getScaledInstance(WIDTH, HEIGHT, 0);
        SPRITE_2 = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidLarge2.png")
                .getScaledInstance(WIDTH, HEIGHT, 0);
        SPRITE_3 = ResourceLoader.loadImage("res/images/entities/asteroids/AsteroidLarge3.png")
                .getScaledInstance(WIDTH, HEIGHT, 0);
    }

    @Override
    public void tick() { // Метод обновления состояния астероида.
        Vector2 position = getPosition(); // Получение текущей позиции астероида.
        position.add(velocity.clone().multiply(Game.getInstance().getDeltaTime())); // Обновление позиции с учетом скорости и времени.
        setPosition(position); // Установка новой позиции.
    }

    @Override
    public void render(Graphics g) { // Метод отрисовки астероида.
        Vector2 position = getPosition(); // Получение текущей позиции астероида.
        Image sprite; // Переменная для хранения текущего спрайта.

        // Определение спрайта в зависимости от текущего здоровья.
        if (currentHealth < 3) {
            sprite = SPRITE_3; // Если здоровье меньше 3, используем третий спрайт.
        } else if (currentHealth < 5) {
            sprite = SPRITE_2; // Если здоровье меньше 5, используем второй спрайт.
        } else {
            sprite = SPRITE_1; // В противном случае используем первый спрайт.
        }

        // Отрисовка спрайта на графическом контексте.
        g.drawImage(sprite, (int) position.getX() - WIDTH / 2, (int) position.getY() - HEIGHT / 2, null);
    }

    @Override
    public void onCollisionEnter(Entity other) { // Метод обработки столкновения с другим объектом.
        if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
            ((PlayerShip) other).damage(BASE_DAMAGE_AMOUNT * Main.difficulty.getModifier()); // Наносим урон кораблю игрока.
            destroyWithExplosion(false); // Уничтожаем астероид с эффектом взрыва.
        }
    }

    @Override
    public void onCollisionExit(Entity other) { // Метод обработки выхода из столкновения.
    }

    @Override
    public void damage(double amount) { // Метод для получения урона.
        currentHealth -= amount; // Уменьшаем текущее здоровье на полученное количество урона.
        if (currentHealth <= 0) {
            destroy(true); // Если здоровье меньше или равно нулю, уничтожаем астероид.
        }
    }

    private void destroy(boolean split) { // Метод для уничтожения астероида с возможностью деления.
        ResourceLoader.loadAudioClip("res/audio/AsteroidHit.wav").start(); // Проигрывание звука удара астероида.
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        scene.addScore((int) (BASE_SCORE_VALUE * Main.difficulty.getModifier())); // Добавление очков за уничтожение астероида.

        scene.removeObject(this); // Удаление астероида из сцены.
        if (split) { // Если нужно разделить астероид.
            Vector2 position = getPosition(); // Получение текущей позиции астероида.
            scene.addObject(new AsteroidSmall(position, velocity)); // Добавление маленького астероида.
            velocity.rotate(-Math.toRadians(30)); // Поворот вектора скорости.
            scene.addObject(new AsteroidSmall(position, velocity)); // Добавление еще одного маленького астероида.
            velocity.rotate(Math.toRadians(60)); // Поворот вектора скорости.
            scene.addObject(new AsteroidSmall(position, velocity)); // Добавление третьего маленького астероида.
        }
    }

    private void destroyWithExplosion(boolean split) { // Метод для уничтожения астероида с эффектом взрыва.
        ResourceLoader.loadAudioClip("res/audio/AsteroidHit.wav").start(); // Проигрывание звука удара астероида.
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        scene.addScore((int) (BASE_SCORE_VALUE * Main.difficulty.getModifier())); // Добавление очков за уничтожение астероида.
        Explosion explosion = new Explosion(getPosition(), 200, 0.4); // Создание эффекта взрыва.
        scene.addObject(explosion); // Добавление эффекта взрыва на сцену.
        scene.removeObject(this); // Удаление астероида из сцены.
        if (split) { // Если нужно разделить астероид.
            Vector2 position = getPosition(); // Получение текущей позиции астероида.
            scene.addObject(new AsteroidSmall(position, velocity)); // Добавление маленького астероида.
            velocity.rotate(-Math.toRadians(30)); // Поворот вектора скорости.
            scene.addObject(new AsteroidSmall(position, velocity)); // Добавление еще одного маленького астероида.
            velocity.rotate(Math.toRadians(60)); // Поворот вектора скорости.
            scene.addObject(new AsteroidSmall(position, velocity)); // Добавление третьего маленького астероида.
        }
    }
}
