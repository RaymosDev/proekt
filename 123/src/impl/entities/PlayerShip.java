package impl.entities; // Объявление пакета, в котором находится класс PlayerShip.

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.event.KeyEvent; // Импорт класса KeyEvent для обработки событий клавиатуры.

import gameEngine.Entity; // Импорт класса Entity, от которого наследуется PlayerShip.
import gameEngine.Game; // Импорт класса Game для доступа к игровым данным.
import gameEngine.InputManager; // Импорт класса InputManager для обработки ввода от пользователя.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Scene; // Импорт класса Scene для работы со сценами игры.
import gameEngine.SceneObject; // Импорт класса SceneObject для объектов на сцене.
import gameEngine.Vector2; // Импорт класса Vector2 для работы с векторами.
import impl.Main; // Импорт главного класса приложения.
import impl.ResolutionConfig; // Импорт класса, конфигурирующего разрешение и размеры объектов.
import impl.scenes.GameScene; // Импорт класса GameScene для работы с игровой сценой.

/**
 * Класс PlayerShip представляет собой корабль игрока, который может двигаться и стрелять лазерами.
 * Он реализует интерфейс DamagableEntity для обработки урона.
 * Основные компоненты класса:
Поля:

WIDTH, HEIGHT: Размеры корабля.
LASER_COOLDOWN: Время между выстрелами лазера.
SPEED: Скорость движения корабля.
MAX_HEALTH: Максимальное здоровье корабля.
LASER_DAMAGE_AMOUNT: Урон от лазера.
LASER_SPEED: Скорость лазера.
LASER_WIDTH, LASER_HEIGHT: Размеры лазера.
PLAYER_1, PLAYER_2, PLAYER_LASER: Изображения для корабля и лазера.
Статический блок:

Вызывается при загрузке класса для обновления размеров и загрузки изображений.
Конструктор:

Инициализирует объект с заданной позицией, изображениями и здоровьем.
Методы:

updateDimensions(): Обновляет размеры объекта и загружает изображения.
tick(): Обновляет состояние корабля на каждом кадре, управляет движением и стрельбой.
clamp(double n, double min, double max): Ограничивает значение в заданных границах.
fireLaser(): Создает и добавляет лазер на сцену.
render(Graphics g): Отрисовывает корабль на экране.
getCurrentHealth(): Возвращает текущее здоровье.
getMaxHealth(): Возвращает максимальное здоровье.
damage(double amount): Обрабатывает получение урона.
heal(double healAmount): Лечит корабль.
destroy(): Уничтожает корабль.
Вложенные классы:

Laser: Представляет лазер, который стреляет корабль игрока, включает логику стрельбы и столкновений.
LaserSpark: Представляет эффект искры от лазера, управляет временем жизни искры и ее отрисовкой.
DamageNotificationFilter: Представляет уведомление о полученном уроне, управляет его отрисовкой и временем жизни.
 */
public class PlayerShip extends Entity implements DamagableEntity {
    private static int WIDTH; // Ширина корабля.
    private static int HEIGHT; // Высота корабля.
    private static final double LASER_COOLDOWN = 0.22; // Задержка между выстрелами лазера.
    private static final double SPEED = 600; // Скорость движения корабля.
    private static final double MAX_HEALTH = 15; // Максимальное здоровье корабля.

    private static final double LASER_DAMAGE_AMOUNT = 1; // Урон от лазера.
    private static final double LASER_SPEED = 1500; // Скорость лазера.
    private static int LASER_WIDTH; // Ширина лазера.
    private static int LASER_HEIGHT; // Высота лазера.

    public static Image PLAYER_1; // Первое изображение корабля игрока.
    public static Image PLAYER_2; // Второе изображение корабля игрока.
    public static Image PLAYER_LASER; // Изображение лазера.

    private Image sprite1; // Текущее изображение корабля.
    private Image sprite2; // Второе изображение для анимации.
    private double currentHealth; // Текущее здоровье корабля.
    private double nextFireTime; // Время следующего выстрела.

    // Статический блок для обновления размеров и загрузки изображений при загрузке класса.
    static {
        updateDimensions();
    }

    // Конструктор класса PlayerShip, принимающий позицию.
    public PlayerShip(Vector2 position) {
        super(position, new Vector2(WIDTH, HEIGHT)); // Вызов конструктора родительского класса.
        sprite1 = PLAYER_1; // Установка первого изображения.
        sprite2 = PLAYER_2; // Установка второго изображения.
        currentHealth = MAX_HEALTH; // Инициализация текущего здоровья.
    }

