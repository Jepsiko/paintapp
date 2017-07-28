package com.jepsiandco.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class Shape {

    private Array<Vector3> shape;

    private float x;
    private float y;
    private float width;
    private float height;

    private static final int closeShapeThreashold = 20;

    Shape (Array<Vector3> line) {
        shape = new Array<Vector3>();
        for (Vector3 point : line) {
            shape.add(new Vector3(point.x, point.y, point.z));
        }

        x = line.first().x;
        for (Vector3 point : line) {
            if (point.x < x) x = point.x;
        }

        y = line.first().y;
        for (Vector3 point : line) {
            if (point.y < y) y = point.y;
        }

        width = line.first().x;
        for (Vector3 point : line) {
            if (point.x > width) width = point.x;
        }
        width -= x;

        height = line.first().y;
        for (Vector3 point : line) {
            if (point.y > height) height = point.y;
        }
        height -= y;

        final int margin = 3;
        x -= margin;
        y -= margin;
        width += margin *2;
        height += margin *2;

        if (shape.first().dst(shape.peek()) <= closeShapeThreashold)
            shape.add(new Vector3(shape.first().x, shape.first().y, 0));
    }

    static void drawShape(Array<Vector3> shape, ShapeRenderer shapeRenderer) {

        if (shape.size == 0) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        Iterator<Vector3> iter = shape.iterator();
        Vector3 previous = shape.first();
        Vector3 current;
        while (iter.hasNext()) {
            current = iter.next();

            shapeRenderer.line(current, previous);

            previous = current;
        }

        shapeRenderer.end();
    }

    void render(ShapeRenderer shapeRenderer) {
        drawShape(shape, shapeRenderer);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1);
        //shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }
}
