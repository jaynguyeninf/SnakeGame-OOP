package com.mygdx.game.entities;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public boolean isUp() {return this == UP;} //check if it's UP
    public boolean isRight() {return this == RIGHT;}
    public boolean isDown() {return this == DOWN;}
    public boolean isLeft() {return this == LEFT;}

    public Direction getOppositeDirection(){
        if(isLeft()){
            return RIGHT;
        } else if(isRight()){
            return LEFT;
        } else if (isUp()){
            return DOWN;
        } else if(isDown()){
            return UP;
        }

        //acts as a return statement
        throw new IllegalArgumentException("Cannot get the opposite expception" + this);

    }

    //check if opposite is as same as new direction
    public boolean isOppositeDirection(Direction direction){
        return getOppositeDirection() == direction;
    }



}