    // Метод для обновления размеров объекта и загрузки изображений.
    public static void updateDimensions() {
        ResolutionConfig.Resolution playerSize = ResolutionConfig.getPlayerShipSize(); // Получение размеров корабля.
        WIDTH = playerSize.width; // Установка ширины.
        HEIGHT = playerSize.height; // Установка высоты.

        ResolutionConfig.Resolution laserSize = ResolutionConfig.getLaserSize(); // Получение размеров лазера.
        LASER_WIDTH = laserSize.width; // Установка ширины лазера.
        LASER_HEIGHT = laserSize.height; // Установка высоты лазера.

        // Загрузка и масштабирование изображений корабля и лазера.
        PLAYER_1 = ResourceLoader.loadImage("res/images/entities/player/Player1.png")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        PLAYER_2 = ResourceLoader.loadImage("res/images/entities/player/Player2.png")
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        
        PLAYER_LASER = ResourceLoader.loadImage("res/images/entities/player/PlayerLaser.png")
            .getScaledInstance(LASER_WIDTH, LASER_HEIGHT, Image.SCALE_SMOOTH);
    }

    // Метод обновления состояния корабля на каждом кадре.
    @Override
    public void tick() {
        if (((GameScene) Game.getInstance().getOpenScene()).isPaused()) { // Если игра приостановлена, ничего не делать.
            return;
        }
        InputManager input = Game.getInstance().getInputManager(); // Получение менеджера ввода.
        double horizontal = input.getHorizontalAxis(); // Получение горизонтального ввода.
        double vertical = input.getVerticalAxis(); // Получение вертикального ввода.
        // Создание вектора движения на основе ввода.
        Vector2 move = new Vector2(horizontal, -vertical).multiply(Game.getInstance().getDeltaTime() * SPEED);

        Vector2 position = getPosition(); // Получение текущей позиции корабля.
        position.add(move); // Обновление позиции с учетом движения.
        // Определение границ, в которых может двигаться корабль.
        double leftBoundary = Main.WIDTH * 0.04;
        double rightBoundary = Main.WIDTH * 1.038;
        double topBoundary = Main.HEIGHT * 0.043;
        double bottomBoundary = Main.HEIGHT * 1.039;

        // Ограничение позиции корабля в заданных границах.
        position.setX(clamp(position.getX(), leftBoundary, rightBoundary - WIDTH));
        position.setY(clamp(position.getY(), topBoundary, bottomBoundary - HEIGHT));
        setPosition(position); // Установка обновленной позиции.

        // Проверка нажатия клавиши для выстрела.
        if (input.getKey(KeyEvent.VK_SPACE)) {
            double time = Game.getInstance().getTime(); // Получение текущего времени.
            if (time > nextFireTime) { // Если время для следующего выстрела пришло.
                nextFireTime = time + LASER_COOLDOWN; // Обновление времени следующего выстрела.
                fireLaser(); // Выстрел лазером.
            }
        }
    }

    // Метод для ограничения значения в заданных границах.
    private double clamp(double n, double min, double max) {
        if (n < min) {
            return min; // Если значение меньше минимума, возвращаем минимум.
        } else if (n > max) {
            return max; // Если значение больше максимума, возвращаем максимум.
        } else {
            return n; // Если значение в пределах, возвращаем его.
        }
    }

    // Метод для выстрела лазером.
    private void fireLaser() {
        Vector2 position = getPosition(); // Получение текущей позиции корабля.
        Laser laser = new Laser(position.add(WIDTH / 2.0 + 20, 0.0)); // Создание лазера с учетом позиции корабля.
        Game.getInstance().getOpenScene().addObject(laser); // Добавление лазера на сцену.
        ResourceLoader.loadAudioClip("res/audio/PlayerLaser.wav").start(); // Запуск звукового эффекта выстрела.
    }

    // Метод отрисовки корабля на экране.
    @Override
    public void render(Graphics g) {
        Vector2 position = getPosition(); // Получение текущей позиции.
        Image sprite; // Переменная для хранения изображения.
        double time = Game.getInstance().getTime(); // Получение текущего времени.
        // Выбор изображения для отрисовки в зависимости от времени.
        if (time % 0.2 < 0.1) {
            sprite = sprite1; // Первое изображение.
        } else {
            sprite = sprite2; // Второе изображение.
        }
        // Отрисовка изображения на экране.
        g.drawImage(sprite, (int) (position.getX() - WIDTH / 2.0), (int) (position.getY() - HEIGHT / 2.0), null);
    }

    // Метод для получения текущего здоровья.
    public double getCurrentHealth() {
        return currentHealth;
    }

    // Метод для получения максимального здоровья.
    public double getMaxHealth() {
        return MAX_HEALTH;
    }

