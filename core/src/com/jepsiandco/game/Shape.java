package com.jepsiandco.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class Shape {

    private Array<Vector3> line;

    private float x;
    private float y;
    private float width;
    private float height;

    Shape (Array<Vector3> line) {
        this.line = new Array<Vector3>();
        for (Vector3 point : line) {
            this.line.add(new Vector3(point.x, point.y, point.z));
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
    }

    static void drawShape(Array<Vector3> shape, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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
        drawShape(line, shapeRenderer);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }
}
