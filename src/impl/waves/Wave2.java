package impl.waves;

import gameEngine.Game;
import impl.Main;
import impl.scenes.GameScene;

public class Wave2 extends Wave {
    private static final double BASE_ENEMY_SPAWN_PERIOD = 1.0;
    private static final int BASE_MAX_ENEMY_COUNT = 15;

    private double modifiedEnemySpawnPeriod;
    private int modifiedMaxEnemyCount;
    private int enemyCount;
    private double startTime;
    private double nextSpawnTime;

    public Wave2() {
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
	switch (enemyCount % 4) {
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
	    scene.addObject(new Wave2());
	}
    }
}