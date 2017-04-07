package com.mygdx.game.entities;

import com.mygdx.game.configs.GameConfig;

/**
 * Created by Jay Nguyen on 4/4/2017.
 */

public class Coin extends EntityBase {

    private boolean available;

    public Coin() {
        setSize(GameConfig.COIN_SIZE, GameConfig.COIN_SIZE);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
