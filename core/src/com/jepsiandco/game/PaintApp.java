package com.jepsiandco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class PaintApp extends ApplicationAdapter { // TODO : rename to FastDraw

    private static final int width = 480;
    private static final int height = 800;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private TexturedShape food;

    private Shape shape;
    private Vector3 currentPoint;
    private boolean drawingShape = false;

    private float success = 0;
	
	@Override
	public void create () {
        Gdx.gl.glEnable(GL20.GL_BLEND);

	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();

        food = new TexturedShape("croissant.png", width, height, 300, 300);
        food.addPoints(ShapesFood.croissantShape);

		shape = new Shape();
		currentPoint = new Vector3();
	}

	@Override
	public void render () {
	    if (food.isWinned()) {
            food = new TexturedShape("croissant.png", width, height, 300, 300);
            food.addPoints(ShapesFood.croissantShape);
        }

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
        batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		batch.begin();
        font.draw(batch, "Success : " + success, width/2, height-100);
        batch.end();

		food.update();
		food.render(batch);
        food.render(shapeRenderer);

        shape.render(shapeRenderer);

        Vector3 touchPos = new Vector3();
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (!drawingShape) {
                System.err.println("new Vector3(" + (int)touchPos.x + ", " + (int)touchPos.y + ", 0),");
                drawingShape = true;
                shape.clear();
            }

            if (touchPos.x != currentPoint.x || touchPos.y != currentPoint.y) {
                shape.addPoint(touchPos);
                currentPoint.x = touchPos.x;
                currentPoint.y = touchPos.y;
            }

        } else {
            if (drawingShape && shape.getSize() > 0) {
                drawingShape = false;

                success = food.getPercentageOfSuccess(shape);

                if (success > 0.6f) {
                    if (success > 0.9f) {
                        food.winAnimation(); // TODO: perfect animation
                    } else if (success > 0.75f) {
                        food.winAnimation(); // TODO: great animation
                    } else {
                        food.winAnimation();
                    }

                    food.textureAnimation();

                } else {
                    food.looseAnimation();
                }

                shape.clear();
            }
        }
	}
	
	@Override
	public void dispose () {
	    batch.dispose();
	    shapeRenderer.dispose();
	}
}
