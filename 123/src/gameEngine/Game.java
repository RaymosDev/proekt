package gameEngine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;

public class Game {
    private static Game instance;

    private String title;
    private int width;
    private int height;
    private InputManager inputManager;
    private Display display;
    private Scene currentScene;
    private Scene nextScene;
    private boolean running;
    private double timeScale;
    private double elapsedTimeSeconds;
    private double deltaTimeSeconds;

    public Game(String title, int width, int height, Image icon) {
        if (instance != null) {
            throw new IllegalStateException("There can be only one!");
        }
        this.title = title;
        this.width = width;
        this.height = height;
        inputManager = new InputManager();
        display = new Display(title, width, height, icon, inputManager.getKeyListener());
        currentScene = null;
        nextScene = null;
        running = false;
        timeScale = 1.0;
        elapsedTimeSeconds = 0.0;
        deltaTimeSeconds = 0.0;
        instance = this;
    }

    public static Game getInstance() {
        return instance;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public Display getDisplay() {
        return display;
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("Game already started!");
        }
        display.open();
        running = true;
        new Thread() {
            @Override
            public void run() {
                long lastTimeMilis = System.currentTimeMillis();
                while (running) {
                    long currentTimeMilis = System.currentTimeMillis();
                    long absoluteDeltaTimeMilis = currentTimeMilis - lastTimeMilis;
                    lastTimeMilis = currentTimeMilis;
                    deltaTimeSeconds = absoluteDeltaTimeMilis * 0.001 * timeScale;
                    elapsedTimeSeconds += deltaTimeSeconds;
                    if (nextScene != null) {
                        if (currentScene != null) {
                            currentScene.dispose();
                        }
                        currentScene = nextScene;
                        nextScene = null;
                        currentScene.initialize();
                    }
                    if (currentScene != null) {
                        tick();
                        render();
                    }
                    inputManager.tick();
                }
                if (currentScene != null) {
                    currentScene.dispose();
                }
                display.close();
            }
        }.start();
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("Game already stopped!");
        }
        running = false;
     
    }

    public Scene getOpenScene() {
        return currentScene;
    }

    public void loadScene(Scene scene) {
    
        nextScene = scene;
    }

    private void tick() {
        currentScene.tick();
    }

    private void render() {
        BufferStrategy bufferStrategy = display.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0, 0, width, height);
        currentScene.render(graphics);
        graphics.dispose();
        bufferStrategy.show();
    }

    public double getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public double getTime() {
        return elapsedTimeSeconds;
    }

    public double getDeltaTime() {
        return deltaTimeSeconds;
    }
}
