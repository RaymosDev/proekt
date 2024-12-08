package impl.entities; // Объявление пакета, в котором находится класс Marauder.

import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.

import gameEngine.Entity; // Импорт класса Entity, от которого наследуется Marauder.
import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Scene; // Импорт класса Scene для работы со сценами игры.
import gameEngine.SceneObject; // Импорт класса SceneObject для объектов на сцене.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт главного класса приложения.
import impl.ResolutionConfig; // Импорт класса, конфигурирующего разрешение и размеры объектов.
import impl.scenes.GameScene; // Импорт класса GameScene для работы с игровой сценой.

/**
 * Класс Marauder представляет собой врага, который может стрелять орбами и наносить урон игроку.
 * Он реализует интерфейс DamagableEntity для обработки урона.
 * Основные компоненты класса:
Поля:

WIDTH, HEIGHT: Размеры объекта.
MAX_HEALTH: Максимальное здоровье Marauder.
SPEED: Скорость движения Marauder.
SCORE_VALUE: Очки, получаемые за уничтожение Marauder.
HEALTH_DROP_CHANCE: Вероятность выпадения HealthDrop при уничтожении.
BASE_CONTACT_DAMAGE: Базовый урон при столкновении.
LARGE_ORB_WIDTH, LARGE_ORB_HEIGHT: Размеры большого орба.
LARGE_ORB_SPEED, LARGE_ORB_LIFETIME, LARGE_ORB_COOLDOWN: Параметры большого орба.
BASE_LARGE_ORB_DAMAGE: Базовый урон большого орба.
SMALL_ORB_WIDTH, SMALL_ORB_HEIGHT: Размеры маленького орба.
SMALL_ORB_SPEED, BASE_SMALL_ORB_DAMAGE: Параметры маленького орба.
Статический блок:

Вызывается при загрузке класса для обновления размеров и загрузки изображений.
Конструктор:

Инициализирует объект с заданной позицией, здоровьем и временем следующего выстрела.
Методы:

updateDimensions(): Обновляет размеры объекта и загружает изображения.
tick(): Обновляет состояние Marauder на каждом кадре, управляет движением и стрельбой.
fireOrb(): Создает и добавляет большой орб на сцену.
render(Graphics g): Отрисовывает Marauder на экране.
damage(double amount): Обрабатывает получение урона.
destroy(): Уничтожает Marauder и обрабатывает логику после уничтожения.
Вложенные классы:

LargeOrb: Представляет большой орб, который стреляет Marauder, включает логику стрельбы и столкновений.
SmallOrb: Представляет маленький орб, который создается при взрыве большого орба.
OrbSpark: Представляет эффект искры от орба, управляет временем жизни искры и ее отрисовкой.
 */
public class Marauder extends Entity implements DamagableEntity {
    private static int WIDTH; // Ширина объекта.
    private static int HEIGHT; // Высота объекта.
    private static final int MAX_HEALTH = 8; // Максимальное здоровье Marauder.
    private static final double SPEED = 200.0; // Скорость движения Marauder.
    private static final int SCORE_VALUE = 300; // Очки, получаемые за уничтожение Marauder.
    private static final double HEALTH_DROP_CHANCE = 0.25; // Вероятность выпадения HealthDrop при уничтожении.
    private static final double BASE_CONTACT_DAMAGE = 2.0; // Базовый урон при столкновении.
    private static int LARGE_ORB_WIDTH; // Ширина большого орба.
    private static int LARGE_ORB_HEIGHT; // Высота большого орба.
    private static final double LARGE_ORB_SPEED = 500; // Скорость большого орба.
    private static final double LARGE_ORB_LIFETIME = 0.7; // Время жизни большого орба.
    private static final double LARGE_ORB_COOLDOWN = 3.0; // Задержка между выстрелами большого орба.
    private static final double BASE_LARGE_ORB_DAMAGE = 3.0; // Базовый урон большого орба.
    private static int SMALL_ORB_WIDTH; // Ширина маленького орба.
    private static int SMALL_ORB_HEIGHT; // Высота маленького орба.
    private static final double SMALL_ORB_SPEED = 600; // Скорость маленького орба.
    private static final double BASE_SMALL_ORB_DAMAGE = 1.0; // Базовый урон маленького орба.

