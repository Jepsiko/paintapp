package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class TexturedNumber {

    private final int number;
    private Texture textures[];

    TexturedNumber(int number) {
        this.number = number;
        String numberString = String.valueOf(number);
        textures = new Texture[numberString.length()];

        for (int i = 0; i < numberString.length(); i++) {
            char numberChar = numberString.charAt(i);
            textures[i] = new Texture(Gdx.files.internal("numbers/" + numberChar + ".png"));
        }
    }

    void draw(SpriteBatch batch, float x, float y, float charSize) { // Draw the number centered in x and y
        String numberString = String.valueOf(number);
        x -= charSize/4 * (numberString.length()+1);
        y -= charSize/2;

        float margin = charSize/2;
        for (int i = 0; i < numberString.length(); i++) {
            Sprite numberSprite = new Sprite(textures[i]);
            numberSprite.setBounds(x, y, charSize, charSize);
            numberSprite.draw(batch);

            x += margin;
        }
    }

    void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
    }
}
