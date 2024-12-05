 package impl.waves;

import gameEngine.Game;
import impl.Main;
import impl.scenes.GameScene;
import impl.scenes.VictoryScene;
import java.util.Random;

public class Wave5 extends Wave {
    private static final double BASE_ENEMY_SPAWN_PERIOD = 0.5; // Период спавна врагов
    private static final int BASE_MAX_ENEMY_COUNT = 60; // Максимальное количество врагов

    private double modifiedEnemySpawnPeriod;
    private int modifiedMaxEnemyCount;
    private int enemyCount = 0;
    private double startTime;
    private double nextSpawnTime;
    private Random random = new Random();
    private int lastSpawnedEnemyType = -1; // Инициализируем с -1, чтобы избежать совпадений

    public Wave5(GameScene gameScene) {
        super(); // Вызов конструктора родительского класса
        modifiedEnemySpawnPeriod = BASE_ENEMY_SPAWN_PERIOD / Main.difficulty.getModifier();
        modifiedMaxEnemyCount = (int) (BASE_MAX_ENEMY_COUNT * Main.difficulty.getModifier());
        enemyCount = 0;
        startTime = Game.getInstance().getTime();
        nextSpawnTime = startTime + GameScene.WAVE_REST_TIME;

        // Устанавливаем сообщение о начале новой волны
        gameScene.setWaveMessage(getWaveMessage());
    }

    @Override
    public void tick() {
        double currentTime = Game.getInstance().getTime();
        if (currentTime >= nextSpawnTime) {
            nextSpawnTime = nextSpawnTime + modifiedEnemySpawnPeriod;
            spawnEnemy();
        }
    }

   private void spawnEnemy() {
    enemyCount++;
    int enemyType;

    // Генерируем новый тип врага, пока он не будет отличаться от предыдущего
    do {
        enemyType = random.nextInt(4);
    } while (enemyType == lastSpawnedEnemyType);

    // Сохраняем тип последнего заспавненного врага
    lastSpawnedEnemyType = enemyType;

    switch (enemyType) {
        case 0:
            spawnAsteroid();
            break;
        case 1:
            spawnJavelin();
            break;
        case 2:
            spawnHornet();
            break;
        case 3:
            spawnMarauder();
            break;
    }

    // Проверяем, достигнуто ли максимальное количество врагов
    if (enemyCount >= modifiedMaxEnemyCount) {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene();
        
        // Здесь мы загружаем экран победы вместо новой волны
        scene.removeObject(this);
        
         new Thread(() -> {
            try {
                Thread.sleep(5000); // Задержка 5 секунд
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int finalScore = scene.getScore(); // Получаем текущий счет
            Game.getInstance().loadScene(new VictoryScene(finalScore)); // Переход к экрану победы
        }).start();
    }
}

    @Override
    protected String getWaveMessage() {
        return "NEW WAVE"; // Сообщение для второй волны
    }
}
