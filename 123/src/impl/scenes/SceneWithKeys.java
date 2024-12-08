package impl.scenes; // Объявление пакета, в котором находится класс SceneWithKeys.

/*  Класс SceneWithKeys расширяет функциональность базового класса Scene, 
    добавляя поддержку для создания и управления меню, которое можно 
    навигировать с помощью клавиатуры. Он включает в себя методы для 
    инициализации сцены, отрисовки меню, обработки навигации 
    по опциям и воспроизведения звуковых эффектов. 
Основные компоненты класса:
Константы:

UI_FONT: Шрифт для отображения текста в пользовательском интерфейсе.
buttonSelected, button: Изображения для выделенной и обычной кнопки.
Методы:

initialize(): Метод для инициализации сцены (в данном случае не выполняет действий).
renderScrollingMenus(Graphics g, String[] options, int currentOption): Метод для отрисовки меню с прокручиваемыми опциями.
upDown(InputManager inputManager, String[] options, int currentOption): Метод для обработки навигации по опциям меню с помощью клавиш "вверх" и "вниз".
onSound(): Метод для воспроизведения звука при взаимодействии с меню.
*/

import java.awt.Color; // Импорт класса Color для работы с цветами.
import java.awt.Font; // Импорт класса Font для работы со шрифтами.
import java.awt.Graphics; // Импорт класса Graphics для рисования на экране.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.event.KeyEvent; // Импорт класса KeyEvent для обработки событий клавиатуры.

import gameEngine.InputManager; // Импорт класса InputManager для обработки ввода пользователя.
import gameEngine.ResourceLoader; // Импорт класса ResourceLoader для загрузки ресурсов.
import gameEngine.Scene; // Импорт класса Scene, от которого наследуется SceneWithKeys.
import impl.Main; // Импорт главного класса приложения.

public class SceneWithKeys extends Scene {
    private static final Font UI_FONT = ResourceLoader.loadFont("res/Font.ttf", 36); // Шрифт для пользовательского интерфейса.

    // Изображения кнопок: выделенная и обычная.
    public final Image buttonSelected = ResourceLoader.loadImage("res/images/ui/ButtonHover.png").getScaledInstance(336, 56, 0);
    public final Image button = ResourceLoader.loadImage("res/images/ui/Button.png").getScaledInstance(336, 56, 0);

    // Метод инициализации сцены. В данном случае не выполняет никаких действий.
    public void initialize() {
    }

    // Метод для отрисовки прокручиваемого меню.
    public void renderScrollingMenus(Graphics g, String[] options, int currentOption) {
        Image buttonState; // Переменная для хранения состояния кнопки (выделенная или обычная).
        g.setFont(UI_FONT); // Установка шрифта для графики.
        int containerWidth = Main.WIDTH; // Получение ширины контейнера.
        int offset = 120; // Смещение для вертикального размещения меню.
        int startY = (Main.HEIGHT - (options.length * 67)) / 2 + offset; // Вычисление начальной позиции по Y для меню.

        // Цикл по всем опциям меню.
        for (int i = 0; i < options.length; i++) {
            // Определение состояния кнопки в зависимости от того, выбрана ли она.
            if (currentOption == i) {
                g.setColor(Color.WHITE); // Установка цвета текста для выделенной кнопки.
                buttonState = buttonSelected; // Установка выделенной кнопки.
            } else {
                g.setColor(Color.WHITE); // Установка цвета текста для обычной кнопки.
                buttonState = button; // Установка обычной кнопки.
            }

            // Вычисление координат для отрисовки кнопки.
            int buttonX = (containerWidth - buttonState.getWidth(null)) / 2; // Центрирование кнопки по X.
            g.drawImage(buttonState, buttonX, startY + 67 * i, null); // Отрисовка кнопки.

            String option = options[i]; // Получение текста текущей опции.
            int width = g.getFontMetrics().stringWidth(option); // Получение ширины текста опции.
            int textX = (containerWidth - width) / 2; // Центрирование текста по X.
            g.drawString(option, textX, startY + 39 + 67 * i); // Отрисовка текста опции.
        }
    }

    // Метод для обработки навигации вверх и вниз по опциям меню.
    public int upDown(InputManager inputManager, String[] options, int currentOption) {
        // Если нажата клавиша "вниз", увеличиваем текущую опцию.
        if (inputManager.getKeyDown(KeyEvent.VK_DOWN)) {
            onSound(); // Воспроизведение звука при выборе.
            currentOption++; // Переход к следующей опции.
            if (currentOption >= options.length) { // Если текущая опция выходит за пределы массива.
                return 0; // Возврат к первой опции.
            }
        }

        // Если нажата клавиша "вверх", уменьшаем текущую опцию.
        if (inputManager.getKeyDown(KeyEvent.VK_UP)) {
            onSound(); // Воспроизведение звука при выборе.
            currentOption--; // Переход к предыдущей опции.
            if (currentOption < 0) { // Если текущая опция меньше нуля.
                return options.length - 1; // Возврат к последней опции.
            }
        }
        return currentOption; // Возврат текущей опции.
    }

    // Метод для воспроизведения звука при взаимодействии с меню.
    public void onSound() {
        ResourceLoader.loadAudioClip("res/audio/Button.wav").start(); // Воспроизведение звука кнопки.
    }
}
