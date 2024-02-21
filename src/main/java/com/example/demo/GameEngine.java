package com.example.demo;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class GameEngine extends Thread {
    private final Object lock = new Object();
    private boolean isPaused = false;
    private PoolElements poolBullets;
    private PoolElements poolTargets;
    private int tarDelta = 2, score = 0;
    private Label scoreCounter;

    public GameEngine(String name, PoolElements poolTargets, PoolElements poolBullets, Label scoreCounter) {
        super(name);
        this.poolBullets = poolBullets;
        this.poolTargets = poolTargets;
        this.scoreCounter = scoreCounter;
    }

    private void moveElement(Circle circle, double directionX, double directionY) {
        circle.setCenterX(circle.getCenterX() + directionX);
        circle.setCenterY(circle.getCenterY() + directionY);
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                while (isPaused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    sleep(10);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            int index = 1;

                            for (Circle circle : poolTargets) {
                                Target target = (Target) circle;
                                int directionY = target.getFlag() ? -tarDelta * index : +tarDelta * index;
                                moveElement(target, 0, directionY);

                                if (target.isOutOfBounce()) {
                                    target.changeFlag();
                                }

                                index += 1;
                            }

                            ArrayList<Circle> bulletToRemove = new ArrayList<Circle>();
                            for (Circle bullet : poolBullets) {
                                moveElement( bullet, 5, 0);
                                
                                if (bullet.getCenterX() + bullet.getRadius() > bullet.getScene().getWidth()) {
                                    bulletToRemove.add(bullet);
                                } else {
                                    index = 1;
                                    for (Circle circle : poolTargets) {
                                        Target target = (Target) circle;
                                        if (target.pointInsideCircle(bullet.getCenterX(), bullet.getCenterY())) {
                                            bulletToRemove.add(bullet);
                                            score += index;
                                            scoreCounter.setText(Integer.toString(score));
                                        }
                                        index++;
                                    }
                                }
                            }
                            poolBullets.removeAll(bulletToRemove);
                        }
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void stopThread() {
        isPaused = true;
    }

    public void resumeThread() {
        synchronized (lock) {
            isPaused = false;
            lock.notify();
        }
    }

    public boolean onPaused() {
        return isPaused;
    }

    public void restart() {
        if (!poolBullets.isEmpty()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    poolBullets.clear();
                }
            });
        }
        for (Circle circle : poolTargets) {
            Target target = (Target) circle;
            target.toStart();
        }
        score = 0;
        scoreCounter.setText(Integer.toString(score));
    }
}