    private static Image SPRITE_1; // Первое изображение Marauder.
    private static Image SPRITE_2; // Второе изображение Marauder.
    private static Image LARGE_ORB; // Изображение большого орба.
    private static Image SMALL_ORB; // Изображение маленького орба.

    private double health; // Текущее здоровье Marauder.
    private double nextFireTime; // Время следующего выстрела.

    // Статический блок для обновления размеров и загрузки изображений при загрузке класса.
    static {
        updateDimensions();
    }

    // Конструктор класса Marauder, принимающий позицию.
    public Marauder(Vector2 position) {
        super(position, new Vector2(WIDTH, HEIGHT)); // Вызов конструктора родительского класса.
        health = MAX_HEALTH; // Инициализация здоровья.
        nextFireTime = Game.getInstance().getTime() + 2; // Установка времени следующего выстрела.
    }

    // Метод для обновления размеров объекта и загрузки изображений.
    public static void updateDimensions() {
        ResolutionConfig.Resolution marauderSize = ResolutionConfig.getMarauderSize(); // Получение размеров Marauder.
        WIDTH = marauderSize.width; // Установка ширины.
        HEIGHT = marauderSize.height; // Установка высоты.

        ResolutionConfig.Resolution largeOrbSize = ResolutionConfig.getLargeOrbSize(); // Получение размеров большого орба.
        LARGE_ORB_WIDTH = largeOrbSize.width; // Установка ширины большого орба.
        LARGE_ORB_HEIGHT = largeOrbSize.height; // Установка высоты большого орба.

        ResolutionConfig.Resolution smallOrbSize = ResolutionConfig.getSmallOrbSize(); // Получение размеров маленького орба.
        SMALL_ORB_WIDTH = smallOrbSize.width; // Установка ширины маленького орба.
        SMALL_ORB_HEIGHT = smallOrbSize.height; // Установка высоты маленького орба.

        // Загрузка и масштабирование изображений Marauder и его орбов.
        SPRITE_1 = ResourceLoader.loadImage("res/images/entities/marauder/Marauder1.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        SPRITE_2 = ResourceLoader.loadImage("res/images/entities/marauder/Marauder2.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        LARGE_ORB = ResourceLoader.loadImage("res/images/entities/marauder/MarauderOrb.png")
                .getScaledInstance(LARGE_ORB_WIDTH, LARGE_ORB_HEIGHT, Image.SCALE_SMOOTH);
        SMALL_ORB = ResourceLoader.loadImage("res/images/entities/marauder/MarauderOrb.png")
                .getScaledInstance(SMALL_ORB_WIDTH, SMALL_ORB_HEIGHT, Image.SCALE_SMOOTH);
    }

    // Метод обновления состояния Marauder на каждом кадре.
    @Override
    public void tick() {
        Vector2 position = getPosition(); // Получение текущей позиции Marauder.
        position.add(-SPEED * Game.getInstance().getDeltaTime(), 0); // Движение влево.
        setPosition(position); // Установка обновленной позиции.
        double currentTime = Game.getInstance().getTime(); // Получение текущего времени.
        // Проверка, пора ли стрелять орбом.
        if (currentTime > nextFireTime) {
            fireOrb(); // Выстрел орбом.
            nextFireTime = currentTime + LARGE_ORB_COOLDOWN; // Обновление времени следующего выстрела.
        }
    }

    // Метод для выстрела большим орбом.
    private void fireOrb() {
        Game.getInstance().getOpenScene().addObject(new LargeOrb(getPosition())); // Создание и добавление большого орба на сцену.
    }

    // Метод отрисовки Marauder на экране.
    @Override
    public void render(Graphics g) {
        Vector2 position = getPosition(); // Получение текущей позиции.
        Image sprite; // Переменная для хранения изображения.
        double time = Game.getInstance().getTime(); // Получение текущего времени.
        // Выбор изображения для отрисовки в зависимости от времени.
        if (time % 0.3 < 0.15) {
            sprite = SPRITE_1; // Первое изображение.
        } else {
            sprite = SPRITE_2; // Второе изображение.
        }
        // Отрисовка изображения на экране.
        g.drawImage(sprite, (int) (position.getX() - WIDTH / 2.0), (int) (position.getY() - HEIGHT / 2.0), null);
    }

    // Метод для получения урона.
    @Override
    public void damage(double amount) {
        health -= amount; // Уменьшение здоровья.
        if (health <= 0) {
            destroy(); // Уничтожение Marauder, если здоровье равно или меньше нуля.
        }
    }

    // Метод для уничтожения Marauder.
    private void destroy() {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей игровой сцены.
        scene.removeObject(this); // Удаление Marauder из сцены.
        scene.addScore(SCORE_VALUE); // Добавление очков за уничтожение.
        Explosion explosion = new Explosion(getPosition(), 300, 0.4); // Создание взрыва.
        scene.addObject(explosion); // Добавление взрыва на сцену.
        // Вероятность выпадения HealthDrop.
        if (Math.random() < HEALTH_DROP_CHANCE) {
            scene.addObject(new HealthDrop(getPosition())); // Добавление HealthDrop на сцену.
        }
    }

    // Обработка столкновения с другим объектом.
    @Override
    public void onCollisionEnter(Entity other) {
        if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
            ((PlayerShip) other).damage(Main.difficulty.getModifier() * BASE_CONTACT_DAMAGE); // Нанесение урона игроку.
            destroy(); // Уничтожение Marauder.
        }
    }

