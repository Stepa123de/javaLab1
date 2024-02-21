package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class HelloApplication extends Application {
    private Pane pane;
    private Button restartButton, pauseButton, shotButton;
    private PoolElements poolBullets, poolTargets;
    private GameEngine gameEngine;
    private Label bulletCounter, bulletCounterTitle, scoreCounter, scoreCounterTitle;
    private int bulletFired;
    private ArrayList<Control> uiElements = new ArrayList<Control>();

    private Button createButton(String name, double posX, double posY, EventHandler<ActionEvent> btnAction) {
        Button button = new Button(name);
        button.setLayoutX(posX);
        button.setLayoutY(posY);
        button.setOnAction(btnAction);
        uiElements.add(button);

        return button;
    }

    private Label createLabel(String name, double posX, double posY) {
        Label label = new Label(name);
        label.setLayoutX(posX);
        label.setLayoutY(posY);
        uiElements.add(label);

        return label;
    }

    private Polygon createPolygon(Double[] dots) {
        Polygon player = new Polygon();
        player.getPoints().addAll(dots);

        return player;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Polygon player = createPolygon(new Double[]{
                50.0, 200.0,
                50.0, 300.0,
                100.0, 250.0});

        restartButton = createButton("Start", 10, 450, new RestartEvent());
        pauseButton = createButton("Pause", 90, 450, new PauseEvent());
        shotButton = createButton("Shot", 180, 450, new ShotEvent());

        bulletCounterTitle = createLabel("Bullets fired", 620, 0);
        bulletCounter = createLabel("0", 620, 20);
        scoreCounterTitle = createLabel("Score", 620, 40);
        scoreCounter = createLabel("0", 620, 60);

        pane = new Pane();
        pane.getChildren().addAll(uiElements);

        Scene scene = new Scene(pane, 700, 500);
        stage.setScene(scene);
        stage.show();

        poolBullets = new PoolElements(pane);
        poolTargets = new PoolElements(pane);
        poolTargets.add(new Target(500, 250, 20));
        poolTargets.add(new Target(600, 250, 10));
        gameEngine = new GameEngine("GameEngin", poolTargets, poolBullets, scoreCounter);
    }

    class RestartEvent implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            bulletFired = 0;
            bulletCounter.setText(Integer.toString(bulletFired));

            if (!gameEngine.isAlive()) {
                gameEngine.start();
            } else {
                gameEngine.restart();
                if (gameEngine.onPaused()) {
                    gameEngine.resumeThread();
                }
            }
        }
    }

    class PauseEvent implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (!gameEngine.isAlive()) return;
            if (!gameEngine.onPaused()) {
                gameEngine.stopThread();
            } else {
                gameEngine.resumeThread();
            }
        }
    }

    class ShotEvent implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (!gameEngine.isAlive()) return;
            if (gameEngine.onPaused()) return;
            Circle bullet = new Circle(100, 250, 2);
            bullet.setFill(Color.BLACK);
            poolBullets.add(bullet);
            bulletCounter.setText(Integer.toString(++bulletFired));
        }
    }

}