package com.skyplus.hockey.objects;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by NVTT on 4/20/2017.
 */

public class Vector2d extends Vector2 {

    public Vector2d (float x, float y) {
        super(x,y);
    }


    // do dai cua vector
    public double getMagnitude(){
        return Math.sqrt(x*x+y*y);
    }

    // bieu thuc toa do cua tich vo huong
    public double dot(Vector2d v){

        return x*v.x+y*v.y;
    }

    public Vector2d times(Float scalar){

        return new Vector2d(x*scalar, y*scalar);
    }


    public Vector2d plus(Vector2d v){

        return new Vector2d(v.x+x, v.y+y);
    }

    public Vector2d proj(Vector2d v){

       Double scalar = this.dot(v)/v.dot(v);


        return v.times(scalar.floatValue());
    }
    public Vector2d getUnitVector(){

        Double tempx = x/getMagnitude();
        Double tempy =  y/getMagnitude();
        return new Vector2d(tempx.floatValue(),tempy.floatValue());
    }
}
