package impl.waves; // Объявление пакета, в котором находится класс Wave5.

/* Класс Wave5 представляет пятую волну врагов в игре и наследует 
    функциональность от абстрактного класса Wave. Он управляет созданием 
    различных типов врагов, их количеством и временем появления, 
    а также переходом к следующей волне после достижения 
    максимального количества врагов.
Основные компоненты класса:
Константы:

BASE_ENEMY_SPAWN_PERIOD: Базовый период появления врагов (1 секунда).
BASE_MAX_ENEMY_COUNT: Максимальное количество врагов в данной волне (20).
Поля:

modifiedEnemySpawnPeriod: Модифицированный период появления врагов в зависимости от уровня сложности.
modifiedMaxEnemyCount: Модифицированное максимальное количество врагов в зависимости от уровня сложности.
enemyCount: Счетчик созданных врагов.
startTime: Время начала волны.
nextSpawnTime: Время следующего появления врага.
random: Экземпляр класса Random для генерации случайных чисел.
lastSpawnedEnemyType: Хранит тип последнего созданного врага, чтобы избежать повторений.
Методы:

Конструктор: Инициализирует параметры волны, включая модификацию периодов и количества врагов в зависимости от сложности, и устанавливает сообщение о волне.
tick(): Метод, вызываемый каждый игровой тик, который проверяет, нужно ли создавать нового врага.
spawnEnemy(): Метод для создания врага, который выбирает случайный тип врага и проверяет достижение максимального количества врагов. Если максимальное количество достигнуто, текущая волна удаляется, и добавляется новая волна (Wave5).
getWaveMessage(): Реализация абстрактного метода, возвращающая сообщение о новой волне.
*/

import gameEngine.Game; // Импорт класса Game для управления игровым процессом.
import impl.Main; // Импорт главного класса приложения.
import impl.scenes.GameScene; // Импорт класса GameScene, представляющего игровую сцену.
import impl.scenes.VictoryScene; // Импорт класса VictoryScene для экрана победы.
import java.util.Random; // Импорт класса Random для генерации случайных чисел.

public class Wave5 extends Wave { // Определение класса Wave5, который наследует от класса Wave.
    private static final double BASE_ENEMY_SPAWN_PERIOD = 0.9; // Базовый период появления врагов (в секундах).
    private static final int BASE_MAX_ENEMY_COUNT = 20; // Максимальное количество врагов в волне.

    private double modifiedEnemySpawnPeriod; // Модифицированный период появления врагов.
    private int modifiedMaxEnemyCount; // Модифицированное максимальное количество врагов.
    private int enemyCount = 0; // Текущее количество созданных врагов.
    private double startTime; // Время начала волны.
    private double nextSpawnTime; // Время следующего появления врага.
    private Random random = new Random(); // Экземпляр класса Random для генерации случайных чисел.
    private int lastSpawnedEnemyType = -1; // Хранит тип последнего созданного врага, чтобы избежать повторений.

    // Конструктор класса Wave5, который принимает игровую сцену в качестве параметра.
    public Wave5(GameScene gameScene) {
        super(); // Вызов конструктора суперкласса.
        modifiedEnemySpawnPeriod = BASE_ENEMY_SPAWN_PERIOD / Main.difficulty.getModifier(); // Модификация периода появления врагов в зависимости от уровня сложности.
        modifiedMaxEnemyCount = (int) (BASE_MAX_ENEMY_COUNT * Main.difficulty.getModifier()); // Модификация максимального количества врагов в зависимости от уровня сложности.
        enemyCount = 0; // Инициализация количества врагов.
        startTime = Game.getInstance().getTime(); // Запись времени начала волны.
        nextSpawnTime = startTime + GameScene.WAVE_REST_TIME; // Установка времени следующего появления врага с учетом времени отдыха волны.

        gameScene.setWaveMessage(getWaveMessage()); // Установка сообщения о текущей волне в игровую сцену.
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

    // Метод для создания врага, который выбирает случайный тип врага и проверяет достижение максимального количества врагов.
    private void spawnEnemy() {
        enemyCount++; // Увеличение счетчика созданных врагов.
        int enemyType; // Переменная для хранения типа врага.

        // Генерация случайного типа врага, избегая повторения последнего созданного типа.
        do {
            enemyType = random.nextInt(4); // Генерация случайного числа от 0 до 3 (включительно).
        } while (enemyType == lastSpawnedEnemyType); // Повторяем, пока не получим другой тип врага.

        lastSpawnedEnemyType = enemyType; // Обновление последнего созданного типа врага.

        // Создание врага в зависимости от сгенерированного типа.
        switch (enemyType) {
            case 0:
                spawnAsteroid(); // Создание астероида.
                break;
            case 1:
                spawnJavelin(); // Создание копья.
                break;
            case 2:
                spawnHornet(); // Создание шершня.
                break;
            case 3:
                spawnMarauder(); // Создание мародера.
                break;
        }

        // Проверка, достигнуто ли максимальное количество врагов.
        if (enemyCount >= modifiedMaxEnemyCount) {
            GameScene scene = (GameScene) Game.getInstance().getOpenScene(); // Получение текущей открытой игровой сцены.
            scene.removeObject(this); // Удаление текущей волны из сцены.
            scene.addObject(new Wave5(scene)); // Добавление новой волны (Wave5) в сцену.

            // Код для перехода к экрану победы, закомментированный для временного отключения.
            /*
            new Thread(() -> {
                try {
                    Thread.sleep(5000); // Задержка 5 секунд.
                } catch (InterruptedException e) {
                    e.printStackTrace(); // Обработка исключения.
                }
                int finalScore = scene.getScore(); // Получение текущего счета.
                Game.getInstance().loadScene(new VictoryScene(finalScore)); // Переход к экрану победы.
            }).start();
            */
        }
    }

    // Реализация абстрактного метода getWaveMessage для предоставления сообщения о текущей волне.
    @Override
    protected String getWaveMessage() {
        return "NEW WAVE"; // Возвращение сообщения о новой волне.
    }
}
