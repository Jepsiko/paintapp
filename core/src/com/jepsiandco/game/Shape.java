package com.jepsiandco.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class Shape {

    private Array<Vector3> shape;
    private static final float minDistBetweenPoints = 20;
    private static final int iterations = 2;

    private static final int minThickness = 1;
    private static final int thickness = 4;
    private static final int strokeThickness = 3;

    private static final int maxSize = 40;

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

    void render (OrthographicCamera camera, ImmediateModeRenderer20 renderer) {

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

        drawShape(true, outputShape, camera, renderer);
        drawShape(false, outputShape, camera, renderer);
    }

    private static void drawShape(boolean stroke, Array<Vector3> outputShape, OrthographicCamera camera,
                                  ImmediateModeRenderer20 renderer) {


        // Add thickness to the shape
        Array<Vector3> shapeCopy = new Array<Vector3>(outputShape);

        Vector3 previous = shapeCopy.get(shapeCopy.size-2);
        Vector3 current = shapeCopy.pop();
        Vector3 tmp = new Vector3(current).sub(previous).scl(10).add(previous);
        shapeCopy.add(tmp);

        previous = shapeCopy.first();
        int index = 1;
        for (int i = 1; i < outputShape.size-1; i++) {
            current = outputShape.get(i);
            tmp = new Vector3(current).sub(previous).nor();
            tmp.set(-tmp.y, tmp.x, 0);

            float scalar = thickness;
            if (stroke) scalar += strokeThickness;
            scalar = scalar * (i / (float) outputShape.size) + minThickness;

            tmp.scl(scalar);

            shapeCopy.removeIndex(index);
            shapeCopy.insert(index, new Vector3(previous).add(tmp));
            index++;
            shapeCopy.insert(index, new Vector3(previous).sub(tmp));
            index++;

            previous = current;
        }

        renderer.begin(camera.combined, GL20.GL_TRIANGLE_STRIP);

        for (Vector3 point : shapeCopy) {
            if (!stroke) renderer.color(1, 0, 0, 1);
            else renderer.color(new Color(0, 0, 1, 1));
            renderer.vertex(point.x, point.y, point.z);
        }

        renderer.end();
    }

    void clear () {
        shape.clear();
    }
}
