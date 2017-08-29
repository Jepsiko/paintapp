package com.jepsiandco.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class TexturedNumber {

    private final int number;

    TexturedNumber(int number) {
        this.number = number;
    }

    void draw(SpriteBatch batch, float x, float y, float charSize) {
        String numberString = String.valueOf(number);

        float margin = charSize/4*3;
        for (int i = 0; i < numberString.length(); i++) {
            char numberChar = numberString.charAt(i);

            Sprite numberSprite = new Sprite(new Texture(Gdx.files.internal("numbers/" + numberChar + ".png")));
            numberSprite.setBounds(x, y, charSize, charSize);
            numberSprite.draw(batch);

            x += margin;
        }
    }

    public int getNumber() {
        return number;
    }
}
