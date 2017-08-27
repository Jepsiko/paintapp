package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class MainMenu implements Screen {

    final FastDraw game;

    private final Sprite backgroundSprite;
    private final Sprite playButton;
    private final Sprite quitButton;

    private OrthographicCamera camera;

    MainMenu(final FastDraw game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);

        backgroundSprite = new Sprite(new Texture(Gdx.files.internal("background-screens/menu-screen.png")));
        backgroundSprite.setBounds(0, 0, FastDraw.width, FastDraw.height);

        playButton = new Sprite(new Texture(Gdx.files.internal("buttons/play-button.png")));
        playButton.setBounds((FastDraw.width - 300)/2, 200, 300, 100);

        quitButton = new Sprite(new Texture(Gdx.files.internal("buttons/quit-button.png")));
        quitButton.setBounds((FastDraw.width -300)/2, 50, 300, 100);
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
        backgroundSprite.draw(game.batch);
        playButton.draw(game.batch);
        quitButton.draw(game.batch);
        game.batch.end();

        Vector3 touchPos = new Vector3();
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (playButton.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                final int topology[] = {0, 1, 2, 3, 4, 5, 0};

                game.setScreen(new FastDrawLevel(game, topology, 10));
                dispose();
            } else if (quitButton.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                dispose();
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
