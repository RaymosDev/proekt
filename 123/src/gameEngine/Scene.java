package gameEngine;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    public List<SceneObject> objects;

    public Scene() {
	objects = new ArrayList<>();
    }

    public final void addObject(SceneObject object) {
	objects.add(object);
	object.initialize();
    }

    public final void removeObject(SceneObject object) {
	objects.remove(object);
	object.dispose();
    }

    public final List<SceneObject> getObjects() {
	return new ArrayList<>(objects);
    }

    public abstract void initialize();

    public void tick() {
	for (int i = 0; i < objects.size(); i++) {
	    SceneObject object = objects.get(i);
	    object.tick();
	}
    }

    public void render(Graphics g) {
	for (int i = 0; i < objects.size(); i++) {
	    SceneObject object = objects.get(i);
	    object.render(g);
	}
    }

    public void dispose() {
	for (int i = 0; i < objects.size(); i++) {
	    objects.get(i).dispose();
	}
    }
}
