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
    private Sprite buttons[];

    private OrthographicCamera camera;

    MainMenu(final FastDraw game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);

        backgroundSprite = new Sprite(new Texture(Gdx.files.internal("background-screens/menu-screen.png")));
        backgroundSprite.setBounds(0, 0, FastDraw.width, FastDraw.height);

        final String buttonsNames[] = {
                "options",
                "play"
        };

        buttons = new Sprite[buttonsNames.length];

        int y = 100;
        final int margin = 20;
        final int buttonsWidth = 200;
        final int buttonsHeight = buttonsWidth / 3;

        for (int i = 0; i < buttonsNames.length; i++) {
            buttons[i] = new Sprite(new Texture(Gdx.files.internal("buttons/" + buttonsNames[i] + "-button.png")));
            buttons[i].setBounds((FastDraw.width - buttonsWidth)/2, y, buttonsWidth, buttonsHeight);

            y += buttonsHeight + margin;
        }

        Gdx.input.setCatchBackKey(false);
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

        for (Sprite button : buttons) {
            button.draw(game.batch);
        }

        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            int touchedButton = -1;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].getBoundingRectangle().contains(touchPos.x, touchPos.y))
                    touchedButton = i;
            }

            switch (touchedButton) {
                case 0: // Options
                    //TODO : options menu
                    dispose();
                    break;

                case 1: // Play
                    game.setScreen(new LevelMenu(game));
                    dispose();
                    break;
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
