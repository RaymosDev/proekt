package impl.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import gameEngine.Collider;
import gameEngine.Entity;
import gameEngine.Entity.EntityCollider;
import gameEngine.Game;
import gameEngine.InputManager;
import gameEngine.ResourceLoader;
import gameEngine.SceneObject;
import gameEngine.Vector2;
import impl.Main;
import impl.entities.PlayerShip;
import impl.waves.Wave1;
import java.util.ArrayList;
import java.util.List;

public class GameScene extends SceneWithKeys {
    public static final double FIRST_WAVE_WAIT_TIME = 2.5;
    public static final double WAVE_REST_TIME = 5.0;
    private static final Vector2 PLAYER_START = new Vector2(250, Main.HEIGHT / 2);
    private final String[] PAUSE_MENU_OPTIONS = { "Продолжить", "Главное меню" };

    private static final int ORIGINAL_BLOCK_WIDTH = 25; // Оригинальная ширина блока HP
    private static final int ORIGINAL_BLOCK_HEIGHT = 35; // Оригинальная высота блока HP
    private static final Image GREEN_HP = ResourceLoader.loadImage("res/images/ui/GreenHp.png")
            .getScaledInstance(scaleSize(ORIGINAL_BLOCK_WIDTH), scaleSize(ORIGINAL_BLOCK_HEIGHT), 0);
    private static final Image BLACK_HP = ResourceLoader.loadImage("res/images/ui/Black.png")
            .getScaledInstance(scaleSize(ORIGINAL_BLOCK_WIDTH), scaleSize(ORIGINAL_BLOCK_HEIGHT), 0);
    private static final Image END_HP = ResourceLoader.loadImage("res/images/ui/EndHp.png")
            .getScaledInstance(scaleSize(ORIGINAL_BLOCK_WIDTH), scaleSize(ORIGINAL_BLOCK_HEIGHT), 0);
    private static final Image YELLOW_HP = ResourceLoader.loadImage("res/images/ui/YellowHp.png")
            .getScaledInstance(scaleSize(ORIGINAL_BLOCK_WIDTH), scaleSize(ORIGINAL_BLOCK_HEIGHT), 0);
    private static final Image RED_HP = ResourceLoader.loadImage("res/images/ui/RedHp.png")
            .getScaledInstance(scaleSize(ORIGINAL_BLOCK_WIDTH), scaleSize(ORIGINAL_BLOCK_HEIGHT), 0);

    private Collider bounds;
    private BufferedImage backgroundImage;
    private Clip backgroundMusic;
    private PlayerShip player;
    private int score;
    private int currentPauseOption;
    private boolean paused;
    private InputManager inputManager;

    // Новая переменная для хранения сообщения о волне
    private String waveMessage = "";
    private double waveMessageDuration = 0; // Длительность отображения сообщения

    @Override
    public void initialize() {
        bounds = new Collider(-500, -500, Main.WIDTH + 500, Main.HEIGHT + 500) {
            @Override
            public void onCollisionEnter(Collider other) {
            }

            @Override
            public void onCollisionExit(Collider other) {
                if (other instanceof EntityCollider) {
                    Entity entity = ((EntityCollider) other).getEntity();
                    Game.getInstance().getOpenScene().removeObject(entity);
                }
            }
        };
        bounds.setActive(true);
        backgroundImage = ResourceLoader.toBufferedImage(
                ResourceLoader.loadImage("res/images/backgrounds/GameBackground.png").getScaledInstance(24750, 825, 0));
        backgroundMusic = ResourceLoader.loadAudioClip("res/audio/GameMusic.wav");
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        currentPauseOption = 0;
        inputManager = Game.getInstance().getInputManager();
        player = new PlayerShip(PLAYER_START);
        addObject(player);
        addObject(new FadeIn(1.5));
        addObject(new Wave1(this)); // Передаем ссылку на GameScene в Wave1
    }

    @Override
    public void tick() {
        super.tick();
        
        // Уменьшаем время отображения сообщения о волне
        if (waveMessageDuration > 0) {
            waveMessageDuration -= Game.getInstance().getDeltaTime();
            if (waveMessageDuration <= 0) {
                waveMessage = ""; // Скрываем сообщение
            }
        }

        if (paused) {
            if (Game.getInstance().getInputManager().getKeyDown(KeyEvent.VK_ESCAPE)) {
                unPause();
            }
            pauseTick();
        } else {
            if (Game.getInstance().getInputManager().getKeyDown(KeyEvent.VK_ESCAPE)) {
                pause();
            }
        }
    }

    private void pauseTick() {
        currentPauseOption = upDown(inputManager, PAUSE_MENU_OPTIONS, currentPauseOption);
        if (inputManager.getKeyDown(KeyEvent.VK_ENTER)) {
            if (currentPauseOption == 0) {
                unPause();
            } else if (currentPauseOption == 1) {
                unPause();
                backgroundMusic.stop();
                Game.getInstance().loadScene(new MainMenuScene("Main"));
            }
        }
    }

