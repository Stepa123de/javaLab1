package com.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    private Pane pane;
    private Button restartButton, pauseButton, shotButton;
    private PoolElements poolBullets, poolTargets;
    private GameEngine gameEngine;
    private Label bulletCounter, bulletCounterTitle, scoreCounter, scoreCounterTitle;
    private int bulletFired;

    @Override
    public void start(Stage stage) throws IOException {
        Polygon player = new Polygon();
        player.getPoints().addAll(new Double[]{
                50.0, 200.0,
                50.0, 300.0,
                100.0, 250.0});
        restartButton = new Button("Start");
        restartButton.setOnAction(new RestartEvent());
        restartButton.setLayoutX(10);
        restartButton.setLayoutY(450);
        pauseButton = new Button("Pause");
        pauseButton.setOnAction(new PauseEvent());
        pauseButton.setLayoutX(90);
        pauseButton.setLayoutY(450);
        shotButton = new Button("Shot");
        shotButton.setOnAction(new ShotEvent());
        shotButton.setLayoutX(180);
        shotButton.setLayoutY(450);

        bulletCounterTitle = new Label("Bullets fired");
        bulletCounterTitle.setLayoutX(620);
        bulletCounterTitle.setLayoutY(0);
        bulletCounter = new Label("0");
        bulletCounter.setLayoutX(620);
        bulletCounter.setLayoutY(20);
        scoreCounterTitle = new Label("Score");
        scoreCounterTitle.setLayoutX(620);
        scoreCounterTitle.setLayoutY(40);
        scoreCounter = new Label("0");
        scoreCounter.setLayoutX(620);
        scoreCounter.setLayoutY(60);

        pane = new Pane();
        pane.getChildren().add(player);
        pane.getChildren().add(restartButton);
        pane.getChildren().add(pauseButton);
        pane.getChildren().add(shotButton);
        pane.getChildren().add(bulletCounterTitle);
        pane.getChildren().add(bulletCounter);
        pane.getChildren().add(scoreCounterTitle);
        pane.getChildren().add(scoreCounter);
        Scene scene = new Scene(pane, 700, 500);
        stage.setScene(scene);
        stage.show();

        poolBullets = new PoolElements(pane);
        poolTargets = new PoolElements(pane);
        Target nearTarget = new Target(500, 250, 20);
        Target farTarget = new Target(600, 250, 10);
        poolTargets.add(nearTarget);
        poolTargets.add(farTarget);
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