package com.jepsiandco.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class Shape {

    private Array<Vector3> shape;
    private static final float minDistBetweenPoints = 25;

    Shape () {
        shape = new Array<Vector3>();
    }

    Shape (Shape shapeToCopy) {
        shape.addAll(shapeToCopy.shape);
    }

    int getSize () {
        return shape.size;
    }

    void addPoint (Vector3 point) {
        if (shape.size == 0) shape.add(point);
        else {
            float lenSq = shape.peek().dst2(point);

            final float minDistanceSq = minDistBetweenPoints * minDistBetweenPoints;
            if (lenSq >= minDistanceSq) {
                shape.add(point);
            }
        }
    }

    void render (ShapeRenderer shapeRenderer) {

        if (shape.size == 0) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);

        Vector3 previous = shape.first();
        Vector3 current;

        Iterator<Vector3> iter = shape.iterator();
        iter.next();
        while (iter.hasNext()) {
            current = iter.next();

            shapeRenderer.line(current, previous);

            previous = current;
        }

        shapeRenderer.end();
    }

    void clear () {
        shape.clear();
    }
}
