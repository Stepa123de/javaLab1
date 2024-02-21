package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Target extends Circle {
    private double startX, startY;
    private boolean animationFlag = false;

    public Target(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.startX = centerX;
        this.startY = centerY;
        this.setFill(Color.WHITE);
        this.setStrokeWidth(2);
        this.setStroke(Color.BLACK);
    }

    public void toStart() {
        this.setCenterX(startX);
        this.setCenterY(startY);
        animationFlag = false;
    }

    public boolean getFlag() {
        return animationFlag;
    }

    public void changeFlag() {
        animationFlag = !animationFlag;
    }

    public boolean pointInsideCircle(double x, double y) {
        double distance = Math.sqrt(Math.pow(x - getCenterX(), 2) + Math.pow(y - getCenterY(), 2));
        return distance <= getRadius();
    }
}
