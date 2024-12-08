package impl.waves; // Объявление пакета, в котором находится класс Wave1.

/*  Класс Wave1 представляет собой первую волну врагов в игре, 
    наследуя функциональность от абстрактного класса Wave. 
    Он управляет созданием врагов, их количеством и временем появления, 
    а также переходом к следующей волне после достижения 
    максимального количества врагов.
Основные компоненты класса:
Константы:

BASE_ENEMY_SPAWN_PERIOD: Базовый период появления врагов.
BASE_MAX_ENEMY_COUNT: Максимальное количество врагов в данной волне.
Поля:

gameScene: Ссылка на текущую игровую сцену.
modifiedEnemySpawnPeriod: Модифицированный период появления врагов, который зависит от уровня сложности.
modifiedMaxEnemyCount: Модифицированное максимальное количество врагов в зависимости от уровня сложности.
enemyCount: Счетчик созданных врагов.
startTime: Время начала волны.
nextSpawnTime: Время следующего появления врага.
Методы:

Конструктор: Инициализирует параметры волны, включая модификацию периодов и количества врагов в зависимости от сложности.
tick(): Метод, вызываемый каждый игровой тик, который проверяет, нужно ли создавать нового врага.
spawnEnemy(): Метод для создания врага и проверки достижения максимального количества врагов. Если максимальное количество достигнуто, текущая волна удаляется, и добавляется следующая волна (Wave2).
getWaveMessage(): Реализация абстрактного метода, возвращающая сообщение о новой волне.
*/

import gameEngine.Game; // Импорт класса Game для управления игровым процессом.
import impl.Main; // Импорт главного класса приложения.
import impl.scenes.GameScene; // Импорт класса GameScene, представляющего игровую сцену.

public class Wave1 extends Wave { // Определение класса Wave1, который наследует от класса Wave.
    private static final double BASE_ENEMY_SPAWN_PERIOD = 0.75; // Базовый период появления врагов (в секундах).
    private static final int BASE_MAX_ENEMY_COUNT = 20; // Максимальное количество врагов в волне.
    private GameScene gameScene; // Ссылка на текущую игровую сцену.
    private double modifiedEnemySpawnPeriod; // Модифицированный период появления врагов.
    private int modifiedMaxEnemyCount; // Модифицированное максимальное количество врагов.
    private int enemyCount = 0; // Текущее количество созданных врагов.
    private double startTime; // Время начала волны.
    private double nextSpawnTime; // Время следующего появления врага.

    // Конструктор класса Wave1, который принимает игровую сцену в качестве параметра.
    public Wave1(GameScene gameScene) {
        this.gameScene = gameScene; // Инициализация ссылки на игровую сцену.
        modifiedEnemySpawnPeriod = BASE_ENEMY_SPAWN_PERIOD / Main.difficulty.getModifier(); // Модификация периода появления врагов в зависимости от уровня сложности.
        modifiedMaxEnemyCount = (int) (BASE_MAX_ENEMY_COUNT * Main.difficulty.getModifier()); // Модификация максимального количества врагов в зависимости от уровня сложности.
        enemyCount = 0; // Инициализация количества врагов.
        startTime = Game.getInstance().getTime(); // Запись времени начала волны.
        nextSpawnTime = startTime + GameScene.FIRST_WAVE_WAIT_TIME; // Установка времени следующего появления врага с учетом времени ожидания первой волны.
    }

    // Метод, который вызывается каждый игровой тик для обновления состояния волны.
    @Override
    public void tick() {
        double currentTime = Game.getInstance().getTime(); // Получение текущего времени игры.
        if (currentTime >= nextSpawnTime) { // Проверка, наступило ли время для появления следующего врага.
            nextSpawnTime = nextSpawnTime + modifiedEnemySpawnPeriod; // Обновление времени следующего появления врага.
            spawnEnemy(); // Вызов метода для появления врага.
        }
    }

    // Метод для создания врага и проверки достижения максимального количества врагов.
    private void spawnEnemy() {
        enemyCount++; // Увеличение счетчика созданных врагов.
        spawnAsteroid(); // Создание астероида.

        GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
        if (enemyCount >= modifiedMaxEnemyCount) { // Проверка, достигнуто ли максимальное количество врагов.
            scene.removeObject(this); // Удаление текущей волны из сцены.
            scene.addObject(new Wave2(scene)); // Добавление следующей волны (Wave2) в сцену.
        }
    }

    // Реализация абстрактного метода getWaveMessage для предоставления сообщения о текущей волне.
    @Override
    protected String getWaveMessage() {
        return "NEW WAVE"; // Возвращение сообщения о новой волне.
    }
}
