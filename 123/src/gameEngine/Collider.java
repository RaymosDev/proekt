package gameEngine;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider {
    private static List<Collider> activeColliders = new ArrayList<>();

    private double minX, minY, maxX, maxY;
    private List<Collider> contacts;

    public Collider(double minX, double minY, double maxX, double maxY) {
	this.minX = minX;
	this.minY = minY;
	this.maxX = maxX;
	this.maxY = maxY;
	contacts = new ArrayList<>();
	activeColliders.add(this);
    }

    public Collider(Vector2 center, double width, double height) {
	this(center.getX() - width / 2.0, center.getY() - height / 2.0, center.getX() + width / 2.0,
		center.getY() + height / 2.0);
    }

    public void setActive(boolean active) {
	if (active) {
	    if (!activeColliders.contains(this)) {
		activeColliders.add(this);
		checkForCollision();
	    }
	} else {
	    activeColliders.remove(this);
	    for (int i = 0; i < contacts.size(); i++) {
		Collider contact = contacts.get(i);
		handleCollisionExit(contact);
	    }
	}
    }

    public double getMinX() {
	return minX;
    }

    public void setMinX(double minX) {
	this.minX = minX;
	checkForCollision();
    }

    public double getMinY() {
	return minY;
    }

    public void setMinY(double minY) {
	this.minY = minY;
	checkForCollision();
    }

    public double getMaxX() {
	return maxX;
    }

    public void setMaxX(double maxX) {
	this.maxX = maxX;
	checkForCollision();
    }

    public double getMaxY() {
	return maxY;
    }

    public void setMaxY(double maxY) {
	this.maxY = maxY;
	checkForCollision();
    }

    public Vector2 getMin() {
	return new Vector2(minX, minY);
    }

    public Vector2 getMax() {
	return new Vector2(maxX, maxY);
    }

    public Vector2 getCenter() {
	return new Vector2((minX + maxX) / 2.0, (minY + maxY) / 2.0);
    }

    public void setCenter(Vector2 center) {
	double semiWidth = (maxX - minX) / 2.0;
	double semiHeight = (maxY - minY) / 2.0;
	minX = center.getX() - semiWidth;
	minY = center.getY() - semiHeight;
	maxX = center.getX() + semiWidth;
	maxY = center.getY() + semiHeight;
	checkForCollision();
    }


    public Vector2 getDimensions() {
	return new Vector2(maxX - minX, maxY - minY);
    }


    public void setDimensions(Vector2 dimensions) {
	Vector2 center = getCenter();
	minX = center.getX() - dimensions.getX() / 2.0;
	minY = center.getY() - dimensions.getY() / 2.0;
	maxX = center.getX() + dimensions.getX() / 2.0;
	maxY = center.getY() + dimensions.getX() / 2.0;
	checkForCollision();
    }

    public boolean isContacting(Collider other) {
	return this.minX <= other.maxX && this.maxX >= other.minX && this.minY <= other.maxY && this.maxY >= other.minY;
    }

    public List<Collider> getContacts() {
	return new ArrayList<>(contacts);
    }

    private void checkForCollision() {
	for (int i = 0; i < activeColliders.size(); i++) {
	    Collider other = activeColliders.get(i);
	    if (other == this) {
		continue;
	    }
	    boolean collides = this.isContacting(other);
	    if (contacts.contains(other)) {
		if (!collides) {
		    handleCollisionExit(other);
		}
	    } else {
		if (collides) {
		    handleCollisionEnter(other);
		}
	    }
	}
    }

    private void handleCollisionEnter(Collider other) {
	this.contacts.add(other);
	other.contacts.add(this);
	this.onCollisionEnter(other);
	other.onCollisionEnter(this);
    }

    private void handleCollisionExit(Collider other) {
	this.contacts.remove(other);
	other.contacts.remove(this);
	this.onCollisionExit(other);
	other.onCollisionExit(this);
    }


    public abstract void onCollisionEnter(Collider other);

    public abstract void onCollisionExit(Collider other);
}
