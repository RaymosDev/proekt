package gameEngine; // Объявление пакета, в котором находится класс ResourceLoader.

/*  Класс ResourceLoader предоставляет статические методы для загрузки различных 
    ресурсов, используемых в игре, таких как изображения, аудиоклипы и шрифты. 
    Он использует механизмы Java для работы с ресурсами, включая URL, 
    InputStream и AudioSystem. Методы обрабатывают возможные исключения и 
    обеспечивают регистрацию шрифтов в графической среде, а также преобразование 
    изображений в формат BufferedImage для удобства работы с ними в 
    графическом контексте.
*/

import java.awt.Font; // Импорт класса Font для работы со шрифтами.
import java.awt.Graphics2D; // Импорт класса Graphics2D для рисования изображений.
import java.awt.GraphicsEnvironment; // Импорт класса GraphicsEnvironment для регистрации шрифтов.
import java.awt.Image; // Импорт класса Image для работы с изображениями.
import java.awt.image.BufferedImage; // Импорт класса BufferedImage для работы с изображениями в памяти.
import java.io.InputStream; // Импорт класса InputStream для работы с потоками данных.
import java.net.URL; // Импорт класса URL для работы с адресами ресурсов.

import javax.imageio.ImageIO; // Импорт класса ImageIO для загрузки изображений.
import javax.sound.sampled.AudioInputStream; // Импорт класса AudioInputStream для работы с аудиопотоками.
import javax.sound.sampled.AudioSystem; // Импорт класса AudioSystem для работы с аудио.
import javax.sound.sampled.Clip; // Импорт класса Clip для воспроизведения звуковых клипов.
import javax.sound.sampled.LineEvent; // Импорт класса LineEvent для отслеживания событий воспроизведения.
import javax.sound.sampled.LineListener; // Импорт интерфейса LineListener для обработки событий линии.

public class ResourceLoader { // Объявление класса ResourceLoader.
    
    public static BufferedImage loadImage(String fileName) { // Метод для загрузки изображения.
        try {
            URL url = ResourceLoader.class.getClassLoader().getResource(fileName); // Получение URL ресурса.
            if (url == null) { // Проверка, найден ли ресурс.
                throw new IllegalArgumentException("Could not find the file specified"); // Исключение, если ресурс не найден.
            }
            return ImageIO.read(url); // Чтение изображения из URL и возврат его.
        } catch (Exception e) { // Обработка исключений.
            e.printStackTrace(); // Печать стека вызовов ошибки.
            return null; // Возврат null в случае ошибки.
        }
    }

    public static Clip loadAudioClip(String fileName) { // Метод для загрузки аудиоклипа.
        try {
            URL url = ResourceLoader.class.getClassLoader().getResource(fileName); // Получение URL ресурса.
            if (url == null) { // Проверка, найден ли ресурс.
                throw new IllegalArgumentException("Could not find the file specified"); // Исключение, если ресурс не найден.
            }
            Clip clip = AudioSystem.getClip(); // Создание нового аудиоклипа.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url); // Получение аудиопотока из URL.
            clip.addLineListener(new LineListener() { // Добавление слушателя для отслеживания событий воспроизведения.
                @Override
                public void update(LineEvent myLineEvent) { // Обработка события изменения состояния линии.
                    if (myLineEvent.getType() == LineEvent.Type.STOP) { // Если событие - остановка,
                        clip.close(); // закрываем клип.
                    }
                }
            });
            clip.open(audioInputStream); // Открытие клипа с аудиопотоком.
            audioInputStream.close(); // Закрытие аудиопотока.
            return clip; // Возврат загруженного клипа.
        } catch (Exception e) { // Обработка исключений.
            e.printStackTrace(); // Печать стека вызовов ошибки.
            return null; // Возврат null в случае ошибки.
        }
    }

    public static Font loadFont(String fileName, int fontSize) { // Метод для загрузки шрифта.
        float size = fontSize; // Приведение размера шрифта к типу float.
        try {
            URL url = ResourceLoader.class.getClassLoader().getResource(fileName); // Получение URL ресурса.
            if (url == null) { // Проверка, найден ли ресурс.
                throw new IllegalArgumentException("Could not find the file specified"); // Исключение, если ресурс не найден.
            }
            InputStream inputStream = url.openStream(); // Открытие потока для чтения шрифта.
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(size); // Создание шрифта из потока и установка размера.
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font); // Регистрация шрифта в графической среде.
            return font; // Возврат загруженного шрифта.
        } catch (Exception e) { // Обработка исключений.
            e.printStackTrace(); // Печать стека вызовов ошибки.
            return null; // Возврат null в случае ошибки.
        }
    }

    public static BufferedImage toBufferedImage(Image image) { // Метод для преобразования изображения в BufferedImage.
        if (image instanceof BufferedImage) { // Если изображение уже является BufferedImage,
            return (BufferedImage) image; // просто возвращаем его.
        }
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), // Создание нового BufferedImage.
                BufferedImage.TYPE_INT_ARGB); // Установка типа изображения.
        Graphics2D g = bufferedImage.createGraphics(); // Получение объекта Graphics2D для рисования.
        g.drawImage(image, 0, 0, null); // Рисование изображения на BufferedImage.
        g.dispose(); // Освобождение ресурсов графики.
        return bufferedImage; // Возврат преобразованного BufferedImage.
    }
}