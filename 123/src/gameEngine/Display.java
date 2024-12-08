package gameEngine; // Объявление пакета, в котором находится класс Display.

/*  Этот класс Display отвечает за создание и 
    управление графическим интерфейсом окна игры, 
    включая настройки размера, курсора и буферизации для отрисовки.
*/

import java.awt.Canvas; // Импорт класса Canvas для создания области рисования.
import java.awt.Cursor; // Импорт класса Cursor для работы с курсором.
import java.awt.Dimension; // Импорт класса Dimension для задания размеров.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.Point; // Импорт класса Point для работы с координатами.
import java.awt.Toolkit; // Импорт класса Toolkit для доступа к системным ресурсам.
import java.awt.event.KeyListener; // Импорт интерфейса KeyListener для обработки событий клавиатуры.
import java.awt.image.BufferStrategy; // Импорт класса BufferStrategy для управления буферизацией.
import java.awt.image.BufferedImage; // Импорт класса BufferedImage для работы с изображениями в памяти.

import javax.swing.JFrame; // Импорт класса JFrame для создания окна приложения.

public class Display { // Объявление класса Display, который отвечает за отображение окна игры.
    private static final int NUM_BUFFERS = 3; // Константа, определяющая количество буферов для двойной буферизации.

    private JFrame window; // Объект окна приложения.
    private Canvas canvas; // Объект холста для рисования.

    public Display(String title, int width, int height, Image icon, KeyListener keyListener) { // Конструктор класса Display.
        Dimension dimension = new Dimension(width, height); // Создание объекта Dimension с заданными шириной и высотой.

        window = new JFrame(title); // Инициализация окна с заданным заголовком.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Установка действия при закрытии окна.
        window.setSize(dimension); // Установка размера окна.
        window.setResizable(false); // Запрет изменения размера окна.
        window.setLocationRelativeTo(null); // Центрирование окна на экране.
        window.setIconImage(icon); // Установка иконки окна.

        // Создание пустого изображения для курсора.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "Blank"); // Создание пустого курсора.
        window.getContentPane().setCursor(blankCursor); // Установка пустого курсора для окна.

        canvas = new Canvas(); // Инициализация холста.
        canvas.setPreferredSize(dimension); // Установка предпочтительного размера холста.
        canvas.setMinimumSize(dimension); // Установка минимального размера холста.
        canvas.setMaximumSize(dimension); // Установка максимального размера холста.
        canvas.setFocusable(false); // Запрет фокусировки на холсте.

        window.add(canvas); // Добавление холста в окно.
        window.pack(); // Упаковка окна с учетом размеров компонентов.
        window.addKeyListener(keyListener); // Добавление слушателя клавиатуры для обработки событий.

        canvas.createBufferStrategy(NUM_BUFFERS); // Создание стратегии буферизации для холста.
    }

    public void resize(int width, int height) { // Метод для изменения размера окна и холста.
        Dimension newDimension = new Dimension(width, height); // Создание нового объекта Dimension с новыми размерами.
        window.setSize(newDimension); // Установка нового размера окна.
        canvas.setPreferredSize(newDimension); // Установка нового предпочтительного размера холста.
        canvas.setMinimumSize(newDimension); // Установка нового минимального размера холста.
        canvas.setMaximumSize(newDimension); // Установка нового максимального размера холста.
        window.pack(); // Упаковка окна с учетом новых размеров компонентов.
    }

    public BufferStrategy getBufferStrategy() { // Метод для получения стратегии буферизации холста.
        return canvas.getBufferStrategy(); // Возврат текущей стратегии буферизации.
    }

    public void open() { // Метод для открытия окна.
        window.setVisible(true); // Установка видимости окна.
    }

    public void close() { // Метод для закрытия окна.
        window.dispose(); // Освобождение ресурсов, связанных с окном.
    }
}
