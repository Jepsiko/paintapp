package com.jepsiandco.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class Shape {

    private Array<Vector3> shape;
    private static final float minDistBetweenPoints = 25;
    private static final int iterations = 2;

    Shape () {
        shape = new Array<Vector3>();
    }

    Shape (Shape shapeToCopy) {
        shape = new Array<Vector3>(shapeToCopy.shape);
    }

    int getSize () {
        return shape.size;
    }

    void addPoint (Vector3 point) {
        if (shape.size == 0) shape.add(point);
        else {
            float lenSq = shape.peek().dst2(point);

            // Simplify the path
            final float minDistanceSq = minDistBetweenPoints * minDistBetweenPoints;
            if (lenSq >= minDistanceSq) {
                shape.add(point);
            }
        }
    }

    private static void smooth(Array<Vector3> input, Array<Vector3> output) {
        output.clear();
        output.add(input.first());

        for (int i = 0; i < input.size-1; i++) {
            Vector3 p0 = input.get(i);
            Vector3 p1 = input.get(i+1);

            Vector3 Q = new Vector3(0.75f*p0.x + 0.25f*p1.x, 0.75f*p0.y + 0.25f*p1.y, 0);
            Vector3 R = new Vector3(0.25f*p0.x + 0.75f*p1.x, 0.25f*p0.y + 0.75f*p1.y, 0);

            output.add(Q);
            output.add(R);
        }

        output.add(input.peek());
    }

    void render (ShapeRenderer shapeRenderer) {

        if (shape.size == 0) return;

        // Smooth the shape
        Array<Vector3> outputShape = new Array<Vector3>();

        if (iterations <= 0) {
            outputShape.addAll(shape);
        } else if (iterations == 1) {
            smooth(shape, outputShape);
        } else {
            int iters = iterations;
            Array<Vector3> shapeCopy = new Array<Vector3>(shape);

            do {
                smooth(shapeCopy, outputShape);
                shapeCopy = new Array<Vector3>(outputShape);

            } while (--iters > 0);
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);

        Vector3 previous = outputShape.first();
        Vector3 current;

        Iterator<Vector3> iter = outputShape.iterator();
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
