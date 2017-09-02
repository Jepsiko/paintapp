package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import static java.lang.Math.min;

class TexturedShape extends Shape {

    private Texture texture;
    private Sprite sprite;
    private float widthTexture;
    private float heightTexture;

    private static final long animationTimeShape = 100000000L;
    private static final long animationTimeTexture = 500000000L;

    private long startTimeShape;
    private long startTimeTexture;

    private boolean winAnimating = false;
    private boolean looseAnimating = false;
    private boolean textureAnimating = false;

    private boolean won = false;

    TexturedShape(String filename, int widthTexture, int heightTexture) {
        super(1);

        this.widthTexture = widthTexture;
        this.heightTexture = heightTexture;

        initTextureDesign(filename);
        initShape(filename);
        initShapeDesign();

        // initAnimation();
    }

    private void initTextureDesign(String filename) {
        texture = new Texture(Gdx.files.internal("food/" + filename + ".png"));
        sprite = new Sprite(texture);
        sprite.setAlpha(0);
    }

    private void initShape(String filename) {
        if (filename.equals("cheese")) {
            addPoints(ShapesFood.cheeseShape);
        } else if (filename.equals("croissant")) {
            addPoints(ShapesFood.croissantShape);
        } else if (filename.equals("pizza")) {
            addPoints(ShapesFood.pizzaShape);
        } else if (filename.equals("cheeseburger")) {
            addPoints(ShapesFood.cheeseburgerShape);
        } else if (filename.equals("watermelon")) {
            addPoints(ShapesFood.watermelonShape);
        } else if (filename.equals("frenchstick")) {
            addPoints(ShapesFood.frenchstickShape);
        }
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
            float initialPercentSize = 0.4f; // Value must be between 0 and 1

            float coef = min((float) currentTimeTexture / (float) animationTimeTexture, 1);
            float width = widthTexture * (coef * (1 - initialPercentSize) + initialPercentSize);
            float height = heightTexture * (coef * (1 - initialPercentSize) + initialPercentSize);
            float x = (FastDraw.width - width) / 2;
            float y = (FastDraw.height - height) / 2 - 100;
            sprite.setBounds(x, y, width, height);
            sprite.setAlpha(coef);

            if (currentTimeTexture > animationTimeTexture) {
                textureAnimating = false;
                won = true;
            }
        }
    }

    boolean isWon() {
        return won;
    }

    void render(SpriteBatch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    float getPercentageOfSuccess(Shape inputShape) {

        float count = 0;
        float thickness = getThickness();

        for (Vector3 point : inputShape.getShape()) // If we draw too far from the shape, it doesn't count
            if (!isPointInShape(point, this, thickness*2)) return 0;

        for (Vector3 point : getShape())
            if (isPointInShape(point, inputShape, thickness)) count++;

        return count / getShape().size;
    }

    void dispose() {
        texture.dispose();
    }

    private boolean isPointInShape(Vector3 point, Shape shape, float thickness) {
        Vector3 current;
        Vector3 previous = shape.getShape().first();
        for (int i = 1; i < shape.getShape().size; i++) {
            current = shape.getShape().get(i);

            if (isPointInRectLine(point, previous.x, previous.y, current.x, current.y, thickness)) return true;

            previous = current;
        }

        return false;
    }

    private static boolean isPointInRectLine(Vector3 point, float x1, float y1, float x2, float y2, float thickness) {
        Vector3 a = new Vector3(x1, y1, 0);
        Vector3 b = new Vector3(x2, y2, 0);
        return distToLineSquared(point, a, b) <= thickness * thickness;
    }

    private static float distToLineSquared(Vector3 p, Vector3 a, Vector3 b) {
        float lineDist = a.dst2(b);
        if (lineDist == 0) return p.dst2(a); // If 'a' and 'b' are the same point

        float t = ((p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y)) / lineDist;
        t = Math.min(Math.max(t, 0), 1);

        return p.dst2(a.x + t * (b.x - a.x), a.y + t * (b.y - a.y), 0);
    }
}
