package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.File;

import static java.lang.Math.min;

class TexturedShape extends Shape {

    private Sprite sprite;

    private static final long animationTimeShape = 80000000L;
    private static final long animationTimeTexture = 500000000L;

    private long startTimeShape;
    private long startTimeTexture;

    private boolean winAnimating = false;
    private boolean looseAnimating = false;
    private boolean textureAnimating = false;

    TexturedShape(String filename, int width, int height) {
        super();
        sprite = new Sprite(new Texture(Gdx.files.internal(filename)));
        sprite.setAlpha(0);
        sprite.setBounds((Gdx.graphics.getWidth()-width)/2, (Gdx.graphics.getHeight()-height)/2, width, height);
        System.err.println(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());

        initShapeDesign();
    }

    private void initShapeDesign() {
        setInnerColor(0.8f, 1, 0.8f, 1);
        setStrokeColor(0.5f, 0.7f, 0.5f, 1);
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

            if (currentTimeShape > animationTimeShape) winAnimating = false;
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
            if (currentTimeTexture > animationTimeTexture) textureAnimating = false;

            final float alpha = min((float) currentTimeTexture / (float) animationTimeTexture, 1);
            sprite.setAlpha(alpha);
        }
    }

    void render (SpriteBatch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
}
