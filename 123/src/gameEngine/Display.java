package gameEngine;

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

public class Display {
    private static final int NUM_BUFFERS = 3;

    private JFrame window;
    private Canvas canvas;

    public Display(String title, int width, int height, Image icon, KeyListener keyListener) {
        Dimension dimension = new Dimension(width, height);

        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(dimension);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setIconImage(icon);
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "Blank");
        window.getContentPane().setCursor(blankCursor);

        canvas = new Canvas();
        canvas.setPreferredSize(dimension);
        canvas.setMinimumSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setFocusable(false);

        window.add(canvas);
        window.pack();
        window.addKeyListener(keyListener);

        canvas.createBufferStrategy(NUM_BUFFERS);
    }

    public void resize(int width, int height) {
        Dimension newDimension = new Dimension(width, height);
        window.setSize(newDimension);
        canvas.setPreferredSize(newDimension);
        canvas.setMinimumSize(newDimension);
        canvas.setMaximumSize(newDimension);
        window.pack();
    }

    public BufferStrategy getBufferStrategy() {
        return canvas.getBufferStrategy();
    }

    public void open() {
        window.setVisible(true);
    }

    public void close() {
        window.dispose();
    }
}
