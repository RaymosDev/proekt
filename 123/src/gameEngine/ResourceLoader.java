package gameEngine; 

/*  
*   Тут методы для загрузки и обработки ресурсов для игры (картинки, аудио, шрифты, ...)
*/

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream; 
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent; 
import javax.sound.sampled.LineListener; 

public class ResourceLoader 
{ 
    
    public static BufferedImage loadImage(String fileName) 
    { 
        try 
        {
            URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
            if (url == null) 
            {
                throw new IllegalArgumentException("картинка не найдена");
            }
            return ImageIO.read(url); // возврат картинки
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null; 
        }
    }

    public static Clip loadAudioClip(String fileName) 
    {
        try 
        {
            URL url = ResourceLoader.class.getClassLoader().getResource(fileName); 
            if (url == null) 
            { 
                throw new IllegalArgumentException("аудио не найдено"); 
            }
            Clip clip = AudioSystem.getClip(); 
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip.addLineListener(new LineListener() 
            {
                @Override
                public void update(LineEvent myLineEvent) // Обработка события изменения состояния линии.
                { 
                    if (myLineEvent.getType() == LineEvent.Type.STOP) // Если событие - остановка,
                    { 
                        clip.close(); // закрываем клип
                    }
                }
            });
            clip.open(audioInputStream); // Открытие клипа с аудиопотоком
            audioInputStream.close(); // Закрытие аудиопотока
            return clip; // Возврат загруженного клипа
        } 
        catch (Exception e) 
        { 
            e.printStackTrace();
            return null; 
        }
    }

    public static Font loadFont(String fileName, int fontSize) 
    {
        float size = fontSize;
        try 
        {
            URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
            if (url == null) 
            { 
                throw new IllegalArgumentException("шрифт не найден");
            }
            InputStream inputStream = url.openStream(); // Открытие потока для чтения шрифта
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(size); // Создание шрифта из потока и установка размера
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font); // Регистрация шрифта в графической среде (что бы это не значило)
            return font; 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для преобразования изображения в BufferedImage
    public static BufferedImage toBufferedImage(Image image) 
    { 
        if (image instanceof BufferedImage) // если уже BufferedImage
        { 
            return (BufferedImage) image; // то просто возвращаем его
        }
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB); 
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null); // Рисование изображения на BufferedImage
        g.dispose();
        return bufferedImage;
    }
}