    // Метод, вызываемый при выходе из столкновения.
    @Override
    public void onCollisionExit(Entity other) {
    }

    // Вложенный класс LargeOrb, представляющий большой орб, который стреляет Marauder.
    private static class LargeOrb extends Projectile {
        private double deathTime; // Время исчезновения орба.

        // Конструктор класса LargeOrb, принимающий позицию.
        public LargeOrb(Vector2 position) {
            super(position, new Vector2(LARGE_ORB_WIDTH, LARGE_ORB_HEIGHT), new Vector2(-LARGE_ORB_SPEED, 0)); // Вызов конструктора родительского класса.
            deathTime = Game.getInstance().getTime() + LARGE_ORB_LIFETIME; // Установка времени исчезновения.
        }

        @Override
        public void initialize() {
            super.initialize(); // Вызов инициализации родительского класса.
            ResourceLoader.loadAudioClip("res/audio/MarauderLargeOrbFire.wav").start(); // Запуск звукового эффекта при создании орба.
        }

        // Метод обновления состояния орба на каждом кадре.
        @Override
        public void tick() {
            super.tick(); // Вызов обновления родительского класса.
            if (Game.getInstance().getTime() > deathTime) { // Если текущее время больше времени исчезновения.
                blowUp(); // Взрыв орба.
            }
        }

        // Метод для взрыва орба.
        private void blowUp() {
            ResourceLoader.loadAudioClip("res/audio/MarauderLargeOrbBlowUp.wav").start(); // Запуск звукового эффекта взрыва.
            Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
            scene.removeObject(this); // Удаление орба из сцены.
            Vector2 smallOrbVelocity = Vector2.fromAngle(2.0 * Math.PI / 3.0, SMALL_ORB_SPEED); // Вычисление начальной скорости маленьких орбов.
            for (int i = 0; i < 5; i++) { // Создание 5 маленьких орбов.
                scene.addObject(new SmallOrb(getPosition(), smallOrbVelocity)); // Добавление маленького орба на сцену.
                smallOrbVelocity.rotate(Math.PI / 6.0); // Поворот скорости для следующего орба.
            }
        }

        // Обработка столкновения большого орба с другим объектом.
        @Override
        public void onCollisionEnter(Entity other) {
            if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
                ((PlayerShip) other).damage(BASE_LARGE_ORB_DAMAGE); // Нанесение урона игроку.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление большого орба из сцены.
                scene.addObject(new OrbSpark(getPosition())); // Создание эффекта искры.
            } else if (other instanceof PlayerShip.Laser) { // Если столкновение с лазером игрока.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление большого орба из сцены.
                scene.addObject(new OrbSpark(getPosition())); // Создание эффекта искры.
                blowUp(); // Взрыв орба.
            }
        }

