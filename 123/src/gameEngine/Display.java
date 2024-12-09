package gameEngine; 

/*  
*   Тут будет в целом про отображение окна игры и мб графический интерфейс
*/

import java.awt.Canvas;
import java.awt.Cursor; 
import java.awt.Dimension;
import java.awt.Image; 
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage; 
import javax.swing.JFrame; 

public class Display 
{ 
    private static final int NUM_BUFFERS = 3; 

    private JFrame window; // Объект окна приложения
    private Canvas canvas; // Объект холста для рисования

    public Display(String title, int width, int height, Image icon, KeyListener keyListener)
    { 
        Dimension dimension = new Dimension(width, height);

        window = new JFrame(title); 
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(dimension); 
        window.setResizable(false); 
        window.setLocationRelativeTo(null); // Центрирование окна на экране
        window.setIconImage(icon);


   
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "Blank"); // Создание пустого курсора
        window.getContentPane().setCursor(blankCursor); // Установка пустого курсора для окна


        canvas = new Canvas();
        canvas.setPreferredSize(dimension); 
        canvas.setMinimumSize(dimension); 
        canvas.setMaximumSize(dimension);
        canvas.setFocusable(false); // Запрет фокусировки на холсте

        window.add(canvas); // добавление холста в окно
        window.pack(); // yпаковка окна с учетом размеров компонентов
        window.addKeyListener(keyListener); // Добавление слушателя клавиатуры для обработки событий

        canvas.createBufferStrategy(NUM_BUFFERS); 
    }



    public void resize(int width, int height) // Метод для изменения размера окна и холста.
    { 
        Dimension newDimension = new Dimension(width, height); 
        window.setSize(newDimension); 
        canvas.setPreferredSize(newDimension); 
        canvas.setMinimumSize(newDimension); 
        canvas.setMaximumSize(newDimension); 
        window.pack(); 
    }

    public BufferStrategy getBufferStrategy() 
    { 
        return canvas.getBufferStrategy(); 
    }

    public void open() // открытие окна
    { 
        window.setVisible(true); // первая лаба ( ͡° ͜ʖ ͡°)
    }

    public void close() // закрытие окна
    { 
        window.dispose(); 
    }
}
