package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

class LevelMenu implements Screen {

    final FastDraw game;
    private boolean justEnteredScreen = true;

    private final int topologies[][] = {
            {0, 1, 0, 1},
            {1, 2, 0, 1, 0, 2, 1},
            {3, 2, 4, 1, 3, 2, 5},
    };

    LevelMenu(final FastDraw game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (Gdx.input.isTouched()) {
            if (justEnteredScreen) return;

            game.setScreen(new FastDrawLevel(game, topologies[1], 10));
            dispose();
        } else {
            justEnteredScreen = false;
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
