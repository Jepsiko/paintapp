package com.jepsiandco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class PaintApp extends ApplicationAdapter {

    private static final int width = 480;
    private static final int height = 800;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private TexturedShape food;

    private Shape shape;
    private Vector3 currentPoint;
    private boolean drawingShape = false;
	
	@Override
	public void create () {
        Gdx.gl.glEnable(GL20.GL_BLEND);

	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        food = new TexturedShape("croissant.png", 300, 300);

        food.addPoint(new Vector3(100, 100, 0));
        food.addPoint(new Vector3(100, 200, 0));
        food.addPoint(new Vector3(200, 200, 0));
        food.addPoint(new Vector3(200, 100, 0));

		shape = new Shape();
		currentPoint = new Vector3();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
        batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		food.update();

		food.render(batch);

        food.render(shapeRenderer);
        shape.render(shapeRenderer);

        Vector3 touchPos = new Vector3();
        if (Gdx.input.isTouched()) {
            if (!drawingShape) {
                drawingShape = true;
                shape.clear();
            }

            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (touchPos.x != currentPoint.x || touchPos.y != currentPoint.y) {
                shape.addPoint(touchPos);
                currentPoint.x = touchPos.x;
                currentPoint.y = touchPos.y;
            }

        } else {
            if (drawingShape && shape.getSize() > 0) {
                drawingShape = false;
                food.winAnimation();
                food.textureAnimation();

            }
        }
	}
	
	@Override
	public void dispose () {
	    batch.dispose();
	    shapeRenderer.dispose();
	}
}
