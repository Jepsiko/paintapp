package com.jepsiandco.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;

public class PaintApp extends ApplicationAdapter {

    private OrthographicCamera camera;
    private ImmediateModeRenderer20 renderer;
    private SpriteBatch batch;

    private Texture redTexture;

    private Shape shape;
    private Vector3 currentPoint;
    private boolean drawingShape = false;
	
	@Override
	public void create () {
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 480, 800);
        renderer = new ImmediateModeRenderer20(false, true, 1);
        batch = new SpriteBatch();

        redTexture = new Texture(Gdx.files.internal("red.png"));

		shape = new Shape();
		currentPoint = new Vector3();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.begin();
		batch.draw(redTexture, 10, 10); // With this line we can see the shape in red ????
		batch.end();

        shape.render(camera, renderer);

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
            if (drawingShape && shape.getSize() != 0) {
                drawingShape = false;
            }
        }
	}
	
	@Override
	public void dispose () {
	    renderer.dispose();
	}
}
