package impl.entities; // Объявление пакета, в котором находится класс Hornet.

import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.

import gameEngine.Entity; // Импорт класса Entity, от которого наследуется Hornet.
import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Scene; // Импорт класса Scene для работы со сценами игры.
import gameEngine.SceneObject; // Импорт класса SceneObject для объектов на сцене.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт главного класса приложения.
import impl.ResolutionConfig; // Импорт класса, конфигурирующего разрешение и размеры объектов.
import impl.scenes.GameScene; // Импорт класса GameScene для работы с игровой сценой.

/**
 * Класс Hornet представляет собой врага, который может наносить урон игроку и стрелять лазерами.
 * Он реализует интерфейс DamagableEntity для обработки урона.
 * Основные компоненты класса:
Поля:

WIDTH, HEIGHT: Размеры объекта.
MAX_HEALTH: Максимальное здоровье Hornet.
SPEED: Скорость движения Hornet.
SCORE_VALUE: Очки, получаемые за уничтожение Hornet.
HEALTH_DROP_CHANCE: Вероятность выпадения HealthDrop.
BASE_CONTACT_DAMAGE: Базовый урон при столкновении.
LASER_COOLDOWN: Задержка между выстрелами лазера.
LASER_WIDTH, LASER_HEIGHT: Размеры лазера.
LASER_SPEED: Скорость лазера.
BASE_LASER_DAMAGE: Базовый урон лазера.
SPRITE_1, SPRITE_2, LASER: Изображения Hornet и лазера.
Статический блок:

Вызывается при загрузке класса для обновления размеров и загрузки изображений.
Конструктор:

Инициализирует объект с заданной позицией, здоровьем и случайным значением для движения.
Методы:

updateDimensions(): Обновляет размеры объекта и загружает изображения.
tick(): Обновляет состояние Hornet на каждом кадре, управляет движением и стрельбой.
render(Graphics g): Отрисовывает Hornet на экране.
fireLaser(): Создает и добавляет лазер на сцену.
onCollisionEnter(Entity other): Обрабатывает столкновение с другими объектами.
damage(double amount): Обрабатывает получение урона.
destroy(): Уничтожает Hornet и обрабатывает логику после уничтожения.
Вложенные классы:

Laser: Представляет лазер Hornet, включает логику стрельбы и столкновений.
LaserSpark: Представляет эффект искры от лазера, управляет временем жизни искры и ее отрисовкой.
 */
public class Hornet extends Entity implements DamagableEntity {
    private static int WIDTH; // Ширина объекта.
    private static int HEIGHT; // Высота объекта.
    private static final int MAX_HEALTH = 2; // Максимальное здоровье Hornet.
    private static final double SPEED = 700.0; // Скорость движения Hornet.
    private static final int SCORE_VALUE = 75; // Количество очков, получаемых за уничтожение Hornet.
    private static final double HEALTH_DROP_CHANCE = 0.15; // Вероятность выпадения HealthDrop при уничтожении.
    private static final double BASE_CONTACT_DAMAGE = 1.0; // Базовый урон при столкновении.
    private static final double LASER_COOLDOWN = 0.5; // Задержка между выстрелами лазера.
    private static int LASER_WIDTH; // Ширина лазера.
    private static int LASER_HEIGHT; // Высота лазера.
    private static final double LASER_SPEED = 1000.0; // Скорость лазера.
    private static final double BASE_LASER_DAMAGE = 1.0; // Базовый урон лазера.
    private static Image SPRITE_1; // Первое изображение Hornet.
    private static Image SPRITE_2; // Второе изображение Hornet.
    private static Image LASER; // Изображение лазера.

    private double health; // Текущее здоровье Hornet.
    private double rand; // Случайное значение для движения.
    private double nextFireTime; // Время следующего выстрела.

    // Статический блок для обновления размеров и загрузки изображений при загрузке класса.
    static {
        updateDimensions();
    }

