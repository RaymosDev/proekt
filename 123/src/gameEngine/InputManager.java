package gameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private static boolean[] keys;
    private Set<Integer> keysDownNow;
    private Set<Integer> keysDownNextUpdate;

    InputManager() {
	keys = new boolean[256];
	keysDownNow = new HashSet<>();
	keysDownNextUpdate = new HashSet<>();
    }

    KeyListener getKeyListener() {
	return new KeyListener() {
	    @Override
	    public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (!getKey(keyCode)) {
		    keysDownNextUpdate.add(keyCode);
		}
		keys[keyCode] = true;
	    }

	    @Override
	    public void keyReleased(KeyEvent event) {
		int keyCode = event.getKeyCode();
		keys[keyCode] = false;
	    }

	    @Override
	    public void keyTyped(KeyEvent event) {
	    }
	};
    }

    void tick() {
	keysDownNow.clear();
	keysDownNow.addAll(keysDownNextUpdate);
	keysDownNextUpdate.clear();
    }

    public boolean getKey(int keyCode) {
	return keys[keyCode];
    }

    public boolean getKeyDown(int keyCode) {
	return keysDownNow.contains(keyCode);
    }

    public double getHorizontalAxis() {
	double horizontal = 0.0;
	if (getKey(KeyEvent.VK_A) || getKey(KeyEvent.VK_LEFT)) {
	    horizontal--;
	}
	if (getKey(KeyEvent.VK_D) || getKey(KeyEvent.VK_RIGHT)) {
	    horizontal++;
	}
	return horizontal;
    }

    public double getVerticalAxis() {
	double vertical = 0.0;
	if (getKey(KeyEvent.VK_S) || getKey(KeyEvent.VK_DOWN)) {
	    vertical--;
	}
	if (getKey(KeyEvent.VK_W) || getKey(KeyEvent.VK_UP)) {
	    vertical++;
	}
	return vertical;
    }
}