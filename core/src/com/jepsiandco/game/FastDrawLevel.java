package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class FastDrawLevel implements Screen {

    final FastDraw game;
    private boolean justEnteredScreen = true;

    private OrthographicCamera camera;

    private static final String[] foods = {
            "cheese",
            "croissant",
            "pizza",
            "cheeseburger",
            "watermelon",
            "frenchstick",
    };

    private TexturedShape food;

    private Shape shape;
    private Vector3 currentPoint;
    private boolean drawingShape = false;

    private int score = 0;
    private int foodDone = 0;
    private int topology[];
    private final int levelTimer; // Timer for the level in seconds //TODO : use the timer

    FastDrawLevel(final FastDraw game, final int topology[]) {
        this.game = game;
        this.topology = new int[topology.length-1];
        System.arraycopy(topology, 1, this.topology, 0, topology.length-1);
        this.levelTimer = topology[0];

        Gdx.gl.glEnable(GL20.GL_BLEND);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);

        food = new TexturedShape(foods[this.topology[foodDone]], 300, 300);

        shape = new Shape();
        currentPoint = new Vector3();

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (food.isWinned()) {
            food.dispose();
            if (foodDone >= topology.length - 1) {
                game.setScreen(new LevelMenu(game));
                dispose();
            } else {
                foodDone++;
                food = new TexturedShape(foods[topology[foodDone]], 300, 300);
            }
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Score : " + score, 20, FastDraw.height-20);
        game.batch.end();

        food.update();  // TODO : make a thread for each animation and try to not call update anymore
        // Like Playing a music for example
        food.render(game.batch);
        food.render(game.shapeRenderer);

        shape.render(game.shapeRenderer);

        if (Gdx.input.isTouched()) {
            if (justEnteredScreen) return;

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (!drawingShape) {
                drawingShape = true;
                shape.clear();
            }

            if (touchPos.x != currentPoint.x || touchPos.y != currentPoint.y) {
                shape.addPoint(touchPos);
                currentPoint.x = touchPos.x;
                currentPoint.y = touchPos.y;
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (justEnteredScreen) return;

            game.setScreen(new LevelMenu(game));
            dispose();
        } else {
            justEnteredScreen = false;

            if (drawingShape && shape.getSize() > 0) {
                drawingShape = false;

                float success = food.getPercentageOfSuccess(shape); // TODO : improve this function

                if (success > 0.6f) {
                    if (success > 0.9f) {
                        food.winAnimation(); // TODO: perfect animation
                        score += 200;
                    } else if (success > 0.75f) {
                        food.winAnimation(); // TODO: great animation
                        score += 100;
                    } else {
                        food.winAnimation();
                        score += 50;
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
