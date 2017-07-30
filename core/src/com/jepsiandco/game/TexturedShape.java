package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import static java.lang.Math.min;

class TexturedShape extends Shape {

    private Sprite sprite;
    private float widthTexture;
    private float heightTexture;
    private float widthScreen;
    private float heightScreen;

    private static final long animationTimeShape = 100000000L;
    private static final long animationTimeTexture = 500000000L;

    private long startTimeShape;
    private long startTimeTexture;

    private boolean winAnimating = false;
    private boolean looseAnimating = false;
    private boolean textureAnimating = false;

    private boolean winned = false;

    TexturedShape(String filename, int widthScreen, int heightScreen, int widthTexture, int heightTexture) {
        super(1);

        this.widthTexture = widthTexture;
        this.heightTexture = heightTexture;
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;

        initTextureDesign(filename);
        initShapeDesign();

        // initAnimation();
    }

    private void initTextureDesign(String filename) {
        sprite = new Sprite(new Texture(Gdx.files.internal(filename)));
        sprite.setAlpha(0);
        sprite.setBounds(widthScreen/2, heightScreen/2, 0, 0);
    }

    private void initShapeDesign() {
        setInnerColor(0.8f, 0.8f, 1, 1);
        setStrokeColor(0.5f, 0.5f, 0.7f, 1);
        setThickness(20);
        setStrokeThickness(10);
    }

    void textureAnimation() {
        textureAnimating = true;
        startTimeTexture = TimeUtils.nanoTime();
    }

    void winAnimation() {
        winAnimating = true;
        looseAnimating = false;
        startTimeShape = TimeUtils.nanoTime();
    }

    void looseAnimation() {
        looseAnimating = true;
        winAnimating = false;
        startTimeShape = TimeUtils.nanoTime();
    }

    void update() {
        long currentTimeShape = TimeUtils.timeSinceNanos(startTimeShape);
        if (winAnimating) {
            setInnerColor(0.5f, 1, 0.5f, 0, (float) currentTimeShape / (float) animationTimeShape);
            setStrokeColor(0.3f, 0.7f, 0.3f, 0, (float) currentTimeShape / (float) animationTimeShape);
            setThickness(20 + (float) currentTimeShape / (float) animationTimeShape * 40);
            setStrokeThickness(10 - (float) currentTimeShape / (float) animationTimeShape * 10);

            if (currentTimeShape > animationTimeShape) {
                winAnimating = false;
            }
        } else if (looseAnimating) {
            setInnerColor(1, 0.5f, 0.5f, 0, (float) currentTimeShape / (float) animationTimeShape);
            setStrokeColor(0.7f, 0.3f, 0.3f, 0, (float) currentTimeShape / (float) animationTimeShape);
            setThickness(20 + (float) currentTimeShape / (float) animationTimeShape * 40);
            setStrokeThickness(10 - (float) currentTimeShape / (float) animationTimeShape * 10);

            if (currentTimeShape > animationTimeShape) {
                looseAnimating = false;
                initShapeDesign();
            }
        }

        long currentTimeTexture = TimeUtils.timeSinceNanos(startTimeTexture);
        if (textureAnimating) {
            float coef = min((float) currentTimeTexture / (float) animationTimeTexture, 1);
            float width = widthTexture * coef;
            float height = heightTexture * coef;
            float x = (widthScreen-width)/2;
            float y = (heightScreen-height)/2;
            sprite.setBounds(x, y, width, height);
            sprite.setAlpha(coef);

            if (currentTimeTexture > animationTimeTexture) {
                textureAnimating = false;
                winned = true;
            }
        }
    }

    boolean isWinned () {
        return winned;
    }

    void render (SpriteBatch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    float getPercentageOfSuccess(Shape inputShape) {

        float distSq = inputShape.getThickness() + inputShape.getStrokeThickness();
        distSq *= distSq;

        boolean tooFar = true;
        float count = 0;
        for (Vector3 point : getShape()) {
            for (Vector3 pointShape: inputShape.getShape()) {
                if (point.dst2(pointShape) <= distSq*1.5) {
                    tooFar = false;
                    if (point.dst2(pointShape) <= distSq)  {
                        count ++;
                    }
                    break;
                }

            }

            if (tooFar) return 0;
        }

        return count / getShape().size;
    }
}