package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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
    private final Texture playButton;
    private final Texture stars[] = {
            new Texture(Gdx.files.internal("stars/yellow-star.png")),
            new Texture(Gdx.files.internal("stars/orange-star.png")),
            new Texture(Gdx.files.internal("stars/red-star.png")),
            new Texture(Gdx.files.internal("stars/grey-star.png"))
    };

    /*
    0 : Cheese
    1 : Croissant
    2 : Pizza
    3 : Cheeseburger
    4 : Watermelon
    5 : Frenchstick
     */

    // [1StarScore, 2StarsScore, 3StarsScore, levelTimer, [food]]
    private final int levels[][] = {
            {300, 500, 800, 50, 0, 1, 0, 1, 1, 0},
            {300, 500, 700, 45, 1, 2, 0, 1, 0, 2, 1},
            {300, 500, 700, 50, 3, 2, 4, 1, 3, 2, 5},
            {300, 500, 700, 30, 5, 0, 1, 5, 5, 0, 1},
            {300, 500, 700, 50, 0, 1, 2, 3, 4, 5},
    };
    private TexturedNumber levelNumbers[];
    private int currentLevel;

    private float xScroll;
    private Vector3 lastPos;

    LevelMenu(final FastDraw game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);
        playButton = new Texture(Gdx.files.internal("buttons/play-button.png"));

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
        Preferences prefs = Gdx.app.getPreferences("Fast Draw");
        final int charSize = 200;
        final int widthCenter = FastDraw.width / 2;

        for (int i = 0; i < levelNumbers.length; i++) {
            float x = (i+1) * widthCenter - xScroll;
            float diff = abs(widthCenter - x);
            if (diff < FastDraw.width) {
                float size = (widthCenter - diff * 0.4f) / (widthCenter) * charSize;
                float y = FastDraw.height / 2;

                levelNumbers[i].draw(game.batch, x, y, size);

                String starsAndScore = prefs.getString(String.valueOf(i), "0 0");

                //final int bestScore = Integer.parseInt(starsAndScore.substring(2));
                //if (bestScore > 0) game.font.draw(game.batch, "Best Score : " + bestScore, x, y - size*0.6f);


                // Draw the stars
                final int starsCount = Character.getNumericValue(starsAndScore.charAt(0));
                final int starSizes[] = {90, 110, 90};
                final float yOffsets[] = {0.5f, 0.6f, 0.5f};
                final float xOffsets[] = {0.8f, 0, -0.8f};
                final int angle = 20;

                Sprite star;
                float currentSize;
                for (int j = 2; j >= 0; j--) {
                    if (starsCount > j)
                        star = new Sprite(stars[j]);
                    else
                        star = new Sprite(stars[3]);
                    currentSize = size * starSizes[j] / charSize;

                    star.setBounds(x-currentSize/2 - currentSize*xOffsets[j],
                            y + size*yOffsets[j], currentSize, currentSize);
                    star.setOrigin(currentSize/2, currentSize/2);
                    star.rotate((-angle * (j-1)));
                    star.draw(game.batch);
                }
            }
        }
        Sprite playButtonSprite = new Sprite(playButton);
        playButtonSprite.setBounds((FastDraw.width-300)/2, 50, 300, 100);
        playButtonSprite.draw(game.batch);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            if (justEnteredScreen) return;

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (playButtonSprite.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                game.setScreen(new FastDrawLevel(game, levels[currentLevel], currentLevel));
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

        for (Texture star : stars) {
            star.dispose();
        }

        playButton.dispose();
    }

    private int roundFloatToInt(float number) {
        if ((int) number + 0.5f > number) return (int) number;
        else return (int) number + 1;
    }

    private float abs(float x) {
        return x < 0 ? -x : x;
    }
}
