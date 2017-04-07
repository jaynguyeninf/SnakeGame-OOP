package com.mygdx.game.entities;

import com.mygdx.game.configs.GameConfig;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class SnakeHead extends EntityBase {

    public SnakeHead() {
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
    }


    public void updateX(float amount) {
        x += amount;
        updateBounds(); //call updateBounds every time object updates
    }

    public void updateY(float amount) {
        y += amount;
        updateBounds();
    }

}
