package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class Shape {

    private Array<Vector3> shape;
    private static final float minDistBetweenPoints = 20;
    private final int iterations;

    private static final int maxSize = 50;

    // Graphic
    private float thickness = 7;
    private float strokeThickness = 4;

    private final Color innerColor = new Color(0.9f, 0.9f, 0.9f, 1);
    private final Color strokeColor = new Color(0.8f, 0.8f, 0.8f, 1);

    Shape () {
        this(2);
    }

    Shape (int iterations) {
        shape = new Array<Vector3>();
        this.iterations = iterations;
    }

    void setThickness (float thickness) {
        this.thickness = thickness;
    }

    void setStrokeThickness (float strokeThickness) {
        this.strokeThickness = strokeThickness;
    }

    void setInnerColor (float r, float g, float b, float a) {
        setInnerColor(r, g, b, a, 1);
    }

    void setInnerColor (float r, float g, float b, float a, float t) {
        if (r < 0 || r > 1 || g < 0 || g > 1 || b < 0 || b > 1 || a < 0 || a > 1) return;
        innerColor.lerp(r, g, b, a, t);
    }

    void setStrokeColor (float r, float g, float b, float a) {
        setStrokeColor(r, g, b, a, 1);
    }

    void setStrokeColor (float r, float g, float b, float a, float t) {
        if (r < 0 || r > 1 || g < 0 || g > 1 || b < 0 || b > 1 || a < 0 || a > 1) return;
        strokeColor.lerp(r, g, b, a, t);
    }

    int getSize () {
        return shape.size;
    }

    float getThickness () {
        return thickness;
    }

    float getStrokeThickness () {
        return strokeThickness;
    }

    Array<Vector3> getShape () {
        return shape;
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

                //System.err.println("new Vector3(" + (int)point.x + ", " + (int)point.y + ", 0),"); // For making the shape
                shape.add(point);
            }
        }
    }

    void addPoints(Vector3[] points) {
        for (Vector3 point : points) {
            shape.add(point);
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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(strokeColor);

        Vector3 current;
        Vector3 previous = outputShape.first();
        for (int i = 1; i < outputShape.size; i++) {
            current = outputShape.get(i);
            shapeRenderer.rectLine(previous.x, previous.y, current.x, current.y, strokeThickness + thickness);
            shapeRenderer.circle(previous.x, previous.y, (strokeThickness + thickness) / 2);
            previous = current;
        }
        shapeRenderer.circle(previous.x, previous.y, (strokeThickness + thickness) / 2);


        shapeRenderer.setColor(innerColor);

        previous = outputShape.first();
        for (int i = 1; i < outputShape.size; i++) {
            current = outputShape.get(i);
            shapeRenderer.rectLine(previous.x, previous.y, current.x, current.y, thickness);
            shapeRenderer.circle(previous.x, previous.y, thickness / 2);
            previous = current;
        }
        shapeRenderer.circle(previous.x, previous.y, thickness / 2);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    void clear () {
        shape.clear();
    }
}
