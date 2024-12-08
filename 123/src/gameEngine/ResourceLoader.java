package gameEngine;

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

public class ResourceLoader {
    
    public static BufferedImage loadImage(String fileName) {
	try {
	    URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
	    if (url == null) {
		throw new IllegalArgumentException("Could not find the file specified");
	    }
	    return ImageIO.read(url);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Clip loadAudioClip(String fileName) {
	try {
	    URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
	    if (url == null) {
		throw new IllegalArgumentException("Could not find the file specified");
	    }
	    Clip clip = AudioSystem.getClip();
	    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
	    clip.addLineListener(new LineListener() {
		@Override
		public void update(LineEvent myLineEvent) {
		    if (myLineEvent.getType() == LineEvent.Type.STOP) {
			clip.close();
		    }
		}
	    });
	    clip.open(audioInputStream);
	    audioInputStream.close();
	    return clip;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Font loadFont(String fileName, int fontSize) {

	float size = fontSize;
	try {
	    URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
	    if (url == null) {
		throw new IllegalArgumentException("Could not find the file specified");
	    }
	    InputStream inputStream = url.openStream();
	    Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(size);
	    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
	    return font;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static BufferedImage toBufferedImage(Image image) {
	if (image instanceof BufferedImage) {
	    return (BufferedImage) image;
	}
	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
		BufferedImage.TYPE_INT_ARGB);
	Graphics2D g = bufferedImage.createGraphics();
	g.drawImage(image, 0, 0, null);
	g.dispose();
	return bufferedImage;
    }
}
