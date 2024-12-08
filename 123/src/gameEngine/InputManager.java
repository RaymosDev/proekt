package gameEngine; // Объявление пакета, в котором находится класс InputManager.

/*  Класс InputManager отвечает за обработку ввода с клавиатуры в игровом движке. 
    Он хранит состояние клавиш (нажаты ли они) и предоставляет методы для 
    проверки нажатия клавиш, а также для получения значений по горизонтальной 
    и вертикальной осям. Класс использует интерфейс KeyListener для обработки 
    событий нажатия и отпускания клавиш. Он также поддерживает концепцию 
    "нажатых клавиш", чтобы отслеживать, какие клавиши были нажаты 
    в текущем и следующем обновлениях.
*/

import java.awt.event.KeyEvent; // Импорт класса KeyEvent для работы с событиями клавиатуры.
import java.awt.event.KeyListener; // Импорт интерфейса KeyListener для обработки событий клавиатуры.
import java.util.HashSet; // Импорт класса HashSet для использования множества.
import java.util.Set; // Импорт интерфейса Set для работы с множествами.

public class InputManager { // Объявление класса InputManager.
    private static boolean[] keys; // Статический массив для хранения состояния клавиш (нажата/не нажата).
    private Set<Integer> keysDownNow; // Множество для хранения клавиш, нажимаемых в текущем обновлении.
    private Set<Integer> keysDownNextUpdate; // Множество для хранения клавиш, нажимаемых в следующем обновлении.

    InputManager() { // Конструктор класса InputManager.
        keys = new boolean[256]; // Инициализация массива состояния клавиш (256 клавиш).
        keysDownNow = new HashSet<>(); // Инициализация множества для текущих нажатий клавиш.
        keysDownNextUpdate = new HashSet<>(); // Инициализация множества для нажатий клавиш в следующем обновлении.
    }

    KeyListener getKeyListener() { // Метод для получения слушателя клавиатуры.
        return new KeyListener() { // Возврат нового объекта, реализующего интерфейс KeyListener.
            @Override
            public void keyPressed(KeyEvent event) { // Обработка события нажатия клавиши.
                int keyCode = event.getKeyCode(); // Получение кода нажатой клавиши.
                if (!getKey(keyCode)) { // Если клавиша не была нажата ранее,
                    keysDownNextUpdate.add(keyCode); // добавляем ее в множество для следующего обновления.
                }
                keys[keyCode] = true; // Устанавливаем состояние клавиши как нажатую.
            }

            @Override
            public void keyReleased(KeyEvent event) { // Обработка события отпускания клавиши.
                int keyCode = event.getKeyCode(); // Получение кода отпущенной клавиши.
                keys[keyCode] = false; // Устанавливаем состояние клавиши как не нажатую.
            }

            @Override
            public void keyTyped(KeyEvent event) { // Метод для обработки событий ввода символов (не используется).
            }
        };
    }

    void tick() { // Метод для обновления состояния нажатых клавиш.
        keysDownNow.clear(); // Очистка множества нажатых клавиш на текущем обновлении.
        keysDownNow.addAll(keysDownNextUpdate); // Перенос нажатых клавиш из следующего обновления в текущее.
        keysDownNextUpdate.clear(); // Очистка множества нажатых клавиш для следующего обновления.
    }

    public boolean getKey(int keyCode) { // Метод для получения состояния клавиши по ее коду.
        return keys[keyCode]; // Возврат состояния клавиши (нажата/не нажата).
    }

    public boolean getKeyDown(int keyCode) { // Метод для проверки, была ли клавиша нажата в текущем обновлении.
        return keysDownNow.contains(keyCode); // Возврат true, если клавиша была нажата в текущем обновлении.
    }

    public double getHorizontalAxis() { // Метод для получения значения по горизонтальной оси.
        double horizontal = 0.0; // Изначальное значение по горизонтали.
        if (getKey(KeyEvent.VK_A) || getKey(KeyEvent.VK_LEFT)) { // Проверка нажатия клавиш A или стрелки влево.
            horizontal--; // Уменьшаем значение, если клавиша нажата.
        }
        if (getKey(KeyEvent.VK_D) || getKey(KeyEvent.VK_RIGHT)) { // Проверка нажатия клавиш D или стрелки вправо.
            horizontal++; // Увеличиваем значение, если клавиша нажата.
        }
        return horizontal; // Возврат значения по горизонтали.
    }

    public double getVerticalAxis() { // Метод для получения значения по вертикальной оси.
        double vertical = 0.0; // Изначальное значение по вертикали.
        if (getKey(KeyEvent.VK_S) || getKey(KeyEvent.VK_DOWN)) { // Проверка нажатия клавиш S или стрелки вниз.
            vertical--; // Уменьшаем значение, если клавиша нажата.
        }
        if (getKey(KeyEvent.VK_W) || getKey(KeyEvent.VK_UP)) { // Проверка нажатия клавиш W или стрелки вверх.
            vertical++; // Увеличиваем значение, если клавиша нажата.
        }
        return vertical; // Возврат значения по вертикали.
    }
}