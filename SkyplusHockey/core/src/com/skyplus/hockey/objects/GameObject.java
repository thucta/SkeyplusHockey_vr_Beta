package com.skyplus.hockey.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by TruongNN on 3/24/2017.
 */

public abstract class GameObject {
    // ham va cham
    public abstract Boolean hits(Circle circle);
    // di chuyen
    public abstract void move(float x,float y);
    public abstract void update(float delta);
    // get toa do X
    public abstract float getX();
    //  get toa do Y
    public abstract float getY();
    // get chieu rong cua the
    public  abstract float getWitdh();
    // get chieu cao cua vat the
    public  abstract float getHeight();
    // get getTexture
    public  abstract Texture getTexture();
    public abstract Circle getBounds();
    public abstract void draw(SpriteBatch batch);
}
