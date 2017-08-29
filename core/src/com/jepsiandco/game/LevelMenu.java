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

    private final int levels[][] = {
            {0, 1, 0, 1},
            {1, 2, 0, 1, 0, 2, 1},
            {3, 2, 4, 1, 3, 2, 5},
    };
    private TexturedNumber levelNumbers[];
    private int currentLevel;
    private float xScroll;

    LevelMenu(final FastDraw game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);
        playButton = new Sprite(new Texture(Gdx.files.internal("buttons/play-button.png")));
        playButton.setBounds((480-300)/2, 50, 300, 100);

        levelNumbers = new TexturedNumber[levels.length];
        for (int i = 0; i < levelNumbers.length; i++) {
            levelNumbers[i] = new TexturedNumber(i+1);
        }
        currentLevel = 1;
        xScroll = 0;

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

        for (int i = 0; i < levelNumbers.length; i++) {
            float x = (i+1) * FastDraw.width / 2 - xScroll;
            float diff = abs(FastDraw.width/2 - x);
            if (diff < FastDraw.width) {
                float size = charSize - 0.5f * diff;
                x -= size / 2;
                float y = (FastDraw.height - size) / 2;
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
                game.setScreen(new FastDrawLevel(game, levels[currentLevel], 100));
            }

            else if (touchPos.x < FastDraw.width/2) {
                xScroll -= 3;
                if (xScroll <= 0)
                    xScroll = 0;
            }

            else {
                xScroll += 3;
                if (xScroll >= FastDraw.width / 2 * (levelNumbers.length-1))
                    xScroll = FastDraw.width / 2 * (levelNumbers.length-1);
            }

            dispose();
        } else if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (justEnteredScreen) return;

            game.setScreen(new MainMenu(game));
            dispose();
        } else {
            justEnteredScreen = false;

            currentLevel = roundFloatToInt(xScroll / (FastDraw.width/2));

            int expectedXScroll = currentLevel * FastDraw.width/2;
            if (expectedXScroll < xScroll) {
                xScroll -= 3;
                if (expectedXScroll > xScroll)
                    xScroll = expectedXScroll;
            } else if (expectedXScroll > xScroll) {
                xScroll += 3;
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

    }

    private int roundFloatToInt(float number) {
        if ((int) number + 0.5f > number) return (int) number;
        else return (int) number + 1;
    }

    private float abs(float x) {
        return x < 0 ? -x : x;
    }
}
