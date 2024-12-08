package impl.waves;

import gameEngine.Game;
import impl.Main;
import impl.scenes.GameScene;
import java.util.Random;

public class Wave3 extends Wave {
    private static final double BASE_ENEMY_SPAWN_PERIOD = 1.0;
    private static final int BASE_MAX_ENEMY_COUNT = 20;
    private GameScene gameScene;
    private double modifiedEnemySpawnPeriod;
    private int modifiedMaxEnemyCount;
    private int enemyCount = 0;
    private double startTime;
    private double nextSpawnTime;
    private Random random = new Random();
    private int lastSpawnedEnemyType = -1; // Инициализируем с -1, чтобы избежать совпадений

    public Wave3(GameScene gameScene) {
        this.gameScene = gameScene;
        modifiedEnemySpawnPeriod = BASE_ENEMY_SPAWN_PERIOD / Main.difficulty.getModifier();
        modifiedMaxEnemyCount = (int) (BASE_MAX_ENEMY_COUNT * Main.difficulty.getModifier());
        enemyCount = 0;
        startTime = Game.getInstance().getTime();
        nextSpawnTime = startTime + GameScene.WAVE_REST_TIME;
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
        //spawnHornet();





    int enemyType;

    // Генерируем новый тип врага, пока он не будет отличаться от предыдущего
    do {
        enemyType = random.nextInt(3);
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
    }



        if (enemyCount >= modifiedMaxEnemyCount) {
            GameScene scene = (GameScene) Game.getInstance().getOpenScene();
            scene.removeObject(this);
            scene.addObject(new Wave4(scene));
        }
    }

    @Override
    protected String getWaveMessage() {
        return "NEW WAVE";
    }
}

