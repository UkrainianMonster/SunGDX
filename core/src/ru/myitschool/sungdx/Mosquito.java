package ru.myitschool.sungdx;

import static ru.myitschool.sungdx.MyGdx.*;

import com.badlogic.gdx.math.MathUtils;

public class Mosquito {
    float x, y;
    float width, height;
    float vx, vy;
    int faza, nFaz = 10;
    boolean isAlive = true;

    Mosquito(){
        x = SCR_WIDTH/2f;
        y = SCR_HEIGHT/2f;
        width = height = MathUtils.random(50, 150);
        vx = MathUtils.random(-5f, 5);
        vy = MathUtils.random(-5f, 5);
        faza = MathUtils.random(0, nFaz-1);
    }

    float getX(){
        return x-width/2;
    }

    float getY(){
        return y-height/2;
    }

    void move() {
        x += vx;
        y += vy;
        if(isAlive) {
            outBounds2();
            changePhase();
        }
    }

    void changePhase(){
        if(++faza == nFaz) faza = 0;
        //faza = ++faza%nFaz;
    }

    void outBounds2(){
        if(x<0+width/2 || x>SCR_WIDTH-width/2) vx = -vx;
        if(y<0+height/2 || y>SCR_HEIGHT-height/2) vy = -vy;
    }

    void outBounds1(){
        if(x<0-width/2) x = SCR_WIDTH+width/2;
        if(x>SCR_WIDTH+width/2) x = 0-width/2;
        if(y<0-height/2) y = SCR_HEIGHT+height/2;
        if(y>SCR_HEIGHT+height/2) y = 0-height/2;
    }

    boolean isFlip() {
        return vx>0;
    }

    boolean hit(float tx, float ty){
        if(x-width/2<tx && tx<x+width/2 && y-height/2<ty && ty<y+height/2) {
            isAlive = false;
            faza = 10;
            vx = 0;
            vy = -8;
            return true;
        }
        return false;
    }
}
