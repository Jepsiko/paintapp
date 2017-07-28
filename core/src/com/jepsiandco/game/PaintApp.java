package com.jepsiandco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PaintApp extends ApplicationAdapter {

    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Array<Shape> shapes;
    private Array<Vector3> currentShape;
    private Vector3 currentPoint;
    private boolean drawingShape = false;
	
	@Override
	public void create () {
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 480, 800);
	    shapeRenderer = new ShapeRenderer();
	    shapeRenderer.setColor(0, 0, 0, 1);

	    shapes = new Array<Shape>();
		currentShape = new Array<Vector3>();
		currentPoint = new Vector3();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (drawingShape) {
            Shape.drawShape(currentShape, shapeRenderer);
        }

        for (Shape shape : shapes) {
            shape.render(shapeRenderer);
        }

        Vector3 touchPos = new Vector3();
        if (Gdx.input.isTouched()) {
            drawingShape = true;

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (touchPos.x != currentPoint.x || touchPos.y != currentPoint.y) {
                currentShape.add(touchPos);
                currentPoint.x = touchPos.x;
                currentPoint.y = touchPos.y;
            }

        } else {
            if (drawingShape) {
                drawingShape = false;

                // Save the shape
                System.out.println("New Shape created");
                shapes.add(new Shape(currentShape));
                currentShape.clear();
            }
        }
	}
	
	@Override
	public void dispose () {
	}
}
