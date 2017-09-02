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
import com.badlogic.gdx.utils.TimeUtils;

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
    private TexturedNumber texturedScore;
    private int foodDone = 0;

    private final Texture foodTextures[];

    private final Texture stars[] = {
            new Texture(Gdx.files.internal("stars/yellow-star.png")),
            new Texture(Gdx.files.internal("stars/orange-star.png")),
            new Texture(Gdx.files.internal("stars/red-star.png")),
            new Texture(Gdx.files.internal("stars/grey-star.png"))
    };

    private final int levelNumber;
    private int starsScore[];
    private int levelTimer; // Timer for the level in seconds
    private int level[];

    private long lastSecond;

    FastDrawLevel(final FastDraw game, final int level[], final int levelNumber) {
        this.game = game;
        this.starsScore = new int[3];
        System.arraycopy(level, 0, starsScore, 0, 3);
        this.levelTimer = level[3];
        this.level = new int[level.length - 4];
        System.arraycopy(level, 4, this.level, 0, level.length - 4);
        this.levelNumber = levelNumber;

        texturedScore = new TexturedNumber();
        lastSecond = TimeUtils.nanoTime();

        Gdx.gl.glEnable(GL20.GL_BLEND);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FastDraw.width, FastDraw.height);

        food = new TexturedShape(foods[this.level[foodDone]], 300, 300);
        foodTextures = new Texture[foods.length];
        for (int i = 0; i < foods.length; i++) {
            foodTextures[i] = new Texture(Gdx.files.internal("food/" + foods[i] + ".png"));
        }

        shape = new Shape();
        currentPoint = new Vector3();

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (food.isWon()) {
            if (foodDone >= level.length - 1) {

                // TODO : show stars earned from score
                if (score < starsScore[0])  // Lost
                    lost();
                else                        // Won
                    won();

                game.setScreen(new LevelMenu(game));
                dispose();
            } else {
                foodDone++;
                food = new TexturedShape(foods[level[foodDone]], 300, 300);
            }
        }

        long now = TimeUtils.nanoTime();
        if (lastSecond + 1000000000L <= now) { // When a second has passed
            lastSecond = now;
            levelTimer--;
        }

        if (levelTimer == 0) { // Lost
            game.setScreen(new LevelMenu(game));
            dispose();
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        game.batch.begin();
        float charSize = 80;
        float margin = charSize*0.6f;
        texturedScore.draw(game.batch, 150, FastDraw.height - 100, charSize, margin);
        game.font.draw(game.batch, "Timer : " + levelTimer, 20, FastDraw.height - 60);

        // Draw the target food
        Sprite targetFoodSprite = new Sprite(foodTextures[level[foodDone]]);
        targetFoodSprite.setBounds(70, FastDraw.height*3/5, 150, 150);
        targetFoodSprite.draw(game.batch);

        // Draw the score bar stars
        int barWidth = 200;
        Sprite star;
        float x;
        for (int i = 2; i >= 0; i--) {
            x = 50 + barWidth * starsScore[i] / starsScore[2];
            if (score > starsScore[i])
                star = new Sprite(stars[i]);
            else
                star = new Sprite(stars[3]);
            star.setBounds(x, FastDraw.height-50, 30, 30);
            star.draw(game.batch);
        }
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

                float success = food.getPercentageOfSuccess(shape);

                if (success > 0.85f) {
                    if (success > 0.99f) {
                        food.winAnimation(); // TODO: perfect animation
                        score += 200;
                    } else if (success > 0.95f) {
                        food.winAnimation(); // TODO: great animation
                        score += 100;
                    } else {
                        food.winAnimation();
                        score += 50;
                    }

                    texturedScore.setNumber(score);
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
        food.dispose();

        for (Texture foodTexture : foodTextures)
            foodTexture.dispose();
    }

    private void lost() {
        System.err.println("You lost..");
    }

    private void won() {
        int starsEarned = 1;
        if (score >= starsScore[2])
            starsEarned++;
        if (score >= starsScore[1])
            starsEarned++;


        // key : value
        // levelNumber : starEarned score
        // 1 : 2 750
        Preferences prefs = Gdx.app.getPreferences("Fast Draw");
        String starsAndScore = prefs.getString(String.valueOf(levelNumber), "0 0");
        int bestScore = Integer.parseInt(starsAndScore.substring(2));
        if (bestScore < score) prefs.putString(String.valueOf(levelNumber), starsEarned + " " + score);
        prefs.flush();
    }
}