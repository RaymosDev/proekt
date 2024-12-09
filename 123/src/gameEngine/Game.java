package gameEngine; 

/*  Класс Game представляет собой основной класс игрового движка, 
    который управляет жизненным циклом игры, включая инициализацию, 
    запуск, остановку и отрисовку сцен. Он использует шаблон синглтона 
    для обеспечения единственного экземпляра игры и включает в себя менеджер 
    ввода, дисплей и механизмы для работы со сценами. 
    Игровой цикл реализован в отдельном потоке и включает в себя 
    обновление состояния игры и отрисовку графики.
*/

import java.awt.Graphics;
import java.awt.Image; 
import java.awt.image.BufferStrategy; 

public class Game { 
    private static Game instance; // Тут храним единственный экземпляр игры

    private String title; // Заголовок 
    private int width; // Ширина окна 
    private int height; // Высота окна 
    private InputManager inputManager; 
    private Display display; // Объект для отображения графики.
    private Scene currentScene; // Текущая сцена
    private Scene nextScene; // Сцена, ожидающая загрузки.
    private boolean running; // Флаг, указывающий, запущена ли игра.
    private double timeScale; // Масштаб времени для управления скоростью игры.
    private double elapsedTimeSeconds; // общее время, прошедшее с начала игры
    private double deltaTimeSeconds; // время, прошедшее с последнего обновления

    public Game(String title, int width, int height, Image icon) {
        if (instance != null) { // Если экземпляр игры уже существует
            throw new IllegalStateException("Экземпляр Game уже существует"); 
        }
        this.title = title; 
        this.width = width;
        this.height = height;
        inputManager = new InputManager(); 
        display = new Display(title, width, height, icon, inputManager.getKeyListener()); 
        currentScene = null; 
        nextScene = null; 
        running = false; 
        timeScale = 1.0; // Установка масштаба времени по умолчанию.
        elapsedTimeSeconds = 0.0; // Изначально прошедшее время = 0.
        deltaTimeSeconds = 0.0; // Изначально время обновления = 0.
        instance = this; // Установка текущего экземпляра как единственного.
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

    public void start() // Метод для запуска игры
    { 
        if (running) // Если уже запущена
        { 
            throw new IllegalStateException("Игра уже запущена");
        }

        display.open(); // Открытие окна отображения
        running = true;
        
        new Thread() { // Поток для выполнения gameloop'а
            @Override
            public void run() { // Переопределение метода run для запуска игрового цикла.
                long lastTimeMilis = System.currentTimeMillis(); // Запись текущего времени в миллисекундах.
                while (running) { // Цикл, продолжающийся, пока игра запущена.
                    long currentTimeMilis = System.currentTimeMillis(); // Получение текущего времени.
                    long absoluteDeltaTimeMilis = currentTimeMilis - lastTimeMilis; // Вычисление времени, прошедшего с последнего обновления.
                    lastTimeMilis = currentTimeMilis; // Обновление времени последнего обновления.
                    deltaTimeSeconds = absoluteDeltaTimeMilis * 0.001 * timeScale; // Вычисление времени обновления в секундах с учетом масштаба времени.
                    elapsedTimeSeconds += deltaTimeSeconds; // Обновление общего времени игры.
                    if (nextScene != null) { // Проверка, есть ли следующая сцена для загрузки.
                        if (currentScene != null) { // Если текущая сцена существует,
                            currentScene.dispose(); // освободить ее ресурсы.
                        }
                        currentScene = nextScene; // Установка следующей сцены как текущей.
                        nextScene = null; // Сброс следующей сцены.
                        currentScene.initialize(); // Инициализация новой текущей сцены.
                    }
                    if (currentScene != null) { // Если текущая сцена существует,
                        tick(); // Обновить состояние сцены.
                        render(); // Отобразить сцену.
                    }
                    inputManager.tick(); // Обновление состояния ввода.
                }
                if (currentScene != null) { // Если текущая сцена существует после выхода из цикла,
                    currentScene.dispose(); // освободить ее ресурсы.
                }
                display.close(); // Закрытие окна отображения.
            }
        }.start(); // Запуск нового потока.
    }

    public void stop() { // Метод для остановки игры.
        if (!running) { // Проверка, запущена ли игра.
            throw new IllegalStateException("Game already stopped!"); // Исключение, если игра уже остановлена.
        }
        running = false; // Установка флага, указывающего на остановку игры.
    }

    public Scene getOpenScene() { // Метод для получения текущей открытой сцены.
        return currentScene; // Возврат текущей сцены.
    }

    public void loadScene(Scene scene) { // Метод для загрузки новой сцены.
        nextScene = scene; // Установка следующей сцены.
    }

    private void tick() { // Метод для обновления состояния текущей сцены.
        currentScene.tick(); // Вызов метода tick у текущей сцены.
    }

    private void render() { // Метод для отрисовки текущей сцены.
        BufferStrategy bufferStrategy = display.getBufferStrategy(); // Получение стратегии буферизации для отображения.
        Graphics graphics = bufferStrategy.getDrawGraphics(); // Получение объекта Graphics для рисования.
        graphics.clearRect(0, 0, width, height); // Очистка области отображения.
        currentScene.render(graphics); // Отрисовка текущей сцены.
        graphics.dispose(); // Освобождение ресурсов графики.
        bufferStrategy.show(); // Отображение обновленного содержимого.
    }

    public double getTimeScale() { // Метод для получения масштаба времени.
        return timeScale; // Возврат масштаба времени.
    }

    public void setTimeScale(double timeScale) { // Метод для установки нового масштаба времени.
        this.timeScale = timeScale; // Установка нового масштаба времени.
    }

    public double getTime() { // Метод для получения общего времени игры.
        return elapsedTimeSeconds; // Возврат общего времени.
    }

    public double getDeltaTime() { // Метод для получения времени обновления.
        return deltaTimeSeconds; // Возврат времени обновления.
    }
}