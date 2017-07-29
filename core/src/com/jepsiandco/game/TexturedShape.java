package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import static java.lang.Math.min;

class TexturedShape extends Shape {

    private Sprite sprite;

    private static final long animationTimeShape = 50000000L;
    private long startTimeShape;
    private static final long animationTimeTexture = 500000000L;
    private long startTimeTexture;

    private boolean winAnimating = false;
    private boolean textureAnimating = false;

    TexturedShape(String filename, int width, int height) {
        super();
        sprite = new Sprite(new Texture(Gdx.files.internal(filename)));
        sprite.setAlpha(0);
        sprite.setBounds((Gdx.graphics.getWidth()-width)/2, (Gdx.graphics.getHeight()-height)/2, width, height);

        initShape();
    }

    private void initShape() {
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
        initShape();
        winAnimating = true;
        startTimeShape = TimeUtils.nanoTime();
    }

    void update() {
        long currentTimeShape = TimeUtils.timeSinceNanos(startTimeShape);
        if (winAnimating) {
            if (currentTimeShape > animationTimeShape) winAnimating = false;

            setInnerColor(0.5f, 1, 0.5f, 0, (float) currentTimeShape / (float) animationTimeShape);
            setStrokeColor(0.3f, 0.7f, 0.3f, 0, (float) currentTimeShape / (float) animationTimeShape);
            setThickness(20 + (float) currentTimeShape / (float) animationTimeShape * 40);
            setStrokeThickness(10 - (float) currentTimeShape / (float) animationTimeShape * 10);
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
