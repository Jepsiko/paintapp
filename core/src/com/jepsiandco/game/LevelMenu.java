package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

class LevelMenu implements Screen {

    final FastDraw game;
    private boolean justEnteredScreen = true;

    private OrthographicCamera camera;
    private final Sprite playButton;

    /*
    0 : Cheese
    1 : Croissant
    2 : Pizza
    3 : Cheeseburger
    4 : Watermelon
    5 : Frenchstick
     */

    private final int levels[][] = { // The first number is the timer and the rest is the food index
            {100, 0, 1, 0, 1, 1, 0},
            {100, 1, 2, 0, 1, 0, 2, 1},
            {100, 3, 2, 4, 1, 3, 2, 5},
            {100, 5, 0, 1, 5, 5, 0, 1},
    };
    private TexturedNumber levelNumbers[];
    private int currentLevel;

    private float xScroll;
    private Vector3 lastPos;

    LevelMenu(final FastDraw game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);
        playButton = new Sprite(new Texture(Gdx.files.internal("buttons/play-button.png")));
        playButton.setBounds((FastDraw.width-300)/2, 50, 300, 100);

        levelNumbers = new TexturedNumber[levels.length];
        for (int i = 0; i < levelNumbers.length; i++) {
            levelNumbers[i] = new TexturedNumber(i+1);
        }
        currentLevel = 1;

        xScroll = 0;
        lastPos = new Vector3(0, 0, -1); // z is the flag value

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        final int charSize = 200;
        final int widthCenter = FastDraw.width / 2;

        for (int i = 0; i < levelNumbers.length; i++) {
            float x = (i+1) * widthCenter - xScroll;
            float diff = abs(widthCenter - x);
            if (diff < FastDraw.width) {
                float size = (widthCenter - diff * 0.4f) / (widthCenter) * charSize;
                float y = FastDraw.height / 2;
                levelNumbers[i].draw(game.batch, x, y, size);
            }
        }
        playButton.draw(game.batch);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            if (justEnteredScreen) return;

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (playButton.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                game.setScreen(new FastDrawLevel(game, levels[currentLevel]));
                dispose();

            } else if (lastPos.z != -1) {
                xScroll += lastPos.x - touchPos.x;
                if (xScroll < 0) xScroll = 0;
                else if (xScroll > widthCenter * (levels.length-1)) xScroll = widthCenter * (levels.length-1);
            }

            lastPos = touchPos;
        } else if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (justEnteredScreen) return;

            game.setScreen(new MainMenu(game));
            dispose();
        } else {
            justEnteredScreen = false;
            lastPos.z = -1;

            currentLevel = roundFloatToInt(xScroll / (widthCenter));

            int expectedXScroll = currentLevel * widthCenter;
            if (expectedXScroll < xScroll) {
                xScroll -= 4;
                if (expectedXScroll > xScroll)
                    xScroll = expectedXScroll;
            } else if (expectedXScroll > xScroll) {
                xScroll += 4;
                if (expectedXScroll < xScroll)
                    xScroll = expectedXScroll;
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
        for (TexturedNumber levelNumber: levelNumbers) {
            levelNumber.dispose();
        }
    }

    private int roundFloatToInt(float number) {
        if ((int) number + 0.5f > number) return (int) number;
        else return (int) number + 1;
    }

    private float abs(float x) {
        return x < 0 ? -x : x;
    }
}
