package gameEngine; // Объявление пакета, в котором находится класс Game.

/*  Класс Game представляет собой основной класс игрового движка, 
    который управляет жизненным циклом игры, включая инициализацию, 
    запуск, остановку и отрисовку сцен. Он использует шаблон синглтона 
    для обеспечения единственного экземпляра игры и включает в себя менеджер 
    ввода, дисплей и механизмы для работы со сценами. 
    Игровой цикл реализован в отдельном потоке и включает в себя 
    обновление состояния игры и отрисовку графики.
*/

import java.awt.Graphics; // Импорт класса Graphics для работы с графикой.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.image.BufferStrategy; // Импорт класса BufferStrategy для управления буферизацией графики.

public class Game { // Объявление класса Game.
    private static Game instance; // Статическая переменная для хранения единственного экземпляра игры (синглтон).

    private String title; // Заголовок игры.
    private int width; // Ширина окна игры.
    private int height; // Высота окна игры.
    private InputManager inputManager; // Менеджер ввода для обработки пользовательских команд.
    private Display display; // Объект для отображения графики.
    private Scene currentScene; // Текущая сцена, отображаемая в игре.
    private Scene nextScene; // Сцена, ожидающая загрузки.
    private boolean running; // Флаг, указывающий, запущена ли игра.
    private double timeScale; // Масштаб времени для управления скоростью игры.
    private double elapsedTimeSeconds; // Общее время, прошедшее с начала игры.
    private double deltaTimeSeconds; // Время, прошедшее с последнего обновления.

    public Game(String title, int width, int height, Image icon) { // Конструктор класса Game.
        if (instance != null) { // Проверка, существует ли уже экземпляр игры.
            throw new IllegalStateException("There can be only one!"); // Исключение, если экземпляр уже существует.
        }
        this.title = title; // Установка заголовка игры.
        this.width = width; // Установка ширины окна.
        this.height = height; // Установка высоты окна.
        inputManager = new InputManager(); // Инициализация менеджера ввода.
        display = new Display(title, width, height, icon, inputManager.getKeyListener()); // Создание объекта Display.
        currentScene = null; // Изначально текущая сцена не установлена.
        nextScene = null; // Изначально следующая сцена не установлена.
        running = false; // Игра не запущена.
        timeScale = 1.0; // Установка масштаба времени по умолчанию.
        elapsedTimeSeconds = 0.0; // Изначально прошедшее время равно 0.
        deltaTimeSeconds = 0.0; // Изначально время обновления равно 0.
        instance = this; // Установка текущего экземпляра как единственного.
    }

    public static Game getInstance() { // Метод для получения единственного экземпляра игры.
        return instance; // Возврат экземпляра игры.
    }

    public String getTitle() { // Метод для получения заголовка игры.
        return title; // Возврат заголовка.
    }

    public int getWidth() { // Метод для получения ширины окна.
        return width; // Возврат ширины.
    }

    public int getHeight() { // Метод для получения высоты окна.
        return height; // Возврат высоты.
    }

    public InputManager getInputManager() { // Метод для получения менеджера ввода.
        return inputManager; // Возврат менеджера ввода.
    }

    public Display getDisplay() { // Метод для получения объекта отображения.
        return display; // Возврат объекта Display.
    }

    public void start() { // Метод для запуска игры.
        if (running) { // Проверка, запущена ли игра.
            throw new IllegalStateException("Game already started!"); // Исключение, если игра уже запущена.
        }
        display.open(); // Открытие окна отображения.
        running = true; // Установка флага, указывающего на то, что игра запущена.
        new Thread() { // Создание нового потока для выполнения игрового цикла.
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