    public boolean isPaused() {
        return paused;
    }

    private void pause() {
        Game.getInstance().setTimeScale(0.0);
        ResourceLoader.loadAudioClip("res/audio/Pause.wav").start();
        paused = true;
    }

    private void unPause() {
        Game.getInstance().setTimeScale(1.0);
        ResourceLoader.loadAudioClip("res/audio/Pause.wav").start();
        paused = false;
    }

    @Override
    public void render(Graphics g) {
        double time = Game.getInstance().getTime();
        int x = (int) (time * 150 % 22195);
        Image backgroundSubImage = backgroundImage.getSubimage(x, 0, Main.WIDTH, Main.HEIGHT);
        g.drawImage(backgroundSubImage, 0, 0, null);
        super.render(g);
        g.setColor(Color.WHITE);
        g.setFont(getScaledFont(50)); // Получаем масштабированный шрифт
        g.drawString("Счёт: " + score, 50, 60);
        
        // Отображаем сообщение о волне, если оно есть
        if (!waveMessage.isEmpty()) {
            g.drawString(waveMessage, Main.WIDTH / 2 - g.getFontMetrics().stringWidth(waveMessage) / 2, 100);
        }
        
        drawHealthBar(g);
        if (paused) {
            Color filter = new Color(0, 0, 0, 150);
            g.setColor(filter);
            g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
            
            g.setColor(Color.WHITE);
            String pauseMessage = "ПАУЗА";
            int containerWidth = Main.WIDTH;
            int pauseMessageWidth = g.getFontMetrics().stringWidth(pauseMessage);
            int pauseMessageX = (containerWidth - pauseMessageWidth) / 2; // Вычисляем координату X для центрирования
            g.drawString(pauseMessage, pauseMessageX, 350);
            
            renderScrollingMenus(g, PAUSE_MENU_OPTIONS, currentPauseOption);
        }
    }

    private void drawHealthBar(Graphics g) {
        double healthProportion = player.getCurrentHealth() / player.getMaxHealth();
        int totalBlocks = (int) player.getMaxHealth();  // Общее количество блоков HP
        int numBars = (int) Math.ceil(healthProportion * totalBlocks); // Количество активных блоков
        int blockWidth = scaleSize(ORIGINAL_BLOCK_WIDTH); // Масштабированная ширина одного блока HP
        int padding = 10; // Отступ от правого края экрана
        int xOffset = Main.WIDTH - (totalBlocks * blockWidth) - padding; // Начальная позиция для отрисовки с учетом отступа

        // Отрисовка рамки или фона для индикатора здоровья
        g.drawImage(END_HP, xOffset - END_HP.getWidth(null), 35, null);

        // Отрисовка блоков HP с изменением цвета
        for (int i = 0; i < totalBlocks; i++) {
            Image hpBlock;
            if (i < numBars) {
                // Определяем цвет блока в зависимости от уровня здоровья
                if (healthProportion < 0.33) {
                    hpBlock = RED_HP; // Красный цвет
                } else if (healthProportion < 0.66) {
                    hpBlock = YELLOW_HP; // Желтый цвет
                } else {
                    hpBlock = GREEN_HP; // Зеленый цвет
                }
                g.drawImage(hpBlock, xOffset + i * blockWidth, 35, null); // Отрисовка активного блока
            } else {
                g.drawImage(BLACK_HP, xOffset + i * blockWidth, 35, null); // Отрисовка блока, если HP меньше
            }
        }
    }

    private static int scaleSize(int originalSize) {
        double scaleX = (double) Main.WIDTH / 1800; // Используйте ваше целевое разрешение
        double scaleY = (double) Main.HEIGHT / 800; // Используйте ваше целевое разрешение
        return (int) (originalSize * Math.min(scaleX, scaleY)); // Применяем масштабирование
    }

    // Метод для получения масштабированного шрифта
    private Font getScaledFont(int originalSize) {
        int scaledSize = scaleSize(originalSize); // Масштабируем размер шрифта
        return ResourceLoader.loadFont("res/Font.ttf", scaledSize); // Загружаем шрифт с новым размером
    }

    public PlayerShip getPlayer() {
        return player;
    }

    public void addScore(int score) {
        this.score += score;
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundMusic.stop();

        // Создаем копию списка объектов
        List<SceneObject> objectsCopy = new ArrayList<>(objects);

        for (SceneObject object : objectsCopy) {
            object.dispose();
        }

        objects.clear(); // Теперь безопасно очищаем оригинальный список
        bounds.setActive(false);
    }

    public void endGame() {
        backgroundMusic.stop();
        Game.getInstance().loadScene(new EndingScene(score));
    }

    public int getScore() {
        return score;
    }

    // Метод для обновления сообщения о волне
    public void setWaveMessage(String message) {
        this.waveMessage = message;
        this.waveMessageDuration = 3.0; // Установите длительность отображения сообщения (в секундах)
    }
}