        // Метод, вызываемый при выходе из столкновения.
        @Override
        public void onCollisionExit(Entity other) {
        }

        // Метод отрисовки большого орба на экране.
        @Override
        public void render(Graphics g) {
            Vector2 position = getPosition(); // Получение текущей позиции орба.
            int x = (int) (position.getX() - LARGE_ORB_WIDTH / 2.0); // Вычисление координаты x.
            int y = (int) (position.getY() - LARGE_ORB_HEIGHT / 2.0); // Вычисление координаты y.
            g.drawImage(LARGE_ORB, x, y, null); // Отрисовка большого орба.
        }
    }

    // Вложенный класс SmallOrb, представляющий маленький орб, который создается при взрыве большого орба.
    private static class SmallOrb extends Projectile {
        // Конструктор класса SmallOrb, принимающий позицию и скорость.
        public SmallOrb(Vector2 position, Vector2 velocity) {
            super(position, new Vector2(SMALL_ORB_WIDTH, SMALL_ORB_HEIGHT), velocity); // Вызов конструктора родительского класса.
        }

        @Override
        public void initialize() {
            super.initialize(); // Вызов инициализации родительского класса.
        }

        // Обработка столкновения маленького орба с другим объектом.
        @Override
        public void onCollisionEnter(Entity other) {
            if (other instanceof PlayerShip) { // Если столкновение с кораблем игрока.
                ((PlayerShip) other).damage(BASE_SMALL_ORB_DAMAGE * Main.difficulty.getModifier()); // Нанесение урона игроку.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление маленького орба из сцены.
                scene.addObject(new OrbSpark(getPosition().add(SMALL_ORB_WIDTH / 2.0, 0))); // Создание эффекта искры.
            } else if (other instanceof PlayerShip.Laser) { // Если столкновение с лазером игрока.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление маленького орба из сцены.
                scene.addObject(new OrbSpark(getPosition().add(SMALL_ORB_WIDTH / 2.0, 0))); // Создание эффекта искры.
            }
        }

        // Метод, вызываемый при выходе из столкновения.
        @Override
        public void onCollisionExit(Entity other) {
        }

        // Метод отрисовки маленького орба на экране.
        @Override
        public void render(Graphics g) {
            Vector2 position = getPosition(); // Получение текущей позиции маленького орба.
            g.drawImage(SMALL_ORB, (int) (position.getX() - SMALL_ORB_WIDTH / 2.0),
                    (int) (position.getY() - SMALL_ORB_HEIGHT / 2.0), null); // Отрисовка маленького орба.
        }

        @Override
        public void dispose() {
            super.dispose(); // Вызов метода освобождения ресурсов родительского класса.
        }
    }

    // Вложенный класс OrbSpark, представляющий эффект искры от орба.
    private static class OrbSpark extends SceneObject {
        private static final double DURATION = 0.1; // Длительность искры.
        private static final int ORB_SPARK_WIDTH = 50; // Ширина искры.
        private static final int ORB_SPARK_HEIGHT = 50; // Высота искры.
        private static final Image ORB_SPARK = ResourceLoader
                .loadImage("res/images/entities/marauder/MarauderOrbSpark.png")
                .getScaledInstance(ORB_SPARK_WIDTH, ORB_SPARK_HEIGHT, 0); // Загрузка изображения искры.

        private Vector2 position; // Позиция искры.
        private double deathTime; // Время исчезновения искры.

        // Конструктор класса OrbSpark, принимающий позицию.
        private OrbSpark(Vector2 position) {
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
            int x = (int) (position.getX() - ORB_SPARK_WIDTH / 2.0); // Вычисление координаты x.
            int y = (int) (position.getY() - ORB_SPARK_HEIGHT / 2.0); // Вычисление координаты y.
            g.drawImage(ORB_SPARK, x, y, null); // Отрисовка искры.
        }

        @Override
        public void dispose() {
        }
    }
}
