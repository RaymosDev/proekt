package gameEngine;

import java.awt.Graphics;

public abstract class SceneObject {

    public abstract void initialize();

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract void dispose();
}
