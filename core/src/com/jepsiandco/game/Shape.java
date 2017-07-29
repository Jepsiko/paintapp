package com.jepsiandco.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class Shape {

    private Array<Vector3> shape;
    private static final float minDistBetweenPoints = 20;
    private static final int iterations = 2;

    private static final int thickness = 7;
    private static final int strokeThickness = 4;

    private static final int maxSize = 50;

    Shape () {
        shape = new Array<Vector3>();
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
                if (shape.size >= maxSize) {
                    System.arraycopy(shape.items, 1, shape.items, 0, shape.size - 1);
                    shape.pop();
                }
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


        // Draw the outputShape
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1); // Stroke Color

        Vector3 current;
        Vector3 previous = outputShape.first();
        for (int i = 1; i < outputShape.size-1; i++) {
            current = outputShape.get(i);
            shapeRenderer.rectLine(previous.x, previous.y, current.x, current.y, strokeThickness + thickness);
            shapeRenderer.circle(previous.x, previous.y, (strokeThickness + thickness) / 2);
            previous = current;
        }
        shapeRenderer.circle(previous.x, previous.y, (strokeThickness + thickness) / 2);


        shapeRenderer.setColor(0.9f, 0.9f, 0.9f, 1); // Inner Color

        previous = outputShape.first();
        for (int i = 1; i < outputShape.size-1; i++) {
            current = outputShape.get(i);
            shapeRenderer.rectLine(previous.x, previous.y, current.x, current.y, thickness);
            shapeRenderer.circle(previous.x, previous.y, thickness / 2);
            previous = current;
        }
        shapeRenderer.circle(previous.x, previous.y, thickness / 2);

        shapeRenderer.end();
    }

    void clear () {
        shape.clear();
    }
}
