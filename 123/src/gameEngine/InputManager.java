package gameEngine; 

/*  
*   Тут обработка ввода с клавиатуры
*/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet; 
import java.util.Set; 

public class InputManager 
{ 
    private static boolean[] keys; // Тут храним состояния клавиш (нажата/не нажата)
    private Set<Integer> keysDownNow; // Множество для хранения клавиш, нажимаемых в текущем обновлении
    private Set<Integer> keysDownNextUpdate; // Множество для хранения клавиш, нажимаемых в следующем обновлении

    InputManager() 
    { 
        keys = new boolean[256]; 
        keysDownNow = new HashSet<>(); 
        keysDownNextUpdate = new HashSet<>();
    }

    KeyListener getKeyListener()
    {
        return new KeyListener() 
        { 
            @Override
            public void keyPressed(KeyEvent event) // нажатие клавиши
            { 
                int keyCode = event.getKeyCode(); 
                if (!getKey(keyCode)) // Если клавиша не была нажата ранее,
                { 
                    keysDownNextUpdate.add(keyCode); // добавляем ее в множество для следующего обновления.
                }
                keys[keyCode] = true; // устанавливаем состояние клавиши как нажатую
            }

            @Override
            public void keyReleased(KeyEvent event) // отпущение клавиши
            { 
                int keyCode = event.getKeyCode(); 
                keys[keyCode] = false; // устанавливаем состояние клавиши как не нажатую
            }

            @Override
            public void keyTyped(KeyEvent event) //обработка ввода символа (пусть будет)
            { 
            }
        };
    }

    void tick() // обновление состояния нажатых клавиш
    { 
        keysDownNow.clear(); // очистили текущие нажатые клавиши
        keysDownNow.addAll(keysDownNextUpdate); // перенесли нажатые клавиши из следующего обновления в текущее
        keysDownNextUpdate.clear();
    }

    public boolean getKey(int keyCode) 
    { 
        return keys[keyCode]; // Возврат состояния клавиши (нажата/не нажата).
    }

    public boolean getKeyDown(int keyCode) 
    {
        return keysDownNow.contains(keyCode); // Возврат true, если клавиша была нажата в текущем обновлении.
    }

    public double getHorizontalAxis() 
    { 
        double horizontal = 0.0; 
        if (getKey(KeyEvent.VK_A) || getKey(KeyEvent.VK_LEFT)) 
        {
            horizontal--; 
        }
        if (getKey(KeyEvent.VK_D) || getKey(KeyEvent.VK_RIGHT)) 
        {
            horizontal++; 
        }
        return horizontal;
    }

    public double getVerticalAxis() 
    {
        double vertical = 0.0;
        if (getKey(KeyEvent.VK_S) || getKey(KeyEvent.VK_DOWN)) 
        { 
            vertical--; 
        }
        if (getKey(KeyEvent.VK_W) || getKey(KeyEvent.VK_UP)) 
        { 
            vertical++; 
        }
        return vertical; 
    }
}