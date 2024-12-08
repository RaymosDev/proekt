 package impl.waves;

import gameEngine.Game;
import impl.Main;
import impl.scenes.GameScene;
import java.util.Random;

public class Wave5 extends Wave {
    private static final double BASE_ENEMY_SPAWN_PERIOD = 0.90;
    private static final int BASE_MAX_ENEMY_COUNT = 20;

    private double modifiedEnemySpawnPeriod;
    private int modifiedMaxEnemyCount;
    private int enemyCount = 0;
    private double startTime;
    private double nextSpawnTime;
    private Random random = new Random();
    private int lastSpawnedEnemyType = -1;

    public Wave5(GameScene gameScene) {
        super();
        modifiedEnemySpawnPeriod = BASE_ENEMY_SPAWN_PERIOD / Main.difficulty.getModifier();
        modifiedMaxEnemyCount = (int) (BASE_MAX_ENEMY_COUNT * Main.difficulty.getModifier());
        enemyCount = 0;
        startTime = Game.getInstance().getTime();
        nextSpawnTime = startTime + GameScene.WAVE_REST_TIME;

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

    do {
        enemyType = random.nextInt(4);
    } while (enemyType == lastSpawnedEnemyType);


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


     if (enemyCount >= modifiedMaxEnemyCount) {
        GameScene scene = (GameScene) Game.getInstance().getOpenScene();
        scene.removeObject(this);
        scene.addObject(new Wave5(scene));
        
     //    new Thread(() -> {
     //       try {
     //           Thread.sleep(5000); // Задержка 5 секунд
      //      } catch (InterruptedException e) {
      //          e.printStackTrace();
       //     }
       //     int finalScore = scene.getScore(); // Получаем текущий счет
       //     Game.getInstance().loadScene(new VictoryScene(finalScore)); // Переход к экрану победы
      //  }).start();
    }
}

    @Override
    protected String getWaveMessage() {
        return "NEW WAVE";
    }
}