    // Конструктор класса Hornet, принимающий позицию.
    public Hornet(Vector2 position) {
        super(position, new Vector2(WIDTH, HEIGHT)); // Вызов конструктора родительского класса.
        health = MAX_HEALTH; // Инициализация здоровья.
        rand = 6.28318530718 * Math.random(); // Инициализация случайного значения.
    }

    // Метод для обновления размеров объекта и загрузки изображений.
    public static void updateDimensions() {
        ResolutionConfig.Resolution hornetSize = ResolutionConfig.getHornetSize(); // Получение размеров Hornet.
        WIDTH = hornetSize.width; // Установка ширины.
        HEIGHT = hornetSize.height; // Установка высоты.

        ResolutionConfig.Resolution laserSize = ResolutionConfig.getLaserHornetSize(); // Получение размеров лазера.
        LASER_WIDTH = laserSize.width; // Установка ширины лазера.
        LASER_HEIGHT = laserSize.height; // Установка высоты лазера.

        // Загрузка и масштабирование изображений Hornet и лазера.
        SPRITE_1 = ResourceLoader.loadImage("res/images/entities/hornet/Hornet1.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        SPRITE_2 = ResourceLoader.loadImage("res/images/entities/hornet/Hornet2.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        LASER = ResourceLoader.loadImage("res/images/entities/hornet/HornetLaser.png")
                .getScaledInstance(LASER_WIDTH, LASER_HEIGHT, Image.SCALE_SMOOTH);
    }

    // Метод обновления состояния Hornet на каждом кадре.
    @Override
    public void tick() {
        Vector2 position = getPosition(); // Получение текущей позиции Hornet.
        // Обновление вертикальной позиции с использованием синусоиды.
        position.setY(Main.HEIGHT * 0.42 * Math.sin(rand + 2.0 * Game.getInstance().getTime()) + Main.HEIGHT / 2.0);
        position.add(-SPEED * Game.getInstance().getDeltaTime(), 0); // Движение влево.
        setPosition(position); // Установка обновленной позиции.

        double currentTime = Game.getInstance().getTime(); // Получение текущего времени.
        // Проверка, пора ли стрелять лазером.
        if (currentTime > nextFireTime) {
            fireLaser(); // Выстрел лазером.
            nextFireTime = currentTime + LASER_COOLDOWN; // Обновление времени следующего выстрела.
        }
    }

    // Метод отрисовки Hornet на экране.
    @Override
    public void render(Graphics g) {
        Vector2 position = getPosition(); // Получение текущей позиции.
        double time = Game.getInstance().getTime(); // Получение текущего времени.
        // Выбор изображения для отрисовки в зависимости от времени.
        Image sprite = (time % 0.3 < 0.15) ? SPRITE_1 : SPRITE_2;
        g.drawImage(sprite, (int) (position.getX() - WIDTH / 2.0), (int) (position.getY() - HEIGHT / 2.0), null);
    }

    // Метод для выстрела лазером.
    private void fireLaser() {
        Laser laser = new Laser(getPosition().add(-80, -10)); // Создание нового лазера с заданной позицией.
        Game.getInstance().getOpenScene().addObject(laser); // Добавление лазера на сцену.
    }

    // Обработка столкновения с другим объектом.
    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
            ((PlayerShip) other).damage(Main.difficulty.getModifier() * BASE_CONTACT_DAMAGE); // Нанесение урона игроку.
            destroy(); // Уничтожение Hornet.
        }
    }

    // Метод, вызываемый при выходе из столкновения.
    @Override
    public void onCollisionExit(Entity other) {
    }

    // Метод для получения урона.
    @Override
    public void damage(double amount) {
        health -= amount; // Уменьшение здоровья.
        if (health <= 0) {
            destroy(); // Уничтожение Hornet, если здоровье равно или меньше нуля.
        }
    }

    // Метод для уничтожения Hornet.
    private void destroy() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей игровой сцены.
        scene.removeObject(this); // Удаление Hornet из сцены.
        scene.addScore(SCORE_VALUE); // Добавление очков за уничтожение.
        Explosion explosion = new Explosion(getPosition(), 150, 0.3); // Создание взрыва.
        scene.addObject(explosion); // Добавление взрыва на сцену.
        // Вероятность выпадения HealthDrop.
        if (Math.random() < HEALTH_DROP_CHANCE) {
            scene.addObject(new HealthDrop(getPosition())); // Добавление HealthDrop на сцену.
        }
    }

    // Вложенный класс Laser, представляющий лазер Hornet.
    public static class Laser extends Projectile {
        public Laser(Vector2 position) {
            super(position, new Vector2(LASER_WIDTH, LASER_HEIGHT), new Vector2(-LASER_SPEED, 0)); // Вызов конструктора родительского класса.
        }

        @Override
        public void initialize() {
            ResourceLoader.loadAudioClip("res/audio/HornetLaser.wav").start(); // Запуск звукового эффекта при создании лазера.
        }

        @Override
        public void render(Graphics g) {
            Vector2 position = getPosition(); // Получение текущей позиции лазера.
            g.drawImage(LASER, (int) (position.getX() - LASER_WIDTH / 2.0),
                    (int) (position.getY() - LASER_HEIGHT / 2.0), null); // Отрисовка лазера.
        }

        // Обработка столкновения лазера с другим объектом.
        @Override
        public void onCollisionEnter(Entity other) {
            if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
                ((PlayerShip) other).damage(BASE_LASER_DAMAGE * Main.difficulty.getModifier()); // Нанесение урона игроку.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление лазера из сцены.
                scene.addObject(new LaserSpark(getPosition().add(LASER_WIDTH / 2.0, 0))); // Создание эффекта искры.
            } else if (other instanceof Projectile) { // Если столкновение с другим снарядом.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление лазера из сцены.
                scene.addObject(new LaserSpark(getPosition().add(LASER_WIDTH / 2.0, 0))); // Создание эффекта искры.
            }
        }

        // Метод, вызываемый при выходе из столкновения.
        @Override
        public void onCollisionExit(Entity other) {
        }
    }

    // Вложенный класс LaserSpark, представляющий эффект искры от лазера.
    private static class LaserSpark extends SceneObject {
        private static final double DURATION = 0.1; // Длительность искры.
        private static final int LASER_SPARK_WIDTH = 35; // Ширина искры.
        private static final int LASER_SPARK_HEIGHT = 35; // Высота искры.
        private static final Image LASER_SPARK = ResourceLoader
                .loadImage("res/images/entities/hornet/HornetLaserSpark.png")
                .getScaledInstance(LASER_SPARK_WIDTH, LASER_SPARK_HEIGHT, Image.SCALE_SMOOTH); // Загрузка изображения искры.

        private Vector2 position; // Позиция искры.
        private double deathTime; // Время исчезновения искры.

        private LaserSpark(Vector2 position) {
            this.position = position; // Установка позиции искры.
            deathTime = Game.getInstance().getTime() + DURATION; // Установка времени исчезновения.
        }

        @Override
        public void initialize() {
        }

        // Метод обновления состояния искры на каждом кадре.
        @Override
        public void tick() {
            if (Game.getInstance().getTime() > deathTime) { // Если текущее время больше времени исчезновения.
                Game.getInstance().getOpenScene().removeObject(this); // Удаление искры из сцены.
            }
        }

        // Метод отрисовки искры на экране.
        @Override
        public void render(Graphics g) {
            int x = (int) (position.getX() - LASER_SPARK_WIDTH / 2.0); // Вычисление координаты x.
            int y = (int) (position.getY() - LASER_SPARK_HEIGHT / 2.0); // Вычисление координаты y.
            g.drawImage(LASER_SPARK, x, y, null); // Отрисовка искры.
        }

        @Override
        public void dispose() {
        }
    }
}