    // Метод для обработки получения урона.
    @Override
    public void damage(double amount) {
        currentHealth -= amount; // Уменьшение текущего здоровья.
        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей игровой сцены.
        scene.addObject(new DamageNotificationFilter()); // Добавление уведомления о полученном уроне.
        if (currentHealth <= 0) { // Если здоровье меньше или равно нулю.
            destroy(); // Уничтожение корабля.
            scene.endGame(); // Завершение игры.
        }
    }

    // Метод для лечения корабля.
    public void heal(double healAmount) {
        currentHealth += healAmount; // Увеличение текущего здоровья.
        if (currentHealth > MAX_HEALTH) { // Если здоровье превышает максимум.
            currentHealth = MAX_HEALTH; // Установка максимального значения здоровья.
        }
    }

    // Метод для уничтожения корабля.
    private void destroy() {
        ResourceLoader.loadAudioClip("res/audio/Explosion.wav").start(); // Запуск звукового эффекта взрыва.
        Game.getInstance().getOpenScene().removeObject(this); // Удаление корабля из сцены.
    }

    // Обработка столкновения с другим объектом.
    @Override
    public void onCollisionEnter(Entity other) {
    }

    // Метод, вызываемый при выходе из столкновения.
    @Override
    public void onCollisionExit(Entity other) {
    }

    // Вложенный класс Laser, представляющий лазер, который стреляет игрок.
    public static class Laser extends Projectile {
        // Конструктор класса Laser, принимающий позицию.
        Laser(Vector2 position) {
            super(position, new Vector2(LASER_WIDTH, LASER_HEIGHT), new Vector2(LASER_SPEED, 0)); // Вызов конструктора родительского класса.
        }

        // Метод отрисовки лазера на экране.
        @Override
        public void render(Graphics g) {
            Vector2 position = getPosition(); // Получение текущей позиции лазера.
            g.drawImage(PLAYER_LASER, (int) (position.getX() - LASER_WIDTH / 2.0),
                (int) (position.getY() - LASER_HEIGHT / 2.0), null); // Отрисовка лазера.
        }

        // Обработка столкновения лазера с другим объектом.
        @Override
        public void onCollisionEnter(Entity other) {
            if (!(other instanceof PlayerShip) && other instanceof DamagableEntity) { // Если столкновение с врагом.
                ((DamagableEntity) other).damage(LASER_DAMAGE_AMOUNT); // Нанесение урона врагу.
                Scene scene = Game.getInstance().getOpenScene(); // Получение текущей сцены.
                scene.removeObject(this); // Удаление лазера из сцены.
                scene.addObject(new LaserSpark(getPosition().add(LASER_WIDTH / 2.0, 0))); // Создание эффекта искры.
            } else if (other instanceof Projectile) { // Если столкновение с другим лазером.
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
            .loadImage("res/images/entities/player/PlayerLaserSpark.png")
            .getScaledInstance(LASER_SPARK_WIDTH, LASER_SPARK_HEIGHT, Image.SCALE_SMOOTH); // Загрузка изображения искры.

        private Vector2 position; // Позиция искры.
        private double deathTime; // Время исчезновения искры.

        // Конструктор класса LaserSpark, принимающий позицию.
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

    // Вложенный класс DamageNotificationFilter, представляющий уведомление о полученном уроне.
    private static class DamageNotificationFilter extends SceneObject {
        private static final double DURATION = 0.25; // Длительность уведомления.
        private static final double MAX_OPAQUENESS = 0.25; // Максимальная непрозрачность.
        private double startTime; // Время начала уведомления.

        // Конструктор класса DamageNotificationFilter.
        private DamageNotificationFilter() {
            startTime = Game.getInstance().getTime(); // Установка времени начала.
        }

        @Override
        public void initialize() {
        }

        // Метод обновления состояния уведомления на каждом кадре.
        @Override
        public void tick() {
        }

        // Метод отрисовки уведомления на экране.
        @Override
        public void render(Graphics g) {
            double time = Game.getInstance().getTime(); // Получение текущего времени.
            double elapsedTime = time - startTime; // Вычисление прошедшего времени.
            double opaqueness = 2.0 * elapsedTime / DURATION; // Вычисление непрозрачности.
            if (opaqueness > 1.0) { // Если непрозрачность больше 1, инвертируем ее.
                opaqueness = 2.0 - opaqueness;
            }
            if (opaqueness < 0) { // Если непрозрачность меньше 0, удаляем уведомление.
                Game.getInstance().getOpenScene().removeObject(this);
                return;
            }
            int alpha = (int) (opaqueness * MAX_OPAQUENESS * 255); // Вычисление значения альфа-канала.
            Color filter = new Color(255, 0, 0, alpha); // Создание красного фильтра с заданной непрозрачностью.
            g.setColor(filter); // Установка цвета для графики.
            g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT); // Отрисовка фильтра на экране.
        }

        @Override
        public void dispose() {
        }
    }
}
