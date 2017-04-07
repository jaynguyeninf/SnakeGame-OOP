package com.mygdx.game.entities;

import com.mygdx.game.configs.GameConfig;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class BodyPart extends EntityBase {

    private boolean justAdded = true;


    public BodyPart() {
        setSize(GameConfig.SNAKE_SIZE, GameConfig.SNAKE_SIZE);
    }

    public boolean isJustAdded() {
        return justAdded;
    }

    public void setJustAdded(boolean justAdded) {
        this.justAdded = justAdded;
    }
}
