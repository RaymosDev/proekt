package gameEngine;

public class Vector2 {
    public double x;
    public double y;

    public Vector2(double x, double y) {
	this.x = x;
	this.y = y;
    }

    public static Vector2 fromAngle(double angle, double magnitude) {
	double x = Math.cos(angle) * magnitude;
	double y = Math.sin(angle) * magnitude;
	return new Vector2(x, y);
    }

    public double getX() {
	return x;
    }

    public Vector2 setX(double x) {
	this.x = x;
	return this;
    }

    public double getY() {
	return y;
    }

    public Vector2 setY(double y) {
	this.y = y;
	return this;
    }

    public Vector2 add(Vector2 other) {
	return add(other.x, other.y);
    }

    public Vector2 add(double x, double y) {
	this.x += x;
	this.y += y;
	return this;
    }

    public Vector2 subtract(Vector2 other) {
	return subtract(other.x, other.y);
    }

    public Vector2 subtract(double x, double y) {
	this.x -= x;
	this.y -= y;
	return this;
    }

    public Vector2 multiply(double scalar) {
	x *= scalar;
	y *= scalar;
	return this;
    }

    public double magnitude() {
	return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize() {
	double magnitude = magnitude();
	x /= magnitude;
	y /= magnitude;
	return this;
    }

    public double dotProduct(Vector2 other) {
	return this.x * other.x + this.y * other.y;
    }

    public double angleBetween(Vector2 other) {
	return Math.acos(this.dotProduct(other) / (this.magnitude() * other.magnitude()));
    }

    public Vector2 rotate(double angle) {
	double sin = Math.sin(angle);
	double cos = Math.cos(angle);
	double newX = x * cos - y * sin;
	double newY = x * sin + y * cos;
	x = newX;
	y = newY;
	return this;
    }

    public Vector2 clone() {
	return new Vector2(x, y);
    }

    @Override
    public String toString() {
	return String.format("[%f, %f]", x, y);
    }
}
