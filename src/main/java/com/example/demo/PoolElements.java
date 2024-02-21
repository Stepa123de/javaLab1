package com.example.demo;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collection;

public class PoolElements extends ArrayList<Circle> {
    private Pane pane;

    PoolElements(Pane pane) {
        this.pane = pane;
    }

    @Override
    public boolean removeAll(Collection<?> elementsOnRemove) {
        pane.getChildren().removeAll(elementsOnRemove);
        return super.removeAll(elementsOnRemove);
    }

    @Override
    public boolean remove(Object elemntOnRemove) {
        pane.getChildren().remove(elemntOnRemove);
        return super.remove(elemntOnRemove);
    }

    @Override
    public boolean add(Circle element) {
        pane.getChildren().add(element);
        return super.add(element);
    }

    @Override
    public void clear() {
        pane.getChildren().removeAll(this);
        super.clear();
    }

}
